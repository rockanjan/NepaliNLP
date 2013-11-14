package np.anjan.data.corpus.manualpos;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class GenerateBaselineTemplateForCRF {
	public static void main(String[] args) throws FileNotFoundException {
		String templateFile = "/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/experiment/baseline.template";
		StringBuffer content = new StringBuffer();
		int featureIndex = 0;
		//words in context
		for(int i=-3; i<=3; i++) {
			content.append(String.format("U%d:%%x[%d,0]\n", featureIndex, i));
			featureIndex++;
		}
		
		//bigrams
		for(int i=-2; i<=1; i++) {
			content.append(String.format("U%d:%%x[%d,0]/%%x[%d,0]\n", featureIndex, i, i+1));
			featureIndex++;
		}
		
		content.append("B0\n");
		//current token and tag
		//content.append("B1:%%x[0, 0]\n");
		
		System.out.println(content.toString());
		PrintWriter pw = new PrintWriter(templateFile);
		pw.println(content.toString());
		pw.close();
		
	}
}
