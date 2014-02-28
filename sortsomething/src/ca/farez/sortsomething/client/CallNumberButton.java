package ca.farez.sortsomething.client;

import com.google.gwt.user.client.ui.Button;

public class CallNumberButton extends Button {

	private String[] levels; // CallNumber broken into strings. For instance, "A100 TA2 2006" is ["A100" "TA2" "2006"]
	
	/* Constructor sets title and fills up the levels array
	 * */
	public CallNumberButton(String string) {
		
		this.setTitle(string);
		levels = string.split("\\s+"); // splitting by whitespace
		// ^^^ YOURE DECLARING IT AGAIN YOU FOOL!!
		for(int i = 0; i < levels.length; i++) {
			System.out.println("Level " + i + " " + levels[i]);
		}		
	}

	/* Compares the strings at levelIndex. Repeatedly called to assess the level of difference!
	 * */
	public boolean isLevelEqual(CallNumberButton cnb, int levelIndex) {
		
		return this.levels[levelIndex] == cnb.levels[levelIndex];
	}
	
	/* Returns the "alphabetically earlier" call number after comparing each level
	 * */
	public CallNumberButton compareLevels(CallNumberButton cnb) {
		
		CallNumberButton[] sorted;
		int max = 0; // Smaller of the two call number levels to avoid null pointer exceptions
		
		System.out.println("this.levels.length = " + this.levels.length);
		
		if (this.levels.length < cnb.levels.length) {
			max = this.levels.length;
		} else 	max = cnb.levels.length;	
		
		/* If have the following call numbers
		 * A100 TA2 B62 1999 and A100 TA2 2006
		 * The latter has 3 levels and the former 4, so max = 3
		 * Level 0 matches -> increment i (aka level index) i = 1
		 * Level 1 matches -> increment i					i = 2
		 * Level 2 doesn't match. Now we compare characters at level 2
		 */
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
		
		
		if(levelIndex > 0) { // Integers are considered as decimals at levels > 0
			int i = 0;
			
			int max = 0;
			// Making max equal the smaller of the two char sizes to avoid null pointer exceptions
			if(char1.length < char2.length) {
				max = char1.length;
			} else max = char2.length;
			
			// TODO Figure out a way to recognise the first numeric char
			// Compare the first character of this level. If they are equal, move on to subsequent chars
			if (char1[i] == char2[i]) {
				
				for(i = 1; i < max; i++) {
					// Compare each subsequent character
					
					if(Character.toLowerCase(char1[i]) < Character.toLowerCase(char2[i])) {
						return cnb1;
					} else {
						if(Character.toLowerCase(char1[i]) > Character.toLowerCase(char2[i])) return cnb2;
					}					
				}
				
			} else {
				// Compare the first character which is an alphabet char and return CNB
				if(Character.toLowerCase(char1[i]) < Character.toLowerCase(char2[i])) {
					return cnb1;
				} else return cnb2;
			}
			
		} else{
			// Apply level0 sorting here!
		}
		return cnb2;
		
	}
}


