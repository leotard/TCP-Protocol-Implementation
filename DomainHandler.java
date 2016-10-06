

import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class DomainHandler {
	String dnsServer;
	String domainName;
	String[] type = { "A" };

	public DomainHandler(String dnsServer, String domainName, String type) {
		this.dnsServer = dnsServer;
		this.domainName = domainName;
		this.type[0] = type;
	}

	public DomainHandler(String dnsServer, String domainName) {
		this.dnsServer = dnsServer;
		this.domainName = domainName;
	}

	public String[] getIpAdress() {
		Set<String> domainList = new TreeSet<String>();
		try{
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
			DirContext dnsInterface = new InitialDirContext(env);
			Attributes dnsInfo = dnsInterface.getAttributes(domainName, type); 
			if(dnsInfo!=null){
				NamingEnumeration<?> dnsInfoIterator = dnsInfo.get(type[0]).getAll();
				while(dnsInfoIterator.hasMoreElements()){
					domainList.add(dnsInfoIterator.next().toString());
				}
			}
			
		} catch(NamingException e){
			//need to handle possible errors 
		}
		return domainList.toArray(new String[domainList.size()]);

	}

}
