/** SERVER */

use std::str;
use std::io::{stdin, stdout, Read, Write};
use std::net::{TcpListener, TcpStream, Shutdown};

const PORT: u16 = 8080;

fn main() {
    let listener: TcpListener = TcpListener::bind(format!("0.0.0.0:{}", PORT)).unwrap();
    match listener.accept() {
        Ok((socket, addr)) => {
            println!("Connected to: {}", addr);
            handle(socket);
        },
        Err(e) => println!("An error occurred while connecting to the client: {}", e)
    }
    drop(listener);

    fn handle(mut socket: TcpStream) {
        let mut messages_count: u32 = 0;
        let mut buffer: [u8; 1024] = [0 as u8; 1024];
        loop {
            match socket.read(&mut buffer) {
                Ok(bytes_read) => {
                    let mut data: String = String::from(str::from_utf8(&buffer[0..bytes_read]).unwrap());
                    if data == "stop" {
                        break;
                    }
                    messages_count += 1;
                    println!("[{}] Received: {}", messages_count, data);
                    print!("Type something: ");
                    let _ = stdout().flush();
                    data = String::new();
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
                },
                Err(_) => {
                    println!("An error occurred while reading.");
                    socket.shutdown(Shutdown::Both).unwrap();
                }
            }
        }
    }
}