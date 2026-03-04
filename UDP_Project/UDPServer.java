/*
 * Server App upon UDP
 * Victoria Lassner
 */ 
 
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Arrays;

public class UDPServer {
    public static void main(String[] args) throws IOException {
         
        DatagramSocket udpServerSocket = new DatagramSocket(5160);
        BufferedReader in = null;
        DatagramPacket udpPacket = null, udpPacket2 = null;
        String fromClient = null;
        String toClient = null;
        boolean morePackets = true;

        // add table to class
        inventory[] inventory_arr;
        inventory_arr = new inventory[6];
        inventory_arr[0] = new inventory("00001", "New Inspiron 15", 379.99, 157);
        inventory_arr[1] = new inventory("00002", "New Inspiron 17", 449.99, 128);
        inventory_arr[2] = new inventory("00003", "New Inspiron 15R", 549.99, 202);
        inventory_arr[3] = new inventory("00004", "New Inspiron 15z Ultrabook", 749.99, 315);
        inventory_arr[4] = new inventory("00005", "XPS 14 Ultrabook", 999.99, 261);
        inventory_arr[5] = new inventory("00006", "New XPS 12 UtrabookXPS", 1199.99, 178);

        byte[] buf = new byte[256];
        
        while (morePackets) {
            try {

                // receive UDP packet from client
                udpPacket = new DatagramPacket(buf, buf.length);
                udpServerSocket.receive(udpPacket);

                fromClient = new String(
                udpPacket.getData(), 0, udpPacket.getLength(), "UTF-8");
                                                        
                // get the response
                for(int i = 0; i < inventory_arr.length; i++)
                    if(inventory_arr[i].id.equals(fromClient)){
                        toClient = inventory_arr[i].toString();
                        break;
                    }
                         
                // send the response to the client at "address" and "port"
                InetAddress address = udpPacket.getAddress();
                int port = udpPacket.getPort();
                byte[] buf2 = toClient.getBytes("UTF-8");
                udpPacket2 = new DatagramPacket(buf2, buf2.length, address, port);
                udpServerSocket.send(udpPacket2);

            } catch (IOException e) {
                e.printStackTrace();
                morePackets = false;
            }
        }
  
        udpServerSocket.close();

    }
}

class inventory {
    String id;
    String item_description;
    double price;
    int inventory;

    public inventory(String id, String desc, double price, int inv) {
        this.id = id;
        this.item_description = desc;
        this.price = price;
        this.inventory = inv;
    }

    @Override
    public String toString() {
        // This defines exactly what gets sent to the client
        return id + "," + "     " + item_description + "," + "      " + price + "," + "     " + inventory;
    }
}