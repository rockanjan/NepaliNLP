package np.anjan.experiment.pos.evaluate;

public class NaiveBaseline {
	/*
	 * if a token appears in training, assign the most frequent tag for that token
	 * if it does not appear in the training, assign the most frequent tag (NN)
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("Usage: <program> trainFile testFile");
			System.exit(-1);
		}
		String trainFile = args[0];
		String testFile = args[1];
		
	}
}
