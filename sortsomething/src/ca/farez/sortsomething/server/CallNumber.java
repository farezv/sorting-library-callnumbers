package ca.farez.sortsomething.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class CallNumber implements IsSerializable {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key cnKey;
	
	@Persistent
	private String callnum;
	
	public CallNumber(String cn) {
		this.callnum = cn;
	}
	
	public String getString() {
		return callnum;
	}
	
	public Key getCallNumKey() {
		return cnKey;
	}
}
