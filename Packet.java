package DnsPack;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Packet {

		//header fields
		private short id;
		private short flags; 
		private short qdCount; 
		private short anCount; 
		private short nsCount; 
		private short arCount; 
		//Qtypes
		public final int TYPE_A_RR = 0X0001;
		public final int TYPE_NS_RR = 0x0002;
		public final int TYPE_MX_RR = 0x000f;
		//question fields
		private byte[] qName; 
		private short qType;
		private short qClass; 
		//RR fields
		private int answerAA;
		private int answerType;
		private int answerClass;
		private int answerTtl;
		private int answerRdLength;
		private String answerRdata;
		private String answerCname;
		private int answerPreference;
		private String answerExchange;
		//other
		private int qNameSize;
		private int cNameSize;
		private final int HEADER_SIZE = 12;
		private Random rng;
		private String name;
		private int type; 

		//Constructor
		public Packet(String name, int type) {
			this.name = name;
			this.type = type;
		}
		//method that initiates the header section
		private void packetHeader() {
			rng = new Random(); 
			this.id = (short) rng.nextInt(Short.MAX_VALUE+1);
			this.flags = 0x0100; // recursion enabled 
			this.qdCount = 0x0001;
			this.anCount= 0x0000;
			this.nsCount= 0x0000;
			this.arCount = 0x0000; 
		}
		//method that initiate the question section
		private void packetQuestion(String name, int type ) {
			this.qName = writeName(name);
			
			//checking the type 
			if(type == 0){
				this.qType = 0x0001;	
			}else if(type == 1){
				this.qType = 0x0002;
			}else {
				this.qType = 0x000f;
			}
			
			this.qClass = 0x0001;
		}
		
		//constructs the qname 
		private byte[] writeName (String name) {
			ByteBuffer data =  ByteBuffer.allocate(32);
			char[] buffer= new char[63];
			int dataSize = name.toCharArray().length;
			byte counter = 0 ;
			int charCounter = 0;
			for (char charData : name.toCharArray() ) {
				charCounter++;
				if ( charData == '.'){
					data.put(counter); // places the count of letters first 
					for (int j = 0; j<counter; j++ ) {
					 	data.put(String.valueOf(buffer[j]).getBytes(StandardCharsets.US_ASCII)); // then places the chars that were stored in the buffer array and puts it in a proper format 
					 } 
					buffer= new char[63]; // reinitialize the buffer to store the coming chars 
					counter =0; // resets the counter 
				}
				else{
					buffer[counter] = charData;
					counter ++;
				}//when the last value is reached aka here whent it reaches a in .ca
				if ( charCounter == dataSize) {
					data.put(counter);
					for (int j = 0; j<counter; j++ ) {
					 	data.put(String.valueOf(buffer[j]).getBytes(StandardCharsets.US_ASCII));
					 } 
					buffer= new char[63];
					counter =0;
				}
			}
			data.put(counter); // have to add 0 at the end 
			this.qNameSize = truncateBytebuffer(data).capacity(); 
			return truncateBytebuffer(data).array() ;
		}
		
		// method that truncated extra space byte buffer 
		private ByteBuffer truncateBytebuffer (ByteBuffer b){
			ByteBuffer copy = b.duplicate();
			b.flip(); 
			int size = 0;
			while (b.hasRemaining()) {
				b.get();
				size++;
			}
			ByteBuffer truncated = ByteBuffer.allocate(size);
			copy.flip();
			while (copy.hasRemaining()) {
				truncated.put(copy.get());
			}
			return truncated;
		}
		
		// Create byte[] in order to turn into a DatagramPacket. 
		public byte[] data() {
			String name = this.name; 
			int type = this.type; 
			packetHeader();
			packetQuestion(name,type);
			ByteBuffer data =  ByteBuffer.allocate(520);
			data.putShort(id);
			data.putShort(flags);
			data.putShort(qdCount);
			data.putShort(anCount);
			data.putShort(nsCount);
			data.putShort(arCount);
			data.put(qName);
			data.putShort(qType);
			data.putShort(qClass);
			return truncateBytebuffer(data).array();

		}
}
