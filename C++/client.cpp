/** CLIENT */

#include <string>
#include <iostream>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define PORT 8080
#define SERVER_IP "127.0.0.1"

using namespace std;

int main() {
	char buffer[1024];
	// Initializing all buffer values to 0.
	bzero(buffer, sizeof(buffer));
	struct sockaddr_in server_address;
	int socket_fs = 0, messages_count = 0;
	if ((socket_fs = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
		cerr << "Unable to create file descriptor." << endl;
		return 1;
	}
	// Setting Address Family to IPv4 (IPv6 = AF_INET6).
	server_address.sin_family = AF_INET;
	// Converting port number from host byte order to network byte order (big-endian).
	// See: https://en.wikipedia.org/wiki/Endianness
	server_address.sin_port = htons(PORT);
	if (inet_pton(AF_INET, SERVER_IP, &server_address.sin_addr) <= 0) {
		cerr << "Unable to convert IP address to binary form." << endl;
		return 1;
	}
	if (connect(socket_fs, (struct sockaddr *)&server_address, sizeof(server_address)) < 0) {
		cerr << "Unable to connect to server socket." << endl;
		return 1;
	}
	while (true) {
		cout << "Type something: "; cin >> buffer;
		send(socket_fs, buffer, strlen(buffer), 0);
		printf("[%d] Sent: %s\n", ++messages_count, buffer);
		if (strcmp(buffer, "stop") == 0) {
			break;
		}
		bzero(buffer, sizeof(buffer));
		if (read(socket_fs, buffer, sizeof(buffer)) < 0) {
			cerr << "Unable to read from socket." << endl;
			return 1;
		}
		if (strcmp(buffer, "stop") == 0) {
			break;
		}
		printf("[%d] Received: %s\n", ++messages_count, buffer);
		bzero(buffer, sizeof(buffer));
	}
	return 0;
}