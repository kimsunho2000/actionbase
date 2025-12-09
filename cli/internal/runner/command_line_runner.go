package runner

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"time"

	"github.com/kakao/actionbase/internal/command"
)

// CommandLineRunner represents a generic command-line interface runner
type CommandLineRunner struct {
	name     string
	running  bool
	commands map[string]command.Command
	reader   *bufio.Reader
	prompt   string
}

// NewCommandLineRunner creates a new CommandLineRunner instance
func NewCommandLineRunner(name, version string) *CommandLineRunner {
	runner := &CommandLineRunner{
		name:     name,
		running:  false,
		commands: make(map[string]command.Command),
		reader:   bufio.NewReader(os.Stdin),
		prompt:   name + "> ",
	}

	// Register default commands
	runner.RegisterCommand("help", command.NewHelp(runner))
	runner.RegisterCommand("exit", command.NewExit(runner))

	return runner
}

// Run starts the command-line interface loop
func (r *ActionbaseCommandLineRunner) Run() {
	r.running = true

	for r.running {
		// Read multi-line input (support \ continuation)
		fullLine, done := r.ReadLines()
		if done {
			return
		}

		line := strings.TrimSpace(fullLine.String())
		if line == "" {
			continue
		}

		parts := r.parseCommand(line)
		cmdName := parts[0]
		args := []string{}
		if len(parts) > 1 {
			args = parts[1:]
		}

		start := time.Now()
		r.executeCommand(cmdName, args)
		elapsed := time.Since(start)
		fmt.Println("Took ", fmt.Sprintf("%.4f", elapsed.Seconds()), " seconds")
	}
}

func (r *ActionbaseCommandLineRunner) ReadLines() (strings.Builder, bool) {
	var fullLine strings.Builder
	isFirstLine := true

	for {
		if isFirstLine {
			fmt.Print(r.BuildPrompt())
			isFirstLine = false
		} else {
			fmt.Print("> ")
		}

		line, err := r.reader.ReadString('\n')

		if err != nil { // EOF or error
			fmt.Println("\nGoodbye!")
			return strings.Builder{}, true
		}

		// Remove trailing newline
		line = strings.TrimRight(line, "\n\r")

		// Check if line ends with backslash (continuation)
		if strings.HasSuffix(line, "\\") {
			// Remove the backslash and continue reading
			line = strings.TrimSuffix(line, "\\")
			fullLine.WriteString(line)
			fullLine.WriteString(" ") // Add space between lines
			continue
		} else {
			// No continuation, finish reading
			fullLine.WriteString(line)
			break
		}
	}
	return fullLine, false
}

// RegisterCommand registers a command with the runner
func (r *CommandLineRunner) RegisterCommand(name string, cmd command.Command) {
	r.commands[strings.ToLower(name)] = cmd
}

// GetCommands returns all registered commands
func (r *CommandLineRunner) GetCommands() map[string]command.Command {
	return r.commands
}

// SetRunning sets the running state
func (r *CommandLineRunner) SetRunning(running bool) {
	r.running = running
}

// BuildPrompt builds the command prompt string
func (r *CommandLineRunner) BuildPrompt() string {
	return fmt.Sprintf("%s> ", strings.ToLower(r.name))
}

func (r *CommandLineRunner) parseCommand(line string) []string {
	return strings.Fields(strings.TrimSpace(line))
}

func (r *CommandLineRunner) executeCommand(cmdName string, args []string) {
	cmd, ok := r.commands[strings.ToLower(cmdName)]

	if ok {
		defer func() {
			if rec := recover(); rec != nil {
				fmt.Printf("Error executing command: %v\n", rec)
			}
		}()
		cmd.Execute(args)
	} else {
		fmt.Println("Unknown command: " + cmdName)
		fmt.Println("Type 'help' for available commands.")
	}
}
