/** SERVER */

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

class Server {

	private static final int PORT = 8080;
	private static final int BUFFER_LENGTH = 1024;

	public static void main(String args[]) throws Exception {
		byte[] buffer = new byte[BUFFER_LENGTH];
		int messagesCount = 0;
		ServerSocket server = new ServerSocket(PORT);
		Socket socket = server.accept();
		System.out.println(String.format("Connected to: %s", socket.getRemoteSocketAddress()));
		DataInputStream clientInputStream = new DataInputStream(socket.getInputStream());
		DataOutputStream clientOutputStream = new DataOutputStream(socket.getOutputStream());
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String data;
		try {
			while (true) {
				int bytesRead = clientInputStream.read(buffer);
				data = new String(buffer, 0, bytesRead, StandardCharsets.US_ASCII);
				if (data.equals("stop")) {
					break;
				}
				System.out.println(String.format("[%d] Received: %s", ++messagesCount, data));
				Arrays.fill(buffer, (byte)0);
				System.out.print("Type something: ");
				data = inputReader.readLine();
				clientOutputStream.write(data.getBytes(StandardCharsets.US_ASCII));
				clientOutputStream.flush();
				System.out.println(String.format("[%d] Sent: %s", ++messagesCount, data));
				if (data.equals("stop")) {
					break;
				}
			}
		} finally {
			inputReader.close();
			clientInputStream.close();
			clientOutputStream.close();
			socket.close();
			server.close();
		}
	}
}