/*
ECSE 489 Experiment 2
Networking Programming and DNS
Ryan Martis		260465757
Richard Cheung	260494981
Wed08
Client.java
*/
package DNS; 

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.math.BigInteger;



public class Client {
	 
	//Format for ip address 

	private static DNS.Request request; 
	private static DNS.Packet packet;


	public static void main (String[] args) throws Exception{
		int argsArraySize = args.length;		
		//Get name from command line
		String name = args[argsArraySize-1]; 
		//Get IP address from command line
		String ip = args[argsArraySize-2]; 
		request = new Request(ip); 
		//Must have at least two field
		if (argsArraySize < 2) {
			System.out.println("ERROR 	Missing required arguments: [@server] [name] are required"); 
			return;
		}
		//If there's two or more field check for valid IP
		if (argsArraySize >= 2) {
			if(!request.ipAddressValidator()){
				return;
			} 
		}
		boolean error = true; 

		//initiate Request
		for (int i = 0 ; i< argsArraySize-2; i++ ) {
			//setting Request options
			 switch (args[i]) {
			 	case "-t":
			 		error= request.isNumeric(args[i+1]);
			 		//gets and sets the inputted value for the timeout option
					int tvalue = Integer.parseInt(args[i+1]);
			 		request.setTimeOut(tvalue);
			 		break;	
			 	case "-r":
			 		error= request.isNumeric(args[i+1]);
					//gets and sets the inputted max retry value
					int rValue = Integer.parseInt(args[i+1]);
			 		request.setMaxRetries(rValue); 	
			 		break;
			 	case "-p":
			 		error= request.isNumeric(args[i+1]);
			 		//gets and sets the inputted value for the port number
					int portValue = Integer.parseInt(args[i+1]);
			 		request.setPort(portValue); 		
			 		break;
			 	// cases where we set the type of the query
			 	case "-mx":
			 		request.setType(request.TYPE_MX_QUERY); 
			 		break;
			 	case "-ns":
			 		request.setType(request.TYPE_NS_QUERY);
			 		break;			
			 }
			 //if there's an non numerical value that is inputted into the command line return and print error
			 if (!error) {
			 	System.out.println("ERROR 	Incorrect Options Syntax: Options [-t] [-r] [-p] only take numeric values"); 
			 	return; 
			 }
		}
		//creates a client socket
		DatagramSocket clientSocket = new DatagramSocket();
		//initialize a packet with default values with the inputted arguments
		packet = new Packet(name, request.getType() ); 
		//sets the timeout value from the request object
		clientSocket.setSoTimeout(request.getTimeOut());
		//creates an empty packet for the received packet	
		byte[] receiveData = new byte[1024];
		//turn the created packet into a byte array packet
		byte[] packetData = packet.data();
		//A counter for the numbers of retries
		int counter = 0;
		// this boolean value is true when there is a response
		boolean passed = false; 
		// parse the String IP address into an InetAddress
		InetAddress ipAddress = request.stringToInetAddress();
		// Creates a DatagramPacket with the query packet, IP address and the port number
		DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, ipAddress, request.getPort());
		// Creates a DatagramPacket with the empty packet that was created earlier.
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		//initialization of the timer
		long start =0;
		long stop =0;
		//DNS client operating loop
		loop:
		while (true) {
			try {
				System.out.println("DnsClient sending request for "+name); 
				System.out.println("Server: "+ request.getIpAddr());
				System.out.println("Request Type: " + request.getStringType());
				//sets the start time
		        start = System.currentTimeMillis( );
		        //sends the query packet
				clientSocket.send(sendPacket);
				//waits for receive packet if there is no response packet an error SocketTimeoutException will be thrown
				clientSocket.receive(receivePacket);	
				//if there is a response up the receivePacket will be update with the answers and set boolean pass to true			
				passed= true;
			}
			//Will only go into the catch when the error SocketTimeoutException is thrown
			catch ( SocketTimeoutException e ){
				//if the counter is not the same as the maximum retry value continue and increase the counter variable by 1
				if (counter == request.getMaxRetries()){
					System.out.println("ERROR 	Maximum number of retires "+ request.getMaxRetries() + " exceeded");
					break loop;
				}
				System.out.println("Retrying...");
				counter++;
			}
			// if the response packet is received gets the stop time and prints the amount of time that is needed to
			// complete this query along with the number of tries
			if( passed ){
			    stop = System.currentTimeMillis( );
			    System.out.println("Response received after "+(double)(stop-start)/1000+" seconds "+ counter+" retries");		
			    //inteprets the receive packet.		
				packet.interpretAnswer(receivePacket.getData());
				break loop;
			}
		}
		clientSocket.close();
	}
}