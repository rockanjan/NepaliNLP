package np.anjan.data.corpus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenProcessor {
	public static String getSmoothedWord(String queryWord) {
		//for nepali numbers: convert them to english
		//String word = queryWord.toLowerCase();
		String word = queryWord;
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
		
		return word;
	}
	
	public static boolean isAllCaps(String word) {
		boolean result = true;
		for(int i=0; i<word.length(); i++) {
			char c = word.charAt(i);
			if(c < 'A' || c > 'Z') {
				result = false;
				break;
			}
		}
		return result;
	}
	
	public static boolean isInitialCap(String word) {
		char c = word.charAt(0); 
		if(c >= 'A' && c <= 'Z') {
			return true;
		}
		return false;
	}
	
	public static String[] prefixes(String word) {
		String prefix3 = "_NA_";
		String prefix4 = "_NA_";
		if(word.length() >= 3) {
			prefix3 = word.substring(0, 3);
		}
		if(word.length() >=4) {
			prefix4 = word.substring(0,4);
		}
		String[] prefixes = {prefix3, prefix4};
		return prefixes;
	}
	
	public static String[] suffixes(String word) {
		String suffix1= "_NA_";
		String suffix2 = "_NA_";
		String suffix3 = "_NA_";
		String suffix4 = "_NA_";
		int wordLength = word.length();
		suffix1 = word.substring(wordLength-1);
		if(wordLength >= 2) {
			suffix2 = word.substring(wordLength-2);
		}
		if(wordLength >= 3) {
			suffix3 = word.substring(wordLength-3);
		}
		if(wordLength >= 4) {
			suffix4 = word.substring(wordLength-4);
		}
		String[] suffixes = {suffix1, suffix2, suffix3, suffix4};
		return suffixes;
	}
	
	public static void main(String[] args) {
		System.out.println(TokenProcessor.getSmoothedWord("1980"));
		System.out.println(TokenProcessor.getSmoothedWord("198012"));
		System.out.println(TokenProcessor.getSmoothedWord("198,012"));
		System.out.println(TokenProcessor.getSmoothedWord("19:30"));
		System.out.println(TokenProcessor.getSmoothedWord("12-10-2012"));
		System.out.println(TokenProcessor.getSmoothedWord("12/10/2012"));
		System.out.println(TokenProcessor.getSmoothedWord("19.02"));
		System.out.println(TokenProcessor.getSmoothedWord("2-for-3"));
		System.out.println(TokenProcessor.isAllCaps("EBay"));
		System.out.println(TokenProcessor.isAllCaps("EBAY"));
		
		System.out.println(TokenProcessor.isInitialCap("eBay"));
		System.out.println(TokenProcessor.isInitialCap("EBay"));
		
		String word = "how";
		String[] prefixes = TokenProcessor.prefixes(word);
		System.out.println("3 prefix : " + prefixes[0]);
		System.out.println("4 prefix : " + prefixes[1]);	
		
		String[] suffixes = TokenProcessor.suffixes(word);
		System.out.println("1 suffix : " + suffixes[0]);
		System.out.println("2 suffix : " + suffixes[1]);
		System.out.println("3 suffix : " + suffixes[2]);
		System.out.println("4 suffix : " + suffixes[3]);		
		
	}
}
