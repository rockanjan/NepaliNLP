package np.org.mpp.morph;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Morph {

	HashMap suf_sub_rule = new HashMap();

	HashMap suffix_del = new HashMap();
	// whether suffix can be attached to the root
	HashMap suffix_exist = new HashMap();

	HashMap suffix_look_posi = new HashMap();

	HashMap suffix_look_char = new HashMap();

	HashMap suffix_desc = new HashMap();

	HashMap suffix_ins = new HashMap();

	HashMap suffix_ins_posi = new HashMap();

	HashMap suffix_morph = new HashMap();

	HashMap suffix_type = new HashMap();

	HashMap suffix_ign = new HashMap();

	HashMap dub_suffix = new HashMap();

	HashMap dub_suffix_morph = new HashMap();

	HashMap dub_suffix_desc = new HashMap();

	HashMap pre_sub_rule = new HashMap();

	HashMap prefix_del = new HashMap();

	HashMap prefix_desc = new HashMap();

	HashMap prefix_ins = new HashMap();

	HashMap prefix_morph = new HashMap();

	HashMap prefix_type = new HashMap();

	static StringBuffer q;

	int parsetwo = 0;

	String[] sdes = new String[10];

	String[] smorph = new String[10];

	int[] subsmorph = new int[10];

	int[] smorph_rulenum = new int[10];

	String[] pdes = new String[10];

	String[] repsuff = new String[10];

	String[] pmorph = new String[10];

	Boolean dspresent;

	int rulenumber, smorph_number, pmorph_number, dub_len, spnt, i = 0, k = 0,
			go, repcom, rec;

	String suffix, prefix, suffix_index, prefix_index, suffix_rule,
			prefix_rule, root, tmpgen, isSuffixRoot, suffix_root,
			root_pos = "", present = "no";

	Hashtable ht_root = new Hashtable();

	Hashtable ht_root_pos = new Hashtable();

	// suffix rule number suffix_ht
	Hashtable suffix_ht = new Hashtable();
	// alternate for root
	Hashtable alt_root = new Hashtable();

	Hashtable ht_suffix = new Hashtable();

	Hashtable prefix_tm = new Hashtable();

	Hashtable ht_prefix = new Hashtable();

	public static StringBuffer w;

	public Morph() {
		/**
		 * Put the root list in hash map
		 */
		try {
			File file = new File("tokenizerdata/newfile2");
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			String root = new String(data, "UTF8");
			String tmp;
			String[] q, tok;
			filereader1.close();
			q = root.split("\n");
			String sa;
			/*
			 * for(int m=0;m<c;m++){ l.add(m,st.nextToken()); }
			 * Collections.sort(l);
			 */

			int lenq = q.length;

			for (int qcount = 0; qcount < lenq; qcount++) {
				tmp = q[qcount];
				// a space present indicates the presence of the POS tag of the
				// root word
				// javax.swing.JOptionPane.showMessageDialog(null,"step1"+tmp);

				if (tmp.contains("|")) {

					tok = tmp.split("\\|");
					int lentok = tok.length;

					sa = tok[0];

					ht_root.put(sa, sa);
					/*
					 * for later changes //putting all the POS in the form of
					 * noun|verb etc. for(int i=2;i<=sa.length;i++){
					 * sa[1]+="|"+sa[i]; }
					 */
					// puting the pos in the hash table.
					ht_root_pos.put(sa, tok[1]);

					if (lentok > 2) {
						suffix_exist.put(sa, tok[2]);
					} else {
						suffix_exist.put(sa, "null");
					}

				} else {
					ht_root.put(tmp, tmp);
					ht_root_pos.put(tmp, "null");
					suffix_exist.put(tmp, "null");

				}
				/*
				 * vroot.addElement(st.nextToken());
				 * System.out.println("initialize root final");
				 */
			}

		} catch (Exception e) {

			System.out.println("error:" + e.toString()
					+ "newfile2 file corrupt");
		}
		try {
			File file = new File("tokenizerdata/alt_root.txt");
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			filereader1.close();
			String suffix = new String(data, "UTF8");

			String[] st, part_st;
			st = suffix.split("\n");
			int lenst = st.length;
			for (int q = 0; q < lenst; q++) {
				part_st = st[q].split(" ");
				alt_root.put(part_st[0], part_st[1]);
				suffix_exist.put(part_st[0], "null");
			}

		} catch (Exception e) {
			System.out.println("error:" + e.toString()
					+ "alt_root.txt file corrupt");
		}

		/**
		 * Put the suffix list in hash map and Put the relating rule number in a
		 * hash
		 */
		try {

			File file = new File("tokenizerdata/suffix.txt");
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			filereader1.close();
			String suffix = new String(data, "UTF8");

			String[] st, part_st;
			st = suffix.split("\n");
			String tmp1;
			int lenst = st.length;
			for (int q = 0; q < lenst; q++) {
				// part_st = new StringTokenizer(st.nextToken(), "|");
				part_st = st[q].split("\\|");
				tmp1 = part_st[0];
				// ht_suffix.put(String.valueOf(i),tmp1);
				ht_suffix.put(tmp1, tmp1);
				// vsuffix.addElement(tmp1);

				if (part_st.length > 1) {
					suffix_ht.put(tmp1, part_st[1]);
				}
				// i++;
			}

		} catch (Exception e) {
			System.out.println("error:" + e.toString());
		}
		/**
		 * for suffix rule
		 */
		try {
			String s_rule, val;
			int value;
			// StringTokenizer part_st, srule;
			String[] st, part_st, srule;

			File file = new File("tokenizerdata/suffix_rule.txt");
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			filereader1.close();
			s_rule = new String(data, "UTF8");

			// StringTokenizer st = new StringTokenizer(s_rule, "\n");
			st = s_rule.split("\n");
			int lenst = st.length;

			// while (st.hasMoreTokens()) {
			for (int q = 0; q < lenst; q++) {
				int nextq = q;
				// part_st = new StringTokenizer(st.nextToken(), " ");
				part_st = st[q].split(" ");

				try {
					// rule number
					val = part_st[0].toString();
					// value for to check whether the first is a number
					value = Integer.parseInt(val);

					suffix_type.put(val, part_st[1]);// whether the rule is of
														// SFXX or SFX

					suf_sub_rule.put(val, part_st[2]);// number of sub rule

					suffix_morph.put(val, part_st[3]);// the morph

					suffix_desc.put(val, part_st[4]);// the tag of the morph

					suffix_ign.put(val, part_st[5]);// To ignore in the second
													// parse ('Y'or'N')

					int s = Integer.parseInt(suf_sub_rule.get(val).toString());// geting
																				// the
																				// sub
																				// rule
					if (suffix_type.get(val).equals("SFX")) {

						for (int b = 0; b < s; b++) {
							nextq = nextq + 1;
							// srule = new StringTokenizer(st.nextToken(), " ");
							srule = st[nextq].split(" ");
							// tmp = srule.nextToken().toString();
							// suffix_del.put(val + String.valueOf(b),
							// srule.nextToken());
							suffix_del.put(val + String.valueOf(b), srule[0]);
							// tmp=srule.nextToken().toString();
							// suffix_ins.put(val + String.valueOf(b), srule
							// .nextToken());
							suffix_ins.put(val + String.valueOf(b), srule[1]);
						}
					} else if (suffix_type.get(val).equals("SFXX")) {
						for (int b = 0; b < s; b++) {
							// srule = new StringTokenizer(st.nextToken(), " ");
							srule = st[nextq].split(" ");
							// string deletion position

							// suffix_del.put(val + String.valueOf(b), srule
							// .nextToken());
							suffix_del.put(val + String.valueOf(b), srule[0]);
							// string lookup position
							// suffix_look_posi.put(val + String.valueOf(b),
							// srule
							// .nextToken());
							suffix_look_posi.put(val + String.valueOf(b),
									srule[1]);
							// string lookup character
							// suffix_look_char.put(val + String.valueOf(b),
							// srule
							// .nextToken());
							suffix_look_char.put(val + String.valueOf(b),
									srule[2]);
							// string instert position
							// suffix_ins_posi.put(val + String.valueOf(b),
							// srule
							// .nextToken());
							suffix_ins_posi.put(val + String.valueOf(b),
									srule[3]);
							// inserting string
							// tmp=srule.nextToken().toString();
							// suffix_ins.put(val + String.valueOf(b), srule
							// .nextToken());
							suffix_ins.put(val + String.valueOf(b), srule[4]);
						}
					}

				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			System.out.println("error:" + e.toString());
		}
		/**
		 * Prefix into hash table
		 */
		try {

			File file = new File("tokenizerdata/prefix.txt");
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			filereader1.close();
			String prefix = new String(data, "UTF8");
			String[] part_st, st;
			st = prefix.split("\n");

			String tmp1;

			int lenst = st.length;

			// while (st.hasMoreTokens()) {
			for (int q = 0; q < lenst; q++) {
				part_st = st[q].split("\\|");
				tmp1 = part_st[0];
				// ht_suffix.put(String.valueOf(i),tmp1);
				ht_prefix.put(tmp1, tmp1);
				// vsuffix.addElement(tmp1);
				int lenpart_st = part_st.length;
				// if (part_st.hasMoreTokens()) {
				if (lenpart_st > 1) {
					prefix_tm.put(tmp1, part_st[1]);
				}
				// i++;
			}

		} catch (Exception e) {
			System.out.println("error:" + e.toString());
		}
		/**
		 * prefix rule
		 */
		try {
			String s_rule, val;
			int value;

			File file = new File("tokenizerdata/prefix_rule.txt");
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			filereader1.close();
			s_rule = new String(data, "UTF8");
			String[] st, part_st, srule;
			st = s_rule.split("\n");
			int lenst = st.length;
			// while (st.hasMoreTokens()) {
			for (int q = 0; q < lenst; q++) {
				// part_st = new StringTokenizer(st.nextToken(), " ");
				part_st = st[q].split(" ");
				try {
					int nextst = q;
					val = part_st[0].toString();
					value = (Integer.parseInt(val));

					prefix_type.put(val, part_st[1]);

					pre_sub_rule.put(val, part_st[2]);

					prefix_morph.put(val, part_st[3]);

					prefix_desc.put(val, part_st[4]);

					int s = Integer.parseInt(pre_sub_rule.get(val).toString());
					if (prefix_type.get(val).equals("PFX")) {

						for (int b = 0; b < s; b++) {
							nextst = nextst + 1;
							// srule = new StringTokenizer(st.nextToken(), " ");
							srule = st[nextst].split(" ");
							// tmp = srule.nextToken().toString();
							// prefix_del.put(val + String.valueOf(b), srule
							// .nextToken());
							prefix_del.put(val + String.valueOf(b), srule[0]);
							// tmp=srule.nextToken().toString();
							// prefix_ins.put(val + String.valueOf(b), srule
							// .nextToken());
							prefix_ins.put(val + String.valueOf(b), srule[1]);
						}
					}
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			System.out.println("error:" + e.toString());
		}

		// for dublicate suffix
		try {
			String dwsuffix;
			String[] a, b;
			File file = new File("tokenizerdata/dub_suffix.txt");
			BufferedInputStream filereader1 = new BufferedInputStream(
					new DataInputStream(new FileInputStream(file)));
			byte[] data = new byte[(int) file.length()];
			filereader1.read(data);
			filereader1.close();
			dwsuffix = new String(data, "UTF8");

			a = dwsuffix.split("\n");
			int i = a.length;
			for (int o = 0; o < i; o++) {

				b = a[o].split(" ");

				dub_suffix.put(b[0], b[0]);

				dub_suffix_morph.put(b[0], b[1]);
				dub_suffix_desc.put(b[0], b[2]);
				// javax.swing.JOptionPane.showMessageDialog(null,a[o]);
				// javax.swing.JOptionPane.showMessageDialog(null,dub_suffix_desc.get(a[o]));
			}

		} catch (Exception e) {
			System.out.println("error:" + e.toString());
		}
	}

	// is it an alternate root?
	public boolean isAltRoot(String tmp) {
		if (alt_root.containsKey(tmp)) {
			setRootPos(ht_root_pos.get(alt_root.get(tmp)).toString());
			return true;
		} else
			return false;
	}

	// get the alternate root
	public String getAltRoot(String tmp) {
		return alt_root.get(tmp).toString();
	}

	public String getSuffixExist(String e) {
		return suffix_exist.get(e).toString();
	}

	public String[] getSDes() {
		return sdes;
	}

	public String getSDesat(int i) {
		return sdes[i];
	}

	public String[] getPDes() {
		return pdes;
	}

	public String[] getSMorph() {
		return smorph;
	}

	public String getSMorphat(int i) {
		return smorph[i];
	}

	public String[] getPMorph() {
		return pmorph;
	}

	public int getSMorph_number() {
		return smorph_number;
	}

	public int getPMorph_number() {
		return pmorph_number;
	}

	public String getRoot() {
		return root;
	}

	public int getRuleNumber() {
		return rulenumber;
	}

	public String getSuffix_rule() {
		return suffix_rule;
	}

	public boolean isRoot(String str) {
		if (str.equals(ht_root.get(str))) {

			root_pos = ht_root_pos.get(str).toString();

			return true;
		}

		return false;
	}

	public String getRootPos() {
		return root_pos;
	}

	public void setRootPos(String pos) {
		root_pos = pos;
	}

	public void setSDes(String d, int i) {
		sdes[i] = d;
	}

	public void setSMorph(String mor, int i) {
		System.out.println(mor);
		smorph[i] = mor;

	}

	public void setSuffixRoot(String m) {
		isSuffixRoot = m;
	}

	public String getSuffixRoot() {
		return isSuffixRoot;
	}

	public void setSMorph_rulenum(int i, int j) {
		smorph_rulenum[i] = j;
	}

	public int getSMorph_rulenum(int i) {
		return smorph_rulenum[i];
	}

	public void setSubSMorph(int subnum, int i) {
		subsmorph[i] = subnum;
	}

	public int getSubSMorph(int i) {
		return subsmorph[i];
	}

	public void setSecParse(int i) {
		parsetwo = i;
	}

	public void setSMorph_number(int number) {
		this.smorph_number = number;
	}

	public void setPDes(String d, int i) {
		pdes[i] = d;
	}

	public void setPMorph(String mor, int i) {
		pmorph[i] = mor;

	}

	public void setPMorph_number(int number) {
		this.pmorph_number = number;
	}

	public void setRoot(String str) {
		root = str;
	}

	public void setRuleNumber(int i) {
		rulenumber = i;
	}

	public void setSuffix_rule(String suffix_rule) {
		this.suffix_rule = suffix_rule;
	}

	/*
	 * mn=morph number. This generate the word with the specific suffix
	 * rn=rulenumber
	 */
	public String generateWord(String r1, int mn) {

		q = new StringBuffer(r1);

		String t = Integer.toString(smorph_rulenum[mn]);
		String a = suffix_del.get(t + String.valueOf(subsmorph[mn])).toString();// smorph[mn];

		// suffix_ht.get(a).toString();
		// javax.swing.JOptionPane.showMessageDialog(null,"t="+t);
		String dot;
		int l = q.length();
		// javax.swing.JOptionPane.showMessageDialog(null,"l="+l);
		int b = Integer.parseInt(suf_sub_rule.get(t).toString());
		// javax.swing.JOptionPane.showMessageDialog(null,"b="+b);
		if (suffix_type.get(t).equals("SFX")) {

			for (int s = 0; s < b; s++) {
				if (subsmorph[mn] == s) {
					// previously without sub rule number record.
					// if(suffix_del.get(t+String.valueOf(s)).toString().equals(a)){
					// javax.swing.JOptionPane.showMessageDialog(null,"del="+suffix_del.get(t+String.valueOf(s)).toString());
					dot = suffix_ins.get(t + String.valueOf(s)).toString();
					if (!dot.equals(".")) {
						int length = dot.length();

						q.replace(l - length, l, "");
						q.append(a);
						// javax.swing.JOptionPane.showMessageDialog(null,q.toString()+"added"+a);
						tmpgen = q.toString();
						break;
					} else
						q.append(a);
					// javax.swing.JOptionPane.showMessageDialog(null,q.toString()+"added"+a);
				}

			}
			tmpgen = q.toString();
		} else if (suffix_type.get(t).equals("SFXX")) {
			for (int s = 0; s < b; s++) {
				if (suffix_del.get(t + String.valueOf(s)).toString().equals(a)) {

				}
				tmpgen = q.toString();
			}

		}
		return tmpgen;

	}

	public void stripSuffix(String word) {
		w = new StringBuffer(word);
		String rulenumber, tmp1, tmp2;
		int l_tmp;

		int b = Integer.parseInt(suf_sub_rule.get(
				String.valueOf(getRuleNumber())).toString());
		rulenumber = String.valueOf(getRuleNumber());

		// javax.swing.JOptionPane.showMessageDialog(null,w.toString());
		setSMorph(suffix_morph.get(rulenumber).toString(), getSMorph_number());
		setSMorph_rulenum(getSMorph_number(), Integer.parseInt(rulenumber));
		setSDes(suffix_desc.get(rulenumber).toString(), getSMorph_number());
		/**
		 * Enter other rules for other types of suffixes SFX
		 */
		if (suffix_type.get(rulenumber).equals("SFX")) {
			for (int s = 0; s < b; s++) {
				int lengthw = w.length();
				if (word.endsWith(suffix_del.get(rulenumber + String.valueOf(s)).toString())) {
					// record the subrule number generation
					setSubSMorph(s, getSMorph_number());

					tmp1 = suffix_del.get(rulenumber + String.valueOf(s))
							.toString();
					tmp2 = suffix_ins.get(rulenumber + String.valueOf(s))
							.toString();

					l_tmp = tmp1.length();

					w.replace(lengthw - l_tmp, lengthw, "");

					if (!tmp2.equals(".")) {
						w.append(tmp2);
					}
					break;
				}
			}
			setRoot(w.toString());
		}
		/**
		 * strip for the rules SFXX
		 */
		if (suffix_type.get(rulenumber).equals("SFXX")) {

			for (int s = 0; s < b; s++) {
				// for looking at different positions of the word
				String s_l_p = suffix_look_posi.get(
						rulenumber + String.valueOf(s)).toString();
				String s_l_c = suffix_look_char.get(
						rulenumber + String.valueOf(s)).toString();

				StringTokenizer look_posi = new StringTokenizer(s_l_p, "|");
				StringTokenizer look_char = new StringTokenizer(s_l_c, "|");

				String l_p[] = new String[2];
				String l_c[] = new String[2];
				int z = 0, q = 0;

				// insert into an array the position and characters to be looked
				// for
				while (look_posi.hasMoreTokens()) {
					l_p[q] = look_posi.nextToken().toString();
					l_c[q] = look_char.nextToken().toString();
					q++;

				}

				// checking whether the look up characters are present
				for (int j = 0; j < q; j++) {
					int ini, fin;
					int l = l_c[j].length();

					// for the > ending position symbol

					if (!l_p[j].equals(">")) {

						ini = Integer.parseInt(l_p[j]);
						fin = Integer.parseInt(l_p[j]) + l;

					} else {

						// make for the ending position. w.lenght has to be
						// subtracted twice
						// as for the last character we will have to -2 as shown
						// below
						ini = w.length() - l + 1;
						fin = w.length() - 1;
					}

					if (w.substring(ini, fin).equals(l_c[j])) {
						z++;
					}

				}

				// if dot present then don't have to add anything
				// if(suffix_ins_posi.get(rulenumber+String.valueOf(s)).toString().equals(".")){

				// if the characters are present then start editing
				if (q == z) {

					int lengthw = w.length();
					String delete_string = suffix_del.get(
							rulenumber + String.valueOf(s)).toString();

					// delete suffix if present
					if (delete_string != ".") {
						w.replace(lengthw - delete_string.length(), lengthw, "");
					}
					// put in array the positions to be inserted into and the
					// characters to be inserted
					String s_i_p = suffix_ins_posi.get(
							rulenumber + String.valueOf(s)).toString();
					String s_i_c = suffix_ins.get(
							rulenumber + String.valueOf(s)).toString();
					StringTokenizer ins_posi = new StringTokenizer(s_i_p, "|");
					StringTokenizer ins_char = new StringTokenizer(s_i_c, "|");
					String s_i_p_a[] = new String[2];
					String s_i_c_a[] = new String[2];

					int p = 0;

					while (ins_posi.hasMoreTokens()) {
						s_i_p_a[p] = ins_posi.nextToken();
						s_i_c_a[p] = ins_char.nextToken();
						p++;
					}

					// Insert the characters
					for (int i = 0; i < p; i++) {

						// if dot present then don't insert
						if (!s_i_p_a[i].equals(".")) {

							// Insert at the end
							if (s_i_p_a[i].equals(">")) {
								// System.out.println("insert at the end");
								w.insert(w.length(), s_i_c_a[i]);

							} else {
								// Insert at the position given

								int in = Integer.parseInt(s_i_p_a[i]);
								// System.out.println("insert at position
								// :"+in);
								w.insert(in, s_i_c_a[i]);
							}// end of else

						}
						// end of if

					}
					break;
				}
			}
		}
		setRoot(w.toString());

	}

	/*
	 * if the root consists of verbs ending with A(nep)
	 */
	public void isASuffix(String word) {
		StringBuffer w = new StringBuffer(word);
		w.append("\u094d");
		String a = w.toString();
		int c = 9998;
		// if(!isRoot(a)){
		// return false;
		// }
		int i = getSMorph_number();
		setSMorph_number(i);
		// System.out.println("print morph"+i);
		setSMorph(suffix_morph.get(String.valueOf(c)).toString(), i);
		setSDes(suffix_desc.get(String.valueOf(c)).toString(), i);
		setRoot(a);
		// return true;
	}

	// check for wheather the rule has to be skipped for smaller suffixes to be
	// handled.
	public boolean isRepeat(String rn) {
		if (suffix_ign.get(rn).toString().equals("Y"))
			return true;
		else
			return false;
	}

	public boolean suffixPresent(String str, int o) {
		String tmp;
		present = "no";

		for (int rn = 1; rn <= str.length() - 1; rn++) {
			tmp = str.substring(rn);
			if (tmp.equals(ht_suffix.get(tmp))) {

				if (parsetwo == 1) {
					if (isRepeat(suffix_ht.get(tmp).toString())) {
						for (int j = 0; j <= rec; j++) {
							if (repsuff[j].equals(tmp)) {
								present = "yes";

							}
						}
						if (present.equals("no")) {
							setRuleNumber(Integer.parseInt(suffix_ht.get(tmp)
									.toString()));
							return true;
						}

					} else {
						setRuleNumber(Integer.parseInt(suffix_ht.get(tmp)
								.toString()));

						return true;
					}

				} else {
					rec = o;
					setRuleNumber(Integer.parseInt(suffix_ht.get(tmp)
							.toString()));
					repsuff[rec] = tmp;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * methods for prefix starts
	 * 
	 * @param str
	 * @return
	 */
	public boolean prefixPresent(String str) {
		String tmp;
		for (int rn = 0; rn < str.length(); rn++) {
			tmp = str.substring(0, rn + 1);
			if (tmp.equals(ht_prefix.get(tmp))) {
				// javax.swing.JOptionPane.showMessageDialog(null,tmp);
				setRuleNumber(Integer.parseInt(prefix_tm.get(tmp).toString()));
				return true;
			}
		}
		return false;

	}

	public void stripPrefix(String word) {
		w = new StringBuffer(word);
		String rulenumber, tmp;

		int b = Integer.parseInt(pre_sub_rule.get(
				String.valueOf(getRuleNumber())).toString());
		rulenumber = String.valueOf(getRuleNumber());

		setPMorph(prefix_morph.get(rulenumber).toString(), getPMorph_number());
		setPDes(prefix_desc.get(rulenumber).toString(), getPMorph_number());
		/**
		 * Enter other rules for other types of suffixes
		 */
		if (prefix_type.get(rulenumber).equals("PFX")) {

			for (int s = 0; s < b; s++) {

				if (word.startsWith(prefix_del.get(
						rulenumber + String.valueOf(s)).toString())) {

					tmp = prefix_del.get(rulenumber + String.valueOf(s))
							.toString();

					// javax.swing.JOptionPane.showMessageDialog(null,tmp);
					w.replace(0, tmp.length(), "");
					// System.out.println(tmp.length());
					// javax.swing.JOptionPane.showMessageDialog(null,w.toString());

					break;
				}

			}

		}
		// javax.swing.JOptionPane.showMessageDialog(null,w.toString());
		setRoot(w.toString());
	}

	public void handleDublicateWord(String s) {

		int dub_len = s.length();
		int recur = dub_len / 2;
		int i = 0;

		while (i < recur) {

			if (s.substring(0, (i + 1)).equals(
					s.substring(i + 1, ((i + 1) * 2)))) {

				if ((i + 1) * 2 != s.length()) {

					String tmp_suffix = s.substring((i + 1) * 2, s.length());

					// javax.swing.JOptionPane.showMessageDialog(null,tmp_suffix+"and"+dub_suffix.get(tmp_suffix));

					if (tmp_suffix.equals(dub_suffix.get(tmp_suffix))) {

						// System.out.println("ok");
						setRoot(s.substring(0, (i + 1) * 2));
						// javax.swing.JOptionPane.showMessageDialog(null,getRoot());
						setRootPos("dublicate");
						// javax.swing.JOptionPane.showMessageDialog(null,getRootPos());
						setPMorph_number(0);
						setSMorph_number(1);

						setSMorph(dub_suffix_morph.get(tmp_suffix).toString(),
								1);
						// javax.swing.JOptionPane.showMessageDialog(null,getSMorph());
						setSDes(dub_suffix_desc.get(tmp_suffix).toString(), 1);
						// javax.swing.JOptionPane.showMessageDialog(null,getSDes());

						// /return true;
					}// else
						// return false;
				} else {
					setPMorph_number(0);
					setSMorph_number(0);
					setRoot(s);
					setRootPos("dublicate");
					// return true;
				}
			}
			i++;
		}

		// return false;

	}

	public void handleCompoundWord(String s) {
		StringBuffer r1 = new StringBuffer(s);
		String tmp;
		i = 0;
		go = 0;
		repcom = 0;
		setPMorph_number(0);
		setSMorph_number(0);
		int lengthw = r1.length();
		while (go == 0) {
			repcom++;
			go++;
			for (int l = 0; l < lengthw; l++) {
				tmp = r1.substring(l);
				if (tmp.equals(ht_suffix.get(tmp))) {
					if (repcom == 1) {
						if (isRepeat(suffix_ht.get(tmp).toString())) {
							go = 0;
							break;
						}
					}
					// w=new StringBuffer(tmp);
					i++;
					setSMorph_number(i);
					setRuleNumber(Integer.parseInt(suffix_ht.get(tmp)
							.toString()));
					String rulenumber, tmp1, tmp2;
					int l_tmp;

					int b = Integer.parseInt(suf_sub_rule.get(
							String.valueOf(getRuleNumber())).toString());
					rulenumber = String.valueOf(getRuleNumber());

					// javax.swing.JOptionPane.showMessageDialog(null,w.toString());
					setSMorph(suffix_morph.get(rulenumber).toString(),
							getSMorph_number());
					setSMorph_rulenum(getSMorph_number(),
							Integer.parseInt(rulenumber));
					setSDes(suffix_desc.get(rulenumber).toString(),
							getSMorph_number());
					/**
					 * Enter other rules for other types of suffixes SFX
					 */
					if (suffix_type.get(rulenumber).equals("SFX")) {

						for (int p = 0; p < b; p++) {

							if (tmp.equals(suffix_del.get(
									rulenumber + String.valueOf(p)).toString())) {
								// record the subrule number generation
								setSubSMorph(p, getSMorph_number());

								tmp1 = suffix_del.get(
										rulenumber + String.valueOf(p))
										.toString();
								tmp2 = suffix_ins.get(
										rulenumber + String.valueOf(p))
										.toString();

								l_tmp = tmp1.length();

								r1.replace(lengthw - l_tmp, lengthw, "");

								if (!tmp2.equals(".")) {
									r1.append(tmp2);
								}
								break;
							}
						}

						// setRoot(r1.toString());
					}
					l = -1;
					lengthw = r1.length();
					// r1=r1.replace(0,r1.length(),"");
					// r1.append(getRoot()) ;

				} else if (tmp.equals(ht_root.get(tmp))
						&& !getSuffixExist(tmp).equals("N")) {
					i++;
					setSMorph_number(i);
					setSMorph(tmp, i);
					setSDes(ht_root_pos.get(tmp).toString(), i);
					r1 = r1.replace(r1.length() - tmp.length(), r1.length(), "");
					lengthw = r1.length();
					l = -1;
				} else /*
						 * if(tmp.substring(0,tmp.length()-1).equals(ht_root.get(
						 * tmp.substring(0,tmp.length()-1)))){
						 * 
						 * r1=r1.replace(r1.length()-tmp.length(),r1.length(),"")
						 * ; lengthw=r1.length(); l=-1; }else
						 */if (tmp.equals(ht_root.get(tmp + "\u093e"))) {
					i++;
					setSMorph_number(i);
					setSMorph(tmp + "\u093e", i);
					setSDes(ht_root_pos.get(tmp + "\u093e").toString(), i);
					r1 = r1.replace(r1.length() - tmp.length(), r1.length(), "");
					lengthw = r1.length();
					l = -1;
				}/*
				 * else if(prefixPresent(tmp)){ k++; setPMorph_number(k);
				 * stripPrefix(tmp); r1=r1.replace(0,r1.length(),"");
				 * r1.append(getRoot()) ; }
				 */
			}
		}
		tmp = r1.toString();
		if (!tmp.isEmpty()) {
			if (tmp.endsWith("\u094d")
					&& isRoot(tmp.substring(0, tmp.length() - 1))) {
				setRoot(tmp.substring(0, tmp.length() - 1));
			} else {
				setRoot("unrecognized");
			}
		} else {
			setRoot(getSMorphat(i));
			setRootPos(getSDesat(i));
			setSMorph_number(i - 1);
		}
	}
	/*
	 * public static void main(String args[]){ Morph m = new Morph(); }
	 */
}
