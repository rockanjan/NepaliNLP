
package np.anjan.data.corpus.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import np.anjan.config.Config;

public class ExtractTokensMPP {
	public static boolean includePosLabel = false;
	public static void main(String[] args) throws IOException {
		String outFile = Config.FOLDER_PROCESSED_POS_DATA + "corpus_clean.txt";
		
		Date startTime = new Date();
		System.out.println("Start Time = " + startTime);
		//Config.FOLDER_POS_DATA = "/data/nepalicorpus/mpp/gc/webtext/m-1asphost-com-jhapalidotcom/";
		File folder = new File(Config.FOLDER_POS_DATA);
		Stack<File> folderList = new Stack<File>(); //list of unprocessed folders
		folderList.push(folder); //add root folder
		int fileCount = 0;
		
		File outFolder = new File(Config.FOLDER_PROCESSED_POS_DATA);
		if (!outFolder.exists()) {
			outFolder.mkdir();
		}
		if (outFolder.isFile() || !outFolder.canWrite()) {
			System.out.println("Saving at /tmp");
			Config.FOLDER_PROCESSED_POS_DATA = "/tmp/";
		}
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		while(! folderList.isEmpty() ) {
			File currentFolder = folderList.pop();
			File[] arrayFiles = currentFolder.listFiles();
			if(arrayFiles == null) {
				System.out.println("folder gave null : " + currentFolder.getAbsolutePath());
			}
			for (int i = 0; i < arrayFiles.length; i++) {
				if (arrayFiles[i].isFile()) {
					if(arrayFiles[i].getName().endsWith(".xml")) {
						String file = arrayFiles[i].getAbsolutePath();
						System.out.println("Parsing file " + i + " " + file);
						fileCount++;
						TokenParserMPP parser = new TokenParserMPP();
						List<String> sentenceList = parser.readConfig(file);
						for(String sentence : sentenceList) {
							pw.println(sentence);
						}
					}
				} else {
					if(! arrayFiles[i].getName().equals(".") && !arrayFiles[i].getName().equals("..")) { 
						folderList.push(arrayFiles[i]);
					}
				}
			}
		}
		System.out.println("Total tokens : " + TokenParserMPP.totalTokens);
		System.out.println(fileCount + " files read.");
		pw.close();
		System.out.println("File written at : " + outFile);
		Date endTime = new Date();
		System.out.println("EndTime = " + endTime);
		System.out.println("Total Time taken : "
				+ (1.0 * (endTime.getTime() - startTime.getTime()) / 1000 / 60)
				+ " minutes");
	}	
}
