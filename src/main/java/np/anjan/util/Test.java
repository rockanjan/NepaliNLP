package np.anjan.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		String text = "१८. ६ प्रतिशत गैरकृषि क्षेत्र मा तथा ०. ३ प्रतिशत ४२. ५२";
		for(int i = 0; i <= 9; i++) {
			  text = text.replace(". " + (char)(0x966+i), "." + (char)(0x966+i));
		}
		System.out.println(text);
	}
}
