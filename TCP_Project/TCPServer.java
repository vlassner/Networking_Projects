/*
 * Server App upon TCP
 */ 
//only server needs to be multithreaded for hw3+4
//flush() after print()

import java.net.*;
import java.io.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverTcpSocket = null;
        try {
            serverTcpSocket = new ServerSocket(5160);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5160.");
            System.exit(1);
        }

        Socket clientTcpSocket = null;

        while (true) {
          try {
            clientTcpSocket = serverTcpSocket.accept();
          } catch (IOException e) {
            System.err.println("Accept failed.");
            break;
          }

          PrintWriter cTcpSocketOut = new PrintWriter(
              clientTcpSocket.getOutputStream(), true);

          BufferedReader cTcpSocketIn = new BufferedReader(
              new InputStreamReader(clientTcpSocket.getInputStream()));

          String fromClient, toClient;
        
          while ((fromClient = cTcpSocketIn.readLine()) != null)
          {
            toClient =fromClient.toUpperCase();

            cTcpSocketOut.println(toClient);
                                 
            if (fromClient.equals("Bye."))
                break;
          }
   
          cTcpSocketOut.close();
          cTcpSocketIn.close();
          clientTcpSocket.close();
        }

        serverTcpSocket.close();
        System.exit(1);
    }
  
}
