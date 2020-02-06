/** SERVER */

package main

import "os"
import "fmt"
import "net"
import "bufio"
import "strings"

const PORT uint16 = 8080

func main() {
	messagesCount := 0
	inputReader := bufio.NewReader(os.Stdin)
	listener, _ := net.Listen("tcp", fmt.Sprintf(":%d", PORT))
	socket, _ := listener.Accept()
	fmt.Println(fmt.Sprintf("Connected to: %s", socket.RemoteAddr()))
	for {
		buffer := make([]byte, 1024)
		bytesRead, err := socket.Read(buffer)
		if err != nil {
			fmt.Println("Unable to read from socket.")
			os.Exit(1)
		}
		data := string(buffer[:bytesRead])
		if data == "stop" {
			break;
		}
		messagesCount += 1;
		fmt.Println(fmt.Sprintf("[%d] Received: %s", messagesCount, data))
		fmt.Print("Type something: ")
		data, _ = inputReader.ReadString('\n')
		data = strings.Trim(data, "\n")
		socket.Write([]byte(data))
		messagesCount += 1;
		fmt.Println(fmt.Sprintf("[%d] Sent: %s", messagesCount, data))
		if data == "stop" {
			break;
		}
	}
}