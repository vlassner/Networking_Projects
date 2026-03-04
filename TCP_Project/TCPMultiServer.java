/*
 * Server App upon TCP
 * A thread is created for each connection request from a client
 * So it can handle Multiple Client Connections at the same time
 * Victoria Lassner
 */ 

import java.net.*;
import java.io.*;

public class TCPMultiServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverTCPSocket = null;
        boolean listening = true;

        System.out.println("Server is starting ...");

        try {
            serverTCPSocket = new ServerSocket(5160);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5160.");
            System.exit(-1);
        }
        System.out.println("Server is ready for requests.");
        while (listening){
               new TCPMultiServerThread(serverTCPSocket.accept()).start();
        }

        serverTCPSocket.close();
    }
}
