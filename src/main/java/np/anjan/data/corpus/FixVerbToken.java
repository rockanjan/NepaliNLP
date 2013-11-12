package np.anjan.data.corpus;

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
import java.util.HashMap;

/*
 * Fixes the separation of verbs in all corpus
 * tries to match to the tokenization in the manually tagged POS tags
 * eg. गरिए को to गरिएको 
 */
public class FixVerbToken {
	public static void main(String[] args) throws IOException {
		HashMap<String, String> searchReplaceRuleMap = new HashMap<String, String>();
		String inFile = "/data/nepalicorpus/processed/mpp/corpus_clean.processed.txt";
		//generalized
		searchReplaceRuleMap.put("ए को ", "एको ");
		searchReplaceRuleMap.put("ए का ", "एका ");
		searchReplaceRuleMap.put("ए की ", "एकी ");
		searchReplaceRuleMap.put("े को ", "ेको ");
		searchReplaceRuleMap.put("े का ", "ेका ");
		searchReplaceRuleMap.put("े की ", "ेकी ");
		searchReplaceRuleMap.put(" एउ टा ", " एउटा ");
		searchReplaceRuleMap.put(" एउ टै ", " एउटै ");
		searchReplaceRuleMap.put(" एउ टी ", " एउटी ");
		searchReplaceRuleMap.put(" दुइ टा " , " दुइटा ");
		searchReplaceRuleMap.put(" दुइ टी " , " दुइटी ");
		searchReplaceRuleMap.put(" ‘ ", " ' ");
		searchReplaceRuleMap.put(" ’ ", " ' ");
		searchReplaceRuleMap.put(" “ ", " `` ");
		searchReplaceRuleMap.put(" ” ", " \" ");
		Reader in = new InputStreamReader(new FileInputStream(inFile), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		String outFile = inFile + ".fixed";
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		String line;
		while((line = br.readLine()) != null) {
			for(String searchKey : searchReplaceRuleMap.keySet()) {
				line = line.replaceAll(searchKey, searchReplaceRuleMap.get(searchKey));
			}
			pw.println(line);
		}
		br.close();
		pw.close();
		//specific
		/*
		searchReplaceRuleMap.put(" भइसके को ", " भइसकेको ");
		searchReplaceRuleMap.put(" गरिए को ", " गरिएको ");
		searchReplaceRuleMap.put(" भइरहे का ", " भइरहेका ");
		searchReplaceRuleMap.put(" गरे को ", " गरेको  ");
		searchReplaceRuleMap.put(" भए का ", " भएका ");
		searchReplaceRuleMap.put(" रोए को ", " रोएको ");
		searchReplaceRuleMap.put(" हेरे को ", " हेरेको ");
		searchReplaceRuleMap.put(" लगाइए को ", " लगाइएको ");
		searchReplaceRuleMap.put(" निस्के को ", " निस्केको ");
		searchReplaceRuleMap.put(" लागे की ", " लागेकी ");
		searchReplaceRuleMap.put(" पिल्सिए को ", " पिल्सिएको ");
		searchReplaceRuleMap.put(" थाले को ", " थालेको ");
		searchReplaceRuleMap.put(" ‘ ", " ' ");
		searchReplaceRuleMap.put(" ’ ", " ' ");
		searchReplaceRuleMap.put(" “ ", " `` ");
		searchReplaceRuleMap.put(" ” ", " \" ");
		searchReplaceRuleMap.put(" गरिबक्से को ", " गरिबक्सेको ");
		*/
	
		//others
		//खड्के को थालिए को परे का
		
		
		
		
		
		//first apply the general rule
		
	}
}
