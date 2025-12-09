package util

import (
	"fmt"
	"strconv"
	"strings"
)

// RepeatChar returns a string with the given character repeated count times
func RepeatChar(c rune, count int) string {
	var sb strings.Builder
	sb.Grow(count)
	for i := 0; i < count; i++ {
		sb.WriteRune(c)
	}
	return sb.String()
}

func ToString(v any) string {
	switch val := v.(type) {
	case int:
		return strconv.Itoa(val)
	case float64:
		return fmt.Sprintf("%.f", val)
	case string:
		return val // 이미 string
	case nil:
		return "null"
	default:
		return fmt.Sprintf("%v", val)
	}
}
