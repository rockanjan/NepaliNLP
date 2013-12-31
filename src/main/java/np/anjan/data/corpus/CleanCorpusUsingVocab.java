package np.anjan.data.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

/*
 * First CleanCorpus (which also fixes tokenization, then run this to select good sentences)
 * if number of OOV words and NUM is greater than some threshold, remove that sentence
 */
public class CleanCorpusUsingVocab {
	public static void main(String[] args) throws IOException {
		String vocabFile = "/data/nepalicorpus/processed/mpp/vocab.txt.thres10";
		String inFile = "/data/nepalicorpus/processed/mpp/corpus_clean.processed.txt.cleaned.fixed";
		String outFile = inFile + ".cleanedbyvocab";
		
		Vocabulary v = new Vocabulary();
		v.readDictionary(vocabFile);
		v.debug = false;
		
		Reader in = new InputStreamReader(new FileInputStream(inFile), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		String line;
		while((line = br.readLine()) != null) {
			line = line.trim();
			//replace multiple spaces with a single space
			line = line.replaceAll("\\s+", " ");
			String[] tokens = line.split("\\s+");
			int totalTokenCount = tokens.length;
			int oovCount = 0;
			int numCount = 0;
			for(String token : tokens) {
				if(v.getIndex(token) == 0) {
					oovCount++;
				}
				if(TokenProcessor.getSmoothedWord(token).equals("_NUM_"))
						//|| token.equals("...") 
				{
					numCount++;
				}
			}
			double fracOOV = 1.0 * oovCount / totalTokenCount;
			double fracNum = 1.0 * numCount / totalTokenCount;
			if(fracOOV <= 0.2 && fracNum <= 0.4) {
				pw.println(line);
				pw.flush();
			}
		}
		pw.println();
		br.close();
		pw.close();
	}
}
