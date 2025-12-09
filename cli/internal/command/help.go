package command

import (
	"fmt"

	"github.com/kakao/actionbase/internal/util"
)

// Help represents the help command
type Help struct {
	runner CommandRunner
}

// CommandRunner interface defines methods needed by help command
type CommandRunner interface {
	GetCommands() map[string]Command
}

// NewHelp creates a new Help command
func NewHelp(runner CommandRunner) *Help {
	return &Help{runner: runner}
}

// Execute executes the help command
func (h *Help) Execute(args []string) {
	commands := []map[string]interface{}{}

	for name, cmd := range h.runner.GetCommands() {
		help := map[string]interface{}{
			"name":        name,
			"description": cmd.GetDescription(),
			"usage":       "`" + cmd.GetType().GetCommand() + "`",
		}
		commands = append(commands, help)
	}

	// Define column order explicitly
	columnOrder := []string{"name", "description", "usage"}

	fmt.Println("\nAvailable commands:")
	fmt.Println(util.PrettyPrintRowsWithOrder(commands, columnOrder))
	fmt.Println()
}

// GetDescription returns the command description
func (h *Help) GetDescription() string {
	return "Show available commands"
}

// GetType returns the command type
func (h *Help) GetType() CommandType {
	return CommandTypeHelp
}
