package np.org.mpp.xmltag;

//import java.util.*;
//import java.io.*;
//import java.io.IOException;
import java.lang.String;

public class Tokenizer {
	static String output1;
	int[] number = new int[9999];
	int[] punctuation = new int[9999];
	int[] brackets = new int[9999];

	/** Creates a new instance of tokenizer */
	public Tokenizer() {
		for (int i = 0; i < 9999; i++) {// just flushing
			punctuation[i] = 0;
			number[i] = 0;
			brackets[i] = 0;

		}
		// now defining the punctuation marks for hashing purpose
		punctuation[2404] = 1;
		punctuation[33] = 1;
		punctuation[34] = 1;
		punctuation[35] = 1;
		punctuation[37] = 1;
		punctuation[38] = 1;
		punctuation[39] = 1;
		punctuation[44] = 1;
		punctuation[45] = 1;
		punctuation[46] = 1;
		punctuation[47] = 1;
		punctuation[9] = 1;
		punctuation[63] = 1;

		// now defining the numbers for hashing purpose
		number[2406] = 1;
		number[2407] = 1;
		number[2408] = 1;
		number[2409] = 1;
		number[2410] = 1;
		number[2411] = 1;
		number[2412] = 1;
		number[2413] = 1;
		number[2414] = 1;
		number[2415] = 1;
		// for brackets
		brackets[40] = 1;
		brackets[41] = 1;
		brackets[91] = 1;
		brackets[93] = 1;
		brackets[123] = 1;
		brackets[125] = 1;

	}

	public String getTokenizedOutput() {
		return output1;
	}

	public boolean is_alphabet(char c) {
		if (c == ' ') {
			return false;
		} else if (c == '\n') {
			return false;
		} else if ((punctuation[(int) c] == 1) || (number[(int) c] == 1)
				|| (brackets[(int) c] == 1)) {

			return false;
		} else

			return true;

	}

	public boolean is_number(char c) {
		if (number[(int) c] == 1) {

			return true;
		}

		return false;

	}

	public boolean punctuation_present(char c) {

		if (punctuation[(int) c] == 1) {

			return true;
		}

		return false;

	}

