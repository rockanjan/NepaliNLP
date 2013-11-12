package np.anjan.data.struct;

import java.util.Comparator;

public class TokenFreqComparator implements Comparator<TokenFreq>{
	public int compare(TokenFreq o1, TokenFreq o2) {
		if(o1.freq > o2.freq) {
			return -1;
		} else if(o1.freq < o2.freq) {
			return 1;
		}
		return 0;
	}
}
