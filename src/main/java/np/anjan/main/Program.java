package np.anjan.main;

public class Program {
	public static void main(String[] args) {
		String[] argsTokenizer = {"/data/nepalicorpus/processed/raw/corpus_clean.txt",
				"/data/nepalicorpus/processed/raw/corpus_clean_tokenized.xml",
				"/data/nepalicorpus/processed/raw/corpus_clean_unrecognized.txt"};
		np.org.mpp.Main.main(argsTokenizer);
	}
}
