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
	public ArrayList<String> sortedCallNums = new ArrayList<String>(); // Built-in sorted solution
	public ArrayList<String> userCallNums = new ArrayList<String>(); // User's solution, maybe sorted or unsorted
	private int mistakes; // Difference between our solution and user's
	
	CallNumberButton[] bucketSortedCnbs;
	// Two dimensional array list of array lists. The first indexes through buckets. Each bucket contains an ArrayList of Call Number Buttons
	public ArrayList<ArrayList<CallNumberButton>> bucketCollection = new ArrayList<ArrayList<CallNumberButton>>();
	
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
	public void builtInSortQuiz() {
		
		String[] cnums = new String[callNums.size()];
		for(int i = 0; i < cnums.length; i++) {
			cnums[i] = callNums.get(i);
		}

		Arrays.sort(cnums); // This sorts based on the first integer found
		System.out.println("\n*****SORTED SOLUTION*****\n");
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
		System.out.println("\n*****COMPARE SOLUTIONS*****\n"); 
		for(int i = 0; i < bucketSortedCnbs.length; i++) {
			if(bucketSortedCnbs[i] == null) {
        		topMenErrorMsg("Whoops! Cleanup in aisle " + i + "! \nHighly advanced janitor monkeys dispatched!");
        	} else
			if(!bucketSortedCnbs[i].getTitle().equals(userCallNums.get(i))) {
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
		
		return this.mistakes; // NEVER DIVIDED MISTAKES BY 2 BUT SIMPLY RETURNED IT!
	}
	
	/* Populates the quiz from user input
	 * */
	public int populate(String string) {
		int newCallNums = 0; // keeps track of any new call numbers added to the textArea
		System.out.println("\n****Populating Quiz****\n");
		// This is how a large input string turns into smaller call number strings.
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
		this.bucketSortedCnbs = new CallNumberButton[callNums.size()];
		return newCallNums;
	}

	/* Checks for duplicates in callNums before populating it.
	 * */
	public boolean isDuplicate(String string) {
		for(int i = 0; i < callNums.size(); i++) {
			//System.out.println("Does " + string + " equal " + callNums.get(i));
			if (string.equals(callNums.get(i))) {				
				return true;
			}
		}		
		return false;
	}
	
	public int getMistakes() {
		return this.mistakes;
	}
	
	/*	Sets indices in the bucket indices array to help with fine sorting the buckets later
	 * */
	public void fillBucketCollection(int size) {
		int firstLetter = 0;
		int bucketAtIndex = 0;
		System.out.println("\n*****SETTING BUCKET INDICES*****\n");
		
		ArrayList<CallNumberButton> cnbal = new ArrayList<CallNumberButton>();
		this.bucketCollection.add(cnbal);
		
		for(int i = 1; i < size; i++) {
			
			CallNumberButton A = new CallNumberButton(this.sortedCallNums.get(i - 1));
			CallNumberButton B = new CallNumberButton(this.sortedCallNums.get(i));
			char a = A.getTitle().charAt(firstLetter);
			char b = B.getTitle().charAt(firstLetter);
			
			// If the first characters of two call numbers are equal, create a button for the left CN, and add it to the bucket
			if (Character.toLowerCase(a) == Character.toLowerCase(b)) {				
				this.bucketCollection.get(bucketAtIndex).add(A);
			} else			
			if (Character.toLowerCase(a) < Character.toLowerCase(b) ) {				
				this.bucketCollection.get(bucketAtIndex).add(A);
				// Initialise the next bucket since we've come to the "boundary" of 2 buckets
				cnbal = new ArrayList<CallNumberButton>();
				this.bucketCollection.add(cnbal);
				bucketAtIndex++;
			}
			if(i == size - 1) {
				this.bucketCollection.get(bucketAtIndex).add(B);
			}
		}
	}
	
	/*	Print BucketCollection
	 * */
	public void printBucketCollection() {
		for(int i = 0; i < this.bucketCollection.size(); i++) {
			System.out.println("Bucket Index " + i);
			for(int j = 0; j < this.bucketCollection.get(i).size(); j++) {
				System.out.println("	" + this.bucketCollection.get(i).get(j).getTitle());
			}
		}
	}
	
	/* Experimental object oriented call number sorting
	 * */
	public void callNumberIntraBucketSorting() {
		
		int rank;
		CallNumberButton alphabeticallyEarlier;
                
        // Perform the "fine" sort. This is O(n^2).
        for(int i = 0; i < this.bucketCollection.size(); i++) { // For each bucket
        	        	
        	for(int j = 0; j < this.bucketCollection.get(i).size(); j++) {
        		
        		for(int k = this.bucketCollection.get(i).size() - 1; k > j; k--) {
        			System.out.println(this.bucketCollection.get(i).get(j).getTitle() + ".compareLevels(" + this.bucketCollection.get(i).get(k).getTitle() + ")\n");
        			alphabeticallyEarlier = this.bucketCollection.get(i).get(j).compareCallNumbers(this.bucketCollection.get(i).get(k));
        			if(alphabeticallyEarlier == this.bucketCollection.get(i).get(k)) {
        				rank = bucketCollection.get(i).get(j).getRank();
        				this.bucketCollection.get(i).get(j).setRank(++rank);
        			} else {
        				rank = bucketCollection.get(i).get(k).getRank();
        				this.bucketCollection.get(i).get(k).setRank(++rank);
        			}
        		}  	   		
        	}
        }
        
        fillFinalSolution();
        
        // Print sorted call number array
        for(int j = 0; j < bucketSortedCnbs.length; j++) {
        	if(bucketSortedCnbs[j] == null) {
        		topMenErrorMsg("Whoops! Cleanup in aisle " + j + "! \nHighly advanced janitor monkeys dispatched!");
        	} else System.out.println("Index " + j + " = " + bucketSortedCnbs[j].getTitle());
	    }
	}
	
	public void topMenErrorMsg(String msg) {
		Window.alert(msg);
	}
	
	public void fillFinalSolution() {
		int arrayIndex = 0;
		for(int i = 0; i < this.bucketCollection.size(); i++) {
			System.out.println("Bucket Index " + i);
			for(int j = 0; j < this.bucketCollection.get(i).size(); j++) {
				System.out.println("	" + this.bucketCollection.get(i).get(j).getTitle());
				this.bucketSortedCnbs[arrayIndex + bucketCollection.get(i).get(j).getRank()] = bucketCollection.get(i).get(j);
			}
			arrayIndex += bucketCollection.get(i).size();
		}
	}
	
	/*	Cleaning up all the data structures
	 * */
	public void clean() {
		
		this.sortedCallNums.clear();
		this.userCallNums.clear();
		this.bucketCollection.clear();
		this.bucketSortedCnbs = new CallNumberButton[callNums.size()];
	}
}
