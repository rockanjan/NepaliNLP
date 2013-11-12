package np.anjan.data.corpus.manualpos;

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
import java.io.Reader;

public class ProcessManualPosData {
	static String folderName = "/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/";
	public static void main(String[] args) throws IOException {
		File folder = new File(folderName);
		File outFolder = new File(folderName + "processed");
		if(! outFolder.exists()) {
			outFolder.mkdir();
		}
		
		File[] fileList = folder.listFiles();
		for(File f : fileList) {
			if(f.isFile()) {
				if(f.getName().endsWith("_pos.txt")) {
					Reader in = new InputStreamReader(new FileInputStream(f), "UTF-8");
					BufferedReader br = new BufferedReader(in);
					String outFile = outFolder + "/" + f.getName();
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
					PrintWriter pwTxtOnly = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile + ".txtonly"), "UTF-8"));
					String line;
					while((line = br.readLine()) != null) {
						line = line.trim();
						//replace line trying to match the revised version
						//most frequent revision being ‘‘ converted to ''
						line = line.replaceAll("‘‘", "''");
						line = line.replaceAll("“", "''");
						line = line.replaceAll("\"", "''");
						if(line.isEmpty()) {
							pw.println();
							continue;
						}
						String[] splitted = line.split("\\s+");
						for(String wordPos : splitted) {
							int openCount = 0; int closeCount = 0;
							for(char c : wordPos.toCharArray()) {
								if(c == '<') {
									openCount++;
									pw.print(" ");
									pwTxtOnly.print(" ");
								} else if(c == '>') {
									closeCount++;
									pw.println();
								} else {
									pw.print(c);
									if(openCount == closeCount) {
										pwTxtOnly.print(c);
									}
								}
							}
							pwTxtOnly.print(" ");
							if(openCount != closeCount) {
								System.err.println("Error: word = " + wordPos);
							}
						}
						pwTxtOnly.println();
						pw.println();
					}
					br.close();
					pw.close();
					pwTxtOnly.close();
				}
			}
		}
	}
}
