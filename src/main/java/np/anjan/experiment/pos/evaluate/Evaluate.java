package np.anjan.experiment.pos.evaluate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import np.anjan.data.corpus.TokenProcessor;
import np.anjan.data.corpus.Vocabulary;

public class Evaluate {
	public static void main(String[] args) throws IOException {
		boolean smooth = true;
		
		
		Vocabulary v = new Vocabulary();
		if(smooth) {
			v.readDictionary("/home/anjan/work/nepali/pos/vocab.txt.thres0"); //test has only lowercase characters
		} else {
			v.readDictionary("/home/anjan/work/nepali/pos/vocab.txt.thres0.nosmooth"); //test has only lowercase characters
		}
		String inputFile = "/home/anjan/work/nepali/pos/hmm25/result/test.rep.c2=0.2";
		//String inputFile = "/home/anjan/work/nepali/pos/baseline/result/test.baseline.c2=0.2"; //baseline
		
		
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
				String gold = splitted[splitted.length - 2].toLowerCase();
				String pred = splitted[splitted.length - 1].toLowerCase();
				//sentence level accuracy
				if(!gold.equals(pred)) {
					currentSentenceHasError = true;
				}
				String word = splitted[0];
				String smoothedWord = null;
				if(smooth) {
					smoothedWord = TokenProcessor.getSmoothedWord(word);
				}
				else {
					smoothedWord = word;
				}
				int vocabIndex = v.getIndex(smoothedWord);
				
				if(vocabIndex == 0) {
					totalUnknown++;
					if(gold.equals(pred)) {
						correctUnknown++;
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
 