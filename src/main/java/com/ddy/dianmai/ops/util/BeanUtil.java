package com.ddy.dianmai.ops.util;

import java.lang.reflect.Method;

public class BeanUtil {

	public Class<?> classExists(String className){
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if(null==classLoader)classLoader = BeanUtil.class.getClassLoader();
			return classLoader.loadClass(className);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Method methodExists(Class<?> class1,String methodName){
		try {
			Method[] methods = class1.getDeclaredMethods();
			for(Method method : methods){
				if(method.getName().equals(methodName)){
					return method;
				}
			}
			return null;
		} catch (SecurityException e) {
			return null;
		}
	}
	
	public boolean parameterTypesExists(Method method){
		try {
			Class<?>[] cc =  method.getParameterTypes();
			if(0==cc.length){
				return true;
			}
			return false;
		} catch (SecurityException e) {
			return false;
		}
	}
	
	
}
