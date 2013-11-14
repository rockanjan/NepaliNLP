package np.anjan.experiment.pos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenProcessorNepali {
	//also changes "purnabiram" (nepali full stop ।) to .
	public static String getSmoothedWord(final String queryWord) {
		String word = queryWord;
		
		//for nepali numbers: convert them to english
		for(int i = 0; i <= 9; i++) {
			  word = word.replace("" + (char)(0x966+i), "" + i);
		}
		
		//try to match date
		Pattern p0 = Pattern.compile("[0-9]{4}"); //possible dates
		
		Pattern p1 = Pattern.compile("^-{0,1}[0-9]+\\.*[0-9]*"); //eg -9, 100, 100.001 etc
		Pattern p2 = Pattern.compile("^-{0,1}[0-9]*\\.*[0-9]+"); //eg. -.5, .5
		Pattern p3 = Pattern.compile("^-{0,1}[0-9]{1,3}[,[0-9]{3}]*\\.*[0-9]*"); //matches 100,000
		
		Pattern p4 = Pattern.compile("[0-9]+\\\\/[0-9]+"); // four \ needed, java converts it to \\
		Pattern p5 = Pattern.compile("[0-9]+:[0-9]+"); //ratios and time
		Pattern p6 = Pattern.compile("([0-9]+-)+[0-9]+"); // 1-2-3, 1-2-3-4 etc
		Pattern p7 = Pattern.compile("([0-9]+/)+[0-9]+"); // 1/2/3, 1/2/3/4 etc
		
		Matcher m0 = p0.matcher(word);
		Matcher m1 = p1.matcher(word);
		Matcher m2 = p2.matcher(word);
		Matcher m3 = p3.matcher(word);
		Matcher m4 = p4.matcher(word);
		Matcher m5 = p5.matcher(word);
		Matcher m6 = p6.matcher(word);
		Matcher m7 = p7.matcher(word);
		
		if(m0.matches()) {
			word = "_" + word.replaceAll("[0-9]", "D") + "_";
		} else if(m4.matches()) { 
			word = "_" + word.replaceAll("[0-9]+", "_NUM_"); //fractions
		} else if(m5.matches()) {
			word = "_" + word.replaceAll("[0-9]", "D") + "_"; //time and ratio
		} else if(m6.matches() || m7.matches()) {
			word = "_" + word.replaceAll("[0-9]", "D") + "_";
		} else if(m1.matches() || m2.matches() || m3.matches()) {
			word = "_NUM_";
		}
		
		word = word.replaceAll("" +
				"([0-9]+)|" + 
				"([0-9]+\\\\/[0-9]+)|" +
				"(([0-9]+-)+[0-9]+)|" +
				"([0-9]+:[0-9]+)|" +
				"(^-{0,1}[0-9]{1,3}[,[0-9]{3}]*\\.*[0-9]*)|" +
				"(^-{0,1}[0-9]*\\.*[0-9]+)|" +
				"(^-{0,1}[0-9]+\\.*[0-9]*)+"
				, "_NUM_"); //for something like 10-years-old, 2-for-3 etc
		
		
		if(queryWord.equals("।")) {
			word = ".";
		}
		
		//if it contains english words
		if(queryWord.matches(".*[a-zA-Z].*")) {
			word = "_ENGLISH_";
			//word = queryWord.replaceAll("[a-zA-Z]*", "ENG");
		}
		return word;
	}
	
	public static boolean containsNumber(String queryWord) {
		boolean containsNumber = false;
		String word = queryWord;
		//for nepali numbers: convert them to english
		for(int i = 0; i <= 9; i++) {
			  if(word.contains("" + (char)(0x966+i))) {
				  containsNumber = true;
				  break;
			  }
			  if(word.contains("" + i)) {
				  containsNumber = true;
				  break;
			  }
		}
		return containsNumber;
	}
	
	public static boolean containsAscii(String queryWord) {
		if(queryWord.matches(".*[a-zA-Z].*")) {
			return true;
		}
		return false;
	}
}
