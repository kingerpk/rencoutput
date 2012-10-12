package com.everlightsz.ncoutput.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ShowAttr {
	
	/**
	 * ��ӡ��������
	 * @param o Ҫ��ӡ�Ķ���
	 * @param title ��ӡʱ�ı���(*^__^*) 
	 */
	public static void showAttr(Object o,String title){
		if(o==null){
			System.out.println("无值");
			return;
		}
		Field[] fields= o.getClass().getDeclaredFields();
		System.out.println("------------------------------------------"+title+"------------------------------------------");
		for(int i=0;i<fields.length;i++){
			String fieldName=fields[i].getName().substring(0, 1).toUpperCase()+fields[i].getName().substring(1);
			try {
				Method getMethod=o.getClass().getMethod("get"+fieldName, new Class[]{});
				System.out.println("<- "+fieldName+" ->"+"\t{"+getMethod.invoke(o, new Object[]{})+"}");
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				System.out.println(fieldName+"没有getter");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ӡһ������
	 * @param <E>
	 * @param os
	 * @param title
	 */
	public  static <E> void showListAttr(List<E> os,String title){
		if(os.size()<1){
			System.out.println("无值");
			return ;
		}
		if(title==null||title.trim().equals("")){
			title=os.get(0).getClass().getName();
		}
		System.out.println("***********"+title+"************");
		for(int i=0;i<os.size();i++){
			showAttr(os.get(i),"");
		}
	}
}
