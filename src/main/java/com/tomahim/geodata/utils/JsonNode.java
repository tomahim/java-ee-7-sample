package com.tomahim.geodata.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonNode {
	
	private String key;
	
	private String valuePath;
	
	private List<JsonNode> nodes;
	
	public JsonNode(String key, String valuePath) {
		this.key = key;
		this.valuePath = valuePath;
	}

	public JsonNode(String key) {
		this.key = key;
		this.valuePath = null;
	}
	
	public JsonNode() {
		this.key = null;
		this.valuePath = null;
	}

	public void addNode(JsonNode node) {
		nodes.add(node);
	}
	
	private static String getNextValue(String value) {
		if(value.contains(".")) {
			String[] attributes = value.split("\\.");
			return value.substring(attributes[0].length() + 1, value.length());
		} else {
			return value;
		}
	}
	
	public JsonNode findNode(String key) {
		for(JsonNode node : nodes) {
			if(node.getKey().equals(key)) {
				return node;
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValuePath() {
		return valuePath;
	}

	public void setValuePath(String valuePath) {
		this.valuePath = valuePath;
	}
	
	public boolean isLeaf() {
		return key != null && valuePath != null && nodes.size() == 0;
	}
}