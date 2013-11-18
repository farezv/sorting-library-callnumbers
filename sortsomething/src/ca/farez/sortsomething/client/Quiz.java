/**
 * 
 */
package ca.farez.sortsomething.client;

import java.util.ArrayList;

/**
 * @author farez
 * 	
 * Generates a call number quiz given a size
 * @param integer size, default param is 10
 */
public class Quiz {
	
	public ArrayList<String> callnums = new ArrayList<String>();
	
	public Quiz() {
		// TODO Check parameter
		
		// TODO for loop adding each individual string from text file
		callnums.add("A100 TA2 2006");
		callnums.add("B200 C20 1991");
		callnums.add("B200 C20 1988");
		callnums.add("B20 A20 2001");
		callnums.add("A2 D25.2 E22 1976");
		
	}
	
	public void fillQuiz() {
		
	}

}
