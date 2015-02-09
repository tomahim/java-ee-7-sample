package com.tomahim.geodata.utils;

import java.util.List;

public class JsonNode {
	
	private String key;
	
	private String valuePath;
	
	private List<JsonNode> attributes;
	
	public JsonNode(String key, String valuePath) {
		this.key = key;
		this.valuePath = valuePath;
	}
	
	public void addNode(JsonNode node) {
		attributes.add(node);
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
		return key != null && valuePath != null && attributes.size() == 0;
	}
}