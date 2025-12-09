package command

import (
	"fmt"

	"github.com/kakao/actionbase/internal/metastore"
)

// Desc represents the desc command
type Desc struct {
	tableCommand *metastore.TableCommand
}

// DescRunner defines the interface for desc command runner
type DescRunner interface {
	GetHost() string
	GetAuthKey() string
	GetCurrentDatabase() string
	GetCurrentTable() string
	SetCurrentTable(table string)
}

// NewDesc creates a new Desc command
func NewDesc(runner DescRunner) *Desc {
	return &Desc{
		tableCommand: metastore.NewTableCommand(runner),
	}
}

// Execute executes the desc command
func (d *Desc) Execute(args []string) {
	if len(args) < 1 {
		fmt.Printf("Usage: %s\n", d.GetType().GetCommand())
		return
	}

	d.tableCommand.Desc(args[0])
}

// GetDescription returns the command description
func (d *Desc) GetDescription() string {
	return "Describe table"
}

// GetType returns the command type
func (d *Desc) GetType() CommandType {
	return CommandTypeDesc
}
