package np.anjan.data.corpus;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Sentence extends ArrayList<DataRow>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(Sentence.class.getName());
	public Sentence(){
		super();		
	}
	public String debugString(){
		StringBuilder sbWord = new StringBuilder();
		StringBuilder sbPos = new StringBuilder();
		StringBuilder sbLabel = new StringBuilder();
		for(DataRow dr : this){
			String word = dr.getWord();
			sbWord.append(word + " ");
			String pos = dr.getPos();
			sbPos.append(pos + " ");
			sbLabel.append(dr.getLabel());
		}
		String sb = sbWord + "\n";
		sb += sbPos + "\n";
		sb += sbLabel;
		//System.out.println(sb);
		return sb.toString();
	}
}
