/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Victoria Lassner
 */ 

import java.net.*;
import java.io.*;
import java.util.Date;

public class TCPMultiServerThread extends Thread {
    private Socket clientTCPSocket = null;

    public TCPMultiServerThread(Socket socket) {
        super("TCPMultiServerThread");
        clientTCPSocket = socket;
    }

    public void run() {

        try {
            PrintWriter cSocketOut = new PrintWriter(
                clientTCPSocket.getOutputStream(), true);

            BufferedReader cSocketIn = new BufferedReader(
                new InputStreamReader(clientTCPSocket.getInputStream()));

        String inputFromClient;
        // while loop: repeat until null is read
        while ((inputFromClient = cSocketIn.readLine()) != null) {
            System.out.println("Received request line: " + inputFromClient);
            String reqLine = inputFromClient;

            // read/display headers
            String headerLine;
            while ((headerLine = cSocketIn.readLine()) != null && !headerLine.isEmpty()) {
                System.out.println("Header: " + headerLine);
            }

            // parse request pieces: method and file name
            String[] httpArr = reqLine.split(" ");
            if (httpArr.length < 2) continue; 

            String method = httpArr[0];
            String name = httpArr[1].replace("/", "");
            File file = new File(name);
            String reqStatus;

            // checks input from client to match to request status
            if (!method.equals("GET")) {
                reqStatus = "HTTP/1.1 400 Bad Request";
            } else if (!file.exists() || file.isDirectory()) {
                reqStatus = "HTTP/1.1 404 Not Found";
            } else {
                reqStatus = "HTTP/1.1 200 OK";
            }

            // HTTP response
            cSocketOut.print(reqStatus + "\r\n");
            cSocketOut.print("Date: " + new Date().toString() + "\r\n");
            cSocketOut.print("Server: Voyager/1.0\r\n");
            cSocketOut.print("\r\n");
            
            // send body if 200 OK
            if (reqStatus.equals("HTTP/1.1 200 OK")) {
                try (BufferedReader readerFile = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = readerFile.readLine()) != null) {
                        cSocketOut.print(line + "\r\n");
                    }
                }
                // four extra empty lines at the end of the body
                cSocketOut.print("\r\n\r\n\r\n\r\n");
            }

            // flush socket
            cSocketOut.flush();
        }

        cSocketOut.close();
        cSocketIn.close();
        clientTCPSocket.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}