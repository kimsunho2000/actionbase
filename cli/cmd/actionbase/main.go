package main

import (
	"fmt"
	"os"
	"time"

	"github.com/kakao/actionbase/internal/runner"
)

func main() {
	if len(os.Args) != 5 {
		fmt.Println("Use --host <host> --authKey <authKey>")
		return
	}

	if os.Args[1] != "--host" {
		fmt.Printf("Invalid argument: %s\n", os.Args[1])
		return
	}

	if os.Args[3] != "--authKey" {
		fmt.Printf("Invalid argument: %s\n", os.Args[3])
		return
	}

	start := time.Now()

	host := os.Args[2]
	authKey := os.Args[4]

	console := runner.NewActionbaseCommandLineRunner(host, authKey)

	console.CheckConnection()
	console.ShowBanner()

	elapsed := time.Since(start)
	fmt.Println("Took ", fmt.Sprintf("%.4f", elapsed.Seconds()), " seconds")

	console.Run()
}
