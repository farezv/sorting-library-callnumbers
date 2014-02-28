/*
 * Copyright 2013 Farez Vadsaria
 */
package ca.farez.sortsomething.client;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

/**
 * @author farez
 * 	
 * Generates a call number quiz given a list
 * @param populate() is given a string which is split by \\n
 */
public class Quiz {
	
	public ArrayList<String> callNums = new ArrayList<String>(); // Quiz input, maybe sorted or unsorted
	public ArrayList<String> sortedCallNums = new ArrayList<String>(); // Our solution, should be sorted
	public ArrayList<String> userCallNums = new ArrayList<String>(); // User's solution, maybe sorted or unsorted
	private int mistakes; // Difference between our solution and user's
	
	private int attempts;
	
	public Quiz() {		
		mistakes = 0;
		attempts = 0;
//		for(int i = 0; i < size; i++) {
//			userCallNums.add(null);
//		}
	}
	
	/* Sorting call number strings. Brace yourself. Winter is coming!
	 * */
	public void sortQuiz() {
		
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

	/* Compare our solution order to the one provided by the user.
	 * */
	public int compare() {
		
		this.mistakes = 0;
		System.out.println("*****COMPARE SOLUTIONS*****\n"); 
		for(int i = 0; i < sortedCallNums.size(); i++) {
			if(!sortedCallNums.get(i).equals(userCallNums.get(i))) {
				// These comparisons are failing after you make a mistake once.
				this.mistakes++;
				System.out.println("MISTAKES INCREMENTED\n" + sortedCallNums.get(i) + " vs " + userCallNums.get(i));
			}
		}
		
		//System.out.println("Mistakes compare() loop = " + mistakes + "\n");
		String msg = "Correct solution!";
		if (this.mistakes != 0) {
			// Mistakes divided by 2 because a single swap counts as two mistakes when comparing each element in 2 lists
			msg = "You have" + " mistakes!!";
			this.mistakes = this.mistakes / 2;
		}
		// Window.alert(msg); // can use confirm() instead if returned boolean desired to close quiz
		
		return this.mistakes; // NEVER DIVIDED MISTAKES BY 2 BUT SIMPLY RETURNED IT!
	}
	
	/* Populates the quiz from user input
	 * */
	public int populate(String string) {
		int newCallNums = 0; // keeps track of any new callnumbers added to the textArea
		System.out.println("****Populating Quiz****\n");
		// This is how a large string turns into smaller call number strings.
		String input[] = string.split("\n");
		
		// TODO Populate this randomly in case the "supervisor" gives an already sorted quiz
		
		for(int i = 0; i < input.length; i++) {
			System.out.println(input[i]);
			
			// Check duplicates!
			if(isDuplicate(input[i])) { // expensive call!
				System.out.println(input[i] + " is already in list!");
			} else {			
					callNums.add(input[i]);
					newCallNums++;
			}
		}
		return newCallNums;
	}

	/* Checks for duplicates in callNums before populating it.
	 * */
	public boolean isDuplicate(String string) {
		for(int i = 0; i < callNums.size(); i++) {
			System.out.println("Does " + string + " equal " + callNums.get(i));
			if (string.equals(callNums.get(i))) {				
				return true;
			}
		}		
		return false;
	}
	
	public int getMistakes() {
		return this.mistakes;
	}
}
