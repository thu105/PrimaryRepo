/**
*	UDP Client Program
*	Connects to a UDP Server
*	Receives a line of input from the keyboard and sends it to the server
*	Receives a response from the server and displays it.
*
*	@author: Andrew
@	version: 2.2
*/

import java.io.*;
import java.net.*;

class Red {
  public static void main(String args[]) throws Exception
  {

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    DatagramSocket clientSocket = new DatagramSocket();

    InetAddress IPAddress = InetAddress.getByName("localhost");

    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    int state = 0;
    String message = "HELLO red";
    String response = "";
    DatagramPacket sendPacket = null;
    DatagramPacket receivePacket = null;
    while (state < 3){
      sendData = new byte[1024];
      receiveData = new byte[1024];
      switch (state){
        case 0:
          sendData = message.getBytes();
          sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
          clientSocket.send(sendPacket);
          receivePacket = new DatagramPacket(receiveData, receiveData.length);
          clientSocket.receive(receivePacket);
          response = new String(receivePacket.getData());
          System.out.println("FROM SERVER:" + response);
          if (response.substring(0,3).equals("100")){
            state = 1;
          }
          else if (response.substring(0,3).equals("200")){
            state = 2;
          }
          break;
        case 1: // Waiting for notification that the second client is ready
          receivePacket = new DatagramPacket(receiveData, receiveData.length);
          clientSocket.receive(receivePacket);
          response = new String(receivePacket.getData());
          System.out.println("FROM SERVER:" + response);
          if(response.substring(0,3).equals("200")){
            state = 2;
          }
          //get message from user and send it to server
          //Chat mode
          //receive message from other client
          break;
        case 2:
          //Chat mode
          //receive message from other client
          //check for Goodbye message
          System.out.println("Type what you want to send to the other client:" + '\n');
          message = inFromUser.readLine();
          sendData = message.getBytes();
          sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
          clientSocket.send(sendPacket);
          receivePacket = new DatagramPacket(receiveData, receiveData.length);
          clientSocket.receive(receivePacket);
          response = new String(receivePacket.getData());
          System.out.println("FROM SERVER:" + response);
          if (response.length()>=7 && response.substring(0,7).equals("Goodbye")){
            state = 3;
            break;
          }
          break;
      }
    }

      clientSocket.close();
    }
}
