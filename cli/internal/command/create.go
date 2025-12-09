package command

import "fmt"

// Create represents the create command
type Create struct{}

// NewCreate creates a new Create command
func NewCreate() *Create {
	return &Create{}
}

// Execute executes the create command
func (c *Create) Execute(args []string) {
	if len(args) < 2 {
		fmt.Printf("Usage: %s\n", c.GetType().GetCommand())
		return
	}
	fmt.Printf("Creating %s: %s\n", args[0], args[1])
	fmt.Println("Created successfully!")
}

// GetDescription returns the command description
func (c *Create) GetDescription() string {
	return "Create database or table"
}

// GetType returns the command type
func (c *Create) GetType() CommandType {
	return CommandTypeCreate
}

