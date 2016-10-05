
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UdpClient {
	
	public static void main(String args[]) throws Exception{
		
		//handling args[] 
		InputHandler ih = new InputHandler(args);
		
		//if the inputs follow a proper format 
		if(ih.checkForRequired()){
		
		
		//Reads the keyboard input 
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in) );
		
		DatagramSocket clientSocket = new DatagramSocket();
		
		//need to replace this by the class that we're going to create 
		InetAddress ipAddress = InetAddress.getByName("localhost");
		
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		System.out.println("Type a message and hit enter.");
		String sentence = inFromUser.readLine();
		
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 9876);
		clientSocket.send(sendPacket);
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("From Server: " + modifiedSentence);
		
		clientSocket.close();
		
		}
		//if the inputs do not follow a proper format
		else{
			
			System.out.println("Please place the required arguments correctly");
		}
	}
	

}
