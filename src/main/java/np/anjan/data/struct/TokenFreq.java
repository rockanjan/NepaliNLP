package np.anjan.data.struct;

public class TokenFreq {
	String word;
	int freq;
	public TokenFreq(String word, int freq) {
		this.word = word;
		this.freq = freq;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TokenFreq)) {
		    return false;
		  }
	  TokenFreq other = (TokenFreq) o;
	  return other.word.equals(this.word);
	}
	
	@Override
	public int hashCode() {
		return word.hashCode();
	}
}
