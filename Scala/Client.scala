/** CLIENT */

import java.io._
import scala.io._
import scala.util.control.Breaks._
import java.net.{ServerSocket, Socket}
import java.nio.charset.StandardCharsets

object Client {

    val Port: Int = 8080
    val Host: String = "127.0.0.1"

    def main(args: Array[String]): Unit = {
        val buffer: Array[Byte] = new Array[Byte](1024)
        var messagesCount: Int = 0
        val socket: Socket = new Socket(Host, Port)
        val serverInputStream: DataInputStream = new DataInputStream(socket.getInputStream())
        val serverOutputStream: DataOutputStream = new DataOutputStream(socket.getOutputStream())
        var data: String = ""
        try {
            breakable {
                while (true) {
                    print("Type something: ")
                    data = StdIn.readLine
                    serverOutputStream.write(data.getBytes(StandardCharsets.US_ASCII))
                    serverOutputStream.flush
                    messagesCount += 1
                    println(s"[${messagesCount}] Sent: ${data}")
                    if (data.equals("stop")) {
                        break
                    }
                    val bytesRead: Int = serverInputStream.read(buffer)
                    data = new String(buffer, 0, bytesRead, StandardCharsets.US_ASCII)
                    if (data.equals("stop")) {
                        break
                    }
                    messagesCount += 1
                    println(s"[${messagesCount}] Received: ${data}")
                }
            }
        } finally {
            serverInputStream.close
            serverOutputStream.close
            socket.close
        }
    }

}