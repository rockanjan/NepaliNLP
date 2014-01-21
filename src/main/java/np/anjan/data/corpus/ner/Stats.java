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
		String file1 = "/home/anjan/work/nepali/nerwsj/brown200/process/wsjcombined.mrg.brown";
		String file2 = "/home/anjan/work/nepali/nerwsj/brown200/process/nercombined.mrg.rem.brown";
		
		//compareExactEntityMatches(file1, file2);
		
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
		List<String> tagNotMatchWords = new ArrayList<String>();
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
					} else {
						tagNotMatchWords.add(word);
					}
				}
			}
		}
		System.out.println("Total unique words with tag in second = " + total);
		System.out.format("Such words not found in the first=%d, found=%d \n", notFound, (total - notFound));
		System.out.println("unique count of not found = " + notFoundMap.size());
		System.out.println("unique count of found = " + foundMap.size());
		System.out.println("word and tag matching without confusion = " + tagMatchWords.size());
		/*
		if(tagMatchWords.size() > 0) {
			System.out.println("List of words whose tags match in both corpus");
			for(String word : tagMatchWords) {
				System.out.println(word);
			}
		}
		*/
		System.out.println("List of words whose tags DO NOT match in both corpus");
		for(String word : tagNotMatchWords) {
			System.out.println(word);
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
			String word = splitted[2];
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
	
	public static void compareExactEntityMatches(String file1, String file2) throws IOException {
		Reader txtReader1 = new InputStreamReader(new FileInputStream(file1),"UTF-8");
		BufferedReader br1 = new BufferedReader(txtReader1);
		TreeSet<String> perList = new TreeSet<String>();
		TreeSet<String> orgList = new TreeSet<String>();
		TreeSet<String> locList = new TreeSet<String>();
		String line;
		boolean cont = false;
		String type = null;
		StringBuffer entity = null;
		int totalFound1 = 0;
		while ((line = br1.readLine()) != null) {
			line = line.trim();
			if(line.isEmpty()) {
				if(cont) {
					totalFound1++;
					if(type.equals("PER")) {
						perList.add(entity.toString());
					} else if(type.equals("ORG")) {
						orgList.add(entity.toString());
					} else if(type.equals("LOC")) {
						locList.add(entity.toString());
					}
				}
				cont = false;
				entity = null;
			}
			String[] splitted = line.split("\\s+");
			String tag = splitted[splitted.length-1];
			String word = splitted[0];
			if(tag.startsWith("B")) {
				if(cont) {
					totalFound1++;
					if(type.equals("PER")) {
						perList.add(entity.toString());
					} else if(type.equals("ORG")) {
						orgList.add(entity.toString());
					} else if(type.equals("LOC")) {
						locList.add(entity.toString());
					}
				}
				cont = true;
				entity = new StringBuffer();
				entity.append(word);
				type = tag.substring(2);
			} else if(tag.startsWith("I")) {
				entity.append(" " + word);
			} else if(tag.equals("O")) {
				if(cont) {
					totalFound1++;
					if(type.equals("PER")) {
						perList.add(entity.toString());
					} else if(type.equals("ORG")) {
						orgList.add(entity.toString());
					} else if(type.equals("LOC")) {
						locList.add(entity.toString());
					}
				}
				cont = false;
				entity = null;
			}
		}
		br1.close();
		
		Reader txtReader2 = new InputStreamReader(new FileInputStream(file2),"UTF-8");
		BufferedReader br2 = new BufferedReader(txtReader2);
		line = null ;
		cont = false;
		type = null;
		entity = null;
		int perMatchCount = 0;
		int locMatchCount = 0;
		int orgMatchCount = 0;
		int totalFound2 = 0;
		int perFound = 0;
		int locFound = 0;
		int orgFound = 0;
		while ((line = br2.readLine()) != null) {
			line = line.trim();
			if(line.isEmpty()) {
				if(cont) {
					totalFound2++;
					if(type.equals("PER")) {
						perFound++;
						if(perList.contains(entity.toString())) {
							perMatchCount++;
							System.out.println("Matched as PER : " + entity.toString());
						}
					} else if(type.equals("ORG")) {
						orgFound++;
						if(orgList.contains(entity.toString())) {
							orgMatchCount++;
							System.out.println("Matched as ORG : " + entity.toString());
						}
					} else if(type.equals("LOC")) {
						locFound++;
						if(locList.contains(entity.toString())) {
							locMatchCount++;
							System.out.println("Matched as LOC : " + entity.toString());
						}
					}
				}
				cont = false;
				entity = null;
			}
			String[] splitted = line.split("\\s+");
			String tag = splitted[splitted.length-1];
			String word = splitted[0];
			if(tag.startsWith("B")) {
				if(cont) {
					totalFound2++;
					if(type.equals("PER")) {
						perFound++;
						if(perList.contains(entity.toString())) {
							perMatchCount++;
							System.out.println("Matched as PER : " + entity.toString());
						}
					} else if(type.equals("ORG")) {
						orgFound++;
						if(orgList.contains(entity.toString())) {
							orgMatchCount++;
							System.out.println("Matched as ORG : " + entity.toString());
						}
					} else if(type.equals("LOC")) {
						locFound++;
						if(locList.contains(entity.toString())) {
							locMatchCount++;
							System.out.println("Matched as LOC : " + entity.toString());
						}
					}
				}
				cont = true;
				entity = new StringBuffer();
				entity.append(word);
				type = tag.substring(2);
			} else if(tag.startsWith("I")) {
				entity.append(" " + word);
			} else if(tag.equals("O")) {
				if(cont) {
					totalFound2++;
					if(type.equals("PER")) {
						perFound++;
						if(perList.contains(entity.toString())) {
							perMatchCount++;
							System.out.println("Matched as PER : " + entity.toString());
						}
					} else if(type.equals("ORG")) {
						orgFound++;
						if(orgList.contains(entity.toString())) {
							orgMatchCount++;
							System.out.println("Matched as ORG : " + entity.toString());
						}
					} else if(type.equals("LOC")) {
						locFound++;
						if(locList.contains(entity.toString())) {
							locMatchCount++;
							System.out.println("Matched as LOC : " + entity.toString());
						}
					}
				}
				cont = false;
				entity = null;
			}
		}
		br2.close();
		//print the stats
		System.out.println("Total entities found in 1: " + totalFound1);
		System.out.println("Total PER entities found in 1: " + perList.size());
		System.out.println("Total ORG entities found in 1: " + orgList.size());
		System.out.println("Total LOC entities found in 1: " + locList.size());
		System.out.println("Total entities found in 2: " + totalFound2);
		System.out.println("Total PER entities found in 2: " + perFound);
		System.out.println("Total ORG entities found in 2: " + orgFound);
		System.out.println("Total LOC entities found in 2: " + locFound);
		System.out.format("Exact Match counts PER=%d, ORG=%d, LOC=%d\n", perMatchCount, orgMatchCount, locMatchCount);
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
