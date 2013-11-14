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

//called by CleanCorpus
public class FixTokenization {
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.err.println("Usage: <program> inFile outFile");
			System.exit(-1);
		}
		SearchReplaceRuleMap searchReplaceRuleMap = new SearchReplaceRuleMap();
		String inFile = args[0];
		String outFile = args[1];
		
		//basic sentence completion
		searchReplaceRuleMap.put("।", " ।");
		
		//NOTE: order matters
		//generalized verbs
		searchReplaceRuleMap.put("ए को ", "एको ");
		searchReplaceRuleMap.put("ए का ", "एका ");
		searchReplaceRuleMap.put("ए की ", "एकी ");
		searchReplaceRuleMap.put("े को ", "ेको "); //problem : गोतामेको
		searchReplaceRuleMap.put("े का ", "ेका ");
		searchReplaceRuleMap.put("े की ", "ेकी ");
		
		searchReplaceRuleMap.put("रहेका", " रहेका "); //separate from verb
		searchReplaceRuleMap.put("रहेको", " रहेको "); //separate from verb
		searchReplaceRuleMap.put("रहेकी", " रहेकी "); //separate from verb
		searchReplaceRuleMap.put("सकेका", " सकेका ");
		searchReplaceRuleMap.put("सकेको", " सकेको ");
		searchReplaceRuleMap.put("सकेकी", " सकेकी ");
		searchReplaceRuleMap.put("भएको", " भएको ");
		searchReplaceRuleMap.put("भएकी", " भएकी ");
		searchReplaceRuleMap.put("भएका", " भएका ");
		searchReplaceRuleMap.put(" भने का ", " भनेका ");
		searchReplaceRuleMap.put("नसक्नु", " नसक्नु ");
		searchReplaceRuleMap.put("सक्नु", " सक्नु ");
		//fix
		searchReplaceRuleMap.put(" न सक्नु ", " नसक्नु ");
		
		searchReplaceRuleMap.put("हुन्थ्यो", " हुन्थ्यो ");
		searchReplaceRuleMap.put("सक्तैन", " सक्तैन");
		searchReplaceRuleMap.put("सक्छ", " सक्छ");
		searchReplaceRuleMap.put("नुहुन्छ", "नु हुन्छ ");
		
		searchReplaceRuleMap.put("चाहिँ", " चाहिँ ");
		//first with न in front
		searchReplaceRuleMap.put(" न रहेका ", " नरहेका ");
		searchReplaceRuleMap.put(" न रहेको ", " नरहेको ");
		searchReplaceRuleMap.put(" न रहेकी ", " नरहेकी ");
		
		searchReplaceRuleMap.put(" न सकेका", " नसकेका ");
		searchReplaceRuleMap.put(" न सकेको", " नसकेको ");
		searchReplaceRuleMap.put(" न सकेकी", " नसकेकी ");
		
		searchReplaceRuleMap.put(" न भएको", " नभएको ");
		searchReplaceRuleMap.put(" न भएकी", " नभएकी ");
		searchReplaceRuleMap.put(" न भएका", " नभएका ");
		
		//others
		searchReplaceRuleMap.put("एउ टा ", " एउटा ");
		searchReplaceRuleMap.put("एउ टै ", " एउटै ");
		searchReplaceRuleMap.put("एउ टी ", " एउटी ");
		searchReplaceRuleMap.put("दुइ टा " , " दुइटा ");
		searchReplaceRuleMap.put("दुइ टी " , " दुइटी ");
		
		searchReplaceRuleMap.put("’‘", "''");
		searchReplaceRuleMap.put("‘", " ' ");
		searchReplaceRuleMap.put("’", " ' ");
		searchReplaceRuleMap.put("“", " `` ");
		searchReplaceRuleMap.put("”", " \" ");
		searchReplaceRuleMap.put("पहि ले", " पहिले ");
		
		searchReplaceRuleMap.put(" ले ले ", "ले ले ");
		searchReplaceRuleMap.put("बक्सिन्छ", " बक्सिन्छ ");
		searchReplaceRuleMap.put("पटक् कै", "पटक्कै");
		searchReplaceRuleMap.put("अरनि को", "अरनिको");
		//important
		searchReplaceRuleMap.put("् को ", "्को "); //eg. घुट् को
		searchReplaceRuleMap.put(" पो का ", " पोका ");
		//things that usually get attached to now behind (mainly important for NER)
		searchReplaceRuleMap.put("प्रसाद", "प्रसाद ");
		searchReplaceRuleMap.put("मध्ये ", " मध्ये ");
		searchReplaceRuleMap.put("हरूभन्दा", " हरू भन्दा ");
		searchReplaceRuleMap.put("बिना ", " बिना "); //प्रजातन्त्रबिना
		searchReplaceRuleMap.put("सहित ", " सहित ");
		searchReplaceRuleMap.put("जस्तै ", " जस्तै ");
		searchReplaceRuleMap.put("जस्ता", " जस्ता ");
		searchReplaceRuleMap.put("जस्तो", " जस्तो ");
		searchReplaceRuleMap.put("विहीन ", " विहीन ");
		searchReplaceRuleMap.put("प्रद्वत", " प्रद्वत "); //संविधानप्रद्वत
		searchReplaceRuleMap.put("सम्बन्धी", " सम्बन्धी ");
		searchReplaceRuleMap.put("लगायत", " लगायत ");
		searchReplaceRuleMap.put("समेत", " समेत ");
		searchReplaceRuleMap.put("विरुद्ध", " विरुद्ध ");
		searchReplaceRuleMap.put("भन्दा", " भन्दा ");
		searchReplaceRuleMap.put("कारले ", "कार ले "); //कंसाकारलेेे
		searchReplaceRuleMap.put("ज्यू ", " ज्यू ");
		searchReplaceRuleMap.put("लगायत", " लगायत ");
		searchReplaceRuleMap.put("संग ", " संग ");
		searchReplaceRuleMap.put("सँग ", " सँग");
		searchReplaceRuleMap.put("सँगै ", " सँगै ");
		searchReplaceRuleMap.put("द्वारा", " द्वारा ");
		searchReplaceRuleMap.put("वासी ", " वासी ");
		searchReplaceRuleMap.put("मार्फत्", " मार्फत् ");
		//searchReplaceRuleMap.put("नै ", " नै ");
		searchReplaceRuleMap.put("देखेर", " देखेर ");
		searchReplaceRuleMap.put("अनुसार", " अनुसार ");
		searchReplaceRuleMap.put("अनुरूप", " अनुरूप ");
		searchReplaceRuleMap.put("अन्तर्गत", " अन्तर्गत ");
		searchReplaceRuleMap.put("पश्चात", " पश्चात ");
		searchReplaceRuleMap.put("सरह ", " सरह ");
		searchReplaceRuleMap.put("वाधक ", " वाधक ");
		searchReplaceRuleMap.put("साहेब", " साहेब " );
		searchReplaceRuleMap.put("सम्बन्धी", " सम्बन्धी ");
		searchReplaceRuleMap.put("वाहेक", " वाहेक ");
		searchReplaceRuleMap.put("अगाडि ", " अगाडि ");
		searchReplaceRuleMap.put("पछाडी ", " पछाडी ");
		searchReplaceRuleMap.put("पछि ", " पछि ");
		searchReplaceRuleMap.put("हरुले ", " हरु ले ");
		//fix
		searchReplaceRuleMap.put(" सँग सँगै ", " सँगसँगै ");
		
		searchReplaceRuleMap.put("पूर्वप्रधानमन्त्री", " पूर्वप्रधानमन्त्री ");
		//fix
		searchReplaceRuleMap.put(" प्र संग ", " प्रसंग ");
		searchReplaceRuleMap.put("माझ् ", " माझ् ");
		//fix
		searchReplaceRuleMap.put(" न माझ् ", " नमाझ् ");
		
		searchReplaceRuleMap.put(" सँग", " सँग "); //most of the times, this rule is correct
		
		searchReplaceRuleMap.put("हरुमै", " हरु मै ");
		searchReplaceRuleMap.put("हरुमा", " हरु मा ");
		searchReplaceRuleMap.put("हरुमा", " हरु मा ");
		//fix
		searchReplaceRuleMap.put(" मा त्रै ", " मात्रै ");
		searchReplaceRuleMap.put(" मा झ ", " माझ ");
		
		
		searchReplaceRuleMap.put("एकाछ", "एका छ");
		searchReplaceRuleMap.put("एकोछ", "एको छ");
		searchReplaceRuleMap.put("एकीछ", "एकी छ");
		searchReplaceRuleMap.put("बक्सेको", " बक्सेको ");
		//searchReplaceRuleMap.put("बक्सिन्न", " बक्सिन्न ");
		//searchReplaceRuleMap.put("दिइबक्सेछ", " दिइ बक्सेछ ");
		
		//not so important
		searchReplaceRuleMap.put("निर्माणकार्य", " निर्माणकार्य ");
		searchReplaceRuleMap.put("छोड्दैनथ्यो", " छोड्दैनथ्यो ");
		
		searchReplaceRuleMap.put(" का लेकी ", "काले की ");
		searchReplaceRuleMap.put(" नि का लेको ", " निकालेको ");
		searchReplaceRuleMap.put(" प्रेमि का ", " प्रेमिका ");
		searchReplaceRuleMap.put(" महानगरपालि का ", " महानगरपालिका ");
		searchReplaceRuleMap.put(" मूल्यअभिवृद्धि ", " मूल्य अभिवृद्धि ");
		searchReplaceRuleMap.put(" जीवनइतिहास ", " जीवन इतिहास ");
		searchReplaceRuleMap.put("त्र सित", " त्रसित ");
		searchReplaceRuleMap.put(" खु डा ", " खुडा ");
		searchReplaceRuleMap.put(" ह सिया ", " हसिया ");
		searchReplaceRuleMap.put("बाहिर", " बाहिर ");
		
		searchReplaceRuleMap.put("रासस", " रासस ");
		searchReplaceRuleMap.put("समाचारपत्र", " समाचारपत्र ");
		searchReplaceRuleMap.put("भ्रष्टाचार ", " भ्रष्टाचार ");
		searchReplaceRuleMap.put("काठमाडौँ ", " काठमाडौँ  ");
		searchReplaceRuleMap.put("काठमाडौं", " काठमाडौं ");
		searchReplaceRuleMap.put("पोखरा", " पोखरा ");
		searchReplaceRuleMap.put("विराटनगर", " विराटनगर ");
		searchReplaceRuleMap.put("दांग", " दांग ");
		searchReplaceRuleMap.put("महोत्तरी", " महोत्तरी ");
		searchReplaceRuleMap.put("मोरंग", " मोरंग ");
		searchReplaceRuleMap.put("ललितपुर", " ललितपुर ");
		searchReplaceRuleMap.put("लांगटांग", " लांगटांग ");
		searchReplaceRuleMap.put("हेटौंडा", " हेटौंडा ");
		searchReplaceRuleMap.put("क्यालिफोर्निया", " क्यालिफोर्निया ");
		searchReplaceRuleMap.put("चरिकोट", " चरिकोट ");
		searchReplaceRuleMap.put("बुटवल" , " बुटवल ");
		searchReplaceRuleMap.put("नवलपरासी", " नवलपरासी ");
		searchReplaceRuleMap.put("थानकोट", " थानकोट ");
		searchReplaceRuleMap.put("चित्लाङ", " चित्लाङ ");
		searchReplaceRuleMap.put("कुलेखानी", " कुलेखानी ");
		searchReplaceRuleMap.put("भिमफेदी", " भिमफेदी ");
		searchReplaceRuleMap.put("भैँसे", " भैँसे ");
		searchReplaceRuleMap.put("कारखाना", " कारखाना ");
		searchReplaceRuleMap.put("पूर्वत्रपश्चिमत्रउत्तरत्रदक्षिण", "पूर्वत्र पश्चिमत्र उत्तरत्र दक्षिण");
		searchReplaceRuleMap.put("पोलिरहेछ", " पोलिरहेछ ");
		searchReplaceRuleMap.put("परराष्ट्रमन्त्री", " परराष्ट्रमन्त्री ");
		searchReplaceRuleMap.put("केटाकेटीहरूलाइ", "केटाकेटीहरूलाइ ");
		searchReplaceRuleMap.put(" सरकार", " सरकार ");
		searchReplaceRuleMap.put("अन्तरराष्ट्रिय", " अन्तरराष्ट्रिय ");
		searchReplaceRuleMap.put("चिच्च्याउँदै", " चिच्च्याउँदै ");
		searchReplaceRuleMap.put("टेलिचलचित्र", " टेलिचलचित्र ");
		searchReplaceRuleMap.put("सर्वोत्कृट", " सर्वोत्कृट ");
		searchReplaceRuleMap.put("स्थित ", " स्थित ");
		//fix
		searchReplaceRuleMap.put(" अव स्थित ", " अवस्थित ");
		searchReplaceRuleMap.put("परिचारि का ", "परिचारिका ");
		searchReplaceRuleMap.put("विद्यार्थी", " विद्यार्थी ");
		searchReplaceRuleMap.put(" हि ले ", "  हिले  ");
		searchReplaceRuleMap.put("जिम् मा", " जिम्मा");
		searchReplaceRuleMap.put("बद्ध ", " बद्ध ");
		searchReplaceRuleMap.put("स्वरूप ", " स्वरूप ");
		searchReplaceRuleMap.put(" घु डा ", " घुडा ");
		
		searchReplaceRuleMap.put(" हु दैन " , " हुदैन ");
		searchReplaceRuleMap.put("हुदैन", " हुदैन ");
		searchReplaceRuleMap.put("हुँदैन", " हुँदैन ");
		searchReplaceRuleMap.put("हाल्नेछ", " हाल्नेछ ");
		//fix
		searchReplaceRuleMap.put("हुदैन थ्यो", "हुदैनथ्यो ");
		searchReplaceRuleMap.put("हुँदैन थ्यो", "हुँदैन थ्यो ");
		searchReplaceRuleMap.put(" हाल्नेछ न् ", " हाल्नेछन् ");
		
		searchReplaceRuleMap.put(" की को ", "की को "); //eg. ट्याङ् की को
		searchReplaceRuleMap.put(" का को ", "का को ");
		searchReplaceRuleMap.put(" का का ", "का का "); //नगरपालि का का
		searchReplaceRuleMap.put(" केको ", " के को ");
		
		
		searchReplaceRuleMap.put("ढो का ", "ढोका ");
		searchReplaceRuleMap.put("द्धारा", " द्धारा ");
		searchReplaceRuleMap.put("छात्राहरूतर्फ", "छात्राहरू तर्फ ");
		searchReplaceRuleMap.put("छात्राहरू ", "छात्रा हरू ");
		
		//lots of this kind of errors
		searchReplaceRuleMap.put("अर्न्तर्गत", " अर्न्तर्गत "); //not a word, still
		
		searchReplaceRuleMap.put(" ले लाई ", "ले लाई ");
		searchReplaceRuleMap.put(" सा झ ", " साझ ");
		searchReplaceRuleMap.put("कहाँ ", " कहाँ");
		searchReplaceRuleMap.put(" आ मा " , " आमा  ");
		searchReplaceRuleMap.put(" सँगकेही ", " सँग केही ");
		searchReplaceRuleMap.put(" सँगबढी ", " सँग बढी ");
		searchReplaceRuleMap.put(" सँगबस्न", " सँग बस्न ");
		searchReplaceRuleMap.put("सबै सँग", " सबै सँग "); //सबै सँगआग्रह
		searchReplaceRuleMap.put("आदि सँग", " आदि सँग "); //आदि सँगप्रशस्त
		searchReplaceRuleMap.put(" यहाँनेर ", " यहाँ नेर ");
		searchReplaceRuleMap.put("अर्कोतर्फ", " अर्को तर्फ ");
		searchReplaceRuleMap.put(" पा ले ", " पाले ");
		searchReplaceRuleMap.put(" गो ले ", " गोले ");
		searchReplaceRuleMap.put("उहि लेका ", " उहिलेका ");
		searchReplaceRuleMap.put(" नहा लेको ", " नहालेको ");
		searchReplaceRuleMap.put("जहि ले ", " जहिले ");
		searchReplaceRuleMap.put("कहि ले ", " कहिले ");
		
		searchReplaceRuleMap.put(" उ दांग ो ", " उदांगो ");
		searchReplaceRuleMap.put(" सि ङ्ग ो ", " सिङ्गो ");
		searchReplaceRuleMap.put("अवकाशप्राप्त", " अवकाशप्राप्त ");
		searchReplaceRuleMap.put(" अवकाशप्राप्त ", " अवकाश प्राप्त ");
		searchReplaceRuleMap.put(" हु दै", " हुदै");
		searchReplaceRuleMap.put("जवाहरलाल ने हरू", "जवाहरलाल नेहरू");
		searchReplaceRuleMap.put("् नु ", "्नु ");
		
		
		//lone inflections
		searchReplaceRuleMap.put(" ा ", " ");
		//lines with lone ि are removed
		searchReplaceRuleMap.put(" ी ", "ी ");		
		searchReplaceRuleMap.put(" ु  ", " ");
		searchReplaceRuleMap.put(" ू ", " ");
		searchReplaceRuleMap.put(" े ", " ");
		//searchReplaceRuleMap.put(" ै " , "ै ");
		searchReplaceRuleMap.put(" सँग ै", " सँगै "); //fix
		searchReplaceRuleMap.put(" ो ", " ");
		searchReplaceRuleMap.put(" ौ ", "ौ "); //fix for results of हाल्नेछ
		
		Reader in = new InputStreamReader(new FileInputStream(inFile), "UTF-8");
		BufferedReader br = new BufferedReader(in);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		String line;
		while((line = br.readLine()) != null) {
			line = line.trim();
			line.replaceAll("\\s+", " ");
			for(String searchKey : searchReplaceRuleMap) {
				line = line.replaceAll(searchKey, searchReplaceRuleMap.get(searchKey));
			}
			pw.println(line);
			pw.flush();
		}
		br.close();
		pw.close();
	}
}
