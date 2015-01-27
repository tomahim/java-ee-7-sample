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
		
	private static JsonObjectBuilder getJsonObjectBuilderFromJavaObject(Object object, int maxDepth) {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		if(object != null && object.getClass() != null) {
			ArrayList<Method> methods = ReflectUtil.findGetters(object.getClass());
			for (Method method : methods) {
				Class<?> returnType = method.getReturnType();
				if(ReflectUtil.isPrimiveObject(returnType)) {
					try {
						jsonBuilder.add(getPropertyFromMethod(method), String.valueOf(method.invoke(object)));
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | SecurityException e) {
						e.printStackTrace();
					}
				} else if(maxDepth > 0) {
					//Avoiding insecure StackOverlow!
					maxDepth = maxDepth - 1;
					if(returnType.equals(List.class)) {
						try {
							jsonBuilder.add(getPropertyFromMethod(method), getJsonArrayBuilderFomJavaList((List<?>) method.invoke(object), maxDepth));
						} catch (IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | SecurityException e) {
							e.printStackTrace();
						}
					} else {
						try {
							jsonBuilder.add(StringUtil.lowercaseFirstLetter(returnType.getSimpleName()), getJsonObjectBuilderFromJavaObject(method.invoke(object), maxDepth));					
						} catch (IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return jsonBuilder;
	}
	
	private static JsonArrayBuilder getJsonArrayBuilderFomJavaList(List<?> list, int maxDepth) {
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	    for(Object o : list) {
	        jsonArrayBuilder.add(getJsonObjectBuilderFromJavaObject(o, 1));
	    }
	    return jsonArrayBuilder;
	}
	
	public static JsonObject createJsonObjectFromJavaObject(Object object, int maxDepth) {
		return getJsonObjectBuilderFromJavaObject(object, maxDepth).build();
	}
	
	public static JsonArray createJsonArrayFromJavaList(List<?> list, int maxDepth) {
	    return getJsonArrayBuilderFomJavaList(list, maxDepth).build();
	}

	public static JsonObject createJsonObjectFromJavaObject(Object object) {
		return getJsonObjectBuilderFromJavaObject(object, DEFAULT_MAX_DEPTH).build();
	}
	
	public static JsonArray createJsonArrayFromJavaList(List<?> list) {
	    return getJsonArrayBuilderFomJavaList(list, DEFAULT_MAX_DEPTH).build();
	}
}
