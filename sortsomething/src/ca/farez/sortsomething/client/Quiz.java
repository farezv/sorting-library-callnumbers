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
	public ArrayList<Integer> bucketIndices = new ArrayList<Integer>(); // Used to fine sort individual buckets later
	CallNumberButton[] bucketSortedCnbs;
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
	public void setBucketIndices(int size) {
		int charindex = 0;
		this.bucketIndices.add(charindex); // First index is always zero		
		System.out.println("\n*****SETTING BUCKET INDICES*****\n" + "BucketIndex " + charindex);
		
		for(int i = 1; i < size; i++) {
			
			char a = sortedCallNums.get(i - 1).charAt(charindex);
			char b = sortedCallNums.get(i).charAt(charindex);
			while(Character.toLowerCase(a) == Character.toLowerCase(b) && 
				  charindex < Math.min(sortedCallNums.get(i).length(), sortedCallNums.get(i-1).length())) {
				// check to see if chars are equal, if so, move on to the next char
				// TODO deal with number to alphabet comparisons
				charindex++;
				if(charindex < sortedCallNums.get(i - 1).length()) {
					a = sortedCallNums.get(i - 1).charAt(charindex);
				}
				if(charindex < sortedCallNums.get(i).length()) {
					b = sortedCallNums.get(i).charAt(charindex);
				}				
			}
			if (Character.toLowerCase(a) < Character.toLowerCase(b) ) {
				this.bucketIndices.add(i);
				System.out.println("BucketIndex " + i);
			}
		}
	}
	
	/* Experimental object oriented call number sorting
	 * */
	public void callNumberIntraBucketSorting() {
		int quizSize = this.callNums.size();
		System.out.println("quizSize = " + quizSize + "\n");
        CallNumberButton[] unsortedCnbs = new CallNumberButton[quizSize];        
        bucketSortedCnbs = new CallNumberButton[quizSize];
        CallNumberButton alphabeticallyEarlier;
        
        // Fill up the unsorted call number button array with partially sorted call number "clusters" from Arrays.sort()
        for(int i = 0; i < quizSize; i++) {
        	CallNumberButton cn = new CallNumberButton(this.sortedCallNums.get(i));
            unsortedCnbs[i] = cn;
        }
        
        // Perform the "fine" sort. This is O(n^2).
        for(int i = 0; i < this.bucketIndices.size(); i++) { // For each bucket
        	
        	// Set the bucket size for each bucket
        	int bucketSize;
        	// The last bucket size is the difference between the last bucket index and the size of the quiz
        	if(i == this.bucketIndices.size() - 1) {
        		bucketSize = this.sortedCallNums.size() - bucketIndices.get(i);
        	} else bucketSize = bucketIndices.get(i + 1); // The next index indicates bucket size
	        
        	System.out.println("bucketSize is " + bucketSize);
        	
        	for(int j = bucketIndices.get(i); j < bucketSize + bucketIndices.get(i); j++) { // For each call number
        		int k_index = bucketIndices.get(i) + bucketSize - 1;
        		int rank = bucketIndices.get(i);
        		
//        		if(i == bucketIndices.size() - 1) { 	 // If we're on the last bucket
//        			k_index = this.sortedCallNums.size() - 1; // make k_index equal the last index in the unsorted list
//        		} else k_index = bucketIndices.get(i + 1) - 1;
        		
        		for(int k = k_index; k > j; k--) { // Compare it to every other call number
	        		System.out.println(unsortedCnbs[j].getTitle() + ".compareLevels(" + unsortedCnbs[k].getTitle() + ")\n");
	        		alphabeticallyEarlier = unsortedCnbs[j].compareLevels(unsortedCnbs[k]);
			        
			        if(alphabeticallyEarlier == unsortedCnbs[k]) { // If the alphabetically earlier call number is k
			        	unsortedCnbs[j].setRank(rank++); // then increase j's rank
			        }	
        		}
	        	// once we're finished increasing the rank, place the j call number in the sorted array
        		bucketSortedCnbs[rank] = unsortedCnbs[j];
		    } // move on to the next j.
        }
        
        // Print sorted call number array TODO FIX BUG WHERE THE FIRST CN NEVER GETS PUT INTO ARRAY
        for(int j = 0; j < bucketSortedCnbs.length; j++) {
	        System.out.println("Index " + j + " = " + bucketSortedCnbs[j].getTitle());
	    }
	}
}
