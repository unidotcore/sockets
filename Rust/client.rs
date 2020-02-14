/** CLIENT */

use std::str;
use std::net::{TcpStream, Shutdown};
use std::io::{stdin, stdout, Read, Write};

const PORT: u16 = 8080;
const HOST: &str = "127.0.0.1";
const BUFFER_LENGTH: u16 = 1024;

fn main() {
    match TcpStream::connect(format!("{}:{}", HOST, PORT)) {
        Ok(socket) => handle(socket),
        Err(e) => println!("An error occurred while connecting to the server: {}", e)
    }

    fn handle(mut socket: TcpStream) {
        let mut messages_count: u32 = 0;
        let mut buffer: [u8; BUFFER_LENGTH] = [0 as u8; BUFFER_LENGTH];
        loop {
            print!("Type something: ");
            let _ = stdout().flush();
            let mut data: String = String::new();
            stdin().read_line(&mut data).expect("Unable to read user input");
            if !data.is_empty() {
                if let Some('\n') = data.chars().next_back() {
                    data.pop();
                }
                if let Some('\r') = data.chars().next_back() {
                    data.pop();
                }
            }
            socket.write(&data.as_bytes()[..]).unwrap();
            messages_count += 1;
            println!("[{}] Sent: {}", messages_count, data);
            if data == "stop" {
                break;
            }
            match socket.read(&mut buffer) {
                Ok(bytes_read) => {
                    data = String::from(str::from_utf8(&buffer[0..bytes_read]).unwrap());
                    if data == "stop" {
                        break;
                    }
                    messages_count += 1;
                    println!("[{}] Received: {}", messages_count, data);
                },
                Err(_) => {
                    println!("An error occurred while reading.");
                    socket.shutdown(Shutdown::Both).unwrap();
                }
            }
        }
    }
}