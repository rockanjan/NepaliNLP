package np.anjan.data.corpus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/*
 * stores each row data of a sentence
 */
public class DataRow {
	private String word;
	private String pos;
	private String label;
	public List<String> representation;
	
	public DataRow(){}
	public DataRow(String line) {
		processLine(line);
	}
	public void processLine(String line){
		//processes a string of line, stores the field
		String[] splitted = line.split("(\\s+|\\t+)");
		if(splitted.length < 3){
			System.err.println("DataRow: Cannot process line : " + line);
			System.exit(1);
		}
		word = splitted[0].trim();
		pos = splitted[1].trim();
		label = splitted[2].trim();
		
		representation = new ArrayList<String>();
		for(int i=3; i<splitted.length; i++) {
			representation.add(splitted[i].trim());
		}		
	}
	
	
	public String getRowWithFeature() {
		StringBuilder sb = new StringBuilder();
		sb.append(word); //word as it is
		sb.append(" ");
		String smoothedWord = TokenProcessor.getSmoothedWord(word);
		sb.append(smoothedWord);
		sb.append(" ");
		sb.append(smoothedWord.toUpperCase()); //all uppercase
		sb.append(" ");
		sb.append(isInitialCap()); //is init cap?
		sb.append(" ");
		sb.append(isAllCaps()); //is init cap?
		sb.append(" ");
		String[] prefixes = TokenProcessor.prefixes(smoothedWord);
		for(String prefix: prefixes) {
			sb.append(prefix);
			sb.append(" ");
		}
		
		String[] suffixes = TokenProcessor.suffixes(smoothedWord);
		for(String suffix: suffixes) {
			sb.append(suffix);
			sb.append(" ");
		}
		
		for(String rep : representation) {
			sb.append(rep);
			sb.append(" ");
		}
		
		sb.append(label);
		return sb.toString();
	}
	
	public String isInitialCap() {
		if(TokenProcessor.isInitialCap(word)) {
			return "Y";
		}
		return "N";
	}
	
	public String isAllCaps() {
		if(TokenProcessor.isAllCaps(word)) {
			return "Y";
		}
		return "N";
	}
	
	
	public String getLabel() {
		return label;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}



	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String spaces(int size) {
		String returnString = "";
		for (int i=0; i<size; i++) {
			returnString += " ";
		}
		return returnString + " ";
	}
}
