package command

// Command represents a CLI command interface
type Command interface {
	Execute(args []string)
	GetDescription() string
	GetType() CommandType
}

