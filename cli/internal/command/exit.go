package command

import (
	"fmt"
)

// Exit represents the exit command
type Exit struct {
	runner Runner
}

// Runner interface defines methods needed by commands to interact with the runner
type Runner interface {
	SetRunning(running bool)
}

// NewExit creates a new Exit command
func NewExit(runner Runner) *Exit {
	return &Exit{runner: runner}
}

// Execute executes the exit command
func (e *Exit) Execute(args []string) {
	fmt.Println("Goodbye!")
	e.runner.SetRunning(false)
}

// GetDescription returns the command description
func (e *Exit) GetDescription() string {
	return "Exit the console"
}

// GetType returns the command type
func (e *Exit) GetType() CommandType {
	return CommandTypeExit
}

