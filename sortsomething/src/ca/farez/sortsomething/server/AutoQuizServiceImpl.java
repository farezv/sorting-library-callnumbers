package ca.farez.sortsomething.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import ca.farez.sortsomething.client.AutoQuizService;

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
		
		return quiz;
	}

	public void addString(String callnum) {
		
		// Check for duplicates here
		CallNumber cn = new CallNumber(callnum);
		
		try {
            pm.makePersistent(cn);
        } finally {
            pm.close();
        }
	}
}
