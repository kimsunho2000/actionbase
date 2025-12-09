package command

import (
	"encoding/json"
	"fmt"
	"slices"
	"strings"

	"github.com/kakao/actionbase/internal/client"
	"github.com/kakao/actionbase/internal/util"
)

type GetRunner interface {
	GetHost() string
	GetAuthKey() string
	GetCurrentDatabase() string
	GetCurrentTable() string
	SetCurrentTable(table string)
}

type GetCommand struct {
	runner GetRunner
}

func NewGetCommand(runner GetRunner) *GetCommand { return &GetCommand{runner: runner} }

func (t *GetCommand) Execute(args []string) {
	if len(args) < 1 {
		fmt.Printf("Usage: %s\n", t.GetType().GetCommand())
		return
	}

	if t.runner.GetCurrentDatabase() == "" {
		fmt.Printf("No database selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return
	}

	sourceArgIndex := slices.IndexFunc(args, func(arg string) bool {
		return strings.HasPrefix(arg, "--source=")
	})
	if sourceArgIndex == -1 {
		fmt.Println("No source provided. Usage: ", t.GetType().GetCommand())
		return
	}
	sourceKeyValue := strings.Split(args[sourceArgIndex], "=")
	if sourceKeyValue[1] == "" {
		fmt.Println("No source provided. Usage: ", t.GetType().GetCommand())
		return
	}

	targetArgIndex := slices.IndexFunc(args, func(arg string) bool {
		return strings.HasPrefix(arg, "--target=")
	})
	if targetArgIndex == -1 {
		fmt.Println("No target provided. Usage: ", t.GetType().GetCommand())
		return
	}
	targetKeyValue := strings.Split(args[targetArgIndex], "=")
	if targetKeyValue[1] == "" {
		fmt.Println("No target provided. Usage: ", t.GetType().GetCommand())
		return
	}

	source := sourceKeyValue[1]
	target := targetKeyValue[1]

	httpClient := client.NewHTTPClient(t.runner.GetHost(), t.runner.GetAuthKey())
	uri := fmt.Sprintf("/graph/v3/databases/%s/tables/%s/edges/get?source=%s&target=%s",
		t.runner.GetCurrentDatabase(),
		t.runner.GetCurrentTable(),
		source,
		target,
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
	for _, item := range edgesResponse {
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
			"version":    util.ToString(edge["version"]),
			"source":     util.ToString(edge["source"]),
			"target":     util.ToString(edge["target"]),
			"properties": strings.Join(properties, "\n"),
		}

		edges = append(edges, data)
	}

	columnOrder := []string{"version", "source", "target", "properties"}

	if len(edges) > 0 {
		fmt.Println(util.PrettyPrintRowsWithOrder(edges, columnOrder))
		fmt.Println()
		return
	}

	emptyEdge := map[string]interface{}{
		"version":    "",
		"source":     "",
		"target":     "",
		"properties": "",
	}
	fmt.Printf("No edges found, [%s -> %s]\n", source, target)
	fmt.Println(util.PrettyPrintWithOrder(emptyEdge, columnOrder))
	fmt.Println()
}

// GetDescription returns the command description
func (u *GetCommand) GetDescription() string {
	return "Query 'get' to table"
}

// GetType returns the command type
func (u *GetCommand) GetType() CommandType {
	return CommandTypeGet
}
