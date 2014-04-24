package ca.farez.sortsomething.server;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import ca.farez.sortsomething.client.AutoQuizService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AutoQuizServiceImpl extends RemoteServiceServlet implements
		AutoQuizService {

	private static final PersistenceManagerFactory PMF =
		      JDOHelper.getPersistenceManagerFactory("autoquiz-optional");
	private PersistenceManager pm = PMF.getPersistenceManager();
	
	@Override
	public String getQuiz(int size) {
		// TODO Generate a random call number quiz of size given using the stored strings in datastore
		String quiz = null;
		for(int i = 0; i < size; i++) {
			
		}
		
		return quiz;
	}

	public void addString(String callnum) {
		
		Key key = KeyFactory.createKey(CallNumber.class.getSimpleName(), callnum);
		CallNumber cn = new CallNumber(callnum);
		
		// Checking for duplicates here
		try {
			CallNumber cndb = (CallNumber) pm.getObjectById(key);		// Retrieve call number object
			if(cndb == null || cndb.getCallNumberString() != callnum) { // If it exists in the db, check its call number
				cn.setKey(key);											// set cn's key
				pm.makePersistent(cn);									// add it to the db
			} else System.out.println("CallNumber already in db");
        } catch(JDOObjectNotFoundException e) {
        	System.out.println("CallNumber not found in db, addint it now");
        	cn.setKey(key);
        	pm.makePersistent(cn);
        } catch(Exception e2) {
        	System.out.println("Exception caught on server: " + e2);
        }
	}
}
