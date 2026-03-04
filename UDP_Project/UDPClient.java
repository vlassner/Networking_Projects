/*
 * Client App upon UDP
 * Victoria Lassner
 */ 
// System.in
// System.out

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        // Print statement for entering DNS/IP
        System.out.println("Please input the DNS or IP of the machine.");
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String host = sysIn.readLine();
        
        //Inventory array for client
        inventory[] inventory_arr;
        inventory_arr = new inventory[6];
        inventory_arr[0] = new inventory("00001", "New Inspiron 15");
        inventory_arr[1] = new inventory("00002", "New Inspiron 17");
        inventory_arr[2] = new inventory("00003", "New Inspiron 15R");
        inventory_arr[3] = new inventory("00004", "New Inspiron 15z Ultrabook");
        inventory_arr[4] = new inventory("00005", "XPS 14 Ultrabook");
        inventory_arr[5] = new inventory("00006", "New XPS 12 UtrabookXPS");

        // creat a UDP socket
        DatagramSocket udpSocket = new DatagramSocket();

        
        String fromServer;
        String fromUser = "";

        while (true) {

          //Display message to user to id input & checks response against array values
          boolean isValid = false;

          while(!isValid){
            System.out.println("Please enter Item ID:");
            fromUser = sysIn.readLine();
            if (fromUser == null) break;
            for(int i = 0; i < inventory_arr.length; i++)
                if(inventory_arr[i].id.equals(fromUser)){
                    isValid = true;
                    break;
                }
                
            if(!isValid){
                System.out.println("Invalid ID. Please try again.");
            }        
           }


          //display user input
          System.out.println("From Client: " + fromUser);
          
          //time before packets are sent
          long startTime = System.currentTimeMillis();
          
          // send request
          InetAddress address = InetAddress.getByName(host);
			 byte[] buf = fromUser.getBytes();
          DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, address, 5160);
          udpSocket.send(udpPacket);
    
          // get response
          byte[] buf2 = new byte[256];
          DatagramPacket udpPacket2 = new DatagramPacket(buf2, buf2.length);
          udpSocket.receive(udpPacket2);

          //Time packets are recieved from server
          long endTime = System.currentTimeMillis();

          //Calculation for RTT
          long rtt = endTime - startTime;

          // display response
          fromServer = new String(udpPacket2.getData(), 0, udpPacket2.getLength());
          System.out.println("From Server: Item ID      Description         Price       Quantity        RTT of Query");
          System.out.println("            " + fromServer + ",       " + rtt);
          
          System.out.println("Would you like to continue? 'yes'/'no'.");
          fromUser = sysIn.readLine();
          if (fromUser == null || fromUser.equals("no"))
              break;
        }
		
        udpSocket.close();
    }
}


class inventory {
    String id;
    String item_description;

    public inventory(String id, String desc) {
        this.id = id;
        this.item_description = desc;
    }
}