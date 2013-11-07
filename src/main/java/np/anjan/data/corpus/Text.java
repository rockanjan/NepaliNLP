package np.anjan.data.corpus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Text implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2342959193531364715L;
	public List<Paragraph> paragraphs;
	
	public Text() {
		paragraphs = new ArrayList<Paragraph>();
	}
	
	public String getRawText() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<paragraphs.size(); i++) {
			//if(paragraphs.get(i).resemblesRealSentence() && paragraphs.get(i).passesAgressiveCleaning()) {
				sb.append(paragraphs.get(i).getRawText());
				sb.append("\n");
			//}
		}
		return sb.toString();
	}
}
