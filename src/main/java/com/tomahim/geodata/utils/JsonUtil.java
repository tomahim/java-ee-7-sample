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

import org.apache.commons.lang3.StringUtils;

public class JsonUtil {
	
	final static int DEFAULT_MAX_DEPTH = 1;
	
	final static String ARRAY_SYMBOL = "[].";
	final static String DOT = ".";
	final static String ARRAY_SPLIT_REGEX = "\\[].";
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
	
	private static String getNextValue(String value) {
		if(value.contains(DOT)) {
			String[] attributes = value.split(DOT_SPLIT_REGEX);
			return value.substring(attributes[0].length() + DOT.length(), value.length());
		} else {
			return value;
		}
	}
	
	private static Map<String, String> getNextSelectionMap(Map<String, String> selection, Entry<String, String> entry) {
		Map<String, String> nextSelection = new HashMap<String, String>();
		String key = entry.getKey();
		String value = entry.getValue();
		if(!key.contains(DOT)) {
			nextSelection.put(key, getNextValue(value));
		} else { 			
			nextSelection.put(getNextValue(key), getNextValue(value));
			String propertyKeys[] = key.split(DOT_SPLIT_REGEX);
			for (Map.Entry<String, String> currentEntry : selection.entrySet()) {
				if(currentEntry.getKey().contains(propertyKeys[0] + DOT)) {
					String currentPropertyKeys[] = currentEntry.getKey().split(DOT_SPLIT_REGEX); 
					if(!currentPropertyKeys[1].equals(propertyKeys[1])) {
						nextSelection.put(getNextValue(currentEntry.getKey()), getNextValue(currentEntry.getValue()));
					}
				}
			}
		}
		return nextSelection;
	}
		
	private static JsonObjectBuilder getJsonObjectFromSpecifiedAttributes(Object object, Map<String, String> selection) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return getJsonObjectFromSpecifiedAttributes(null, object, selection);
	}
	
	private static JsonObjectBuilder getJsonObjectFromSpecifiedAttributes(JsonObjectBuilder jsonB, Object object, Map<String, String> selection) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonObjectBuilder jsonBuilder = (jsonB != null) ? jsonB : Json.createObjectBuilder();
		List<Method> methods = getAccessibleGettersMethods(object);
		for(Method method : methods) {
			String propertyName = computeAttributeNameFromMethod(method);
			for(Iterator<Map.Entry<String, String>> it = selection.entrySet().iterator(); it.hasNext(); ) {
			    Map.Entry<String, String> entry = it.next();
				String currentValue = entry.getValue();
				if(StringUtils.countMatches(currentValue, DOT) > 0) {
					String[] values = currentValue.split(DOT_SPLIT_REGEX);
					if(propertyName.equals(values[0])) {
						Map<String, String> nextSelectionMap = getNextSelectionMap(selection, entry);
						String key = entry.getKey();
						if(key.contains(DOT)) {
							String propertyKeys[] = key.split(DOT_SPLIT_REGEX); 
							if(!multipleObjectsReturned(method)) { //Simple object to parse
								jsonBuilder.add(propertyKeys[0], getJsonObjectFromSpecifiedAttributes(method.invoke(object), nextSelectionMap));
							} else {  //Collection of objects
								//TODO : make it compatible not only for List
								jsonBuilder.add(propertyKeys[0], getJsonArrayFromSpecifiedAttributes((List<?>) method.invoke(object), nextSelectionMap));								
							}
						} else {
							getJsonObjectFromSpecifiedAttributes(jsonBuilder, method.invoke(object), nextSelectionMap);							
						}
					}
				} else { //Stop condition
					if(propertyName.equals(entry.getValue())) {
						addToJsonBuilderMethod(jsonBuilder, object, method, 0, entry.getKey());	
				        it.remove();
					}
				}
			}
		}
		return jsonBuilder;
	}

	//TODO : make it compatible not only for List
	private static JsonArrayBuilder getJsonArrayFromSpecifiedAttributes(List<?> list, Map<String, String> selection) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return getJsonArrayFromSpecifiedAttributes(null, list, selection);
	}
	
	private static JsonArrayBuilder getJsonArrayFromSpecifiedAttributes(JsonArrayBuilder jsonB, List<?> list, Map<String, String> selection) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonArrayBuilder jsonArrayBuilder = (jsonB != null) ? jsonB : Json.createArrayBuilder();
	    for(Object o : list) {
	        try {
	        	Map<String, String> map = new HashMap<String, String>(selection);
				jsonArrayBuilder.add(getJsonObjectFromSpecifiedAttributes(o, map));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
	    }
	    return jsonArrayBuilder;
	}
	
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
	
	public static JsonObject toJson(Object o, Map<String, String> selection) {
		try {
			return getJsonObjectFromSpecifiedAttributes(o, selection).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray toJsonArray(List<?> list, Map<String, String> selection) {
		try {
			return getJsonArrayFromSpecifiedAttributes(list, selection).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
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
			return getJsonObjectFromSpecifiedAttributes(o, map).build();
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
			return getJsonArrayFromSpecifiedAttributes(list, map).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}		
	
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
		Map<String, String> map  = transformVarargsToMap(attributes);
		try {
			return getJsonObjectFromSpecifiedAttributes(o, map).build();
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
			return getJsonArrayFromSpecifiedAttributes(list, map).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}		
}
