package command

// CommandType represents the type and usage of a command
type CommandType struct {
	Name    string
	Command string
}

var (
	// CommandTypeCreate represents the create command
	CommandTypeCreate = CommandType{
		Name:    "CREATE",
		Command: "create <database|table> <name>",
	}

	// CommandTypeDesc represents the desc command
	CommandTypeDesc = CommandType{
		Name:    "DESC",
		Command: "desc <name>",
	}

	// CommandTypeExit represents the exit command
	CommandTypeExit = CommandType{
		Name:    "EXIT",
		Command: "exit",
	}

	// CommandTypeHelp represents the help command
	CommandTypeHelp = CommandType{
		Name:    "HELP",
		Command: "help",
	}

	// CommandTypeShow represents the show command
	CommandTypeShow = CommandType{
		Name:    "SHOW",
		Command: "show <databases|tables|indices|groups>",
	}

	// CommandTypeStatus represents the status command
	CommandTypeStatus = CommandType{
		Name:    "STATUS",
		Command: "status",
	}

	// CommandTypeUse represents the use command
	CommandTypeUse = CommandType{
		Name:    "USE",
		Command: "use <database|table> <name> or use <database>:<table>",
	}

	CommandTypeGet = CommandType{
		Name:    "GET",
		Command: "get --source=<source> --target=<target>",
	}

	CommandTypeScan = CommandType{
		Name:    "Scan",
		Command: "scan --index=<index> --start=<start> --direction=<direction> --limit=<limit> --ranges=<ranges>",
	}
)

// GetCommand returns the command usage string
func (ct CommandType) GetCommand() string {
	return ct.Command
}
