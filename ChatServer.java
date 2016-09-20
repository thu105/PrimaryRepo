/**
*	UDP Server Program
*	Listens on a UDP port
*	Receives a line of input from a UDP client
*	Returns an upper case version of the line to the client
*
*	@author: Kevin Hewitt
* @teammate:  Andrew Krager 
@	version: 2.1
*/

import java.io.*;
import java.net.*;

class ChatServer
{
  public static void main(String args[]) throws Exception
  {

    DatagramSocket serverSocket = null;
    int port = 0, port1 = 0, port2 = 0;
    InetAddress IPAddress = null, IPAddress1 = null, IPAddress2 = null;
    String message = "";
    String response = "";
    DatagramPacket receivePacket = null;
    DatagramPacket sendPacket1 = null;
    DatagramPacket sendPacket2 = null;
    int state = 0;
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    byte[] messageBytes = new byte[1024];

	  
  try
		{
      serverSocket = new DatagramSocket(9876);
		}
	
	catch(Exception e)
		{
			System.out.println("Failed to open UDP socket");
			System.exit(0);
		}

    while(state<3)
    {
      receiveData = new byte[1024];
      sendData = new byte[1024];


      switch(state)
      {
        case 0: 
          //recieve information from 1st address, construct response
          DatagramPacket receivePacket =
             new DatagramPacket(receiveData, receiveData.length);
           serverSocket.receive(receivePacket);

          String message = new String(receivePacket.getData());

          //send response to client over DatagramSocket
          InetAddress IPAddress1 = receivePacket.getAddress();

          int port1 = receivePacket.getPort();

          String capitalizedMessage = message.toUpperCase();

          sendData = capitalizedMessage.getBytes();

          DatagramPacket sendPacket1 =
             new DatagramPacket(sendData, sendData.length, IPAddress1,
                               port1);

          serverSocket.send(sendPacket1);
          state = 1;
          break;
          //end case 0

        case 1:
          //recieve information from 2nd address, construct response
          DatagramPacket receivePacket =
             new DatagramPacket(receiveData, receiveData.length);
           serverSocket.receive(receivePacket);
           
          String message = new String(receivePacket.getData());

          //send response to client over DatagramSocket
          InetAddress IPAddress2 = receivePacket.getAddress();

          int port2 = receivePacket.getPort();

          String capitalizedMessage = message.toUpperCase();

          sendData = capitalizedMessage.getBytes();

          //create sepereate packets for the two clients
          DatagramPacket sendPacket1 =
             new DatagramPacket(sendData, sendData.length, IPAddress1,
                               port1);

             DatagramPacket sendPacket2 =
             new DatagramPacket(sendData, sendData.length, IPAddress2,
                               port2);

          serverSocket.send(sendPacket1);
          serverSocket.send(sendPacket2);
          state = 2;
          break;
        //end case 1

        case 2:
          //recieve the incomming message information
          DatagramPacket receivePacket =
             new DatagramPacket(receiveData, receiveData.length);
           serverSocket.receive(receivePacket);

          //if not goodbye...
          message = new String( receivePacket.getData());
          if (message.length()>=7 && message.substring(0,7).equals("Goodbye"))
          {
            state = 3;
            break;
          }
          //...relay message to the other client
          IPAddress = receivePacket.getAddress();
          port = receivePacket.getPort();
          //if the address comming in is the same as IP1
          if ((port == port1) && (IPAddress.equals(IPAddress1)))
          {
            //the person talking is IP1, set the address and port to the OTHER person
            IPAddress = IPAddress2;
            port = port2;
          }else{
            //otherwise, adress and port still = the OTHER person
            IPAddress = IPAddress1;
            port = port1;
          }
          //address and port now contain info of should-be recipient
          //pack all the info into a packet, send it to the recipient
          DatagramPacket sendPacket =
             new DatagramPacket(sendData, sendData.length, IPAddress,
                               port);
          //send the message to the other person


          break;
          //end case 2

      }//end switch

    }//end while

  }//end main

}//end class
