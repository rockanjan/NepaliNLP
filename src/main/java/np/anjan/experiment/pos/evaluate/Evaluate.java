package np.anjan.experiment.pos.evaluate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Evaluate {
	public static void main(String[] args) throws IOException {
		int GOLD_COL = 3; //third col
		int PRED_COL = 2; //third col
		
		int UNKNOWN_COL = 1; //first column
		String UNKNOWN = "0";
		//input file with last column pred and second to last gold tags
		if(args.length != 1) {
			System.err.println("Usage: <program> goldPredFile");
			System.exit(-1);
		}
		String inputFile = args[0];
		System.out.println("Processing: " + inputFile);
		int totalSentences = 0;
		int correctSentences = 0;
		int totalTokens = 0;
		int correctTokens = 0;
		int totalUnknown = 0;
		int correctUnknown = 0;
		Reader in = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		String line;
		List<Confusion> confusionList = new ArrayList<Confusion>();
		boolean prevEmptyLine = true;
		boolean currentSentenceHasError = false;
		while((line = br.readLine()) != null) {
			line = line.trim();
			if(! line.isEmpty()) {
				if(prevEmptyLine) {
					totalSentences++;
					prevEmptyLine=false;
				}
				String[] splitted = line.split("\\s+");
				String gold = splitted[GOLD_COL-1].toLowerCase();
				String pred = splitted[PRED_COL-1].toLowerCase();
				//sentence level accuracy
				if(!gold.equals(pred)) {
					currentSentenceHasError = true;
				}
				//unknown
				if(splitted.length > 2) {
					if(splitted[UNKNOWN_COL-1].equals(UNKNOWN)) {
						totalUnknown++;
						if(gold.equals(pred)) {
							correctUnknown++;
						}
					}
				}
				//all
				Confusion c = new Confusion(gold, pred);
				if(confusionList.contains(c)) {
					confusionList.get(confusionList.indexOf(c)).count++;
				} else {
					c.count = 1;
					confusionList.add(c);
				}
			} else {
				if(! prevEmptyLine) {
					if(! currentSentenceHasError) {
						correctSentences++;
					}
				}
				prevEmptyLine=true;
				currentSentenceHasError = false;
			}
		}
		//in case the last line of the file is not empty, total sentence count already updated but not the correct
		if(! prevEmptyLine) {
			if(!currentSentenceHasError) {
				correctSentences++;
			}
		}
		
		br.close();
		//sort confusions by freq
		Collections.sort(confusionList, new ConfusionCountComparator());
		int maxTopError = 10;
		int topErrorCount = 0;
		System.out.println("Displaying top " + maxTopError + " errors");
		for(Confusion confusion : confusionList) {
			totalTokens += confusion.count;
			if(confusion.pred.equals(confusion.gold)) {
				correctTokens += confusion.count;
			} else {
				if(topErrorCount < maxTopError) {
					System.out.println(confusion.gold + " \t" + confusion.pred + " \t" + confusion.count);
					topErrorCount++;
				}
			}
		}
		System.out.println("Sentences \tcorrect = " + correctSentences + " \ttotal = " + totalSentences);
		if(totalUnknown > 0) {
			System.out.println("Unknown \tcorrect = " + correctUnknown + "\ttotal = " + totalUnknown);
		}
		System.out.println("Tokens \t\tcorrect = " + correctTokens + " \ttotal= " + totalTokens);
		
		System.out.println();
		double sentenceAccuracy = 100.0 * correctSentences / totalSentences;
		System.out.format("Sentence \tAccuracy = %.2f\n", sentenceAccuracy);
		if(totalUnknown > 0) {
			double unknownAccuracy = 100.0 * correctUnknown / totalUnknown;
			System.out.format("Unknown \tAccuracy = %.2f\n", unknownAccuracy);
		}
		double accuracy = 100.0 * correctTokens / totalTokens;
		System.out.format("Overall \tAccuracy = %.2f\n", accuracy);
	}
}
 