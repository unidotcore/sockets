/** CLIENT */

'use strict';

const net = require('net');

const PORT = 8080;
const HOST = '127.0.0.1'

const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});
const client = new net.Socket();
const exit = () => {
    readline.close();
    client.destroy();
    process.exit(0);
};
const send = () => readline.question('Type something: ', (input) => {
    if (!client.write(input)) {
        client.pause();
        return;
    }
    console.log(`[${++messagesCount}] Sent: ${input}`);
    if (input === 'stop') {
        exit();
    }
});

let messagesCount = 0;

client.connect(PORT, HOST);
client.setEncoding('ascii');
client.on('data', (data) => {
    if (data === 'stop') {
        exit();
    }
	console.log(`[${++messagesCount}] Received: ${data}`);
	send();
});
client.on('drain', () => client.resume());
client.on('end', () => exit());

send();