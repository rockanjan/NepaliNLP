package np.anjan.data.corpus.ner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//take .ann file from "Brat" software and merge with corresponding .txt file
public class MergeTextWithAnnotation {
	public static Map<String, String> annotationMap = new HashMap<String, String>();
	static {
		annotationMap.put("Person", "PER");
		annotationMap.put("GPE", "LOC");
		annotationMap.put("Organization", "ORG");
	}

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		String commonFilePrefix = "/var/www/brat/data/nepali_ner/test/xaa";
		String annFile = commonFilePrefix + ".ann";
		String txtFile = commonFilePrefix + ".txt";
		String outFile = txtFile + ".mrg";
		List<NerAnnotation> annotationList = getAnnotationList(annFile);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "UTF-8"));

		Reader txtReader = new InputStreamReader(new FileInputStream(txtFile),
				"UTF-8");
		Reader br = new BufferedReader(txtReader);
		int r;

		NerAnnotation currentAnnotation = null;
		boolean beginning = false; // if the active annotation is beginning or
									// inside (continuing)
		int index = -1;
		while ((r = br.read()) != -1) {
			index++;
			if(currentAnnotation == null && annotationList.size() > 0) {
				if(annotationList.get(0).startIndex == index) {
					currentAnnotation = annotationList.remove(0);
					beginning = true;
				}
			}
			char ch = (char) r;
			if (ch != '\n') {
				pw.print(ch);
			} else {
				pw.print(" ");
			}
			if (ch == ' ' || ch == '\n') {
				if(currentAnnotation != null) {
					if (beginning) {
						pw.println("B-"
								+ annotationMap.get(currentAnnotation.tagType));
					} else {
						pw.println("I-"
								+ annotationMap.get(currentAnnotation.tagType));
					}
					beginning = false;
				} else {
					pw.println("O");
				}
			}
			if(currentAnnotation != null && currentAnnotation.endIndex == index) {
				currentAnnotation = null;
			}
			if (ch == '\n') {
				pw.println();
			}
		}
		br.close();
		pw.close();
	}

	public static List<NerAnnotation> getAnnotationList(String filename)
			throws NumberFormatException, IOException {
		List<NerAnnotation> annotationList = new ArrayList<NerAnnotation>();
		Reader in = new InputStreamReader(new FileInputStream(filename),
				"UTF-8");
		BufferedReader br = new BufferedReader(in);
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				String[] splitted = line.split("\\s+");
				if (splitted.length < 5) {
					System.err.println("#cols in annotation < 5 in line : "
							+ line);
					System.exit(-1);
				}
				String tagType = splitted[1];
				if (!annotationMap.containsKey(tagType)) {
					System.err.println("No mapping found for annotation : "
							+ tagType);
					System.err.println("Line : " + line);
					System.exit(-1);
				}

				int startIndex = Integer.parseInt(splitted[2]);
				int endIndex = Integer.parseInt(splitted[3]);
				NerAnnotation n = new NerAnnotation(tagType, startIndex,
						endIndex);
				annotationList.add(n);
			}
		}
		System.out.println("Number of Annotations : " + annotationList.size());
		// indices might not be in order, sort them by startIndex
		Collections.sort(annotationList, new NerAnnotationComparator());
		br.close();
		return annotationList;
	}
}
