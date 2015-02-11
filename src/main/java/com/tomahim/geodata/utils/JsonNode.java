package com.tomahim.geodata.utils;

import java.util.ArrayList;
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
		this.nodes = new ArrayList<JsonNode>();
	}

	public JsonNode(String key) {
		this.key = key;
		this.valuePath = null;
		this.nodes = new ArrayList<JsonNode>();
	}
	
	public JsonNode() {
		this.key = null;
		this.valuePath = null;
		this.nodes = new ArrayList<JsonNode>();
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
	
	public boolean isRoot() {
		return key == null && valuePath == null;
	}
	
	public boolean isLeaf() {
		return key != null && valuePath != null && nodes.size() == 0;
	}
	
	public String toString() {
		if(isLeaf()) {
			return "{" + key + " : " + valuePath + "}";
		} else {
			String nodeString = "";
			for(int i = 0; i < nodes.size(); i++) {
				nodeString += nodes.get(i);
				if(i != nodes.size()) {
					nodeString += ", ";
				}
			}
			return "{" + key + " : " + nodeString + "}";
 		}
	}
}