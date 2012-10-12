package com.everlightsz.ncoutput.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class MyLog {
	
	static Logger log=Logger.getLogger(MyLog.class);
	
	static HashMap<String, Logger> logs=new HashMap<String, Logger>();
	
	public static Logger getLogger(String name,String path){
		
		Logger oLogger=logs.get(name);
		
		if(oLogger!=null){
			return oLogger;
		}
		
		Logger myTest = Logger.getLogger(name);  
		  
		Layout layout = new PatternLayout("[%d{yyyy-MM-dd HH:mm:ss}] %m%n");  
		
		File dir=new File(path);
		if(!dir.isDirectory()){
			 if(!dir.mkdir()){
				 System.out.println("创建"+path+"失败");
				 return null;
			 }
			 else{
				 System.out.println(path+"已存在");
			 }
		}
		
		File logFile=new File(path+"\\"+name+".log");
		if(!logFile.exists()){
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				System.out.println(logFile.getPath()+"创建失败"+e.toString());
				System.out.println(e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		
		DailyRollingFileAppender  appender=null;
		try {
			appender = new DailyRollingFileAppender(layout, path+"\\"+name+".log","'.'yyyy-MM-dd");
			appender.setEncoding("utf-8");
			appender.setAppend(true);
			appender.activateOptions();   //激活选项
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}  
		  
		myTest.addAppender(appender); 
		logs.put(name, myTest);
		return myTest;
	}
	
	public static Logger getLogger(String name){
	
			return getLogger(name, loadLogRootPath());
		
	}
	
	public static String loadLogRootPath() 
	{
		//获取存放Properties文件的目录
		String path=null;
		try {
			path = MyLog.class.getResource("/").toURI().getPath();
			path = new File(path).getParent();
			path = new File(path).getParent();
		} catch (URISyntaxException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return path+"/logs";
	}

}
