package com.tomahim.geodata.utils;

import java.util.HashMap;
import java.util.Map;

public class JsonTreeBuilder {

	
	final static String DOT = ".";
	final static String DOT_SPLIT_REGEX = "\\."; 


	public static void main(String[] args) {
		
		JsonNode country = new JsonNode();	
		JsonNode countryName = new JsonNode("name", "name");
		country.addNode(countryName);

		Map<String, String> selection  = new HashMap<String, String>();
		selection.put("id", "id");
		selection.put("name", "name");
		selection.put("region.id", "region.id");
		selection.put("region.name", "region.name");
		selection.put("cities.name", "cities.name");
		selection.put("cities.id", "cities.id");
		selection.put("cities.id", "cities.id2");
		selection.put("region.countries.cities.name", "cities.name2");
		JsonNode jsonTree = new JsonNode(); 

		constructTreeFromMap(jsonTree, selection);
		
		System.out.println("jsonTree " + jsonTree);
		
	}
	
	private static String getNextValue(String value) {
		if(value.contains(DOT)) {
			String[] attributes = value.split(DOT_SPLIT_REGEX);
			return value.substring(attributes[0].length() + DOT.length(), value.length());
		} else {
			return value;
		}
	}
	
	/*public static JsonNode findNode(JsonNode node, String keyPath) {
		if(keyPath.contains(".")) {
			String[] keys = keyPath.split(DOT_SPLIT_REGEX);
			JsonNode found = node.findNode(keys[0]);
			if(found == null) {
				return node;
			} else {
				return node.findNode(getNextValue(keyPath));
			}
		} else {
			return node.findNode(keyPath);
		}
	}*/
	
	private static JsonNode constructTreeFromMap(JsonNode node, Map<String, String> selection) {
		for(Map.Entry<String, String> entry : selection.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if(key.contains(DOT)) {
				String[] attributes = key.split(DOT_SPLIT_REGEX);
				JsonNode found = node.findNode(attributes[0]);
				Map<String, String> nextMap = new HashMap<String, String>();
				String nextKey = getNextValue(key);
				nextMap.put(nextKey, value);
				if(found == null) {
					JsonNode newNode = new JsonNode(attributes[0]);
					node.addNode(newNode);
					constructTreeFromMap(newNode, nextMap);
				} else {
					constructTreeFromMap(found, nextMap);					
				}
			} else {
				node.addNode(new JsonNode(key, value));
			}			
		}
		return node;
	}

}
