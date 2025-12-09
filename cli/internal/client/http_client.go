package client

import (
	"fmt"
	"io"
	"net/http"
	"time"
)

// HTTPClient represents an HTTP client for Actionbase API
type HTTPClient struct {
	baseURL string
	authKey string
	client  *http.Client
}

// NewHTTPClient creates a new HTTP client instance
func NewHTTPClient(baseURL, authKey string) *HTTPClient {
	return &HTTPClient{
		baseURL: baseURL,
		authKey: authKey,
		client: &http.Client{
			Timeout: 5 * time.Second,
		},
	}
}

// Get performs a GET request to the specified URI
func (c *HTTPClient) Get(uri string) (string, error) {
	url := c.baseURL + uri
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", fmt.Errorf("failed to create request: %w", err)
	}

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Accept", "application/json")
	req.Header.Set("Authorization", c.authKey)

	resp, err := c.client.Do(req)
	if err != nil {
		return "", fmt.Errorf("failed to execute request: %w", err)
	}

	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {

		}
	}(resp.Body)

	if resp.StatusCode != http.StatusOK && resp.StatusCode != http.StatusCreated {
		return "", fmt.Errorf("HTTP error code: %d", resp.StatusCode)
	}

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return "", fmt.Errorf("failed to read response body: %w", err)
	}

	return string(body), nil
}
