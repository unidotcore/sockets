### CLIENT ###

import sys

if sys.version_info[0] < 3:
    raise Exception('Please, use Python 3.')

PORT = 8080
HOST = '127.0.0.1'


def main():

    import socket

    messages_count = 0
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server:
        server.connect((HOST, PORT))
        while True:
            data = input('Type something: ')
            server.sendall(data.encode('ascii'))
            if data == 'stop':
                break
            messages_count += 1
            print('[%d] Sent: %s' % (messages_count, data))
            data = server.recv(1024)
            if not data:
                break
            data = data.decode('ascii')
            if data == 'stop':
                break
            messages_count += 1
            print('[%d] Received: %s' % (messages_count, data))


if __name__ == "__main__":
    main()