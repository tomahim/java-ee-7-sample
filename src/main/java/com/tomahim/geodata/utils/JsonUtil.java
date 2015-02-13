package com.tomahim.geodata.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
public class JsonUtil {
	
	final static int DEFAULT_MAX_DEPTH = 1;
	
	final static String DOT = ".";
	final static String DOT_SPLIT_REGEX = "\\."; 
	
	private static String getPropertyFromMethod(Method method) {
		return StringUtil.lowercaseFirstLetter(method.getName().substring(3, method.getName().length()));
	}
	
	private static List<Method> getAccessibleGettersMethods(Object o) {
		List<Method> methodsAccessible = new ArrayList<Method>();
		if(o != null && o.getClass() != null) {
			ArrayList<Method> methods = ReflectUtil.findGetters(o.getClass());
			for (Method method : methods) {
				method.setAccessible(true);
				if(method.isAccessible()) {
					methodsAccessible.add(method);
				}
			}
		}		
		return methodsAccessible;
	}
	
	private static String computeAttributeNameFromMethod(Method m) {
		Class<?> returnType = m.getReturnType();
		if(ReflectUtil.isPrimiveObject(returnType) || returnType.equals(List.class)) {
			return getPropertyFromMethod(m);
		}
		return StringUtil.lowercaseFirstLetter(returnType.getSimpleName());
	}
	
	private static boolean needRecusivity(Method m) {
		Class<?> returnType = m.getReturnType();
		return !ReflectUtil.isPrimiveObject(returnType);
	}
	
	private static boolean multipleObjectsReturned(Method m) {
		Class<?> returnType = m.getReturnType();
		return returnType.equals(List.class) || returnType.equals(Set.class);
	}
	
	private static void addToJsonBuilderMethod(JsonObjectBuilder jsonBuilder, Object o, Method method, int maxDepth, String propertyName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String property = propertyName != null ? propertyName : computeAttributeNameFromMethod(method);
		if(!needRecusivity(method)) {
			jsonBuilder.add(property, String.valueOf(method.invoke(o)));				
		} else if(maxDepth > 0) {
			//Avoiding insecure StackOverlow!
			if(multipleObjectsReturned(method)) {
				jsonBuilder.add(property, getJsonArrayBuilderFomJavaList((List<?>) method.invoke(o), maxDepth - 1));
			} else {
				jsonBuilder.add(property, getJsonObjectBuilderFromJavaObject(method.invoke(o), maxDepth - 1));					
			}				
		}
	}
	
	private static void addToJsonBuilderMethod(JsonObjectBuilder jsonBuilder, Object o, Method method, int maxDepth) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		addToJsonBuilderMethod(jsonBuilder, o, method, maxDepth, null);
	}
		
	private static JsonObjectBuilder getJsonObjectBuilderFromJavaObject(Object object, int maxDepth) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		List<Method> methods = getAccessibleGettersMethods(object);
		for (Method method : methods) {			
			addToJsonBuilderMethod(jsonBuilder, object, method, maxDepth);
		}
		return jsonBuilder;
	}
	
	private static JsonArrayBuilder getJsonArrayBuilderFomJavaList(List<?> list, int maxDepth) {
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	    for(Object o : list) {
	        try {
				jsonArrayBuilder.add(getJsonObjectBuilderFromJavaObject(o, maxDepth));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
	    }
	    return jsonArrayBuilder;
	}
	
	public static String getNextValue(String value) {
		if(value.contains(DOT)) {
			String[] attributes = value.split(DOT_SPLIT_REGEX);
			return value.substring(attributes[0].length() + DOT.length(), value.length());
		} else {
			return value;
		}
	}
	
	private static JsonArrayBuilder resolveArrayValuePath(JsonArrayBuilder jsonArrayBuilder, List<?> list, String key, String valuePath) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonArrayBuilder jsonB = (jsonArrayBuilder != null) ? jsonArrayBuilder : Json.createArrayBuilder();
	    for(Object o : list) {
	        try {
	        	jsonB.add(resolveValuePath(Json.createObjectBuilder(), o, key, valuePath));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
	    }
	    return jsonB;
	}
	
	private static JsonObjectBuilder resolveValuePath(JsonObjectBuilder jsonBuilder, Object object, String key, String valuePath) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Method> methods = getAccessibleGettersMethods(object);
		for(Method method : methods) {
			String propertyName = computeAttributeNameFromMethod(method);
			if(valuePath.contains(DOT)) {
				String[] values = valuePath.split(DOT_SPLIT_REGEX);
				if(propertyName.equals(values[0])) {
					String nextValue = getNextValue(valuePath);
					if(!multipleObjectsReturned(method)) { //Simple object to parse
						resolveValuePath(jsonBuilder, method.invoke(object), key, nextValue);
					} else {  //Collection of objects
						//TODO : make it compatible not only for List (Collection interface ?)
						jsonBuilder.add(key, resolveArrayValuePath(null, (List<?>) method.invoke(object), key, nextValue));
					}
				}
			} else {
				if(propertyName.equals(valuePath)) {
					addToJsonBuilderMethod(jsonBuilder, object, method, 0, key);					
				}				
			}			
		}
		return jsonBuilder;
	}
	
	private static JsonObjectBuilder getJsonObjectFromTree(JsonObjectBuilder jsonBuilder, Object object, JsonNode jsonNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonObjectBuilder jsonB = (jsonBuilder != null) ? jsonBuilder : Json.createObjectBuilder();
		if(jsonNode.isLeaf()) {
			//add attribute to jsonB + calculate value of valuePath
			resolveValuePath(jsonB, object, jsonNode.getKey(), jsonNode.getValuePath());
		} else {
			for(JsonNode node : jsonNode.getNodes()) {
				if(node.isLeaf()) {
					//add attribute to jsonB + calculate value of valuePath	
					resolveValuePath(jsonB, object, node.getKey(), node.getValuePath());			
				} else {
					jsonB.add(node.getKey(), getJsonObjectFromTree(null, object, node));
				}
			}
		}
		return jsonB;
	}	
	
	/* API Definition */
	
	/*
	 * Getting all fields with getter methods 
	 */
	
	public static JsonObject toJson(Object object, int maxDepth) {
		try {
			return getJsonObjectBuilderFromJavaObject(object, maxDepth).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray toJsonArray(List<?> list, int maxDepth) {
	    return getJsonArrayBuilderFomJavaList(list, maxDepth).build();
	}

	public static JsonObject toJson(Object object) {
		return toJson(object, DEFAULT_MAX_DEPTH);
	}
	
	public static JsonArray toJsonArray(List<?> list) {
	    return getJsonArrayBuilderFomJavaList(list, DEFAULT_MAX_DEPTH).build();
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
		return getJsonObjectFromTree(null, o, JsonTreeBuilder.constructTreeFromMap(rootNode, selection)).build();
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
