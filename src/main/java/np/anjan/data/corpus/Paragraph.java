package np.anjan.data.corpus;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Paragraph implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4319552732753623686L;
	private String rawText;
	public static boolean debug = false;
	
	public Paragraph() {
		
	}
	
	public static String unEscapeString(String s){
	    StringBuilder sb = new StringBuilder();
	    for (int i=0; i<s.length(); i++)
	        switch (s.charAt(i)){
	            case '\n': sb.append("\\n"); break;
	            case '\t': sb.append("\\t"); break;
	            // ... rest of escape characters
	            default: sb.append(s.charAt(i));
	        }
	    return sb.toString();
	}
	
	public void setRawText(String rawText) {
		this.rawText = rawText.trim();
		//System.out.println(this.rawText);
	}
	
	public void mergeMultiline() {
		//if the paragraph has multi lines, make it single line
		//System.out.println(unEscapeString(rawText));
		rawText = rawText.replaceAll("\\n", " ");
		//System.out.println(unEscapeString(rawText));
	}
	
	public String getRawText() {
		return rawText;
	}
	
	public String getCleanText() {
		//TODO: cleaning code
		String cleanText = rawText;
		return cleanText;
	}
	
	//Percy Liang's technique (in his thesis, page 51), for filtering sentences 
	//(a paragraph should contain at least 90% of lowercase characters and white space)
	public boolean resemblesRealSentence() {
		double THRESHOLD = 0.9;
		int allCount = rawText.length();
		int lowercaseCount = 0;
		for(int i=0; i<allCount; i++) {
			char c = rawText.charAt(i);
			if(c ==' ' || (c >= 'a' && c <= 'z')) {
				lowercaseCount++;
			}
		}
		double fraction = 1.0 * lowercaseCount / allCount;
		if(debug) {
			System.out.println(rawText);
			System.out.println("fraction = " + fraction);
		}
		if(fraction >= THRESHOLD) {
			return true;
		}
		return false;
	}
	
	//aggressive: 
	public boolean passesAgressiveCleaning() {
		boolean passes = true;
		int THRESHOLD = 2;
		int numberOfTokens = rawText.split("\\W+").length;
		//if first line of the paragraph does not have at least 5 tokens, whole paragraph is disregarded (probably a table header)
		if(numberOfTokens < THRESHOLD) {
			passes = false;
		}

		//should not start with hyphen - or (
		if(rawText.trim().startsWith("-") || rawText.trim().startsWith("(") || rawText.trim().startsWith("*")) {
			passes = false;
		}
		
		//for example, 1), 2), a) etc
		Pattern p = Pattern.compile("^[a-z0-9]*\\).*");
		Matcher m = p.matcher(rawText.trim());
		
		if(m.matches()) {
			passes = false;
		}
		//should end in either . or ? or ! or ."
		if(! (rawText.trim().endsWith(".") || rawText.trim().endsWith("?") || rawText.trim().endsWith("!") || 
				rawText.trim().endsWith(".\"") || rawText.trim().endsWith("?\"") || rawText.trim().endsWith("!\"")) ) {
			passes = false;
		}
		return passes;
	}
	
	
	public static void main(String[] args) {
		Paragraph.debug = true;
		Paragraph p = new Paragraph();
		//p.rawText = "composite index of 500 stocks fell from 0.89 to 665.69";
		p.rawText = "\"The focus is back on Mexican fundamentals,\" " +
				"said Lars Schonander, head of researcher at Santander in Mexico City. " +
				"\"You have a continuing decline in inflation, a stronger-than-expected GDP growth figure " +
				"and the lack of any upward move in U.S. rates.\"";
		p.rawText = "b) Future of Community policy on state aid in connection with the situation regarding ratifications of the OECD Aereement.";
		System.out.println(p.passesAgressiveCleaning());
		//System.out.println(p.resemblesRealSentence());
		
		p.rawText = "Thi is \n a test";
		//System.out.println(p.resemblesRealSentence());
		
	}
}
