package np.anjan.data.struct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VocabIndexNoSmooth {
	/*
	 * No number normalization
	 */
	
	/*
	 * Reads file to create feature. Converts the words into integer indices
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		int THRESHOLD = 0;
		
		if(args.length != 2) {
			System.err.println("Usage: <vocabindexprogram> input_file output_file");
			System.exit(1);
		}
		File file = new File(args[0]);
		String outFile = args[1];
		int SIZE = 200000;
		HashMap<String, Integer> wordCount = new HashMap<String, Integer>(SIZE);
		Reader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		String line = "";
		System.out.println("Counting...");
		while ((line = br.readLine()) != null) {
			if (!line.trim().equals("")) {
				String[] splitted = line.split(" ");
				String word = splitted[0];
				if (wordCount.containsKey(word)) {
					wordCount.put(word, wordCount.get(word) + 1);
				} else {
					wordCount.put(word, 1);
				}
			}
		}
		br.close();
		List<TokenFreq> tokenFreqList = new ArrayList<TokenFreq>();
		for (String key : wordCount.keySet()) {
			TokenFreq tf = new TokenFreq(key, wordCount.get(key));
			tokenFreqList.add(tf);
		}
		System.out.println("Total unique words : " + tokenFreqList.size());
		//sort by freq
		TokenFreqComparator comparator = new TokenFreqComparator();
		Collections.sort(tokenFreqList, comparator);
		
		//write
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		int index = 1;
		for(TokenFreq tf : tokenFreqList) {
			if(tf.freq > THRESHOLD) {
				pw.println(index + "\t" + tf.word + "\t" + tf.freq);
				index++;
			} else {
				pw.println("-1" + "\t" + tf.word + "\t" + tf.freq);
			}
		}
		pw.flush();
		pw.close();
		System.out.println("Size of vocab : " + index);
		System.out.println("Done");
	}
}
