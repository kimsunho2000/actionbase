package command

import "fmt"

// Status represents the status command
type Status struct {
	runner ActionbaseRunner
}

// ActionbaseRunner interface defines methods needed by status command
type ActionbaseRunner interface {
	GetHost() string
	GetCurrentDatabase() string
	GetCurrentTable() string
}

// NewStatus creates a new Status command
func NewStatus(runner ActionbaseRunner) *Status {
	return &Status{runner: runner}
}

// Execute executes the status command
func (s *Status) Execute(args []string) {
	database := s.runner.GetCurrentDatabase()
	if database == "" {
		database = "-"
	}

	table := s.runner.GetCurrentTable()
	if table == "" {
		table = "-"
	}

	fmt.Printf("- host: %s\n", s.runner.GetHost())
	fmt.Printf("- database: %s\n", database)
	fmt.Printf("- table: %s\n", table)
}

// GetDescription returns the command description
func (s *Status) GetDescription() string {
	return "Show current status"
}

// GetType returns the command type
func (s *Status) GetType() CommandType {
	return CommandTypeStatus
}

