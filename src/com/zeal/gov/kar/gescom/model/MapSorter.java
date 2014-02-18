package com.zeal.gov.kar.gescom.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapSorter {
	  public static <E extends Comparable,T extends Comparable> Map<E,T> sortByKeys(Map<E,T> map){
	        List<E> keys = new LinkedList<E>(map.keySet());
	        Collections.sort(keys);
	        Map<E,T> sortedMap = new LinkedHashMap<E,T>();
	        for(E key: keys){
	            sortedMap.put(key, map.get(key));
	        }
	      
	        return sortedMap;
	    }
}
