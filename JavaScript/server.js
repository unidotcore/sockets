/** SERVER */

'use strict';

const net = require('net');

const PORT = 8080;
const HOST = '127.0.0.1'

const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});
const server = net.createServer();
const exit = (socket) => {
    readline.close();
    server.close();
    socket.destroy();
    process.exit(0);
};

let messagesCount = 0;

server.on('connection', (socket) => {
    console.log(`Connected to: ${socket.remoteAddress}:${socket.remotePort}`);
    socket.setEncoding('ascii');
    socket.on('data', (data) => {
        if (data === 'stop') {
            exit(socket);
        }
        console.log(`[${++messagesCount}] Received: ${data}`);
        readline.question('Type something: ', (input) => {
            if (!socket.write(input)) {
                socket.pause();
                return;
            }
            console.log(`[${++messagesCount}] Sent: ${input}`);
            if (input === 'stop') {
                exit(socket);
            }
        });
    });
    socket.on('drain', () => socket.resume());
    socket.on('end', () => exit(socket));
});
server.maxConnections = 1;
server.listen(PORT, HOST);