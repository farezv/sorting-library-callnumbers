package ca.farez.sortsomething.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AutoQuizServiceAsync {
	
	public void getQuiz(int size, AsyncCallback<String> callback);
}
