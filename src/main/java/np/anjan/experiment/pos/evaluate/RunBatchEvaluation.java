package np.anjan.experiment.pos.evaluate;

import java.io.IOException;

public class RunBatchEvaluation {
	public static void main(String[] args) throws IOException {
		String folder;
		String file;
		
		// folder = "/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/experiment/";
		System.out.println("BASELINE");
		System.out.println("__________________________________");
		folder = "/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/experiment/svmtoolbaselinenew/";
		file = "test.pred.withunk";
		String[] argForEvaluation = 
			{folder + file};
		Evaluate.main(argForEvaluation);
		
		System.out.println("Word+HMM");
		System.out.println("_____________________________________");
		folder = "/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/experiment/svmtoolHMM_wordhmm_combined/";
		file = "test.txt.feature.hmm.pred.withunk";
		String[] argForEvaluationHMM = 
			{folder + file};
		Evaluate.main(argForEvaluationHMM);
		
		
		
		System.out.println("50stateHMM Train only split");
		System.out.println("_____________________________________");
		folder = "/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/experiment/svmtoolHMM_wordhmm_combined/";
		file = "test.txt.feature.hmm_word.pred.withunk";
		String[] argForEvaluationHMMCombined = 
			{folder + file};
		Evaluate.main(argForEvaluationHMMCombined);
		
		
		
		
	}
}
