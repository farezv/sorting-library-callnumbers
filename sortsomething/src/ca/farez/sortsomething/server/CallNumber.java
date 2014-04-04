package ca.farez.sortsomething.server;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class CallNumber {
	
	@Persistent
	private String callnum;
	
	public CallNumber(String cn) {
		this.callnum = cn;
	}
}
