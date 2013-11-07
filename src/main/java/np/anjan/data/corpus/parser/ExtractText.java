package np.anjan.data.corpus.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import np.anjan.config.Config;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class ExtractText {
	public static void main(String[] args) throws SAXException, IOException {
		Date startTime = new Date(); 
		System.out.println("Start Time = " + startTime);
		
		File outFolder = new File(Config.FOLDER_PROCESSED_POS_DATA);
		if( ! outFolder.exists() ) {
			outFolder.mkdir();
		}
		String outFile = Config.FOLDER_PROCESSED_POS_DATA + "corpus_clean.txt";
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		File folder = new File("/data/nepalicorpus/mpp/gc/webtext/m-1asphost-com-jhapalidotcom/");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String file = listOfFiles[i].getAbsolutePath();
				
				if (file.endsWith(".xml")) {
					File f = new File(file);
					InputStream inputStream = new FileInputStream(f);
	                ReuterCorpusParser parser = new ReuterCorpusParser(inputStream);
	                List<String> text = parser.parse();
	                for(String sent : text) {
	                	pw.println(sent);
	                }
					inputStream.close();
					
	            }
	        }	
		}
		pw.close();
		System.out.println("File written at : " + outFile);
		Date endTime= new Date();
		System.out.println("EndTime = " + endTime);
		System.out.println("Total Time taken : " + ( (endTime.getTime() - startTime.getTime())/1000/60) + " minutes" ); 
	}

}
