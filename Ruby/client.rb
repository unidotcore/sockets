### CLIENT ###

require 'socket'

PORT = 8080
HOST = '127.0.0.1'

messages_count = 0

socket = TCPSocket.new(HOST, PORT)
loop do
    print('Type something: ')
    data = gets.chomp
    socket.write(data.encode('ASCII'))
    messages_count += 1
    puts('[%d] Sent: %s' % [messages_count, data] + $/)
    if data == 'stop'
        break
    end
    data = socket.recv(1024)
    if data == 'stop'
        break
    end
    messages_count += 1
    puts('[%d] Received: %s' % [messages_count, data] + $/)
end
socket.close