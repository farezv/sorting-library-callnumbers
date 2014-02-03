package ca.farez.sortsomething.client;

import com.google.gwt.user.client.ui.Button;

public class CallNumberButton extends Button {

	String[] levels;
	
	public CallNumberButton(String string) {
		
		this.setTitle(string);
		String[] levels = string.split("\\s+");
		
		for(int i = 0; i < levels.length; i++) {
			System.out.println("Level " + i + " " + levels[i]);
		}
		
	}

	public boolean isLevelEqual(CallNumberButton cnb, int levelIndex) {
		
		return this.levels[levelIndex] == cnb.levels[levelIndex];
	}
	
	/* Returns the "alphabetically earlier" callnumber after comparing each level
	 * */
	public CallNumberButton compareLevels(CallNumberButton cnb) {
		
		CallNumberButton[] sorted;
		int max = 0; // Smaller of the two call number levels
		
		if (this.levels.length < cnb.levels.length) {
			max = this.levels.length;
		} else 	max = cnb.levels.length;	
		
		
		int i = 0;
		while(this.isLevelEqual(cnb, i) && i != max) {
			i++;
		}
		
		if(i == max) {
			return this;
		} else {
			return compareChars(this,cnb,i); //	
		}
	}

	/* Compares individual characters based on the level being considered
	 * 
	 * Returns the alphabetically earlier call number
	 * */
	public CallNumberButton compareChars(CallNumberButton cnb1, CallNumberButton cnb2, int levelIndex) {
		
		char[] char1 = cnb1.levels[levelIndex].toCharArray();
		char[] char2 = cnb2.levels[levelIndex].toCharArray();
		
		
		if(levelIndex > 0) {
			int i = 0;
			
			int max = 0;
			if(char1.length < char2.length) {
				max = char1.length;
			} else max = char2.length;
			
			// Compare the first character of this level
			if (char1[i] == char2[i]) { // If they are equal, move on to subsequent number chars
				
				for(i = 1; i < max; i++) {
					// compare each subsequent number character
					
					if(char1[i] < char2[i]) {
						return cnb1;
					} else {
						if(char1[i] > char2[i]) return cnb2;
					}					
				}
				
			} else {
				// compare the first character which is an alphabet char and return CNB
			}
			
		} else{
			// Apply level0 aka sorting here!
		}
		return cnb2;
		
	}
}


