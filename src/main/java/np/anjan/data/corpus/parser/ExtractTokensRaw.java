
package np.anjan.data.corpus.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ExtractTokensRaw {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		String filename = "/data/nepalicorpus/processed/raw/corpus_clean_tokenized.xml";
		String outFilename = "/data/nepalicorpus/processed/raw/corpus_clean_tokenized.txt";
		TokenParserRaw parser = new TokenParserRaw();
		List<String> sentenceList = parser.readTokens(filename);
		System.out.println("Total sentences = " + sentenceList.size());
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFilename), "UTF-8"));
		for(String sentence : sentenceList) {
			pw.println(sentence.trim());
		}
		pw.close();
	}
	
}
