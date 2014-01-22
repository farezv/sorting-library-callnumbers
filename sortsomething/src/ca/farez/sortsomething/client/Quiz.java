/*
 * Copyright 2013 Farez Vadsaria
 */
package ca.farez.sortsomething.client;

import java.text.Collator;
import java.util.ArrayList;

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
	
	public Quiz() {
		// TODO Check parameter
		
		// TODO for loop adding each individual string from text file
		callNums.add("A100 TA2 2006");
		callNums.add("B200 C20 1991");
		callNums.add("B200 C20 1988");
		callNums.add("B20 A20 2001");
		callNums.add("A2 D25.2 E22 1976");
		
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
	
		System.out.println(sortedCallNums);
	}

	// TODO Get the user order.
	public void compare() {
		
				
	}
}
