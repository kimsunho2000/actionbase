package command

import (
	"encoding/json"
	"fmt"
	"slices"
	"strconv"
	"strings"

	"github.com/kakao/actionbase/internal/client"
	"github.com/kakao/actionbase/internal/util"
)

type ScanRunner interface {
	GetHost() string
	GetAuthKey() string
	GetCurrentDatabase() string
	GetCurrentTable() string
	SetCurrentTable(table string)
}

type ScanCommand struct {
	runner ScanRunner
}

func NewScanCommand(runner ScanRunner) *ScanCommand { return &ScanCommand{runner: runner} }

func (t *ScanCommand) Execute(args []string) {
	if len(args) < 1 {
		fmt.Printf("Usage: %s\n", t.GetType().GetCommand())
		return
	}

	if t.runner.GetCurrentDatabase() == "" {
		fmt.Printf("No database selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return
	}

	indexArgIndex := slices.IndexFunc(args, func(arg string) bool {
		return strings.HasPrefix(arg, "--index=")
	})
	if indexArgIndex == -1 {
		fmt.Println("No index provided. Usage: ", t.GetType().GetCommand())
		return
	}
	indexKeyValue := strings.Split(args[indexArgIndex], "=")
	if indexKeyValue[1] == "" {
		fmt.Println("No index provided. Usage: ", t.GetType().GetCommand())
		return
	}

	startArgIndex := slices.IndexFunc(args, func(arg string) bool {
		return strings.HasPrefix(arg, "--start=")
	})
	if startArgIndex == -1 {
		fmt.Println("No start provided. Usage: ", t.GetType().GetCommand())
		return
	}
	startKeyValue := strings.Split(args[startArgIndex], "=")
	if startKeyValue[1] == "" {
		fmt.Println("No start provided. Usage: ", t.GetType().GetCommand())
		return
	}

	directionArgIndex := slices.IndexFunc(args, func(arg string) bool {
		return strings.HasPrefix(arg, "--direction=") || strings.HasPrefix(arg, "--dir=")
	})
	if directionArgIndex == -1 {
		fmt.Println("No direction provided. Usage: ", t.GetType().GetCommand())
		return
	}
	directionKeyValue := strings.Split(args[directionArgIndex], "=")
	if directionKeyValue[1] == "" {
		fmt.Println("No direction provided. Usage: ", t.GetType().GetCommand())
		return
	}

	limitArgIndex := slices.IndexFunc(args, func(arg string) bool {
		return strings.HasPrefix(arg, "--limit=")
	})
	if limitArgIndex == -1 {
		fmt.Println("No limit provided. Usage: ", t.GetType().GetCommand())
		return
	}
	limitKeyValue := strings.Split(args[limitArgIndex], "=")
	if limitKeyValue[1] == "" {
		fmt.Println("No limit provided. Usage: ", t.GetType().GetCommand())
		return
	}

	rangesArgIndex := slices.IndexFunc(args, func(arg string) bool {
		return strings.HasPrefix(arg, "--ranges=")
	})
	ranges := ""
	if rangesArgIndex > -1 {
		rangesKeyValue := strings.Split(args[rangesArgIndex], "=")
		if rangesKeyValue[1] == "" {
			fmt.Println("No ranges provided. Usage: ", t.GetType().GetCommand())
			return
		}
		ranges = rangesKeyValue[1]
	}

	index := indexKeyValue[1]
	start := startKeyValue[1]
	direction := directionKeyValue[1]
	limit := limitKeyValue[1]

	rangesQueryParam := ""
	if rangesArgIndex > -1 {
		rangesQueryParam = "&ranges=" + ranges
	}

	httpClient := client.NewHTTPClient(t.runner.GetHost(), t.runner.GetAuthKey())
	uri := fmt.Sprintf("/graph/v3/databases/%s/tables/%s/edges/scan/%s?start=%s&direction=%s&limit=%s%s",
		t.runner.GetCurrentDatabase(),
		t.runner.GetCurrentTable(),
		index,
		start,
		direction,
		limit,
		rangesQueryParam,
	)

	responseString, err := httpClient.Get(uri)
	if err != nil {
		fmt.Printf("Failed to call tables: %s\n", err.Error())
		return
	}

	var response map[string]interface{}
	if err := json.Unmarshal([]byte(responseString), &response); err != nil {
		fmt.Printf("Failed to parse response: %s\n", err.Error())
		return
	}

	edgesResponse, ok := response["edges"].([]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return
	}

	edges := []map[string]interface{}{}
	for idx, item := range edgesResponse {
		edge, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		property, ok := edge["properties"].(map[string]interface{})
		if !ok {
			continue
		}

		var properties []string
		for key, value := range property {
			keyString := util.ToString(key)
			valueString := util.ToString(value)
			propertyString := keyString + ": " + valueString
			properties = append(properties, propertyString)
		}

		data := map[string]interface{}{
			"#":          "[" + strconv.Itoa(idx+1) + "]",
			"version":    util.ToString(edge["version"]),
			"source":     util.ToString(edge["source"]),
			"target":     util.ToString(edge["target"]),
			"properties": strings.Join(properties, "\n"),
		}

		edges = append(edges, data)
	}

	columnOrder := []string{"#", "version", "source", "target", "properties"}

	if len(edges) > 0 {
		fmt.Println(util.PrettyPrintRowsWithOrder(edges, columnOrder))
		fmt.Println()
		return
	}

	emptyEdge := map[string]interface{}{
		"#":          "",
		"version":    "",
		"source":     "",
		"target":     "",
		"properties": "",
	}

	fmt.Println("No edges found")
	fmt.Println(util.PrettyPrintWithOrder(emptyEdge, columnOrder))
	fmt.Println()
}

// GetDescription returns the command description
func (u *ScanCommand) GetDescription() string {
	return "Query 'scan' to table"
}

// GetType returns the command type
func (u *ScanCommand) GetType() CommandType {
	return CommandTypeScan
}
