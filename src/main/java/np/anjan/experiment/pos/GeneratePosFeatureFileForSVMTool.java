package np.anjan.experiment.pos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

import np.anjan.data.struct.VocabIndexReader;

public class GeneratePosFeatureFileForSVMTool {
	/*
	 * change "purnabiram" (nepali full stop ।) to .
	 * add columns: contains numbers?, contains english characters?
	 * normalize numbers into special symbol eg. *num*  
	 */
	public static void main(String[] args) throws IOException {
		String folderName = "/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/experiment/";
		String outFolderName = folderName + "svmtoolbaselinenew/";
		String[] fileList = {"train.txt", "test.txt"};
		for(String file : fileList) {
			String fileAbsPath = folderName + file;
			Reader in = new InputStreamReader(new FileInputStream(fileAbsPath), "UTF-8");
			BufferedReader br = new BufferedReader(in);
			String outFile = outFolderName + "/" + file + ".feature";
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
			String line;
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(! line.isEmpty()) {
					String splitted[] = line.split("\\s+");
					String word = splitted[0];
					String pos = splitted[1].toUpperCase();
					//get features
					//smooth word
					String smoothedWord = TokenProcessorNepali.getSmoothedWord(word);
					String containsNum = "N";
					String containsAscii = "N";
					if(TokenProcessorNepali.containsNumber(word)) {
						containsNum = "Y";
					}
					if(TokenProcessorNepali.containsAscii(word)) {
						containsAscii = "Y";
					}
					pw.println(smoothedWord + " " + pos + " " + containsNum + " " + containsAscii);
				} else {
					pw.println();
				}
			}
			br.close();
			pw.close();
			System.out.println("Feature files created");
		}
	}
}
