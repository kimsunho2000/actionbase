package command

import (
	"fmt"
	"strings"

	"github.com/kakao/actionbase/internal/metastore"
)

// Use represents the use command
type Use struct {
	runner          UseRunner
	databaseCommand *metastore.DatabaseCommand
	tableCommand    *metastore.TableCommand
}

// UseRunner defines the interface for use command runner
type UseRunner interface {
	GetHost() string
	GetAuthKey() string
	GetCurrentDatabase() string
	GetCurrentTable() string
	SetCurrentDatabase(database string)
	SetCurrentTable(table string)
}

// NewUse creates a new Use command
func NewUse(runner UseRunner) *Use {
	return &Use{
		runner:          runner,
		databaseCommand: metastore.NewDatabaseCommand(runner),
		tableCommand:    metastore.NewTableCommand(runner),
	}
}

// Execute executes the use command
func (u *Use) Execute(args []string) {
	if len(args) < 1 {
		fmt.Printf("Usage: %s\n", u.GetType().GetCommand())
		return
	}

	commandType := args[0]

	if commandType == "database" {
		if len(args) < 2 {
			fmt.Printf("Usage: %s\n", u.GetType().GetCommand())
			return
		}
		u.databaseCommand.UseDatabase(args[1], true)
		return
	}

	if commandType == "table" {
		if len(args) < 2 {
			fmt.Printf("Usage: %s\n", u.GetType().GetCommand())
			return
		}
		u.tableCommand.UseTable(args[1], true)
		return
	}

	// Check for database:table format
	split := strings.Split(args[0], ":")
	if len(split) > 1 {
		isDatabaseSelected := u.databaseCommand.UseDatabase(split[0], false)
		if !isDatabaseSelected {
			return
		}

		isTableSelected := u.tableCommand.UseTable(split[1], false)
		if !isTableSelected {
			return
		}

		fmt.Printf("Changed to %s:%s\n", u.runner.GetCurrentDatabase(), u.runner.GetCurrentTable())
		return
	}

	fmt.Printf("Usage: %s\n", u.GetType().GetCommand())
}

// GetDescription returns the command description
func (u *Use) GetDescription() string {
	return "Select a database or table to use"
}

// GetType returns the command type
func (u *Use) GetType() CommandType {
	return CommandTypeUse
}
