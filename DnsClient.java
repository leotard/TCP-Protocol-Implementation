package DnsPack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class DnsClient {

public static void main(String args[]) throws Exception{
		
		//handling args[] 
		InputHandler ih = new InputHandler(args);
	
		//if the inputs follow a proper format 
		if(ih.checkForRequired()){
	
		//Creating the packets with the appropriate inputs 
		Packet packet = new Packet(ih.getDomainName(), ih.getAMxNs());
		
		//Creating the client socket and setting the picked options 
		DatagramSocket clientSocket = new DatagramSocket();
		clientSocket.setSoTimeout(ih.getTimeout());
		
		//need to replace this by the class that we're going to create 
		InetAddress ipAddress = ih.stringToInetAddress();
		
		//Creating packets where there's an empty buffer for the received packet and inserting the data of the formed packet  	
		byte[] sendData = packet.data();
		byte[] receiveData = new byte[1024];
		
		//Creating a DatagramPacket with the picked arguments 
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, ih.getPort());

		//Creating a DatagramPacket with the packet that will receive the Dns answer
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		//clientSocket.close();
		startConnection(ih,clientSocket,sendPacket,receivePacket);
		}
		//if the inputs do not follow a proper format
		else{
			
			System.out.println("Please place the required arguments correctly");
		}
		
	}

	public static void startConnection(InputHandler ih, DatagramSocket clientSocket, DatagramPacket sendPacket,DatagramPacket receivePacket) throws Exception{ //DatagramSocket clientSocket, DatagramPacket sendPacket,DatagramPacket receivePacket
		
				int counter = 0; //Initializing the counter too keep track of the number of retried events
				boolean success = false; // boolean to check if connection was well established 
				
				//initialization of the timer
				long startTimer =0;
				long stopTimer =0;
				//DNS client operating loop
				connection:
				while (true) {
					try {
						System.out.println("Server: "+ ih.getServer() +"\n" + 
										  "Domain name requesting: "+ih.getDomainName() +"\n"+
										  "Query Type: " + ih.getAMxNs()); 
						
						//sets the start time
						startTimer = System.currentTimeMillis( );
				        //sends the query packet
						clientSocket.send(sendPacket);
						//waits for receive packet if there is no response packet an error SocketTimeoutException will be thrown
						clientSocket.receive(receivePacket);	
						//if there is a response up the receivePacket will be update with the answers and set boolean pass to true			
						success= true;
					}
					//Will only go into the catch when the error SocketTimeoutException is thrown
					catch ( SocketTimeoutException e ){
						//if the counter is not the same as the maximum retry value continue and increase the counter variable by 1
						if (counter == ih.getMaxRetries()){
							System.out.println("SocketTimeoutException: Maximum number of retries has been exceeded");
							break connection;
						}else{
						System.out.println("SocketTimeoutException: Resending");
						counter++;
						}
					}
					// if the response packet is received gets the stop time and prints the amount of time that is needed to
					// complete this query along with the number of tries
					if( success ){
						stopTimer = System.currentTimeMillis( );
					    System.out.println("Success --- " +"\n"+
					    		 		    "Retries: "+ counter +"\n"+
					    				    "Time(milliSeconds): "+(double)(stopTimer-startTimer));	
					    //Interprets the receive packet.		
						//packet.interpretAnswer(receivePacket.getData());
						break connection;
					}
				}
				clientSocket.close();		
		
		
		
	}
}
