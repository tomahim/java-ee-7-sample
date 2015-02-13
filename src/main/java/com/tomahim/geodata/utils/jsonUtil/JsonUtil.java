package com.tomahim.geodata.utils.jsonUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/* API Definition */
public class JsonUtil {
	
	final static int DEFAULT_MAX_DEPTH = 1;
	
	/*
	 * Getting all fields with getter methods 
	 */
	
	public static JsonObject toJson(Object object, int maxDepth) {
		try {
			return JsonCompute.getJsonObjectBuilderFromJavaObject(object, maxDepth).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray toJsonArray(List<?> list, int maxDepth) {
	    return JsonCompute.getJsonArrayBuilderFomJavaList(list, maxDepth).build();
	}

	public static JsonObject toJson(Object object) {
		return toJson(object, DEFAULT_MAX_DEPTH);
	}
	
	public static JsonArray toJsonArray(List<?> list) {
	    return JsonCompute.getJsonArrayBuilderFomJavaList(list, DEFAULT_MAX_DEPTH).build();
	}
	
	/*
	 * Using map selection
	 */
	
	private static JsonArray toJsonArrayFromMap(List<?> list, Map<String, String> map) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	    for(Object o : list) {	        
			jsonArrayBuilder.add(toJsonFromMap(o, map));
	    }
	    return jsonArrayBuilder.build();
	}
	
	private static JsonObject toJsonFromMap(Object o, Map<String, String> selection) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonNode rootNode = new JsonNode();
		return JsonCompute.getJsonObjectFromTree(null, o, JsonTreeBuilder.constructTreeFromMap(rootNode, selection)).build();
	}
	
	public static JsonObject toJson(Object o, Map<String, String> selection) {
		try {
			return toJsonFromMap(o, selection);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray toJsonArray(List<?> list, Map<String, String> selection) {
		try {
		return toJsonArrayFromMap(list, selection);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	/*
	 * Using Set
	 */
	
	private static Map<String, String> transformSetToMap(Set<String> set) {
		Map<String, String> map  = new HashMap<String, String>();
		for(String el : set) {
			map.put(el, el);
		}
		return map;
	}
	
	public static JsonObject toJson(Object o, Set<String> attributes) {
		Map<String, String> map  = transformSetToMap(attributes);
		try {
			return toJsonFromMap(o, map);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray toJsonArray(List<?> list, Set<String> attributes) {
		Map<String, String> map  = transformSetToMap(attributes);
		try {
			return toJsonArrayFromMap(list, map);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}		
	
	/*
	 * Using varargs
	 */
	
	private static Map<String, String> transformVarargsToMap(String[] values) {
		if (values.length == 0) {
		   throw new IllegalArgumentException("No values supplied.");
	    }

		Map<String, String> map  = new HashMap<String, String>();
	    for (String el : values) {
	    	map.put(el, el);
	    }
	    return map;
	}
	
	public static JsonObject toJson(Object o, String... attributes) {
		Map<String, String> map = transformVarargsToMap(attributes);
		try {
			return toJsonFromMap(o, map);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static JsonArray toJsonArray(List<?> list, String... attributes) {
		Map<String, String> map  = transformVarargsToMap(attributes);
		try {
			return toJsonArrayFromMap(list, map);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}		
}
