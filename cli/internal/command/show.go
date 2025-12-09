package command

import (
	"fmt"

	"github.com/kakao/actionbase/internal/metastore"
)

// Show represents the show command
type Show struct {
	databaseCommand *metastore.DatabaseCommand
	tableCommand    *metastore.TableCommand
}

// ShowRunner defines the interface for show command runner
type ShowRunner interface {
	GetHost() string
	GetAuthKey() string
	GetCurrentDatabase() string
	GetCurrentTable() string
	SetCurrentDatabase(database string)
	SetCurrentTable(table string)
}

// NewShow creates a new Show command
func NewShow(runner ShowRunner) *Show {
	return &Show{
		databaseCommand: metastore.NewDatabaseCommand(runner),
		tableCommand:    metastore.NewTableCommand(runner),
	}
}

// Execute executes the show command
func (s *Show) Execute(args []string) {
	if len(args) < 1 {
		fmt.Printf("Usage: %s\n", s.GetType().GetCommand())
		return
	}

	commandType := args[0]

	switch commandType {
	case "databases":
		s.databaseCommand.ShowAll()
	case "tables":
		s.tableCommand.ShowAll()
	case "indices":
		s.tableCommand.ShowIndices()
	case "groups":
		s.tableCommand.ShowGroups()
	default:
		fmt.Printf("Usage: %s\n", s.GetType().GetCommand())
	}
}

// GetDescription returns the command description
func (s *Show) GetDescription() string {
	return "Show databases, tables, table indices, or table groups"
}

// GetType returns the command type
func (s *Show) GetType() CommandType {
	return CommandTypeShow
}
