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

public class CleanCorpus {
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.err.println("Usage: <program> inFile outFile");
			System.exit(-1);
		}
		/*****First, fix tokenization****/
		String inFile = args[0];
		String outFile = args[1];
		
		String tmpOutput = "/tmp/tokenizationFixed";
		File tmpFile = new File(tmpOutput);
		if(tmpFile.exists()) {
			tmpFile.delete();
		}
		String[] argsFixTokenization = {inFile, tmpOutput};
		FixTokenization.main(argsFixTokenization);
		
		/***** Clean further ****/
		//clean the corpus using simple rules
		//1. if it contains words with length (of characters) greater than 30
		//2. if the number of words is greater than 50
		//3. if the total characters in the word is greater than 500
		int wordLengthThres = 30;
		int numberOfWordsThres = 50;
		int numberOfWordsThresLower = 5;
		int numberOfCharactersThres = 500;
		Reader in = new InputStreamReader(new FileInputStream(tmpOutput), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		String line;
		while((line = br.readLine()) != null) {
			line = line.trim();
			//replace multiple spaces with a single space
			line = line.replaceAll("\\s+", " ");
			if(line.length() > numberOfCharactersThres) continue;
			String[] tokens = line.split("\\s+");
			if(tokens.length > numberOfWordsThres && tokens.length < numberOfWordsThresLower) continue;
			
			boolean okLength = true;
			for(String token : tokens) {
				if(token.length() > wordLengthThres) {
					okLength = false;
					break;
				}
			}
			//few simple rules to rule out non-nepali
			//found by finding words of length greater than 50
			if(line.contains("मबचथ") 
					//|| line.contains("त्जभ")
					|| line.contains("टछछघघ")
					|| line.contains("उबचकभ")
					//|| line.contains("बनबज")
					|| line.contains("ष्चअस")
					|| line.contains("चभभत")
					|| line.contains("प्बलअजबलसअ")
					|| line.contains("ाब्कष्थि")
					|| line.contains("कभतत्ष्")
					|| line.contains("भ्ग्एज्इ")
					|| line.contains("भ्लतधष्")
					|| line.contains("बतिस्कय")
					|| line.contains("ःभ्ल्क्ष्क्")
					|| line.contains("ःकयद्यय")
					|| line.contains("ाब्कष्थि")
					|| line.contains("कतचयध")
					|| line.contains("७३ज्ञण्")
					|| line.contains("म्ब्ल्")
					|| line.contains("त्गदभच")
					|| line.contains("ढछघद्")
					|| line.contains("ीब्ऋइग्च्")
					|| line.contains("तष्मिभस")
					|| line.contains("ष्चयध")
					|| line.contains("द्दछउतस")
					|| line.contains("द्दघद्धद्ध")
					|| line.contains("मादृक्प्")
					|| line.contains("क्ष्ल्त्भ्")
					|| line.contains("कष्थिस्ग्च")
					|| line.contains("गभचष्त")
					|| line.contains("चबभलप")
					|| line.contains("लतबकथ")
					|| line.contains("ततगअ")
					|| line.contains("लभतधय")
					|| line.contains("बलयतजभच")
					|| line.contains("म्ष्बउजबलष्ब")
					|| line.contains("यखभष्अयििष्क")
					|| line.contains("च्थबलयमष्लभ")
					|| line.contains("एष्भचष्क")
					|| line.contains("ीभउत")
					|| line.contains(" ि ") //lone ि are removed
					
					) {
				continue;
			}
			if(okLength) {
				pw.println(line);
				pw.flush();
			}
		}
		br.close();
		pw.close();
	}
}
