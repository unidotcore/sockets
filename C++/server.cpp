/** SERVER */

#include <string>
#include <iostream>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define PORT 8080
#define BACKLOG_QUEUE_MAX_SIZE 3

using namespace std;

int main() {
	char buffer[1024];
	// Initializing all buffer values to 0.
	bzero(buffer, sizeof(buffer));
	struct sockaddr_in address;
	int socket_fs, socket, opt = 1, socket_length = sizeof(address), messages_count = 0;
	if ((socket_fs = ::socket(AF_INET, SOCK_STREAM, 0)) == 0) {
		cerr << "Unable to create file descriptor." << endl;
		return 1;
	}
	if (setsockopt(socket_fs, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt))) {
		cerr << "Unable to set socket options." << endl;
		return 1;
	}
	// Setting Address Family to IPv4 (IPv6 = AF_INET6).
	address.sin_family = AF_INET;
	// Accept connections from any IP.
	address.sin_addr.s_addr = INADDR_ANY;
	// Converting port number from host byte order to network byte order (big-endian).
	// See: https://en.wikipedia.org/wiki/Endianness
	address.sin_port = htons(PORT);
	if (::bind(socket_fs, (struct sockaddr *)&address, sizeof(address)) < 0) {
		cerr << "Unable to forcefully attach socket to port " << PORT << "." << endl;
		return 1;
	}
	if (listen(socket_fs, BACKLOG_QUEUE_MAX_SIZE) < 0) {
		cerr << "Unable to start listening for new connections." << endl;
		return 1;
	}
	if ((socket = accept(socket_fs, (struct sockaddr *)&address, (socklen_t *)&socket_length)) < 0) {
		cerr << "Unable to accept new connections." << endl;
		return 1;
	}
	printf("Connected to: %d\n", socket);
	while (true) {
		if (read(socket, buffer, sizeof(buffer)) < 0) {
			cerr << "Unable to read from socket." << endl;
			return 1;
		}
		if (strcmp(buffer, "stop") == 0) {
			break;
		}
		printf("[%d] Received: %s\n", ++messages_count, buffer);
		bzero(buffer, sizeof(buffer));
		cout << "Type something: "; cin >> buffer;
		send(socket, buffer, strlen(buffer), 0);
		printf("[%d] Sent: %s\n", ++messages_count, buffer);
		if (strcmp(buffer, "stop") == 0) {
			break;
		}
		bzero(buffer, sizeof(buffer));
	}
	return 0;
}