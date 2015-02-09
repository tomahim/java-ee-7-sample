package com.tomahim.geodata.utils;

import java.util.List;

public class JsonRootObject {

	private List<JsonNode> nodes;

	public List<JsonNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<JsonNode> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(JsonNode node) {
		this.nodes.add(node);
	}
	
}
