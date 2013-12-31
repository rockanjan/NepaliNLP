package np.anjan.data.corpus.ner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
 * Extract named entities using tagged hindi corpus
 */
public class ExtractFromHindi {
	public static void main(String[] args) throws IOException {
		String inFolder = "/data/hindi/";
		String inFile = "combined-data-hindi.txt";
		//gazetteers
		String outFilePer = "hindi-per.gaz.txt";
		String outFileOrg = "hindi-org.gaz.txt";
		String outFileLoc = "hindi-loc.gaz.txt";
		ArrayList<String> per = new ArrayList<String>();
		ArrayList<String> org = new ArrayList<String>();
		ArrayList<String> loc = new ArrayList<String>();
		
		Reader in = new InputStreamReader(new FileInputStream(inFolder + inFile), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		String line;
		boolean contNep = false; //continuing person
		boolean contNeo = false;
		boolean contNel = false;
		StringBuffer entity = null;
		while( (line = br.readLine()) != null) {
			if(line.contains("<ne=NEP>")) {
				contNep = true;
				entity = new StringBuffer();
				continue;
			}
			if(line.contains("<ne=NEO>")) {
				contNeo = true;
				entity = new StringBuffer();
				continue;
			}
			if(line.contains("<ne=NEL>")) {
				contNel = true;
				entity = new StringBuffer();
				continue;
			}
			if(line.contains("))")) {
				if(contNep) {
					per.add(entity.toString());
				}
				if(contNeo) {
					org.add(entity.toString());					
				}
				if(contNel) {
					loc.add(entity.toString());
				}
				contNep = false;
				contNeo = false;
				contNel = false;
				entity = null;
			}
			if(contNep || contNeo || contNel) {
				String[] splitted = line.split("\\s+");
				entity.append(splitted[1] + " ");
			}
		}
		write(per, inFolder + outFilePer);
		write(loc, inFolder + outFileLoc);
		write(org, inFolder + outFileOrg);
		
		br.close();
	}
	
	public static void write(List<String> neList, String filename) throws UnsupportedEncodingException, FileNotFoundException {
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
		for(String ne : neList) {
			pw.println(ne.trim());
		}
		pw.close();
	}
}
