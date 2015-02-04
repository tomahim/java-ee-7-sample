package com.tomahim.geodata.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.mysql.fabric.xmlrpc.base.Array;

public class JsonUtil {
	
	final static int DEFAULT_MAX_DEPTH = 1;
	
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
	
	private static void addToJsonBuilderMethod(JsonObjectBuilder jsonBuilder, Object o, Method method, int maxDepth) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String propertyName = computeAttributeNameFromMethod(method);
		if(!needRecusivity(method)) {
			jsonBuilder.add(propertyName, String.valueOf(method.invoke(o)));				
		} else if(maxDepth > 0) {
			//Avoiding insecure StackOverlow!
			if(multipleObjectsReturned(method)) {
				jsonBuilder.add(propertyName, getJsonArrayBuilderFomJavaList((List<?>) method.invoke(o), maxDepth - 1));
			} else {
				jsonBuilder.add(propertyName, getJsonObjectBuilderFromJavaObject(method.invoke(o), maxDepth - 1));					
			}				
		}
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
	
	private static JsonObjectBuilder getJsonObjectFromSpecifiedAttributes(Object object, Map<String, String> selection, int maxDepth) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		List<Method> methods = getAccessibleGettersMethods(object);
		for(Method method : methods) {
			String propertyName = computeAttributeNameFromMethod(method);
			if(selection.containsValue(propertyName)) {
				addToJsonBuilderMethod(jsonBuilder, object, method, maxDepth);
			}
		}
		return jsonBuilder;
	}
	
	private static JsonArrayBuilder getJsonArrayFromSpecifiedAttributes(List<?> list, Map<String, String> selection, int maxDepth) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	    for(Object o : list) {
	        try {
				jsonArrayBuilder.add(getJsonObjectFromSpecifiedAttributes(o, selection, maxDepth));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
	    }
	    return jsonArrayBuilder;
	}
	
	public static JsonObject createJsonObjectFromJavaObject(Object object, int maxDepth) {
		try {
			return getJsonObjectBuilderFromJavaObject(object, maxDepth).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray createJsonArrayFromJavaList(List<?> list, int maxDepth) {
	    return getJsonArrayBuilderFomJavaList(list, maxDepth).build();
	}

	public static JsonObject createJsonObjectFromJavaObject(Object object) {
		return createJsonObjectFromJavaObject(object, DEFAULT_MAX_DEPTH);
	}
	
	public static JsonArray createJsonArrayFromJavaList(List<?> list) {
	    return getJsonArrayBuilderFomJavaList(list, DEFAULT_MAX_DEPTH).build();
	}
	
	public static JsonObject toJson(Object o, Map<String, String> selection, int maxDepth) {
		try {
			return getJsonObjectFromSpecifiedAttributes(o, selection, maxDepth).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray toJsonList(List<?> list, Map<String, String> selection, int maxDepth) {
		try {
			return getJsonArrayFromSpecifiedAttributes(list, selection, maxDepth).build();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
