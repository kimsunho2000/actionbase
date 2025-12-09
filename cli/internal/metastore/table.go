package metastore

import (
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/kakao/actionbase/internal/client"
	"github.com/kakao/actionbase/internal/util"
)

// TableRunner defines the interface for table command runner
type TableRunner interface {
	GetHost() string
	GetAuthKey() string
	GetCurrentDatabase() string
	GetCurrentTable() string
	SetCurrentTable(table string)
}

// TableCommand handles table-related operations
type TableCommand struct {
	runner TableRunner
}

// NewTableCommand creates a new TableCommand instance
func NewTableCommand(runner TableRunner) *TableCommand {
	return &TableCommand{runner: runner}
}

// ShowAll displays all tables in the current database
func (t *TableCommand) ShowAll() {
	if t.runner.GetCurrentDatabase() == "" {
		fmt.Printf("No database selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return
	}

	httpClient := client.NewHTTPClient(t.runner.GetHost(), t.runner.GetAuthKey())
	uri := fmt.Sprintf("/graph/v2/service/%s/alias", t.runner.GetCurrentDatabase())
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

	content, ok := response["content"].([]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return
	}

	tables := []map[string]interface{}{}
	for idx, item := range content {
		table, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		data := map[string]interface{}{
			"#":      "[" + strconv.Itoa(idx+1) + "]",
			"name":   table["name"],
			"desc":   table["desc"],
			"target": table["target"],
			"active": table["active"],
		}
		tables = append(tables, data)
	}

	// Define column order explicitly
	columnOrder := []string{"#", "name", "desc", "target", "active"}

	if len(tables) > 0 {
		fmt.Printf("%v Tables in %s:\n", response["count"], t.runner.GetCurrentDatabase())
		fmt.Println(util.PrettyPrintRowsWithOrder(tables, columnOrder))
		fmt.Println()
	} else {
		emptyTable := map[string]interface{}{
			"#":      "",
			"name":   "",
			"desc":   "",
			"target": "",
			"active": "",
		}
		fmt.Printf("0 Tables in %s:\n", t.runner.GetCurrentDatabase())
		fmt.Println(util.PrettyPrintWithOrder(emptyTable, columnOrder))
		fmt.Println()
	}
}

// ShowIndices displays all indices for the current table
func (t *TableCommand) ShowIndices() {
	if t.runner.GetCurrentDatabase() == "" {
		fmt.Printf("No database selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return
	}

	if t.runner.GetCurrentTable() == "" {
		fmt.Printf("No table selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return
	}

	httpClient := client.NewHTTPClient(t.runner.GetHost(), t.runner.GetAuthKey())
	uri := fmt.Sprintf("/graph/v2/service/%s/alias/%s",
		t.runner.GetCurrentDatabase(),
		t.runner.GetCurrentTable())
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

	label, ok := response["label"].(map[string]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return
	}

	indicesResponse, ok := label["indices"].([]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return
	}

	indices := []map[string]interface{}{}
	for idx, item := range indicesResponse {
		index, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		fields, ok := index["fields"].([]interface{})
		if !ok {
			continue
		}

		name := ""
		order := ""

		for idx, fieldItem := range fields {
			field, ok := fieldItem.(map[string]interface{})
			if !ok {
				continue
			}

			if fieldName, ok := field["name"].(string); ok {
				name += fieldName
				if idx < len(fields)-1 {
					name += "\n"
				}
			}

			if fieldOrder, ok := field["order"].(string); ok {
				order += fieldOrder
				if idx < len(fields)-1 {
					order += "\n"
				}
			}
		}

		data := map[string]interface{}{
			"#":              "[" + strconv.Itoa(idx+1) + "]",
			"name":           index["name"],
			"desc":           index["desc"],
			"fields[].name":  name,
			"fields[].order": order,
		}
		indices = append(indices, data)
	}

	// Define column order explicitly
	columnOrder := []string{"#", "name", "desc", "fields[].name", "fields[].order"}

	if len(indices) > 0 {
		fmt.Printf("%d Indices in %s:\n", len(indicesResponse), t.runner.GetCurrentTable())
		fmt.Println(util.PrettyPrintRowsWithOrder(indices, columnOrder))
		fmt.Println()
	} else {
		emptyIndex := map[string]interface{}{
			"#":              "",
			"name":           "",
			"desc":           "",
			"fields[].name":  "",
			"fields[].order": "",
		}
		fmt.Printf("0 Indices in %s:\n", t.runner.GetCurrentTable())
		fmt.Println(util.PrettyPrintWithOrder(emptyIndex, columnOrder))
		fmt.Println()
	}
}

// ShowGroups displays all groups for the current table
func (t *TableCommand) ShowGroups() {
	if t.runner.GetCurrentDatabase() == "" {
		fmt.Printf("No database selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return
	}

	if t.runner.GetCurrentTable() == "" {
		fmt.Printf("No table selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return
	}

	httpClient := client.NewHTTPClient(t.runner.GetHost(), t.runner.GetAuthKey())
	uri := fmt.Sprintf("/graph/v2/service/%s/alias/%s",
		t.runner.GetCurrentDatabase(),
		t.runner.GetCurrentTable())
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

	label, ok := response["label"].(map[string]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return
	}

	groupsResponse, ok := label["groups"].([]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return
	}

	groups := []map[string]interface{}{}
	for idx, item := range groupsResponse {
		group, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		fields, ok := group["fields"].([]interface{})
		if !ok {
			continue
		}

		fieldNames := ""
		fieldBucketTypes := ""
		fieldBucketNames := ""
		fieldBucketUnits := ""
		fieldBucketTimezones := ""
		fieldBucketFormats := ""

		for idx, fieldItem := range fields {
			field, ok := fieldItem.(map[string]interface{})
			if !ok {
				continue
			}

			fieldNames += field["name"].(string)
			if idx < len(fields)-1 {
				fieldNames += "\n"
			}

			bucket, ok := field["bucket"].(map[string]interface{})
			if ok && bucket != nil {
				if fieldType, ok := field["type"].(string); ok {
					fieldBucketTypes += fieldType
					if idx < len(fields)-1 {
						fieldBucketTypes += "\n"
					}
				}

				if bucketName, ok := bucket["name"].(string); ok {
					fieldBucketNames += bucketName
					if idx < len(fields)-1 {
						fieldBucketNames += "\n"
					}
				}

				if bucketUnit, ok := bucket["unit"].(string); ok {
					fieldBucketUnits += bucketUnit
					if idx < len(fields)-1 {
						fieldBucketUnits += "\n"
					}
				}

				if bucketTimezone, ok := bucket["timezone"].(string); ok {
					fieldBucketTimezones += bucketTimezone
					if idx < len(fields)-1 {
						fieldBucketTimezones += "\n"
					}
				}

				if bucketFormat, ok := bucket["format"].(string); ok {
					fieldBucketFormats += bucketFormat
					if idx < len(fields)-1 {
						fieldBucketFormats += "\n"
					}
				}
			} else {
				fieldBucketTypes += ""
				fieldBucketNames += ""
				fieldBucketUnits += ""
				fieldBucketTimezones += ""
				fieldBucketFormats += ""
			}
		}

		// Convert ttl to string (avoid scientific notation)
		ttlStr := ""
		switch v := group["ttl"].(type) {
		case float64:
			ttlStr = fmt.Sprintf("%.0f", v)
		case float32:
			ttlStr = fmt.Sprintf("%.0f", v)
		case int:
			ttlStr = fmt.Sprintf("%d", v)
		case int64:
			ttlStr = fmt.Sprintf("%d", v)
		default:
			ttlStr = fmt.Sprintf("%v", v)
		}

		data := map[string]interface{}{
			"#":                        "[" + strconv.Itoa(idx+1) + "]",
			"group":                    group["group"],
			"type":                     group["type"],
			"valueField":               group["valueField"],
			"comment":                  group["comment"],
			"directionType":            group["directionType"],
			"ttl":                      ttlStr,
			"fields[].name":            fieldNames,
			"fields[].bucket.type":     fieldBucketTypes,
			"fields[].bucket.name":     fieldBucketNames,
			"fields[].bucket.unit":     fieldBucketUnits,
			"fields[].bucket.timezone": fieldBucketTimezones,
			"fields[].bucket.format":   fieldBucketFormats,
		}
		groups = append(groups, data)
	}

	// Define column order explicitly
	columnOrder := []string{
		"#", "group", "type", "valueField", "comment", "directionType", "ttl",
		"fields[].name", "fields[].bucket.type", "fields[].bucket.name",
		"fields[].bucket.unit", "fields[].bucket.timezone", "fields[].bucket.format",
	}

	if len(groups) > 0 {
		fmt.Printf("%d Groups in %s:\n", len(groupsResponse), t.runner.GetCurrentTable())
		fmt.Println(util.PrettyPrintRowsWithOrder(groups, columnOrder))
		fmt.Println()
	} else {
		emptyGroup := map[string]interface{}{
			"#":                        "",
			"group":                    "",
			"type":                     "",
			"valueField":               "",
			"comment":                  "",
			"directionType":            "",
			"ttl":                      "",
			"fields[].name":            "",
			"fields[].bucket.type":     "",
			"fields[].bucket.name":     "",
			"fields[].bucket.unit":     "",
			"fields[].bucket.timezone": "",
			"fields[].bucket.format":   "",
		}
		fmt.Printf("0 Groups in %s:\n", t.runner.GetCurrentTable())
		fmt.Println(util.PrettyPrintWithOrder(emptyGroup, columnOrder))
		fmt.Println()
	}
}

// Desc describes a table
func (t *TableCommand) Desc(name string) {
	httpClient := client.NewHTTPClient(t.runner.GetHost(), t.runner.GetAuthKey())
	uri := fmt.Sprintf("/graph/v2/service/%s/alias/%s", t.runner.GetCurrentDatabase(), name)
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

	label, ok := response["label"].(map[string]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return
	}

	table := map[string]interface{}{
		"name":     label["name"],
		"desc":     label["desc"],
		"type":     label["type"],
		"dirType":  label["dirType"],
		"event":    label["event"],
		"readOnly": label["readOnly"],
		"mode":     label["mode"],
	}
	tableColumnOrder := []string{"name", "desc", "type", "dirType", "event", "readOnly", "mode"}
	fmt.Println(util.PrettyPrintWithOrder(table, tableColumnOrder))
	fmt.Println()

	schemaResponse, ok := label["schema"].(map[string]interface{})
	if !ok {
		fmt.Println("Invalid schema format")
		return
	}

	sourceResponse, ok := schemaResponse["src"].(map[string]interface{})
	if ok {
		source := map[string]interface{}{
			"type": sourceResponse["type"],
			"desc": sourceResponse["desc"],
		}
		srcTgtColumnOrder := []string{"type", "desc"}
		fmt.Println("\n>> Source")
		fmt.Println(util.PrettyPrintWithOrder(source, srcTgtColumnOrder))
	}

	targetResponse, ok := schemaResponse["tgt"].(map[string]interface{})
	if ok {
		target := map[string]interface{}{
			"type": targetResponse["type"],
			"desc": targetResponse["desc"],
		}
		srcTgtColumnOrder := []string{"type", "desc"}
		fmt.Println("\n>> Target")
		fmt.Println(util.PrettyPrintWithOrder(target, srcTgtColumnOrder))
		fmt.Println()
	}

	fieldsResponse, ok := schemaResponse["fields"].([]interface{})
	if !ok {
		fmt.Println("Invalid fields format")
		return
	}

	fields := []map[string]interface{}{}
	for idx, item := range fieldsResponse {
		field, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		data := map[string]interface{}{
			"#":        "[" + strconv.Itoa(idx+1) + "]",
			"name":     field["name"],
			"type":     field["type"],
			"nullable": field["nullable"],
			"desc":     field["desc"],
		}
		fields = append(fields, data)
	}

	fieldColumnOrder := []string{"#", "name", "type", "nullable", "desc"}

	if len(fields) > 0 {
		fmt.Printf(">> Fields (%d) :\n", len(fields))
		fmt.Println(util.PrettyPrintRowsWithOrder(fields, fieldColumnOrder))
		fmt.Println()
	} else {
		emptyField := map[string]interface{}{
			"#":        "",
			"name":     "",
			"type":     "",
			"nullable": "",
			"desc":     "",
		}
		fmt.Println(">> Fields (0) :")
		fmt.Println(util.PrettyPrintWithOrder(emptyField, fieldColumnOrder))
		fmt.Println()
	}
}

// UseTable selects a table to use
func (t *TableCommand) UseTable(table string, print bool) bool {
	if t.runner.GetCurrentDatabase() == "" {
		fmt.Printf("No database selected. Use 'use <database|table> <name> or use <database>:<table>'\n")
		return false
	}

	httpClient := client.NewHTTPClient(t.runner.GetHost(), t.runner.GetAuthKey())
	uri := fmt.Sprintf("/graph/v2/service/%s/alias", t.runner.GetCurrentDatabase())
	responseString, err := httpClient.Get(uri)
	if err != nil {
		fmt.Printf("Failed to call tables: %s\n", err.Error())
		return false
	}

	var response map[string]interface{}
	if err := json.Unmarshal([]byte(responseString), &response); err != nil {
		fmt.Printf("Failed to parse response: %s\n", err.Error())
		return false
	}

	content, ok := response["content"].([]interface{})
	if !ok {
		fmt.Println("Invalid response format")
		return false
	}

	fullTableName := fmt.Sprintf("%s.%s", t.runner.GetCurrentDatabase(), table)
	isTableExist := false
	for _, item := range content {
		tbl, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		name, _ := tbl["name"].(string)
		if name == fullTableName {
			isTableExist = true
			break
		}
	}

	if !isTableExist {
		fmt.Printf("Table '%s' does not exist in %s.", table, t.runner.GetCurrentDatabase())
		return false
	}

	t.runner.SetCurrentTable(table)

	if print {
		fmt.Printf("Changed to %s:%s\n", t.runner.GetCurrentDatabase(), t.runner.GetCurrentTable())
	}

	return true
}
