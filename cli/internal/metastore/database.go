package metastore

import (
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/kakao/actionbase/internal/client"
	"github.com/kakao/actionbase/internal/util"
)

// DatabaseRunner defines the interface for database command runner
type DatabaseRunner interface {
	GetHost() string
	GetAuthKey() string
	SetCurrentDatabase(database string)
	SetCurrentTable(table string)
	GetCurrentDatabase() string
}

// DatabaseCommand handles database-related operations
type DatabaseCommand struct {
	runner DatabaseRunner
}

// NewDatabaseCommand creates a new DatabaseCommand instance
func NewDatabaseCommand(runner DatabaseRunner) *DatabaseCommand {
	return &DatabaseCommand{runner: runner}
}

// ShowAll displays all available databases
func (d *DatabaseCommand) ShowAll() {
	httpClient := client.NewHTTPClient(d.runner.GetHost(), d.runner.GetAuthKey())

	responseString, err := httpClient.Get("/graph/v2/service")
	if err != nil {
		fmt.Printf("Failed to call databases: %s\n", err.Error())
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

	databases := []map[string]interface{}{}
	for idx, item := range content {
		db, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		name, _ := db["name"].(string)
		if name == "sys" {
			continue
		}

		data := map[string]interface{}{
			"#":      "[" + strconv.Itoa(idx) + "]",
			"name":   db["name"],
			"desc":   db["desc"],
			"active": db["active"],
		}
		databases = append(databases, data)
	}

	// Define column order explicitly
	columnOrder := []string{"#", "name", "desc", "active"}

	if len(databases) > 0 {
		fmt.Printf("Available databases (%d) :\n", len(databases))
		fmt.Println(util.PrettyPrintRowsWithOrder(databases, columnOrder))
		fmt.Println()
	} else {
		fmt.Println("No available databases found")
	}
}

// UseDatabase selects a database to use
func (d *DatabaseCommand) UseDatabase(database string, print bool) bool {
	httpClient := client.NewHTTPClient(d.runner.GetHost(), d.runner.GetAuthKey())

	responseString, err := httpClient.Get("/graph/v2/service")
	if err != nil {
		fmt.Printf("Failed to call databases: %s\n", err.Error())
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

	isDatabaseExist := false
	for _, item := range content {
		db, ok := item.(map[string]interface{})
		if !ok {
			continue
		}

		name, _ := db["name"].(string)
		if name == database {
			isDatabaseExist = true
			break
		}
	}

	if !isDatabaseExist {
		fmt.Printf("Database '%s' does not exist.\n", database)
		return false
	}

	d.runner.SetCurrentDatabase(database)
	d.runner.SetCurrentTable("")

	if print {
		fmt.Printf("Changed to %s\n", d.runner.GetCurrentDatabase())
	}

	return true
}
