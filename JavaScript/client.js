/** CLIENT */

'use strict';

const net = require('net');

const PORT = 8080;
const HOST = '127.0.0.1'

const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});
const socket = new net.Socket();
const exit = () => {
    readline.close();
    socket.destroy();
    process.exit(0);
};
const send = () => readline.question('Type something: ', (input) => {
    if (!socket.write(input)) {
        socket.pause();
        return;
    }
    console.log(`[${++messagesCount}] Sent: ${input}`);
    if (input === 'stop') {
        exit();
    }
});

let messagesCount = 0;

socket.connect(PORT, HOST);
socket.setEncoding('ascii');
socket.on('data', (data) => {
    if (data === 'stop') {
        exit();
    }
	console.log(`[${++messagesCount}] Received: ${data}`);
	send();
});
socket.on('drain', () => socket.resume());
socket.on('end', () => exit());

send();