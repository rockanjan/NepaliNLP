package np.anjan.data.corpus.ner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/*
 * provides stats of individual files and compares two files
 */
public class Stats {
	static List<String> tagList; //index to tagname
	static Map<String, Integer> tagNameToIndex;
	public static void main(String[] args) throws IOException {
		String file1 = "/home/anjan/work/ner/nepali/baseline/wsjcombined.mrg";
		String file2 = "/home/anjan/work/ner/nepali/baseline/nercombined.mrg.200";
		
		System.out.println("NOTE: Excluding tags O");
		populateTagList(file1);
		populateTagList(file2); //appends if new tags are found
		System.out.println("Total Unique Tags = " + tagList.size());
		
		Map<String, Tags> wordToTag1 = getWordTagMap(file1); 
		Map<String, Tags> wordToTag2 = getWordTagMap(file2);
		
		
		//check the stats
		int total = wordToTag2.size();
		
		//total not found
		int notFound = 0; 
		
		//unique among the total not found
		Map<String, Integer> notFoundMap = new HashMap<String, Integer>();
		Map<String, Integer> foundMap = new HashMap<String, Integer>();
		
		List<String> tagMatchWords = new ArrayList<String>();
		for(String word : wordToTag2.keySet()) {
			if(! wordToTag1.containsKey(word)) {
				notFound++;
				if(notFoundMap.containsKey(word)) {
					notFoundMap.put(word, notFoundMap.get(word) + 1);
				} else {
					notFoundMap.put(word, 1);
				}
			} else {
				if(foundMap.containsKey(word)) {
					foundMap.put(word, foundMap.get(word) + 1);
				} else {
					foundMap.put(word, 1);
				}
				//among the found, do the tags match?
				Tags tags1 = wordToTag1.get(word);
				Tags tags2 = wordToTag2.get(word);
				boolean hasUniqueTag = tags1.hasUnique(); //does the word have unique tag in the first file?
				if(hasUniqueTag) {
					if(tags1.getUniqueIndex() == tags2.getUniqueIndex()) {
						tagMatchWords.add(word);
					}
				}
			}
		}
		System.out.println("Total unique words with tag in second = " + total);
		System.out.format("Such words not found in the first=%d, found=%d \n", notFound, (total - notFound));
		System.out.println("unique count of not found = " + notFoundMap.size());
		System.out.println("unique count of found = " + foundMap.size());
		System.out.println("word and tag matching without confusion = " + tagMatchWords.size());
		if(tagMatchWords.size() > 0) {
			System.out.println("List of words whose tags match in both corpus");
			for(String word : tagMatchWords) {
				System.out.println(word);
			}
		}
	}
	
	public static void populateTagList(String filename) throws IOException{
		TreeSet<String> tagSet = new TreeSet<String>(); //for efficient search
		if(tagList != null && tagList.size() > 0) {
			tagSet.addAll(tagList);
		}
		if(tagList == null) {
			tagList = new ArrayList<String>();
			tagNameToIndex = new HashMap<String, Integer>();
		}
		
		Reader txtReader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
		BufferedReader br = new BufferedReader(txtReader);
		String line;
		while((line = br.readLine()) != null) {
			line = line.trim();
			if(line.isEmpty()) continue;
			String[] splitted = line.split("\\s+");
			String tag = splitted[1];
			if(tag.equals("O")) continue;
			if(! tagSet.contains(tag)) { //search
				tagNameToIndex.put(tag, tagList.size());
				tagSet.add(tag);
				tagList.add(tag);
			}
		}
		br.close();
		
	}
	
	public static Map<String, Tags> getWordTagMap(String filename) throws IOException {
		Map<String, Tags> result = new HashMap<String, Tags>();
		Reader txtReader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
		BufferedReader br = new BufferedReader(txtReader);
		String line;
		while((line = br.readLine()) != null) {
			line = line.trim();
			if(line.isEmpty()) continue;
			String[] splitted = line.split("\\s+");
			String word = splitted[0];
			String tag = splitted[1];
			if(tag.equals("O")) continue;
			int tagIndex = tagNameToIndex.get(tag);
			if(result.containsKey(word)) {
				result.get(word).tags[tagIndex] = true;
			} else {
				Tags tags = new Tags(tagList.size());
				tags.tags[tagIndex] = true;
				result.put(word, tags);
			}
		}
		br.close();
		return result;
	}
	
	private static class Tags {
		public boolean[] tags;
		public Tags(int n) {
			tags = new boolean[n]; //default value is null;
		}
		
		public boolean hasUnique() {
			int count = 0;
			for(boolean b : tags) {
				if(b) count++;
			}
			if(count == 0) {
				throw new RuntimeException("No tag is initialized!");
			}
			return (count == 1) ? true : false;
		}
		
		/*
		 * Return -1 if no unique index exists
		 */
		public int getUniqueIndex() {
			int index = -1;
			if(hasUnique()) {
				for(int i=0; i<tags.length; i++) {
					if(tags[i]) {
						index = i;
						break;
					}
				}
			}
			return index;
		}
	
	
	}
}
