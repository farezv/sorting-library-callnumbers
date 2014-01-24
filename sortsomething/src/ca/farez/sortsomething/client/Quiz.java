/*
 * Copyright 2013 Farez Vadsaria
 */
package ca.farez.sortsomething.client;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

/**
 * @author farez
 * 	
 * Generates a call number quiz given a size
 * @param integer size, default param is 10
 */
public class Quiz {
	
	public ArrayList<String> callNums = new ArrayList<String>();
	public ArrayList<String> sortedCallNums = new ArrayList<String>();
	public ArrayList<String> userCallNums = new ArrayList<String>();
	private int mistakes;
	
	public Quiz() {
		// TODO Check parameter
		
		// TODO for loop adding each individual string from text file
		callNums.add("A100 TA2 2006");
		callNums.add("B200 C20 1991");
		callNums.add("B200 C20 1988");
		callNums.add("B40 A20 2001");
		callNums.add("A2 D25.2 E22 1976");
		
		mistakes = 0;
	}
	
	// Sorting call number strings. Brace yourself. Winter is coming!
	public void sortQuiz() {
		
//		Collator myCollator = Collator.getInstance();
//		
//		// Sort and print sorted call number list
//		for(int i = 0; i < callnums.size(); i++) {
//			
//			String source = callnums.get(i);
//			String target = callnums.get(i+1);
//			if(myCollator.compare(source,target) < 0) {
//				sortedCallnums.add(i, source);
//				System.out.println("Earlier < Later");
//			} else {
//				sortedCallnums.add(i, target);
//				System.out.println("Later < Earlier");
//			}
		
		String[] cnums = new String[callNums.size()];
		for(int i = 0; i < cnums.length; i++) {
			cnums[i] = callNums.get(i);
		}

		Arrays.sort(cnums); // This sorts based on the first integer found
		System.out.println("*****SORTED SOLUTION*****\n");
		for(int i = 0; i < cnums.length; i++) {
			System.out.println(cnums[i]);
			sortedCallNums.add(i, cnums[i]);
		}
	
		//System.out.println(sortedCallNums);
	}

	// TODO Get the user order.
	public void compare() {
		mistakes = 0;
		System.out.println("*****COMPARE SOLUTIONS*****\n"); 
		for(int i = 0; i < sortedCallNums.size(); i++) {
			if(!sortedCallNums.get(i).equals(userCallNums.get(i))) {
				mistakes++;
			}
		}
		String msg = "Correct solution!";
		if (this.mistakes != 0) {
			// Mistakes divided by 2 because a single swap counts as two mistakes when comparing each element in 2 lists
			msg = "You have" + " mistakes!!";
		}
		Window.alert(msg); // can use confirm() instead if returned boolean desired to close quiz
	}
}
