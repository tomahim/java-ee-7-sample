package com.tomahim.geodata.utils.jsonUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class JsonCompute {	
	
	final static String DOT = ".";
	final static String DOT_SPLIT_REGEX = "\\.";
	
	private static String computeAttributeNameFromMethod(Method m) {
		Class<?> returnType = m.getReturnType();
		if(ReflectUtil.isPrimiveObject(returnType) || returnType.equals(List.class)) {
			return ReflectUtil.getPropertyFromMethod(m);
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
		
	public static JsonObjectBuilder getJsonObjectBuilderFromJavaObject(Object object, int maxDepth) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		List<Method> methods = ReflectUtil.getAccessibleGettersMethods(object);
		for (Method method : methods) {			
			addToJsonBuilderMethod(jsonBuilder, object, method, maxDepth);
		}
		return jsonBuilder;
	}
	
	public static JsonArrayBuilder getJsonArrayBuilderFomJavaList(List<?> list, int maxDepth) {
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
		List<Method> methods = ReflectUtil.getAccessibleGettersMethods(object);
		for(Method method : methods) {
			String propertyName = computeAttributeNameFromMethod(method);
			if(valuePath.contains(DOT)) {
				String[] values = valuePath.split(DOT_SPLIT_REGEX);
				if(propertyName.equals(values[0])) {
					String nextValue = StringUtil.getNextValue(valuePath);
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
	
	public static JsonObjectBuilder getJsonObjectFromTree(JsonObjectBuilder jsonBuilder, Object object, JsonNode jsonNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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

}