	public void tokenize(String str) {
		int end_pun_present = 0;
		StringBuffer text = new StringBuffer();
		text.append("  " + str);// initial attachments
		// System.out.println(""+(text.toString()).lastIndexOf('\u0964')+">>>"+text.length());
		if (((text.toString())).lastIndexOf('\u0964') == text.length() - 1) {
			end_pun_present = 1;
		} else if (((text.toString())).lastIndexOf('\n') == text.length() - 1) {
			end_pun_present = 1;
		}
		StringBuffer output = new StringBuffer();
		output.append("<text>\n<s>\n");

		text.append("  ");
		// Variables to test optimization
		int word_count = 0;
		int loop_count = 0;
		// now.getTime();
		// System.out.println("Loop Statred at"+now+"\n");//printing the
		// starting time

		for (int i = 1; i < text.length() - 1; i++) {
			loop_count++;

			if (i <= 1)// avoid initial attachments
			{

			} else if (i >= text.length() - 2)// avoid initial attachments
			{

			}

			else if ((!is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == '-')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (!is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				// Here we will have to put the conditions of consisting of HARU
				output.append("<token type=\"w\" morph=\"\" pos=\"\">"
						+ text.charAt(i) + text.charAt(i + 1)
						+ text.charAt(i + 2) + "</token>");
				i = i + 2;
			} else if ((is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == '-')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (!is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				output.append(text.charAt(i) + "" + text.charAt(i + 1) + ""
						+ text.charAt(i + 2) + "</token>");
				i = i + 2;
			} else if ((!is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == '-')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				output.append("<token type=\"w\" morph=\"\" pos=\"\">"
						+ text.charAt(i) + text.charAt(i + 1)
						+ text.charAt(i + 2));
				i = i + 2;
			} else if ((is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == '-')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				output.append("" + text.charAt(i) + text.charAt(i + 1)
						+ text.charAt(i + 2));
				i = i + 2;
			}

			else if ((!is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == ':')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (!is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				output.append("<token type=\"w\" morph=\"\" pos=\"\">"
						+ text.charAt(i) + text.charAt(i + 1)
						+ text.charAt(i + 2) + "</token>");
				i = i + 2;
			} else if ((is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == ':')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (!is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				output.append(text.charAt(i) + "" + text.charAt(i + 1) + ""
						+ text.charAt(i + 2) + "</token>");
				i = i + 2;
			} else if ((!is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == ':')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				output.append("<token type=\"w\" morph=\"\" pos=\"\">"
						+ text.charAt(i) + text.charAt(i + 1)
						+ text.charAt(i + 2));
				i = i + 2;
			} else if ((is_alphabet(text.charAt(i - 1)))
					&& (text.charAt(i + 1) == ':')
					&& (is_alphabet(text.charAt(i)))
					&& (is_alphabet(text.charAt(i + 2)))
					&& (is_alphabet(text.charAt(i + 3))))// handling ?-????
			{
				output.append("" + text.charAt(i) + text.charAt(i + 1)
						+ text.charAt(i + 2));
				i = i + 2;
			}

			else if ((is_alphabet(text.charAt(i)))
					&& (!is_alphabet(text.charAt(i - 1)))
					&& (!is_alphabet(text.charAt(i + 1))))// if !
			{
				output.append("<token type=\"w\" morph=\"\" pos=\"\">"
						+ text.charAt(i) + "</token>\n");
				word_count++;
			} else if ((is_alphabet(text.charAt(i)))
					&& (!is_alphabet(text.charAt(i - 1))))// if !
			{
				output.append("<token type=\"w\" morph=\"\" pos=\"\">"
						+ text.charAt(i));
			} else if ((is_alphabet(text.charAt(i)))
					&& (!is_alphabet(text.charAt(i + 1))))// if !
			{
				output.append(text.charAt(i) + "</token>\n");
				word_count++;
			}

			else if ((is_number(text.charAt(i)))
					&& (is_number(text.charAt(i + 1)))
					&& (is_number(text.charAt(i + 2)))
					&& (is_number(text.charAt(i + 3)))
					&& (text.charAt(i + 4) == '-')
					&& (is_number(text.charAt(i + 5)))
					&& (is_number(text.charAt(i + 6)))
					&& (text.charAt(i + 7) == '-')
					&& (is_number(text.charAt(i + 8)))
					&& (is_number(text.charAt(i + 9)))) {
				output.append("<token type=\"d\">" + text.charAt(i)
						+ text.charAt(i + 1) + text.charAt(i + 2)
						+ text.charAt(i + 3) + text.charAt(i + 4)
						+ text.charAt(i + 5) + text.charAt(i + 6)
						+ text.charAt(i + 7) + text.charAt(i + 8)
						+ text.charAt(i + 9) + "</token>\n");
				i = i + 9;
			}

			else if ((is_number(text.charAt(i)))
					&& (!is_number(text.charAt(i - 1)))
					&& (!is_number(text.charAt(i + 1))))// if !
			{
				output.append("<token type=\"n\">" + text.charAt(i)
						+ "</token>\n");
				word_count++;
			}

			else if ((is_number(text.charAt(i)))
					&& (!is_number(text.charAt(i - 1))))// if !
			{
				output.append("<token type=\"n\">" + text.charAt(i));
			} else if ((is_number(text.charAt(i)))
					&& (!is_number(text.charAt(i + 1))))// if !
			{
				output.append(text.charAt(i) + "</token>\n");
				word_count++;
			} else if ((text.charAt(i) == '\u0964')
					&& (text.charAt(i + 1) == '\n'))// pruna biram and \n is
													// adjancent
			{
				output.append("<token type=\"p\">\u0964</token>\n<token type=\"nl\">\n</token></s>\n<s>\n");

				i = i + 1;
			}

			else if ((text.charAt(i) == ' '))// if !
			{
				output.append("<token type=\"sp\">" + text.charAt(i)
						+ "</token>\n");
				word_count++;
			} else if (text.charAt(i) == '\n')// if !
			{
				output.append("<token type=\"nl\">\n</token>\n</s>\n<s>\n");
				word_count++;
			} else if (text.charAt(i) == '?') {
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token></s>\n<s>\n");
				// word_count++;
			} else if (text.charAt(i) == '!') {
				output.append("</s>\n<s>\n");
				// word_count++;
			} else if (text.charAt(i) == '-') {
				output.append("<token type=\"p\">-</token>");
				// word_count++;
			} else if (text.charAt(i) == '.') {
				output.append("<token type=\"p\">.</token>");
				// word_count++;
			} else if (text.charAt(i) == '(') {
				output.append("<token type=\"br\">(</token>");
				// word_count++;
			} else if (text.charAt(i) == ')') {
				output.append("<token type=\"br\">)</token>");
				// word_count++;
			} else if (text.charAt(i) == '[') {
				output.append("<token type=\"br\">[</token>");
				// word_count++;
			} else if (text.charAt(i) == ']') {
				output.append("<token type=\"br\">]</token>");
				// word_count++;
			} else if (text.charAt(i) == '{') {
				output.append("<token type=\"br\">{</token>");
				// word_count++;
			} else if (text.charAt(i) == '}') {
				output.append("<token type=\"br\">}</token>");
				// word_count++;
			} else if (text.charAt(i) == '\u0964')// if purna biram
			{
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n</s>\n<s>\n");
			} else if (text.charAt(i) == '\u0021') {
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if (text.charAt(i) == '\u0022')// if "
			{
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if (text.charAt(i) == '\u0023')// if #
			{
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if (text.charAt(i) == '\u0024')// if %
			{
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if (text.charAt(i) == '\u0026')// if &
			{
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if (text.charAt(i) == '\'')// if '
			{
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if (text.charAt(i) == '\u002A') {
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if ((int) text.charAt(i) == 44)// if ,
			{
				output.append("<token type=\"p\">" + text.charAt(i)
						+ "</token>\n");
			} else if ((int) text.charAt(i) == 9)// if tab
			{
				output.append("<token type=\"t\">" + text.charAt(i)
						+ "</token>\n");
			}

			else {
				output.append(text.charAt(i));
			}
		}// end of for
		output1 = output.toString();

		if (end_pun_present == 0) {
			output1 = output.append("</s>\n</text>").toString();
			// output1=output+"</s>";
		} else {
			output.replace(output.length() - 4, output.length(), "");
			// output1=output1.substring(0,output1.length()-4);
			output1 = output.append("</text>").toString();
		}

	}

}