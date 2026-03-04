/*
 * Client App upon TCP
 *
 * Victoria Lassner
 *
 */ 


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        // Ask user for input for DNS name/ip
        System.out.println("Enter the DNS name/IP of HTTP Server:");
        String host = sysIn.readLine();


        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
                  
        try {
            //calc for RTT for TCP connnection
            long startTime = System.currentTimeMillis();
            tcpSocket = new Socket(host, 5160); //can hardcode port number
            long endTime = System.currentTimeMillis();

            //connection message & rtt calculation
            long rtt = endTime - startTime;
            System.out.println("Tcp connection established. RTT is " + rtt + " ms.");

            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            
            boolean query_is_true = true;

            while(query_is_true){
                //asks user for method,name of file, version of HTTP, User-agent
                System.out.println("Please enter a method type (GET, PUT, etc):");
                String method = sysIn.readLine();
                System.out.println("Please enter the name of the htm file (.htm):");
                String name = sysIn.readLine();
                System.out.println("Please enter the HTTP version (ex. 1.1):");
                String version = sysIn.readLine();
                System.out.println("Please enter the User-Agent (Chrome1.6):");
                String agent = sysIn.readLine();
                String manualEmptyLine = sysIn.readLine();

                //puts it into a single variable; format based on requirments
                String httpRequest = method + " /" +
                  name + " HTTP/" + version + "\r\n" +
                   "Host: " + host + "\r\n" +
                    "User-Agent: " + agent +
                     "\r\n" + manualEmptyLine + "\r\n";
                
                System.out.println(httpRequest);
                
                //calculate query response time for http
                long reqStartTime = System.currentTimeMillis();
                socketOut.print(httpRequest);
                socketOut.flush();

                //reads in status of http & saves end time for query response
                String statusHTTP = socketIn.readLine();
                long reqEndTime = System.currentTimeMillis();
                
                //calculates rtt time for query response
                long rttReq = reqEndTime - reqStartTime;

                //prints out rtt and response status to user
                System.out.println("Query Response RTT = " + rttReq + " ms");
                System.out.println("Response Status: " + statusHTTP);

                // initalizes header line & verifies that is not null/empty before displaying to user
                String header;
                while ((header = socketIn.readLine()) != null && !header.isEmpty()){
                    System.out.println("Header: " + header);
                }
                // if '200 OK', will be saved to new file called "new_" + name
                if(statusHTTP != null && statusHTTP.contains("200 OK")){

                    //writes 200 OK to new file
                    BufferedWriter writer = new BufferedWriter(new FileWriter("new_" + name));
                    String lines;
                    while((lines = socketIn.readLine()) != null){
                        System.out.println(lines);
                        writer.write(lines);
                        writer.newLine();

                        if (lines.equalsIgnoreCase("</html>")){
                            break;
                        }
                    }
                    System.out.print("\r\n\r\n\r\n\r\n");

                    //4 empty lines at the end of 200 OK message
                    socketIn.readLine();
                    socketIn.readLine();
                    socketIn.readLine();
                    socketIn.readLine();

                    writer.close();
                    System.out.println("The entity body for '200 OK' is saved to new_" + name);

                }

                // asks if user wants to quit/continue
                System.out.println("Would you like to continue? 'yes'/'no'.");
                String userInput = sysIn.readLine();
                //Used instead of userInput.equalsIgnoreCase("no") for program to exit unles given a yes
                //was an issue in previous hw
                if(!userInput.equalsIgnoreCase("yes")){
                    query_is_true = false;
                }

            }
            
            System.out.println("Client Connection Terminated.");
            socketOut.close();
            socketIn.close();
            sysIn.close();
            tcpSocket.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host);
            System.exit(1);
        }

    }
}
