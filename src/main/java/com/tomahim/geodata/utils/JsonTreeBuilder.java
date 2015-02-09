package com.tomahim.geodata.utils;

public class JsonTreeBuilder {

	public static void main(String[] args) {
		
		JsonRootObject country = new JsonRootObject();		
		JsonNode countryName = new JsonNode("name", "name");
		country.addNode(countryName);
	}

}
