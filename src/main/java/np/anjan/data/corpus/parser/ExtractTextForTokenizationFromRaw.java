package np.anjan.data.corpus.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import np.anjan.config.Config;
import np.anjan.data.corpus.Corpus;
import np.anjan.data.corpus.Paragraph;
import np.anjan.data.corpus.Text;

public class ExtractTextForTokenizationFromRaw {
	public static void main(String args[]) throws IOException {
		Date startTime = new Date();
		System.out.println("Start Time = " + startTime);
		Corpus corpus = new Corpus();
		File folder = new File(Config.FOLDER_RAW_DATA);
		Stack<File> folderList = new Stack<File>(); //list of unprocessed folders
		folderList.push(folder); //add root folder
		int fileCount = 0;
		while(! folderList.isEmpty() ) {
			File currentFolder = folderList.pop();
			File[] arrayFiles = currentFolder.listFiles();
			for (int i = 0; i < arrayFiles.length; i++) {
				if (arrayFiles[i].isFile()) {
					String file = arrayFiles[i].getAbsolutePath();
					if(file.endsWith(".xml")) {
						System.out.println("Parsing file " + i + " " + file);
						fileCount++;
						TextParserFromRaw parser = new TextParserFromRaw();
						List<Text> textList = parser.readConfig(file);
						for(Text text : textList) {
							corpus.texts.add(text);
						}				
					} else if(file.endsWith(".txt")) {
						System.out.println("Parsing file " + i + " " + file);
						fileCount++;
						Text text= new Text();
						Paragraph p = new Paragraph();
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
						String line = "";
						while( (line = br.readLine()) != null) {
							line = line.trim();
							p.setRawText(line);
						}
						p.mergeMultiline();
						text.paragraphs.add(p);
						br.close();
					}
				} else {
					if(! arrayFiles[i].getName().equals(".") && !arrayFiles[i].getName().equals("..")) { 
						folderList.push(arrayFiles[i]);
					}
				}
			}
		}
		System.out.println(fileCount + " files read. writing...");
		File outFolder = new File(Config.FOLDER_PROCESSED_RAW_DATA);
		if (!outFolder.exists()) {
			outFolder.mkdir();
		}
		if (outFolder.isFile() || !outFolder.canWrite()) {
			System.out.println("Saving at /tmp");
			Config.FOLDER_PROCESSED_RAW_DATA = "/tmp/";
		}
		String outFile = Config.FOLDER_PROCESSED_RAW_DATA + "corpus_clean.txt";
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		corpus.writeRawTextSerially(pw);
		//pw.print(corpus.getRawText());
		pw.close();
		System.out.println("File written at : " + outFile);
		Date endTime = new Date();
		System.out.println("EndTime = " + endTime);
		System.out.println("Total Time taken : "
				+ ((endTime.getTime() - startTime.getTime()) / 1000 / 60)
				+ " minutes");
	}
}
