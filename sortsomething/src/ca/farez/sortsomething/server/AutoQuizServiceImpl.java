package ca.farez.sortsomething.server;

import ca.farez.sortsomething.client.AutoQuizService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AutoQuizServiceImpl extends RemoteServiceServlet implements
		AutoQuizService {

	@Override
	public String getQuiz(int size) {
		// TODO Generate a random call number quiz of size given using the stored strings in datastore
		String quiz = null;
		
		return quiz;
	}

}
