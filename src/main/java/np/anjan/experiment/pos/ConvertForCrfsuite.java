package np.anjan.experiment.pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ConvertForCrfsuite {
	public static void main(String[] args) throws IOException {
		String inFile = "/home/anjan/work/nepali/pos/hmm25/test.txt";
		String outFile = inFile + ".crfsuite";
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		String splitDelim = "\\s+";
		
		File file = new File(inFile);
	    FileInputStream fis = new FileInputStream(file);
	    byte[] data = new byte[(int)file.length()];
	    fis.read(data);
	    fis.close();
	    //
	    String s = new String(data, "UTF-8");
	    String[] sentences = s.split("\n\n|\n\\s+\n");
	    int totalSentences = 0;
	    for(String sentence : sentences) {
	    	sentence = sentence.trim();
	    	if(sentence.equals("")) {
	    		continue;
	    	}
		    totalSentences++;
	    	String[] tokenLines = sentence.split("\n");
	    	for(int p=0; p<tokenLines.length; p++) {
	    		String tokenLine = tokenLines[p];
	    		String[] tokens = tokenLine.split(splitDelim);
	    		String word = tokens[0];
	    		String tag = tokens[1];
	    		String[] rep = null;
	    		if(tokens.length > 2) {
	    			int repLength = tokens.length - 2;
	    			rep = new String[repLength];
	    			for(int d=0; d<repLength; d++) {
	    				rep[d] = tokens[d+2];
	    			}
	    		}
	    		StringBuffer features = new StringBuffer();
	    		String smoothedWord = TokenProcessorNepali.getSmoothedWord(word);
	    		features.append(tag + "\t" + smoothedWord);
	    		
	    		if(rep != null) {
	    			for(int d=0; d<rep.length; d++) {
	    				features.append("\t" + "rep_" + d + "=" + rep[d]);
		    			for(int i=1; i<=1; i++) {
		    				if(p >= i) {
			    				features.append("\t" + "rep_" + d + "-" + i + "=" + tokenLines[p-i].split(splitDelim)[2+d] + "\t");
			    			}	
		    			}
		    			for(int i=1; i<=1; i++) {
		    				if(p <= tokenLines.length-i-1) {
			    				features.append("\t" + "rep_" + d + "+" + i + "=" + tokenLines[p+i].split(splitDelim)[2+d] + "\t");
			    			}	
		    			}
	    			}
	    		}
	    		
	    		//prev, next words
	    		for(int i=1; i<=2; i++) {
	    			if(p >= i) {
	    				features.append("\t" + "word_-" + i + "=" + tokenLines[p-i].split(splitDelim)[0] + "\t");
	    			}
	    		}
	    		for(int i=1; i<=2; i++) {
	    			if(p <= tokenLines.length-i-1) {
	    				features.append("\t" + "word_+" + i + "=" + tokenLines[p+i].split(splitDelim)[0] + "\t");	    				
	    			}
	    		}
	    		//prefix, suffix
	    		for(int i=1; i<=4; i++) {
	    			if(word.length() >= i) {
	    				features.append("\t" + "pre" + i + "=" + word.substring(0, i));	    				
	    			}
	    		}
	    		
	    		for(int i=1; i<=4; i++) {
	    			if(word.length() >= i) {
	    				features.append("\t" + "suf" + i + "=" + word.substring(word.length()-i, word.length()));	    				
	    			}
	    		}
	    		
	    		if(TokenProcessorNepali.containsNumber(word)) {
	    			features.append("\tHAS_NUM");
	    		}
	    		if(TokenProcessorNepali.containsAscii(word)) {
	    			features.append("\tHAS_ASCII");
	    		}
	    		if(word.contains("-")) {
	    			features.append("\tHAS_HYP");
	    		}
	    		
	    		pw.println(features.toString().replaceAll(":", "_COL_").replaceAll("\\\\", "_SLH_"));
	    	}
	    	pw.println();
	    	pw.flush();
	    
	    }
	    System.out.println("Total Sentences = " + totalSentences);
	    
	    pw.flush();
	    pw.close();
	}
}
