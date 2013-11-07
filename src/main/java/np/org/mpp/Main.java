package np.org.mpp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;


import np.org.mpp.morph.Morph;
import np.org.mpp.xmltag.ReadWriteXML;
import np.org.mpp.xmltag.Tokenizer;

public class Main {
	static String tag_input, r1,p;

	static int bk, cm,rep = 0, i = 0, k = 0, repc = 0;

	public static void main(String args[]) {
		if(args.length != 3) {
			System.err.println("usage: <program> input_file output_file unrecognized_out_file");
			System.exit(-1);
		}
		String inFile = args[0];
		String outFile = args[1];
		String unrecognizedFile = args[2];
		
		Date d = new Date();
		d.getTime();
		String unrecog="\n";

		Morph m = new Morph();
		// m.setSecParse();
		try {
			File file = new File(inFile);
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			filereader1.close();
			tag_input = new String(data, "UTF8");
			//System.out.println(tag_input);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		Tokenizer w = new Tokenizer();
		w.tokenize(tag_input);

		String out = w.getTokenizedOutput();
		//System.out.println(out);
		ReadWriteXML r = new ReadWriteXML(out);
		cm = r.getNodeListLength();
		
		/*
		//System.out.println(cm);
		for (int c = 0; c < cm; c++) {
			String n = r.getTextCont(c); //if type=="w" get word, else nocontent
			//First check if the token is a word or not
		if(!n.equals("nocontent")){
			m.setPMorph_number(0);
			m.setSMorph_number(0);
			m.setRootPos(null);
			rep = 0;
			m.setSecParse(rep);
			p="yes";
			r1 = n;
			i = 0;
			k = 0;
			//start while
			while (!r1.isEmpty()) {
				try{
					if (m.isRoot(r1) && p.equals("yes") ||( m.isAltRoot(r1) && i>0)) {
						if(m.getSuffixExist(r1).equals("N") && i>0)
						{
							p="no";
							
						}else{
							if( m.isAltRoot(r1) && i>0)							
								m.setRoot(m.getAltRoot(r1));
							else
							m.setRoot(r1);
							break;
						}
						
					}
	//				for verbs ending with halanta
					else if (m.isRoot(r1 + "\u094d")) {
						i++;
						m.setSMorph_number(i);
						m.isASuffix(r1);
						r1 = m.getRoot();
	
					}
					else if (r1.endsWith("\u094d") && m.isRoot(r1.substring(0,r1.length()-1))){
						r1=r1.substring(0,r1.length()-1);
						m.setRoot(r1);
						
					} else if (m.suffixPresent(r1,i)) {

						i++;
						try{
							m.setSMorph_number(i);
							m.stripSuffix(r1);
							
							}catch(Exception e){
								//javax.swing.JOptionPane.showMessageDialog(null,r1+i);
								System.err.println("word = " + r1);
						}
	
						r1 = m.getRoot();
						//javax.swing.JOptionPane.showMessageDialog(null,r1);
	
					} else if (m.prefixPresent(r1)) {
						k++;
	
						m.setPMorph_number(k);
						m.stripPrefix(r1);
						r1 = m.getRoot();
	
					} else
					//If 1 or more prefix present and the root is not found try combining the suffix
					//if prefix and suffix present
					if (k > 0 && i > 0) {
						String tm, tmp;
						String[] a = m.getPMorph();
	
						for (int k1 = k; k1 > 0; k1--) {
							tmp = r1;
							for (int l = i; l > 0; l--) {
	
								tm = m.generateWord(tmp, l);
	
								//javax.swing.JOptionPane.showMessageDialog(null,"return1"+tm);
	
								if (m.isRoot(tm)||( m.isAltRoot(tm) && i>0)) {
									bk = 1;
									m.setSMorph_number(l - 1);
									m.setPMorph_number(k);
									
									if(( m.isAltRoot(tm) && i>0))
										m.setRoot(m.getAltRoot(tm));
									else
									m.setRoot(tm);
									r1 = m.getRoot();
	
									break;
								} else {
									bk = 0;
									tmp = tm;
								}
	
							}
							if (bk == 1) {
								break;
							}
	
							if (k1 > 1)
								r1 = a[k1] + r1;
	
						}
						if (bk != 1) {
							r1 = "unrecognized";
							k = 0;
						}
	
					} else {
						m.setRoot("unrecognized");
						
					} 
					//for ignoring longest suffix
					if (m.getRoot().equals("unrecognized")) {
						rep++;
	
						int repeat = 0;
						//for the second parse
						if (rep == 1) {
							//check for if the any rulenumber of the suffix contains repeat sign "Y"					
							for (int l = i; l > 0; l--) {
								if (m.isRepeat(Integer.toString(m
										.getSMorph_rulenum(l)))) {
									repeat = 1;
									break;//for any suffix that has a repeat sign.
								} else
									repeat = 0;
							
							}
						}
						//if any rulenumber has the suffix content as repeat sign"Y"
						if (repeat == 1) {
							r1 = n;
							m.setPMorph_number(0);
							m.setSMorph_number(0);
							i = 0;
							k = 0;
							p="yes";
	
							m.setSecParse(rep);
							// m.suffixPresent(r1);
						} else
							break;
	
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}//while ends
//			For dublicated words.
			if (m.getRoot().equals("unrecognized")) {
				//call function from morphological analyser for dublicated words.
				
				m.handleDublicateWord(n);
				//javax.swing.JOptionPane.showMessageDialog(null,m.getRootPos());

			}
//			For compound words
			if (m.getRoot().equals("unrecognized")) {
				//System.out.println(n);
				m.handleCompoundWord(n);
			}
			
			if(m.getRoot().equals("unrecognized")){
				unrecog=unrecog+n+"\n";
			}

			//Creating the output.....
			StringBuffer a = new StringBuffer("");
			//morpheme suffix pos
			StringBuffer mspos = new StringBuffer("");
			//morpheme prefix pos
			StringBuffer mppos = new StringBuffer("");
			StringBuffer b = new StringBuffer("");
			String[] pmorpheme = m.getPMorph();
			String[] pdt = m.getPDes();
			String[] smorpheme = m.getSMorph();
			String[] sdt = m.getSDes();

			int p = m.getPMorph_number();

			int s = m.getSMorph_number();
			//add prefix

			for (int j = 0; j < p; j++) {

				b.append(pmorpheme[j + 1] + "(" + pdt[j + 1] + ")" + "+");
				mppos.append(pdt[j + 1] + "_");
			}
			//add suffix
			for (int j = s; j > 0; j--) {
				//add for "Morph"
				a.append("+" + smorpheme[j] + "(" + sdt[j] + ")");
				//add for "POS"
				mspos.append("_" + sdt[j]);
			}

			r.setMorphContent(c, b + m.getRoot() + "(" + m.getRootPos() + ")"
					+ a);

			if (!m.getRoot().equals("unrecognized")) {
				
				r.setPosContent(c, mppos + m.getRootPos() + mspos);
			}
		}
		}
		*/
		
		try {
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFile), "UTF8"));
			buffer.write(r.getDocumentString());
			buffer.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		//for list of unrecognized words
		try {
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(unrecognizedFile), "UTF8"));
			buffer.write(unrecog);
			buffer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//finished unrecognized words
		Date d1 = new Date();
		d1.getTime();
		System.out.println("start:" + d + "\nend:" + d1);

	}

}