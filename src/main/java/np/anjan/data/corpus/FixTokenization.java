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
		
		searchReplaceRuleMap.put(" मह७ष्नचबखभसव", " महत्व");
		searchReplaceRuleMap.put("168", "ङ्ग");
		searchReplaceRuleMap.put("186", "फ्");
		searchReplaceRuleMap.put("197", "हृ");
		searchReplaceRuleMap.put("215", " ");
		searchReplaceRuleMap.put("217 ", " — "); //some kind of punctuation
		searchReplaceRuleMap.put("219", " ! ");
		searchReplaceRuleMap.put("338", "त्");
		searchReplaceRuleMap.put("339", "त्र्"); //भि339याइएका,ख339याकखुत्रुक
		searchReplaceRuleMap.put("710", "फ्");
		searchReplaceRuleMap.put("8249", "ङ्घ");
		searchReplaceRuleMap.put("8226", "ङ्ग"); //प्रधाना8226, प्लटि8226
		searchReplaceRuleMap.put("8212", " — ");
		//searchReplaceRuleMap.put("8250", ""); //sometimes tra, sometimes dra (eg. rastriya, birendra)
		
		searchReplaceRuleMap.put("घुर का ले", "घुरका ले"); //name
		searchReplaceRuleMap.put("पेस् की", "पेस्की");
		//from full corpus
		searchReplaceRuleMap.put("विश्लेषण", " विश्लेषण ");
		searchReplaceRuleMap.put("मात्रै", " मात्रै ");
		searchReplaceRuleMap.put("र्सार्वजनिक", "सार्वजनिक");
		//searchReplaceRuleMap.put("र्ू", "ू");
		searchReplaceRuleMap.put("पर्ूव", "पूर्व");
		searchReplaceRuleMap.put("दुष्ट्याइा", "दुष्ट्याइ");
		searchReplaceRuleMap.put("सम्भेँ्क", "सम्झें");
		searchReplaceRuleMap.put("प्रि क्वेन्सी", " फ्रिक्वेन्सी ");
		searchReplaceRuleMap.put("एा", "ए");
		searchReplaceRuleMap.put("गेा", "गे");
		searchReplaceRuleMap.put("ौा", "ौ");
		searchReplaceRuleMap.put("ुा", "ु");
		searchReplaceRuleMap.put("सा ँचो", "साँचो");
		searchReplaceRuleMap.put("समाचारदाता", " समाचारदाता ");
		searchReplaceRuleMap.put("सुझ््काव", "सुझाव");
		searchReplaceRuleMap.put("साझ्ा", "साझा");
		searchReplaceRuleMap.put("सम्झ्ौता", "सम्झौता");
		//searchReplaceRuleMap.put("्ौ", "ै");
		searchReplaceRuleMap.put("मझ्ौला", "मझ्यौला");
		searchReplaceRuleMap.put("बाहिर िइन्", "बहिरिइन्");
		searchReplaceRuleMap.put("त्रू्करता", "क्रुरता");
		searchReplaceRuleMap.put("हांै", "हौँ");
		searchReplaceRuleMap.put(" धुकधु की ", " धुकधुकी ");
		searchReplaceRuleMap.put("झैं ", " झैं ");
		//fix
		searchReplaceRuleMap.put("अ झैं ", "अझैं ");
		
		
		searchReplaceRuleMap.put("जोडनेपाल", "जोड नेपाल");
		
		//searchReplaceRuleMap.put("आाखा", "आँखा");
		//generalize
		searchReplaceRuleMap.put("पटक ", " पटक ");
		searchReplaceRuleMap.put("मुस्पि्ककर", "मुस्पिक्कर");
		searchReplaceRuleMap.put("भित्त्या", "भित्र्या");
		searchReplaceRuleMap.put("आा", "आँ"); //आाप, आाखा, आागन
		searchReplaceRuleMap.put("फुजिया मा ट्रेकिंग", " फुजियामा ट्रेकिंग ");
		searchReplaceRuleMap.put("सधैा", "सधैं");
		searchReplaceRuleMap.put("पश्चात ्ताप", "पश्चाताप");
		searchReplaceRuleMap.put("गनर्े", "गर्ने");
		searchReplaceRuleMap.put("चश् मा", "चश्मा");
		searchReplaceRuleMap.put("भांडाकंुडा", " भांडाकुंडा  ");
		searchReplaceRuleMap.put("कार्गाे", "कार्गो");
		searchReplaceRuleMap.put("सर्वाेच्च", "सर्वोच्च");
		searchReplaceRuleMap.put("सरकार ीस्तर", "सरकारी स्तर");
		searchReplaceRuleMap.put("काठमाडँैं", " काठमाडौँ ");
		searchReplaceRuleMap.put("काठमाडांै", " काठमाडौं ");
		searchReplaceRuleMap.put(" दि लाई ", " दिलाई ");
		searchReplaceRuleMap.put("आंै", "औं");
		searchReplaceRuleMap.put(" भइरह को ेछ ", " भइ रहेको छ ");
		searchReplaceRuleMap.put("बंैक", "बैंक");
		searchReplaceRuleMap.put("विदेशी", " विदेशी ");
		searchReplaceRuleMap.put("केन्द्रीय", " केन्द्रीय ");
		searchReplaceRuleMap.put("मन्त्रिपरिषद्को", "मन्त्रिपरिषद् को");
		searchReplaceRuleMap.put("सोहीमुताबिक", "सोही मुताबिक");
		searchReplaceRuleMap.put("कोक लेकी", "कोकले की");
		searchReplaceRuleMap.put(" का की ", " काकी ");
		searchReplaceRuleMap.put("र्ु", "ु");
		searchReplaceRuleMap.put("उू", "उ");
		searchReplaceRuleMap.put("उा", "उ");
		searchReplaceRuleMap.put("पर्ूण्ा", "पूर्ण");
		//searchReplaceRuleMap.put("पार्टर्ीी", " पार्टी ");
		searchReplaceRuleMap.put("र्टर्ीी", "र्टी"); //generalized
		//searchReplaceRuleMap.put("पर्ुनबहाली", "पुनर्बहाली");
		searchReplaceRuleMap.put("र्ुन", "ुनर्");//generalized
		searchReplaceRuleMap.put("शर्ीष्ास्थ", "शिर्षस्थ");
		searchReplaceRuleMap.put("झ्ै", "झै");
		searchReplaceRuleMap.put("झ्ु", "झु");
		
		
		searchReplaceRuleMap.put("श्र्ाृ", "श्रृ");
		
		searchReplaceRuleMap.put("व्इापारी", "व्यापारी");
		searchReplaceRuleMap.put("द्ध ारा ", " द्वारा ");
		searchReplaceRuleMap.put("पुरातात्ति्वक", "पुरातात्विक");
		searchReplaceRuleMap.put(" उ दांग ो ", " उदांगो ");
		searchReplaceRuleMap.put(" सि ङ्ग ो ", " सिङ्गो ");
		searchReplaceRuleMap.put("् नु ", "्नु ");
		searchReplaceRuleMap.put("राष्टि्रय", "राष्ट्रिय");
		searchReplaceRuleMap.put("टि्र", "ट्रि"); //eg मेटि्रक
		searchReplaceRuleMap.put(" आप्ः्नो ", " आफ्नो ");
		searchReplaceRuleMap.put(" गएि को ", " गरिएको ");
		//general
		searchReplaceRuleMap.put("ुु", "ु"); //कुुलेखानी, भन्नुुभयो , विद्युुत्
		searchReplaceRuleMap.put("िू", "ि"); //देखिूदैन
		searchReplaceRuleMap.put("ा ा ", "ा "); //आासु , आाखा
		
		
		//below: found mainly in the NER dataset
		searchReplaceRuleMap.put("पडि्कंदा", "पड्किंदा"); //may be able to generalize
		
		//searchReplaceRuleMap.put("क्ष्ँेत्र", "क्षेत्र" );
		searchReplaceRuleMap.put("क्ष्ँ", "क्ष"); //generalized अध्यक्ष्ँ,क्ष्ँेत्र 
		//searchReplaceRuleMap.put("निक्ष्ँेप", "निक्षेप");
		//searchReplaceRuleMap.put("पक्ष्ँ","पक्ष" );
		
		searchReplaceRuleMap.put("ज्यष्ेठ", "ज्येष्ठ");
		
		
		searchReplaceRuleMap.put("प्ः्", "फ्"); //eg.आप्ः्ना, 
		searchReplaceRuleMap.put("अबर्ौं", "अर्बौं");
		searchReplaceRuleMap.put("करोडांै", "करोडौँ");
		
		//searchReplaceRuleMap.put("टे्र", "ट्रे");
		searchReplaceRuleMap.put("े्र", "्रे"); //generalize
		
		searchReplaceRuleMap.put("हंुदै", "हुँदै");
		searchReplaceRuleMap.put(" पक् की ", " पक्की ");
		searchReplaceRuleMap.put("छांै", "छौँ");
		searchReplaceRuleMap.put("सिगंो", "सिंगो");
		searchReplaceRuleMap.put("टांै", "टौं"); //eg. हेटांैडा
		searchReplaceRuleMap.put("प्रँप्त", "प्राप्त");
		searchReplaceRuleMap.put("वषार्ंै", "बर्षौ");
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
		searchReplaceRuleMap.put("173", "");
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
		searchReplaceRuleMap.put(" दुई टा ", " दुईटा ");
		searchReplaceRuleMap.put("टा लेको ", " टालेको");
		
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
		searchReplaceRuleMap.put("अवकाशप्राप्त", " अवकाशप्राप्त ");
		searchReplaceRuleMap.put(" अवकाशप्राप्त ", " अवकाश प्राप्त ");
		searchReplaceRuleMap.put(" हु दै", " हुदै");
		searchReplaceRuleMap.put("जवाहरलाल ने हरू", "जवाहरलाल नेहरू");
		
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
		searchReplaceRuleMap.put("पश्चात्", "पश्चात् ");
		searchReplaceRuleMap.put("पश्चात", " पश्चात ");
		//fix
		searchReplaceRuleMap.put("पश्चात ाप", " पश्चाताप ");
		searchReplaceRuleMap.put("पश्चात ्ताप", " पश्चाताप ");
		searchReplaceRuleMap.put("पश्चात ्", "पश्चात्");
		searchReplaceRuleMap.put("सरह ", " सरह ");
		searchReplaceRuleMap.put("वाधक ", " वाधक ");
		searchReplaceRuleMap.put("साहेब", " साहेब " );
		searchReplaceRuleMap.put("सम्बन्धी", " सम्बन्धी ");
		searchReplaceRuleMap.put("वाहेक", " वाहेक ");
		searchReplaceRuleMap.put("अगाडि ", " अगाडि ");
		searchReplaceRuleMap.put("पछाडी ", " पछाडी ");
		searchReplaceRuleMap.put("पछि ", " पछि ");
		searchReplaceRuleMap.put("हरुले ", " हरु ले ");
		searchReplaceRuleMap.put("बापत", " बापत");
		searchReplaceRuleMap.put("लगायत", " लगायत ");
		
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
		searchReplaceRuleMap.put("नगरपालि का ", "नगरपालिका ");
		searchReplaceRuleMap.put(" मूल्यअभिवृद्धि ", " मूल्य अभिवृद्धि ");
		searchReplaceRuleMap.put(" जीवनइतिहास ", " जीवन इतिहास ");
		searchReplaceRuleMap.put("त्र सित", " त्रसित ");
		searchReplaceRuleMap.put(" खु डा ", " खुडा ");
		searchReplaceRuleMap.put(" ह सिया ", " हसिया ");
		searchReplaceRuleMap.put("बाहिर", " बाहिर ");
		searchReplaceRuleMap.put("सुइं को", "सुइंको");
		searchReplaceRuleMap.put(" ना का ", " नाका ");
		searchReplaceRuleMap.put(" मुरार का ", " मुरारका "); //surname
		searchReplaceRuleMap.put(" राज मा ", " राजमा ");
		searchReplaceRuleMap.put("राजधानीआसपास ", "राजधानी आसपास ");
		searchReplaceRuleMap.put("फर्पिंगहुंदै", "फर्पिंग हुंदै");
		searchReplaceRuleMap.put("वृद्धिपूर्वाञ्चल", "वृद्धि पूर्वाञ्चल");
		searchReplaceRuleMap.put("भ्रष्टाचार ", " भ्रष्टाचार ");
		searchReplaceRuleMap.put("सुठोे", " सुठो ");
		searchReplaceRuleMap.put("गाउं लेको", "गाउंले को");
		searchReplaceRuleMap.put("जांच की लाई", "जांचकी लाई ");
		searchReplaceRuleMap.put("अघिबढाएको", "अघि बढाएको");
		
		searchReplaceRuleMap.put("पौडेल", " पौडेल ");
		searchReplaceRuleMap.put("शर्मा", " शर्मा ");
		searchReplaceRuleMap.put("हुमागार्इं", " हुमागाइं ");
		searchReplaceRuleMap.put("श्रष्ठ", "श्रेष्ठ");
		searchReplaceRuleMap.put("सिग्देल", " सिग्देल ");
		
		//searchReplaceRuleMap.put("रुप मा", " रुप मा");
		//searchReplaceRuleMap.put("बास्क ओटा", "बास्कोटा");
		//searchReplaceRuleMap.put("सापक ओटा", "सापकोटा");
		//generalized
		searchReplaceRuleMap.put("क ओटा", "कोटा");
		
		searchReplaceRuleMap.put("भट्टराई", " भट्टराई ");
		searchReplaceRuleMap.put("कारखाना", " कारखाना ");
		
		searchReplaceRuleMap.put("रासस", " रासस ");
		
		searchReplaceRuleMap.put("भा का नाघे", "भाका नाघे");
		searchReplaceRuleMap.put("भा का भित्र", "भाका भित्र");
		searchReplaceRuleMap.put("निर्देशि का", "निर्देशिका");
		searchReplaceRuleMap.put("खँद्य", "खाद्य");
		searchReplaceRuleMap.put(" खँद संस्थान ", " खाद्य संस्थान ");
		
		searchReplaceRuleMap.put("झ्ापा", "झापा");
		searchReplaceRuleMap.put("झापा", " झापा ");
		searchReplaceRuleMap.put("सिस्नेरी", " सिस्नेरी ");
		searchReplaceRuleMap.put("मकवानपुर", " मकवानपुर ");
		searchReplaceRuleMap.put("त्रिशूली", " त्रिशूली ");
		searchReplaceRuleMap.put("बिर्तामोड", " बिर्तामोड ");
		searchReplaceRuleMap.put("काठमाडाँै", "काठमाडौं");
		searchReplaceRuleMap.put("काठमाडौँ ", " काठमाडौँ  ");
		searchReplaceRuleMap.put("काठमाडौं", " काठमाडौं ");
		searchReplaceRuleMap.put("पोखरा", " पोखरा ");
		searchReplaceRuleMap.put("विराटनगर", " विराटनगर ");
		searchReplaceRuleMap.put("दांग", " दांग ");
		searchReplaceRuleMap.put("धादिंग", " धादिंग ");
		searchReplaceRuleMap.put("महोत्तरी", " महोत्तरी ");
		searchReplaceRuleMap.put("मोरंग", " मोरंग ");
		searchReplaceRuleMap.put("ललितपुर", " ललितपुर ");
		searchReplaceRuleMap.put("वीरगन्ज", " वीरगन्ज ");
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
		searchReplaceRuleMap.put("सुनसरी", " सुनसरी " );
		searchReplaceRuleMap.put("भैँसे", " भैँसे ");
		searchReplaceRuleMap.put("सप्तकोशी", " सप्तकोशी ");
		searchReplaceRuleMap.put("गोरखा", " गोरखा ");
		searchReplaceRuleMap.put("गोरखा पत्र", "गोरखापत्र");
		searchReplaceRuleMap.put("खोटांग", " खोटांग ");
		searchReplaceRuleMap.put("क्याम्बि्रज", " क्याम्ब्रिज ");
		searchReplaceRuleMap.put("बिर्तामोड", " बिर्तामोड ");
		searchReplaceRuleMap.put(" ढा का ", " ढाका ");
		
		
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
		searchReplaceRuleMap.put("अव्यव स्थित", "अव्यवस्थित");
		searchReplaceRuleMap.put("अव स्थित ", "अवस्थित ");
		searchReplaceRuleMap.put("उप स्थित ", "उपस्थित ");
		
		//words below act like adjectives, do not split
		//searchReplaceRuleMap.put("अहिलेको", "अहिले को");
		//searchReplaceRuleMap.put("मध्येको", "मध्ये को");
		//searchReplaceRuleMap.put("पहिलेका", "पहिले का");
		
		//searchReplaceRuleMap.put("प्रतिशेयर", "प्रति शेयर");
		searchReplaceRuleMap.put("स्वीकार्नुहुने", "स्वीकार्नु हुने");
		searchReplaceRuleMap.put(" क्रेको ", " क्रे को "); //lastname
		
		searchReplaceRuleMap.put("अनुरुप ", " अनुरुप ");
		searchReplaceRuleMap.put("बराबर ", " बराबर ");
		
		searchReplaceRuleMap.put("परिचारि का ", "परिचारिका ");
		searchReplaceRuleMap.put("विद्यार्थी", " विद्यार्थी ");
		searchReplaceRuleMap.put(" हि ले ", "  हिले  ");
		searchReplaceRuleMap.put("जिम् मा", " जिम्मा");
		searchReplaceRuleMap.put("राजीना मा", "राजीनामा");
		searchReplaceRuleMap.put("बद्ध ", " बद्ध ");
		
		//fix
		searchReplaceRuleMap.put("कटि बद्ध", "कटिबद्ध");
		searchReplaceRuleMap.put("सम् बद्ध", "सम्बद्ध");
		searchReplaceRuleMap.put(" सम् बद्ध ", " सम्बद्ध ");
		
		
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
		searchReplaceRuleMap.put(" न् ", "न् "); //attach to prev

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
		searchReplaceRuleMap.put("त्यहा ँ", "त्यहाँ");
		searchReplaceRuleMap.put("पुनर्तालि का ", "पुनर्तालिका ");
		//searchReplaceRuleMap.put("तालि का ", "तालि का ");
		searchReplaceRuleMap.put(" ल जना ", " लजना "); //name
		searchReplaceRuleMap.put("परियो जना", "परियोजना");
		searchReplaceRuleMap.put(" यो जना ", " योजना ");
		searchReplaceRuleMap.put("समाचारपत्र", " समाचारपत्र ");
		searchReplaceRuleMap.put("नेपाल समाचारपत्र", " नेपाल समाचारपत्र ");
		searchReplaceRuleMap.put("सरकार ी", "सरकारी ");
		searchReplaceRuleMap.put("बैंककेन्द्रीय", "बैंक केन्द्रीय");
		searchReplaceRuleMap.put("कार्यालयविदेशी", "कार्यालय विदेशी");
		searchReplaceRuleMap.put("निराशानेपाल", "निराशा नेपाल");
		searchReplaceRuleMap.put("शुरुरत्नप्रसाद", "शुरु रत्नप्रसाद");
		searchReplaceRuleMap.put("प्रति बद्ध", "प्रतिबद्ध");
		searchReplaceRuleMap.put("हिराओ का", "हिराओका"); //surname
		
		/*
		searchReplaceRuleMap.put("बन्दरगाहमै", "बन्दरगाह मै");
		searchReplaceRuleMap.put("भविष्यमै", "भविष्य मै");
		searchReplaceRuleMap.put("समयमै", "समय मै");
		searchReplaceRuleMap.put("परिसूचकमै", "परिसूचक मै ");
		searchReplaceRuleMap.put("अंकमै", "अंक मै ");
		searchReplaceRuleMap.put("विश्वमै", "विश्व मै");
		*/
		//generalize
		searchReplaceRuleMap.put("मै ", " मै ");
		//fix
		searchReplaceRuleMap.put(" काय मै ", " कायमै ");
		searchReplaceRuleMap.put("एकद मै", "एकदमै");
		searchReplaceRuleMap.put("क मै", "कमै");
		searchReplaceRuleMap.put("बित्ति कै", " बित्तिकै ");
		searchReplaceRuleMap.put(" ना मै ", " नामै ");
		searchReplaceRuleMap.put(" जम् मै ", " जम्मै ");
		//searchReplaceRuleMap.put("ाा", "ा");
		//multiple occurrences of inflections replaced by 1
		searchReplaceRuleMap.put("ा+", "ा");
		searchReplaceRuleMap.put("ि+", "ि");
		searchReplaceRuleMap.put("ी+", "ी");
		searchReplaceRuleMap.put("ु+", "ु");
		searchReplaceRuleMap.put("ू+", "ू");
		searchReplaceRuleMap.put("े+", "े");
		searchReplaceRuleMap.put("ै+", "ै");
		searchReplaceRuleMap.put("ो+", "ो");
		searchReplaceRuleMap.put("ौ+", "ौ");
		
		//lone inflections
		searchReplaceRuleMap.put(" ा ", " ");
		//lines with lone ि are removed
		searchReplaceRuleMap.put(" ी ", "ी ");		
		searchReplaceRuleMap.put(" ु ", " ");
		searchReplaceRuleMap.put(" ू ", " ");
		searchReplaceRuleMap.put(" े ", " ");
		searchReplaceRuleMap.put(" ै " , "ै ");
		//searchReplaceRuleMap.put(" सँग ै", " सँगै "); //fix
		searchReplaceRuleMap.put(" ो ", " ");
		searchReplaceRuleMap.put(" ौ ", "ौ "); //fix for results of हाल्नेछ  ं
		searchReplaceRuleMap.put(" ् ", "् "); //set to previous word
		
		searchReplaceRuleMap.put("अनु आमा", "अनु लामा");
		searchReplaceRuleMap.put("जोडदिएका", "जोड दिएका");
		searchReplaceRuleMap.put("घुम्नुपरेको", "घुम्नु परेको");
		searchReplaceRuleMap.put("लायक", " लायक ");
		//fix
		searchReplaceRuleMap.put(" ना लायक " , " नालायक ");
		searchReplaceRuleMap.put(" बे लायक " , " बेलायक ");
		searchReplaceRuleMap.put("घटयोशम्भु ", "घटयो शम्भु ");
		searchReplaceRuleMap.put("अँएको", "आएको");
		searchReplaceRuleMap.put("खँद्यान्न", "खाद्यान्न");
		searchReplaceRuleMap.put("अर्थमन्त्री", " अर्थमन्त्री ");
		
		searchReplaceRuleMap.put(" का मा ", "का मा");
		searchReplaceRuleMap.put("सकिए मा ", "सकिएमा ");
		searchReplaceRuleMap.put(" टु की ", " टुकी ");
		searchReplaceRuleMap.put("ाू", "ाँ");
		searchReplaceRuleMap.put(" अर् कै ", " अर्कै ");
		searchReplaceRuleMap.put("हुूदैन", "हुँदैन");
		searchReplaceRuleMap.put("हँुदैन", "हुँदैन");
		searchReplaceRuleMap.put("श्रमसमुह", "श्रम समुह");
		searchReplaceRuleMap.put("लोरि लार्ड", "लोरिलार्ड"); //name
		searchReplaceRuleMap.put("विश्‍वविद्यालयका", "विश्‍वविद्यालय का");
		searchReplaceRuleMap.put("मेसोथेलियो मा", "मेसोथेलियोमा");
		searchReplaceRuleMap.put("सदनले", "सदन ले");
		searchReplaceRuleMap.put("श्री मती", "श्रीमती");
		searchReplaceRuleMap.put("श्रीमतीहरू", "श्रीमती हरू");
		searchReplaceRuleMap.put("भइन सकेको", "भइनसकेको");
		searchReplaceRuleMap.put("फेन्नी मेका", "फेन्नी मे का");
		searchReplaceRuleMap.put("फेनी मेको ", "फेनी मे को ");
		searchReplaceRuleMap.put(" ढि की ", " ढिकी ");
		searchReplaceRuleMap.put("बुध बारे", "बुधबारे");
		searchReplaceRuleMap.put("अम्बि का", "अम्बिका");
		searchReplaceRuleMap.put("आ , वान", "आह्वान");
		searchReplaceRuleMap.put("निर्ण्र्ा", "निर्णय ");
		searchReplaceRuleMap.put("गर्छु", "गर्छु ");
		searchReplaceRuleMap.put("गु्रप", "ग्रुप");
		searchReplaceRuleMap.put(" टं की ", " टंकी "); //place
		searchReplaceRuleMap.put(" डां का ", " डांका ");
		searchReplaceRuleMap.put("प्रहरीचौ की", "प्रहरीचौकी");
		searchReplaceRuleMap.put(" जाइ का ", " जाइका ");
		searchReplaceRuleMap.put("खानोले", "खानो ले"); //name
		searchReplaceRuleMap.put("दुईमहिने", "दुई महिने");
		searchReplaceRuleMap.put("जनँएको", "जनाएको");
		searchReplaceRuleMap.put("जनँइएको", "जनाइएको");
		searchReplaceRuleMap.put("प्रँप्त", "प्राप्त");
		searchReplaceRuleMap.put("बैंकहरु", "बैंक हरु");
		searchReplaceRuleMap.put(" बी मा समूह", " बीमा समूह");
		searchReplaceRuleMap.put("अफि्र का", "अफ्रिका");
		searchReplaceRuleMap.put("आयोजना हरु", "आयोजना हरु");
		searchReplaceRuleMap.put(" म कै ", " मकै ");
		
		
		//most freq errors by looking at top vocabs
		//searchReplaceRuleMap.put(" बा की ", " बाँकी ");
		searchReplaceRuleMap.put(" बा च्न ", " बाँच्न ");
		searchReplaceRuleMap.put(" बा धिए", " बाँधिए");
		searchReplaceRuleMap.put("बा लेको", " बालेको ");
		searchReplaceRuleMap.put(" बा दर ", " बाँदर ");
		searchReplaceRuleMap.put(" प ले टी ", " पलेटी ");
		searchReplaceRuleMap.put("रू प ", "रूप ");
		searchReplaceRuleMap.put("अनु रूप", "अनुरूप");
		searchReplaceRuleMap.put(" प का ", " पका ");
		searchReplaceRuleMap.put(" को प मा ", " को रुप मा ");
		searchReplaceRuleMap.put(" का प मा ", " का रुप मा");
		searchReplaceRuleMap.put(" प मा ", " रुप मा ");
		searchReplaceRuleMap.put("उहाँह ", "उहाँ हरू ");
		searchReplaceRuleMap.put(" स लाई ", " सलाई ");
		searchReplaceRuleMap.put(" स मा ", " समा ");
		searchReplaceRuleMap.put(" बा स ", " बास ");
		searchReplaceRuleMap.put("स – सान", "स–सान"); //eg स – सानो
		searchReplaceRuleMap.put("स \\( स", "स–स");
		searchReplaceRuleMap.put(" स ग", " सँग");
		searchReplaceRuleMap.put("स ग ", " सँग ");
		searchReplaceRuleMap.put(" स गस गै ", " सँगसँगै  ");
		searchReplaceRuleMap.put("गुरा स ", "गुरास ");
		searchReplaceRuleMap.put(" पा ले", " पाले");
		searchReplaceRuleMap.put(" पा च", " पाँच");
		searchReplaceRuleMap.put("पा ' च", "पाँच");
		searchReplaceRuleMap.put(" पा को ", " पाको ");
		searchReplaceRuleMap.put(" पा की ", " पाकी ");
		searchReplaceRuleMap.put(" पा का ", " पाका ");
		searchReplaceRuleMap.put(" सा ' ब ", " साब ");
		searchReplaceRuleMap.put(" सा ब ", " साब ");
		searchReplaceRuleMap.put(" सा झ", " साँझ");
		searchReplaceRuleMap.put(" सा का ला का ", " साकालाका ");
		searchReplaceRuleMap.put(" सा घु", " साँघु");
		searchReplaceRuleMap.put("सा ठगा ठ", " साँठ गाँठ ");
		searchReplaceRuleMap.put(" ले सा ", " ले सा"); //attach to next word
		searchReplaceRuleMap.put(" मा सा  ", " मासा");
		searchReplaceRuleMap.put(" को सा ", " को सा");
		searchReplaceRuleMap.put(" सा चो ", " साँचो ");
		searchReplaceRuleMap.put(" सा च्", " साच्");
		searchReplaceRuleMap.put(" सा ग", " साग");
		searchReplaceRuleMap.put(" सा घ", " साघ");
		searchReplaceRuleMap.put(" आ गन ", " आगन ");
		searchReplaceRuleMap.put(" प्र वास", " प्रवास");
		searchReplaceRuleMap.put(" प्र . ", " प्र. ");
		searchReplaceRuleMap.put("स्व . ", "स्व. ");
		//remove other dots
		searchReplaceRuleMap.put(" . ", " ");
		searchReplaceRuleMap.put("आपैँ्क", "आँफै");
		searchReplaceRuleMap.put(" ढ ङ्ग ", " ढङ्ग ");
		searchReplaceRuleMap.put("अ ङ्ग भ ङ्ग", "अङ्ग भङ्ग");
		searchReplaceRuleMap.put("प्रस ङ्ग ", "प्रसङ्ग ");
		searchReplaceRuleMap.put("सि ङ्ग ", "सिंगो ");
		searchReplaceRuleMap.put("जिया ङ्ग ", "जियाङ्ग ");
		searchReplaceRuleMap.put("द ङ्ग ", "दङ्ग ");
		//remove others
		searchReplaceRuleMap.put(" ङ्ग ", " ");
		
		searchReplaceRuleMap.put(" टी का ", " टीका ");
		searchReplaceRuleMap.put("टी का – टिप्पणी", "टीका–टिप्पणी");
		searchReplaceRuleMap.put("सुन्दा – सुन्दा", "सुन्दा–सुन्दा");
		searchReplaceRuleMap.put("बी – बीच", "बी–बीच");
		
		searchReplaceRuleMap.put(" क हा ", " कहाँ ");
		searchReplaceRuleMap.put(" हा के ", " हाँके ");
		searchReplaceRuleMap.put("कहाँ", " कहाँ ");
		searchReplaceRuleMap.put(" हा स्", " हास्");
		searchReplaceRuleMap.put(" हा क्", " हाक्");
		
		searchReplaceRuleMap.put(" बी मा ", " बीमा ");
		searchReplaceRuleMap.put(" भा डा ", " भाडा ");
		searchReplaceRuleMap.put(" भा डो ", " भाडो ");
		searchReplaceRuleMap.put(" भा का ", " भाका ");
		searchReplaceRuleMap.put(" भा को ", " भाको ");
		searchReplaceRuleMap.put(" भा की ", " भाकी ");
		searchReplaceRuleMap.put("भा चिएका", "भाचिएका");
		searchReplaceRuleMap.put(" भा डाकु डा ", " भाडाकुडा ");
		searchReplaceRuleMap.put(" भा छ ", " भा'छ ");
		searchReplaceRuleMap.put(" भा ' छ ", " भा'छ ");
		searchReplaceRuleMap.put(" सु कै ", " सुकै ");
		searchReplaceRuleMap.put(" सु का ", " सुका ");
		searchReplaceRuleMap.put(" सु – ", " सु–");
		searchReplaceRuleMap.put("ज – जस", "ज–जस");
		
		searchReplaceRuleMap.put(" आ सु ", " आँसु ");
		searchReplaceRuleMap.put(" आ खा ", " आँखा ");
		searchReplaceRuleMap.put(" ँ ", "ँ "); //attach to prev word
		searchReplaceRuleMap.put("उँ दै", "उँदै"); //fix verbs
		searchReplaceRuleMap.put("गोरखा ली ", "गोरखाली ");
		searchReplaceRuleMap.put("झापा ली ", "झापाली ");
		searchReplaceRuleMap.put("काठमाडौं ली", "काठमाडौंली");
		searchReplaceRuleMap.put("अन्तर्रर्ाट्रय", "अन्तराष्ट्रिय");
		searchReplaceRuleMap.put("कहाँबाट", "कहाँ बाट");
		searchReplaceRuleMap.put(" ब ल", " बल");
		searchReplaceRuleMap.put(" बा ध ", " बाँध ");
		searchReplaceRuleMap.put(" दोसा ध ", " दोसाध ");
		searchReplaceRuleMap.put(" ध मा ", " धमा ");
		//searchReplaceRuleMap.put(" खा का ", " खाका ");
		searchReplaceRuleMap.put(" खा क", " खाक"); //generalized
		searchReplaceRuleMap.put(" खा चो ", " खाँचो ");
		searchReplaceRuleMap.put(" खा ' क", " खा'क"); //eg. खा ' का
		searchReplaceRuleMap.put(" खा द", " खाँद"); //generalized
		searchReplaceRuleMap.put(" बि मा ", " बिमा ");
		searchReplaceRuleMap.put(" ना कै ", " नाकै ");
		searchReplaceRuleMap.put(" आ \\) ना ", " आफ्ना ");
		searchReplaceRuleMap.put(" आप ना ", " आफ्ना ");
		searchReplaceRuleMap.put(" आफ् ना ", " आफ्ना ");
		searchReplaceRuleMap.put(" गो मा ", " गोमा ");
		searchReplaceRuleMap.put(" मह गो ", " महँगो ");
		searchReplaceRuleMap.put(" दि दा ", " दिँदा ");
		searchReplaceRuleMap.put(" दि दै", " दिँदै");
		searchReplaceRuleMap.put(" दि ज्यू ", " दिज्यू ");
		searchReplaceRuleMap.put(" निर्ण्र्ाई ", " निर्णय ");
		searchReplaceRuleMap.put("हुूदैछ", "हुदैछ");
		searchReplaceRuleMap.put("बँुदा", "बुँदा");
		searchReplaceRuleMap.put(" सि चाइ ", " सिचाइ ");
		searchReplaceRuleMap.put(" सि मा ", " सिमा ");
		searchReplaceRuleMap.put(" सि को ", " सिको ");
		searchReplaceRuleMap.put(" सि की ", " सिकी ");
		searchReplaceRuleMap.put(" सि नींग ", " सिनींग ");
		searchReplaceRuleMap.put(" अ ध्या", " अध्या");
		searchReplaceRuleMap.put(" अ सित ", " असित ");
		searchReplaceRuleMap.put(" अ सित ", " असित ");
		searchReplaceRuleMap.put(" ज ले", " जले");
		searchReplaceRuleMap.put(" फ ले ", " फले ");
		searchReplaceRuleMap.put(" ज मै का ", " जमैका ");
		searchReplaceRuleMap.put("मेक्सि को ", "मेक्सिको ");
		searchReplaceRuleMap.put(" प्रि ज ", " फ्रिज ");
		
		searchReplaceRuleMap.put(" ह लाई ", " हरु लाई ");
		searchReplaceRuleMap.put(" ह बाट", " हरु बाट");
		searchReplaceRuleMap.put(" ह रू ", " हरू ");
		searchReplaceRuleMap.put(" ह को ", " हरू को ");
		searchReplaceRuleMap.put(" ह का ", " हरू का ");
		searchReplaceRuleMap.put("विद्यार्थी ह ", "विद्यार्थी हरू ");
		searchReplaceRuleMap.put(" ह वस ", " हवस ");
		searchReplaceRuleMap.put("सोर् ह ", "सोह्र ");
		searchReplaceRuleMap.put("बार् ह ", "बाह्र ");
		searchReplaceRuleMap.put("तेर् ह ", "तेह्र ");
		searchReplaceRuleMap.put(" ह की ", " हकी ");
		searchReplaceRuleMap.put("शहरह", "शहर");
		searchReplaceRuleMap.put(" ह स", " हँस"); //eg. हँसिलो
		searchReplaceRuleMap.put("सि. ह ", "सिंग ");
		
		searchReplaceRuleMap.put(" दो ", "दो ");//fixes most tokens
		
		searchReplaceRuleMap.put(" भू – ", " भू–"); //merge with next word
		searchReplaceRuleMap.put(" भू \\( ", " भू–");
		
		searchReplaceRuleMap.put(" भू मा ", " भूमा ");
		searchReplaceRuleMap.put(" मु मा ", " मुमा ");
		searchReplaceRuleMap.put(" जम् का ", " जम्का ");
		searchReplaceRuleMap.put(" हि ड", " हिड");
		searchReplaceRuleMap.put(" हि ल", " हिल");
		searchReplaceRuleMap.put(" हि मा ", " हिमा ");
		
		searchReplaceRuleMap.put(" झु मा ", "  झुमा ");
		searchReplaceRuleMap.put(" झु ल", " झुल");
		searchReplaceRuleMap.put(" अर्काे ", " अर्को ");
		searchReplaceRuleMap.put(" सुझ्ाव ", " सुझाव ");
		searchReplaceRuleMap.put(" पर््रदर्शन ", " प्रदर्शन ");
		searchReplaceRuleMap.put(" सत्तै्क", " सक्तै");
		
		searchReplaceRuleMap.put(" इ को ", " इको ");
		searchReplaceRuleMap.put(" इ टा ", "  इँटा ");
		searchReplaceRuleMap.put(" इ ट्टा ", " इ ट्टा ");
		searchReplaceRuleMap.put(" इ – ", " इ–");
		searchReplaceRuleMap.put(" इ लिस ", " इंग्लिश ");
		searchReplaceRuleMap.put(" इ को ", " इको ");
		
		searchReplaceRuleMap.put(" वषर्ी", " वर्षी");
		searchReplaceRuleMap.put(" बु दे ", " बुंदे ");
		
		searchReplaceRuleMap.put("उँ्क" , "उँ"); //eg. खाउँ्क, जाउँ्क
		searchReplaceRuleMap.put("भैँ्क", " झैँ ");
		searchReplaceRuleMap.put(" जु ", " जु"); //attach with next word
		searchReplaceRuleMap.put("मु फ् त", "मुफ्त");
		searchReplaceRuleMap.put(" मु मा ", " मुमा ");
		
		searchReplaceRuleMap.put("पुर् या", "पुर्या");
		
		searchReplaceRuleMap.put("पु र् या", "पुर्या");
		searchReplaceRuleMap.put(" र् याली ", " र्याली ");
		searchReplaceRuleMap.put(" र् याम्प ", " र्याम्प ");
		searchReplaceRuleMap.put(" र् हास ", " र्हास ");
		searchReplaceRuleMap.put(" र् याल ", " र्याल ");
		searchReplaceRuleMap.put("हो र् याए", "होर्याए");
		searchReplaceRuleMap.put("ग र् यो", "गर्यो");
		searchReplaceRuleMap.put(" र् ", " "); //remove rest
		searchReplaceRuleMap.put(" रो की ", " रोकी ");
		searchReplaceRuleMap.put(" रो का ", " रोका ");
		searchReplaceRuleMap.put(" रो मा ", " रोमा ");
		
		searchReplaceRuleMap.put(" ने हरू ", " नेहरू ");
		searchReplaceRuleMap.put("ड् ने ", "ड्ने ");
		searchReplaceRuleMap.put(" गा उमा ", " गाउँ मा ");
		searchReplaceRuleMap.put(" गा जा ", " गाँजा ");
		searchReplaceRuleMap.put(" गा ' को ", " गा'को ");
		searchReplaceRuleMap.put("गा ' छ", "गा' छ");
		searchReplaceRuleMap.put(" रा गा ", " राँगा ");
		searchReplaceRuleMap.put(" गा स", " गाँस");
		searchReplaceRuleMap.put(" मह गा ", " महँगा ");
		searchReplaceRuleMap.put(" गा ठो", " गाँठो");
		searchReplaceRuleMap.put("अ गा ले", "अँगाले");
		
		searchReplaceRuleMap.put(" सार् की ", " सार्की ");
		searchReplaceRuleMap.put(" सार् है ", " सार्है ");
		searchReplaceRuleMap.put(" सार् यौ ", " सार्यौ ");
		searchReplaceRuleMap.put(" बाँ क", " बाँक");
		searchReplaceRuleMap.put("साहेब नी ", "साहेबनी ");
		
		searchReplaceRuleMap.put(" यौ टा ", " यौटा ");
		searchReplaceRuleMap.put("दुई टा ", "दुईटा ");
		searchReplaceRuleMap.put("ठट् टा ", "ठट्टा ");
		searchReplaceRuleMap.put(" टा सि", " टाँसि");
		searchReplaceRuleMap.put(" टा ग", " टाँग");
		searchReplaceRuleMap.put(" मान्छेको " , " मान्छे को ");
		searchReplaceRuleMap.put(" आ ट ", " आँट ");
		searchReplaceRuleMap.put(" फा ट ", " फाँट ");
		searchReplaceRuleMap.put("सप ट ", "सफ्ट ");
		searchReplaceRuleMap.put("हुू", "हुँ");
		searchReplaceRuleMap.put("हँु", "हुँ");
		//searchReplaceRuleMap.put("हुूदा", v)
		
		searchReplaceRuleMap.put(" कु दिए", " कुदिए");
		searchReplaceRuleMap.put(" कु मा ले", " कुमाले ");
		searchReplaceRuleMap.put("कु माले", "कुमाले ");
		
		searchReplaceRuleMap.put(" चक् ", " चक्");
		searchReplaceRuleMap.put("दुर्ई", "दुई");
		searchReplaceRuleMap.put(" फा ", " फा");
		//searchReplaceRuleMap.put(" – ", "–");
		searchReplaceRuleMap.put(" ात्मक ", "ात्मक ");
		searchReplaceRuleMap.put("वाग् ले", "वाग्ले ");
		searchReplaceRuleMap.put("मि्र", "म्रि");
		searchReplaceRuleMap.put("फ ओटा", "फोटा");
		searchReplaceRuleMap.put(" फ लेक", " फलेक");
		searchReplaceRuleMap.put(" गोर् ", " गोर्");
		searchReplaceRuleMap.put(" चौ का ", " चौका ");
		searchReplaceRuleMap.put(" पा चौ ", " पाँचौ ");
		
		searchReplaceRuleMap.put("तपार्इं", "तपाइं");
		searchReplaceRuleMap.put(" रै ' ", " रै'");
		searchReplaceRuleMap.put(" रि – ", " रि–");
		searchReplaceRuleMap.put(" रि ", " रि");
		searchReplaceRuleMap.put(" नहु ", " नहुँ");
		
		
		
		searchReplaceRuleMap.put("आरुनो", "आफ्नो");
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
			
			//remove space before decimal numbers
			for(int i = 0; i <= 9; i++) {
				  line = line.replace(" ." + (char)(0x966+i), "." + (char)(0x966+i));
			}
			
			//remove space after decimal numbers
			for(int i = 0; i <= 9; i++) {
				  line = line.replace(". " + (char)(0x966+i), "." + (char)(0x966+i));
			}
			
			//errors like *num*छ, last should be a number 6
			for(int i=0; i<=9; i++) {
				line = line.replace(""+ (char)(0x966+i) + "छ", "" + (char)(0x966+i) + (char)(0x966+6));
			}
			
			
			pw.println(line);
			pw.flush();
		}
		br.close();
		pw.close();
	}
}
