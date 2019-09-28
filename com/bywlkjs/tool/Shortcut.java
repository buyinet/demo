	package com.bywlkjs.tool;

import java.lang.reflect.Method;

/**
 * 	通过反射，快捷的使用方法
 * 	由于此类不是使用与多数情况，所以不能调用有 形式参数 的方法
 */
public class Shortcut{
	
	public static Object go(Object constructor,String operation) {
		try {
			Class clazz=Class.forName(constructor.getClass().toString().substring(6));
			Method method=clazz.getMethod(operation);
			return method.invoke(constructor);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return null;
	}
	
	public static Object go(Object constructor,String operation,String type) {
		try {
			Class clazz=Class.forName(constructor.getClass().toString().substring(6));
			Method method=clazz.getMethod(operation+"_"+(type.toLowerCase()));
			return method.invoke(constructor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}	
