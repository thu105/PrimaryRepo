/**
*	UDP Server Program
*	Listens on a UDP port
*	Receives a line of input from a UDP client
* Receives a line from a 2nd client
*	Establish communication between clients untill one enters "Goodbye"
*
*	@author: Kevin Hewitt
* @Co-author: Aaron Weinberg
* @teammate:  Andrew Krager 
@	version: 2.3
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
    String serverReply = "";
    DatagramPacket receivePacket = null;
    DatagramPacket receivePacket2 = null;
    DatagramPacket sendPacket = null;
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
           DatagramPacket receivePacket1 =
             new DatagramPacket(receiveData, receiveData.length);
           serverSocket.receive(receivePacket1);

          message = new String( receivePacket1.getData());
          if (message.length()>=9 && (message.substring(0,9).equals("HELLO red") || message.substring(0,10).equals("HELLO blue")))
          {
                      //send response to client over DatagramSocket
          IPAddress1 = receivePacket1.getAddress();

          port1 = receivePacket1.getPort();

          serverReply = "100";

          sendData = serverReply.getBytes();

          sendPacket1 =
             new DatagramPacket(sendData, sendData.length, IPAddress1,
                               port1);

          serverSocket.send(sendPacket1);
          state = 1;
          break;
          //end case 0
          }
          else
          {
            System.out.println("502 5.5.2 Error: command not recognized");
            break;
          }

        case 1:
          //recieve information from 2nd address, construct response
          receivePacket =
             new DatagramPacket(receiveData, receiveData.length);
           serverSocket.receive(receivePacket);

          //send response to client over DatagramSocket
          IPAddress2 = receivePacket.getAddress();

          port2 = receivePacket.getPort();

          serverReply = "200";

          sendData = serverReply.getBytes();

             sendPacket2 =
             new DatagramPacket(sendData, sendData.length, IPAddress2,
                               port2);

             sendPacket1 =
             new DatagramPacket(sendData, sendData.length, IPAddress1,
                               port1);

          serverSocket.send(sendPacket1);
          serverSocket.send(sendPacket2);
          state = 2;
          break;
        //end case 1

        case 2:
          //recieve the incomming message information
          receivePacket2 =
             new DatagramPacket(receiveData, receiveData.length);
           serverSocket.receive(receivePacket2);

           if (IPAddress == sendPacket2.getAddress())
           {
            System.out.println("The wrong person is talking");
           }
           else
           {


            //if not goodbye...
            message = new String( receivePacket.getData());
            if (message.length()>=7 && message.substring(0,7).equals("Goodbye"))
            {
              serverReply = "Goodbye";

          sendData = serverReply.getBytes();

              sendPacket2 =
             new DatagramPacket(sendData, sendData.length, IPAddress2,
                               port2);

             sendPacket1 =
             new DatagramPacket(sendData, sendData.length, IPAddress1,
                               port1);

          serverSocket.send(sendPacket1);
          serverSocket.send(sendPacket2);
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




            sendData = message.getBytes();




            //address and port now contain info of should-be recipient
            //pack all the info into a packet, send it to the recipient
            sendPacket =
               new DatagramPacket(sendData, sendData.length, IPAddress,
                                 port);
            //send the message to the other person

            serverSocket.send(sendPacket);
          }
          //end "else"


            break;
            //end case 2

      }//end switch

    }//end while

  }//end main

}//end class