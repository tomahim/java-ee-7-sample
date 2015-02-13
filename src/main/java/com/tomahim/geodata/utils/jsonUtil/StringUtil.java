package com.tomahim.geodata.utils.jsonUtil;

public class StringUtil {

	final static String DOT = ".";
	final static String DOT_SPLIT_REGEX = "\\.";
	
	public static String lowercaseFirstLetter(String name) {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}	
	
	public static String getNextValue(String value) {
		if(value.contains(DOT)) {
			String[] attributes = value.split(DOT_SPLIT_REGEX);
			return value.substring(attributes[0].length() + DOT.length(), value.length());
		} else {
			return value;
		}
	}
}
