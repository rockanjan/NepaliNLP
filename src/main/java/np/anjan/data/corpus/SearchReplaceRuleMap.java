package np.anjan.data.corpus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * use as Map but the indices are important
 */
public class SearchReplaceRuleMap extends ArrayList<String>{
	Map<String, String> searchReplaceMap;
	public SearchReplaceRuleMap() {
		searchReplaceMap = new HashMap<String, String>();
	}
	
	public void put(String k, String v) {
		add(k);
		searchReplaceMap.put(k, v);
	}
	
	public String get(String k) {
		return searchReplaceMap.get(k);
	}
}
