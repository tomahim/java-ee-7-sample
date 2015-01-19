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
		
	private static JsonObjectBuilder getJsonObjectBuilderFromJavaObject(Object object) {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		if(object != null && object.getClass() != null) {
			ArrayList<Method> methods = ReflectUtil.findGetters(object.getClass());
			String propertyName = null;
			for (Method method : methods) {
				Class<?> returnType = method.getReturnType();
				if(ReflectUtil.isPrimiveObject(returnType)) {
					propertyName = method.getName().substring(3, method.getName().length());
					propertyName = StringUtil.lowercaseFirstLetter(propertyName);
					try {
						jsonBuilder.add(propertyName, String.valueOf(method.invoke(object)));
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | SecurityException e) {
						e.printStackTrace();
					}
				} else if(returnType.equals(List.class)) {
					 //TODO : What should we do with arrays ? StackOverflow risk !
					/*try {
						jsonBuilder.add(propertyName, getJsonArrayBuilderFomJavaList((List<?>) method.invoke(object)));
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | SecurityException e) {
						e.printStackTrace();
					}*/
				} else {
					/*try {
						jsonBuilder.add(StringUtil.lowercaseFirstLetter(returnType.getSimpleName()), getJsonObjectBuilderFromJavaObject(method.invoke(object)));					
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | SecurityException e) {
						e.printStackTrace();
					}*/
				}
			}
		}
		return jsonBuilder;
	}
	
	private static JsonArrayBuilder getJsonArrayBuilderFomJavaList(List<?> list) {
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
	    for(Object o : list) {
	        jsonArrayBuilder.add(getJsonObjectBuilderFromJavaObject(o));
	    }
	    return jsonArrayBuilder;
	}
	
	public static JsonObject createJsonObjectFromJavaObject(Object object) {
		return getJsonObjectBuilderFromJavaObject(object).build();
	}
	
	public static JsonArray createJsonArrayFromJavaList(List<?> list) {
	    return getJsonArrayBuilderFomJavaList(list).build();
	}
}
