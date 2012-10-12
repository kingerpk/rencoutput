package com.everlightsz.ncoutput.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * 读取Properties的工具类
 * @author Administrator
 *
 */
public class PropertiesUnit {
	/**
	 * 根据字段名读取属性
	 * @param name
	 * @return
	 */
	public static String getValue(String name)
	{
		Properties p=null;
		String value=null;
		try {
			p = loadProperties();
			value= p.getProperty(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}		
		return value;
	}
	/**
	 * 获取Properties对象
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties() throws IOException
	{
		//获取存放Properties文件的目录
		String path=null;
		
			path = PropertiesUnit.class.getProtectionDomain().getCodeSource().getLocation().getPath();//PropertiesUnit.class.getResource("/").toURI().getPath();
			File jarDir=new File(path).getParentFile();
		InputStream in = new BufferedInputStream(new FileInputStream(jarDir.getPath()+"/output.properties"));
		Properties p = new Properties();
		p.load(in);
		return p;
	}
	
	/**
	 * 获取到项目根目录下的路径，项目是evlocean的话，返回的就是 *************\evlocean\
	 * @return
	 */
	public static String getBasePath() {
		String path;
		try {
			path = new File(PropertiesUnit.class.getResource("/").toURI().getPath()).getParentFile().getParent();
			return new File(path).toURI().toString().replaceFirst("file:/", "");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 获取配置文件中的ftp地址
	 * @return
	 */
	public static String getFtpAddress(){
		return getValue("ftp");
	}
	
	/**
	 * 获取配置文件中的ftp用户名
	 * @return
	 */
	public static String getFtpUser(){
		return getValue("ftpuser");
	}
	
	/**
	 * 获取配置文件中的ftp密码
	 * @return
	 */
	public static String getFtpPw(){
		return getValue("ftppw");
	}
	
	/**
	 * 获取配置文件中的本地文件夹路径
	 * @return
	 */
	public static String getlocalPath(){
		return getValue("local");
	}
	
	/**
	 * 获取配置文件中的ftp文件夹路径
	 * @return
	 */
	public static String getFtpPath(){
		return getValue("ftpPath");
	}
	

}
