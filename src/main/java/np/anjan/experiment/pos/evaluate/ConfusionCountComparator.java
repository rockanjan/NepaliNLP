package np.anjan.experiment.pos.evaluate;

import java.util.Comparator;

public class ConfusionCountComparator implements Comparator<Confusion>{
	public int compare(Confusion o1, Confusion o2) {
		if(o1.count > o2.count) {
			return -1;
		} else if(o1.count < o2.count){
			return 1;
		}
		return 0;
	}
}
