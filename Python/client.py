### CLIENT ###

import sys

if sys.version_info[0] < 3:
    raise Exception('Please, use Python 3.')

PORT = 8080
HOST = '127.0.0.1'


def main():

    import socket as skt

    messages_count = 0
    with skt.socket(skt.AF_INET, skt.SOCK_STREAM) as socket:
        socket.connect((HOST, PORT))
        while True:
            data = input('Type something: ')
            socket.sendall(data.encode('ascii'))
            if data == 'stop':
                break
            messages_count += 1
            print('[%d] Sent: %s' % (messages_count, data))
            data = socket.recv(1024)
            if not data:
                break
            data = data.decode('ascii')
            if data == 'stop':
                break
            messages_count += 1
            print('[%d] Received: %s' % (messages_count, data))


if __name__ == "__main__":
    main()