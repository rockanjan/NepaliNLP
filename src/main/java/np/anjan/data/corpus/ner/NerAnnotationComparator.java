package np.anjan.data.corpus.ner;

import java.util.Comparator;

public class NerAnnotationComparator implements Comparator<NerAnnotation>{
	public int compare(NerAnnotation o1, NerAnnotation o2) {
		if(o1.startIndex > o2.startIndex) {
			return 1;
		} else if(o1.startIndex < o2.startIndex) {
			return -1;
		}
		return 0;
	}
}
