package np.anjan.data.corpus;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * zero index always reserved for OOV no matter if it appears or not
 * if it does not appear, the index will not be used
 */

public class Vocabulary {
	public static boolean debug = false;
	public static boolean smooth = true;
	public static boolean lower = true;
	public int vocabThreshold = 10;
	//index zero reserved for *unk* (low freq features)
	public int vocabReadIndex = 0;
	public int vocabSize = 0;
	public static String UNKNOWN = "*unk*";
	public Map<String, Integer> wordToIndex = new HashMap<String, Integer>();
	public ArrayList<String> indexToWord = new ArrayList<String>();
	public Map<Integer, Integer> indexToFrequency = new HashMap<Integer, Integer>();
	
	public static void main(String[] args) throws IOException {
		boolean WPL = false; //word per line or sentence per line?
		String folder = "/data/nepalicorpus/processed/mpp/";
		Reader in = new InputStreamReader(new FileInputStream(folder + "corpus_clean.processed.txt.cleaned.fixed"), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		String line = "";
		Vocabulary v = new Vocabulary();
		v.vocabReadIndex = 1;
		v.indexToFrequency.put(0, 0);
		v.indexToWord.add(v.UNKNOWN);
		v.wordToIndex.put(v.UNKNOWN, 0);
		while( (line = br.readLine()) != null) {
			if(! line.trim().isEmpty()) {
				if(WPL) {
					String word = line.split("\\s+")[0].toLowerCase();
					word = TokenProcessor.getSmoothedWord(word);
					v.addItem(word);
				} else {
					String[] words = line.split("\\s+");
					for(String word : words) {
						if(lower) {
							word = word.toLowerCase();
						}
						if(smooth) {
							word = TokenProcessor.getSmoothedWord(word);
						}
						v.addItem(word);
					}
				}
				
			}
		}
		System.out.println("Vocab size before reduction : " + v.vocabSize);
		v.reduceVocab();
		System.out.println("Vocab size after reduction : " + v.vocabSize);
		v.writeDictionary(folder + "vocab.txt.thres" + v.vocabThreshold);
		br.close();
	}
	
	public int addItem(String word) {
		int returnId = -1;
		if(wordToIndex.containsKey(word)) {
			int wordIndex = wordToIndex.get(word);
			int oldFreq = indexToFrequency.get(wordIndex);
			indexToFrequency.put(wordIndex, oldFreq + 1);
			returnId = wordIndex;
		} else {
			wordToIndex.put(word, vocabReadIndex);
			indexToWord.add(word);
			indexToFrequency.put(vocabReadIndex, 1);
			returnId = vocabReadIndex;
			vocabReadIndex++;
		}
		vocabSize = vocabReadIndex;
		return returnId;
	}
	
	/* used when reading from dictionary */
	public int addItem(String word, int freq) {
		int returnId = -1;
		if(wordToIndex.containsKey(word)) {
			throw new RuntimeException(word + " found more than once in the dictionary");
		} else {
			wordToIndex.put(word, vocabReadIndex);
			indexToWord.add(word);
			indexToFrequency.put(vocabReadIndex, freq);
			returnId = vocabReadIndex;
			vocabReadIndex++;
		}
		return returnId;
	}
	
	public void writeDictionary(String filename) {
		try{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
			//write vocabSize
			pw.println(this.vocabSize);
			for(int i=0; i<indexToWord.size(); i++) {
				pw.println(indexToWord.get(i) + " " + indexToFrequency.get(i));
			}
			pw.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}
	
	//reads from the dictionary
	public void readDictionary(String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		wordToIndex.clear();
		String line = null;
		try {
			line = br.readLine().trim();
			vocabSize = Integer.parseInt(line);
			while( (line = br.readLine()) != null) {
				line = line.trim();
				if(line.isEmpty()) {
					continue;
				}
				String[] splitted = line.split("\\s+");
				String word = splitted[0];
				int freq = 0;
				if(splitted.length > 1) {
					freq = Integer.parseInt(splitted[1]);
				}
				addItem(word, freq);
			}
			System.out.println("Dictionary Loaded Vocab Size: " + wordToIndex.size());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("error reading dictionary file");
		}
		if(vocabSize != wordToIndex.size()) {
			System.out.println("dictionary file corrputed: header size and the vocab size do not match");
			System.exit(-1);
		}
	}
	
	public void reduceVocab() {
		System.out.println("Reducing vocab");
		Map<String, Integer> wordToIndexNew = new HashMap<String, Integer>();
		ArrayList<String> indexToWordNew = new ArrayList<String>();
		Map<Integer, Integer> indexToFrequencyNew = new HashMap<Integer, Integer>();
		wordToIndexNew.put(UNKNOWN, 0);
		if(wordToIndex.containsKey(UNKNOWN)) {
			indexToFrequencyNew.put(0, indexToFrequency.get(0)); //TODO: decide if this matters
		} else {
			indexToFrequencyNew.put(0, 0);
		}
		indexToWordNew.add(UNKNOWN);
		
		int featureIndex = 1;
		for(int i=1; i<indexToWord.size(); i++) {
			if(indexToFrequency.get(i) > vocabThreshold) {
				wordToIndexNew.put(indexToWord.get(i), featureIndex);
				indexToWordNew.add(indexToWord.get(i));
				indexToFrequencyNew.put(featureIndex, indexToFrequency.get(i));
				featureIndex = featureIndex + 1;
			} else {
				indexToFrequencyNew.put(0, indexToFrequencyNew.get(0) + indexToFrequency.get(i));
			}
		}
		indexToWord = null; indexToFrequency = null; wordToIndex = null;
		indexToWord = indexToWordNew;
		indexToFrequency = indexToFrequencyNew;
		wordToIndex = wordToIndexNew;
		vocabSize = wordToIndex.size();
		//System.out.println("New vocab size : " + vocabSize);
	}
	
	public void debug() {
		StringBuffer sb = new StringBuffer();
		sb.append("DEBUG: Corpus\n");
		sb.append("=============\n");
		sb.append("vocab size : " + vocabSize);
		sb.append("\nvocab frequency: \n");
		for (int i = 0; i < vocabSize; i++) {
			sb.append("\t" + i + " --> " + "\t" + indexToWord.get(i) + " --> " + indexToFrequency.get(i));
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}
	
	public int getIndex(String word) {
		if(wordToIndex.containsKey(word)) {
			return wordToIndex.get(word);
		} else {
			//word not found in vocab
			if(debug) {
				System.out.println(word + " not found in vocab");
			}
			return 0; //unknown id
		}
	}
	
	
}
