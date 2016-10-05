
public class InputHandler {

	String[] args;

	public InputHandler(String[] args) {
		this.args = args;
	}

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

	public String getDomainName() {
		String domainName = "";
		int indexOfDomain = args.length - 1;

		domainName = args[indexOfDomain];

		return domainName;
	}

	public String getTimeout() {
		String timeout = "";
		int argIndex;
		int i = 0;

		for (String str : args) {
			if (str.equals("-t")) {
				argIndex = i +1;
				timeout = args[argIndex];
			}
			i++;
		}
		
		return timeout;
	}
	
	public String getMaxRetries() {
		String maxRetries = "";
		int argIndex;
		int i = 0;

		for (String str : args) {
			if (str.equals("-r")) {
				argIndex = i +1;
				maxRetries = args[argIndex];
			}
			i++;
		}
		
		return maxRetries;
	}
	
	public String getPort() {
		String port = "";
		int argIndex;
		int i = 0;

		for (String str : args) {
			if (str.equals("-p")) {
				argIndex = i +1;
				port = args[argIndex];
			}
			i++;
		}
		
		return port;
	}
	
	public String getMxNx() {
		String mxNx = "";
		int argIndex;
		int i = 0;

		for (String str : args) {
			if (str.equals("-mx")) {
				mxNx = "Mx";
			}else if(str.equals("-nx")){
				mxNx = "Nx";
			}
			i++;
		}
		
		return mxNx;
	}
	
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

}
