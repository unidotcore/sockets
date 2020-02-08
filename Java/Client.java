/** CLIENT */

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

class Client {

	private static final int PORT = 8080;
	private static final String HOST = "127.0.0.1";

	public static void main(String args[]) throws Exception {
		byte[] buffer = new byte[1024];
		int messagesCount = 0;
		Socket socket = new Socket(HOST, PORT);
		DataInputStream serverInputStream = new DataInputStream(socket.getInputStream());
		DataOutputStream serverOutputStream = new DataOutputStream(socket.getOutputStream());
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String data;
		try {
			while (true) {
				System.out.print("Type something: ");
				data = inputReader.readLine();
				serverOutputStream.write(data.getBytes(StandardCharsets.US_ASCII));
				serverOutputStream.flush();
				System.out.println(String.format("[%d] Sent: %s", ++messagesCount, data));
				if (data.equals("stop")) {
					break;
				}
				Arrays.fill(buffer, (byte)0);
				int bytesRead = serverInputStream.read(buffer);
				data = new String(buffer, 0, bytesRead, StandardCharsets.US_ASCII);
				if (data.equals("stop")) {
					break;
				}
				System.out.println(String.format("[%d] Received: %s", ++messagesCount, data));
			}
		} finally {
			inputReader.close();
			serverInputStream.close();
			serverOutputStream.close();
			socket.close();
		}
	}
}