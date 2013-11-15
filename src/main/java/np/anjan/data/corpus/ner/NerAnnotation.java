package np.anjan.data.corpus.ner;
//datastructure to store NER annotation
public class NerAnnotation {
	public String tagType;
	public int startIndex;
	public int endIndex;

	public NerAnnotation(String tagType, int startIndex, int endIndex) {
		this.tagType = tagType;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

}