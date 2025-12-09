package runner

import (
	"fmt"
	"os"

	"github.com/kakao/actionbase/internal/client"
	"github.com/kakao/actionbase/internal/command"
	"github.com/kakao/actionbase/internal/util"
)

// ActionbaseCommandLineRunner represents an Actionbase-specific CLI runner
type ActionbaseCommandLineRunner struct {
	*CommandLineRunner
	host            string
	authKey         string
	currentDatabase string
	currentTable    string
}

// NewActionbaseCommandLineRunner creates a new ActionbaseCommandLineRunner instance
func NewActionbaseCommandLineRunner(host, authKey string) *ActionbaseCommandLineRunner {
	runner := &ActionbaseCommandLineRunner{
		CommandLineRunner: NewCommandLineRunner("Actionbase", "0.0.1"),
		host:              host,
		authKey:           authKey,
		currentDatabase:   "",
		currentTable:      "",
	}

	// Register Actionbase-specific commands
	runner.RegisterCommand("status", command.NewStatus(runner))
	runner.RegisterCommand("create", command.NewCreate())
	runner.RegisterCommand("show", command.NewShow(runner))
	runner.RegisterCommand("use", command.NewUse(runner))
	runner.RegisterCommand("desc", command.NewDesc(runner))
	runner.RegisterCommand("get", command.NewGetCommand(runner))
	runner.RegisterCommand("scan", command.NewScanCommand(runner))

	return runner
}

// GetHost returns the host URL
func (r *ActionbaseCommandLineRunner) GetHost() string {
	return r.host
}

// GetCurrentDatabase returns the current database
func (r *ActionbaseCommandLineRunner) GetCurrentDatabase() string {
	return r.currentDatabase
}

// SetCurrentDatabase sets the current database
func (r *ActionbaseCommandLineRunner) SetCurrentDatabase(database string) {
	r.currentDatabase = database
}

// GetCurrentTable returns the current table
func (r *ActionbaseCommandLineRunner) GetCurrentTable() string {
	return r.currentTable
}

// SetCurrentTable sets the current table
func (r *ActionbaseCommandLineRunner) SetCurrentTable(table string) {
	r.currentTable = table
}

// GetAuthKey returns the authentication key
func (r *ActionbaseCommandLineRunner) GetAuthKey() string {
	return r.authKey
}

// ShowBanner displays the welcome banner and checks connection
func (r *ActionbaseCommandLineRunner) ShowBanner() {
	separator := util.RepeatChar('=', 60)
	fmt.Println(separator)
	fmt.Println(r.name + " Console")
	fmt.Println(separator)
	fmt.Println("Type 'help' for available commands, 'exit' to quit.")
}

// BuildPrompt builds the command prompt string with current context
func (r *ActionbaseCommandLineRunner) BuildPrompt() string {
	if r.currentTable != "" {
		return fmt.Sprintf("%s(%s:%s)> ", "actionbase", r.currentDatabase, r.currentTable)
	}

	if r.currentDatabase != "" {
		return fmt.Sprintf("%s(%s)> ", "actionbase", r.currentDatabase)
	}

	return r.CommandLineRunner.BuildPrompt()
}

func (r *ActionbaseCommandLineRunner) CheckConnection() {
	httpClient := client.NewHTTPClient(r.host, r.authKey)
	_, err := httpClient.Get("/graph/v3")
	if err != nil {
		fmt.Println("Connection Failed")
		os.Exit(0)
	}

	fmt.Printf("Connected to %s\n", r.host)
}
