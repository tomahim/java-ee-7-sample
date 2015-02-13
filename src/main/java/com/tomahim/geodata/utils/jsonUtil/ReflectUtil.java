package com.tomahim.geodata.utils.jsonUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectUtil {
	
	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(BigDecimal.class);
        ret.add(Void.class);
        ret.add(String.class);
        return ret;
    }
    
    public static boolean isPrimiveObject(Class<?> c) {
    	return c.isPrimitive() || isWrapperType(c);
    }
	
	private static boolean isGetter(Method method) {
	   if (Modifier.isPublic(method.getModifiers()) &&
	      method.getParameterTypes().length == 0) {
	         if (method.getName().matches("^get[A-Z].*") &&
	            !method.getReturnType().equals(void.class)) {
	               return true;
	         }
	         if (method.getName().matches("^is[A-Z].*") &&
	            method.getReturnType().equals(boolean.class)) {
	               return true;
	         }
	   }
	   return false;
	}
	
	public static ArrayList<Method> findGetters(Class<?> c) {
	   ArrayList<Method> list = new ArrayList<Method>();
	   Method[] methods = c.getDeclaredMethods();
	   for (Method method : methods) {
	      if (isGetter(method)) {
		     list.add(method);
	      }
	   }
	   //Include also getters of super class
	   for (Method method : c.getSuperclass().getDeclaredMethods()) {
		   if (isGetter(method)) {
		     list.add(method);
	       }
	   }
	   return list;
	}
	
	public static String getPropertyFromMethod(Method method) {
		String name = method.getName();
		if(name.startsWith("get")) {
			return StringUtil.lowercaseFirstLetter(name.substring(3, method.getName().length()));
		}
		return name; 
	}
	
	public static List<Method> getAccessibleGettersMethods(Object o) {
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
}
