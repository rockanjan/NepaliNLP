package np.anjan.data.corpus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Corpus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1630492275470489841L;
	public List<Text> texts;
	
	public Corpus() {
		texts = new ArrayList<Text>();
	}
	
	public String getRawText() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<texts.size(); i++) {
			String rawText = texts.get(i).getRawText().trim();
			if(! rawText.isEmpty()) {
				sb.append(rawText);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public void writeRawTextSerially(PrintWriter pw) {
		for(int i=0; i<texts.size(); i++) {
			String rawText = texts.get(i).getRawText().trim();
			if(! rawText.isEmpty()) {
				pw.println(rawText);
				pw.println();
			}
		}
	}
	
	public void readTextFromFile(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			while( (line = br.readLine()) != null) {
				Paragraph p = new Paragraph();
				p.setRawText(line);
				Text t = new Text();
				t.paragraphs.add(p);
				texts.add(t);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
