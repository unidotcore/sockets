/** SERVER */

import java.io._
import scala.io._
import scala.util.control.Breaks._
import java.net.{ServerSocket, Socket}
import java.nio.charset.StandardCharsets

object Server {

    val Port: Int = 8080

    def main(args: Array[String]): Unit = {
        val buffer: Array[Byte] = new Array[Byte](1024)
        var messagesCount: Int = 0
        val server: ServerSocket = new ServerSocket(Port)
        val client: Socket = server.accept
        println(s"Connected to: ${client.getRemoteSocketAddress}")
        val clientInputStream: DataInputStream = new DataInputStream(client.getInputStream())
		val clientOutputStream: DataOutputStream = new DataOutputStream(client.getOutputStream())
        var data: String = ""
        try {
            breakable {
                while (true) {
                    val bytesRead: Int = clientInputStream.read(buffer)
                    data = new String(buffer, 0, bytesRead, StandardCharsets.US_ASCII)
                    if (data.equals("stop")) {
                        break
                    }
                    messagesCount += 1
                    println(s"[${messagesCount}] Received: ${data}")
                    print("Type something: ")
                    data = StdIn.readLine
                    clientOutputStream.write(data.getBytes(StandardCharsets.US_ASCII))
                    clientOutputStream.flush
                    messagesCount += 1
                    println(s"[${messagesCount}] Sent: ${data}")
                    if (data.equals("stop")) {
                        break
                    }
                }
            }
        } finally {
            clientInputStream.close
            clientOutputStream.close
            client.close
			server.close
        }
    }

}