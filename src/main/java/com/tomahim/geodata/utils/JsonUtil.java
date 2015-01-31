package com.tomahim.geodata.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonUtil {
	
	final static int DEFAULT_MAX_DEPTH = 1;
	
	private static String getPropertyFromMethod(Method method) {
		return StringUtil.lowercaseFirstLetter(method.getName().substring(3, method.getName().length()));
	}
		
	private static JsonObjectBuilder getJsonObjectBuilderFromJavaObject(Object object, int maxDepth) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		if(object != null && object.getClass() != null) {
			ArrayList<Method> methods = ReflectUtil.findGetters(object.getClass());
			for (Method method : methods) {
				Class<?> returnType = method.getReturnType();
				if(ReflectUtil.isPrimiveObject(returnType)) {
					jsonBuilder.add(getPropertyFromMethod(method), String.valueOf(method.invoke(object)));
				} else if(maxDepth > 0) {
					//Avoiding insecure StackOverlow!
					maxDepth = maxDepth - 1;
					if(returnType.equals(List.class)) {
						jsonBuilder.add(getPropertyFromMethod(method), getJsonArrayBuilderFomJavaList((List<?>) method.invoke(object), maxDepth));
					} else {
						jsonBuilder.add(StringUtil.lowercaseFirstLetter(returnType.getSimpleName()), getJsonObjectBuilderFromJavaObject(method.invoke(object), maxDepth));					
						
					}
				}
			}
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
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
}
