package np.anjan.experiment.pos.evaluate;

public class Confusion {
	//gold can be equal to pred as well
	String gold;
	String pred;
	public int count = 0;
	public Confusion(String gold, String pred) {
		if(gold == null || pred == null || gold.equals("") || pred.equals("")) {
			System.err.println("gold/pred tag cannot be null or empty");
		}
		this.gold = gold;
		this.pred = pred;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Confusion)) {
		    return false;
		  }
		Confusion other = (Confusion) o;
		return other.toString().equals(this.toString());
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public String toString() {
		return gold + "+" + pred;
	}
}
