package ca.farez.sortsomething.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("autoQuiz")
public interface AutoQuizService extends RemoteService {
	
	String getQuiz(int size);
}
