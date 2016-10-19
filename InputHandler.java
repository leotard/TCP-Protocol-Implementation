package DnsPack;

import java.net.InetAddress;

public class InputHandler {

	private String[] args;
	public static final int TYPE_A_DEFAULT=0;
	public static final int TIMEOUT_DEFAULT = 5000; 
	public static final int MAX_RETRIES_DEFAULT = 3;
	public static final int PORT_DEFAULT = 53; 

	public InputHandler(String[] args) {
		this.args = args;
	}

	//getting the server 
	public String getServer() {
		String serverName = "";
		int indexOfChar;

		for (String str : args) {
			if (str.contains("@")) {
				indexOfChar = str.indexOf("@");
				serverName = str.substring(indexOfChar + 1);
			}
		}

		if (serverName.length() > 0) {

			return serverName;
		} else {
			return "server name wasn't specified";
		}
	}

	//getting the requested domain name 
	public String getDomainName() {
		String domainName = "";
		int indexOfDomain = args.length - 1;

		domainName = args[indexOfDomain];

		return domainName;
	}

	//getting the timeout 
	public int getTimeout() {
		String timeout = "";
		boolean isSet = false;
		int argIndex;
		int i = 0;

		for (String str : args) {
			if (str.equals("-t")) {
				argIndex = i +1;
				timeout = args[argIndex];
				isSet= true; 
			}
			i++;
		}
		if(isSet){
		return Integer.parseInt(timeout);
		}else {
		return 	TIMEOUT_DEFAULT;
		}
	}
	
	//getting the max entries 
	public int getMaxRetries() {
		String maxRetries = "";
		boolean isSet = false; 
		int argIndex;
		int i = 0;

		for (String str : args) {
			if (str.equals("-r")) {
				argIndex = i +1;
				maxRetries = args[argIndex];
				isSet = true; 
			}
			i++;
		}
		
		if(isSet){
		return Integer.parseInt(maxRetries);
		}else{
		return MAX_RETRIES_DEFAULT;	
		}
	}
	
	//getting the port 
	public int getPort() {
		String port = "";
		int argIndex;
		boolean isSet = false; 
		int i = 0;

		for (String str : args) {
			if (str.equals("-p")) {
				argIndex = i +1;
				port = args[argIndex];
				isSet = true; 
			}
			i++;
		}
		if(isSet){
		return Integer.parseInt(port);
		}else{
		return 	PORT_DEFAULT;
		}
	}

	//getting type 
	public int getAMxNs() {
		int AMxNs = TYPE_A_DEFAULT; 
		boolean isSet = false;
		int argIndex;
		int i = 0;

		for (String str : args) {
			if (str.equals("-A")) {
				AMxNs = TYPE_A_DEFAULT;
				break;
			}
			if (str.equals("-mx")) {
				AMxNs = 2;
				break;
			}
			if(str.equals("-nx")){
				AMxNs = 1;
				break;
			}
			i++;
		}
		
		return AMxNs;
	}
	
	// checks if the required fields are present 
	public boolean checkForRequired(){
		int count=0; 
		int lastIndex = args.length-1; 
		boolean domainNamePresent = true; 
		for(String str: args){
			if(str.contains("@")){
				count++;
			}
		}
		
		if(args[lastIndex].contains("@")){
			domainNamePresent = false;
		}
		
		if(count==1 && domainNamePresent == true){
			return true;
		}else{
			return false;
		}
	}
	
	public InetAddress stringToInetAddress () throws Exception {
		String ipAddr = getServer();
		int[] ipV4 = new int[4];
		int counter=0;
		for (String s : ipAddr.split("\\.") ) {
			ipV4[counter]= Integer.valueOf(s);
			counter++;
		}
		InetAddress addr1 = InetAddress.getByAddress(new byte [] {(byte)ipV4[0],(byte)ipV4[1],(byte)ipV4[2],(byte)ipV4[3]});
		return addr1;
	}
	

}
