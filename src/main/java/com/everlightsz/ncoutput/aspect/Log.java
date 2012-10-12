/**
 * 
 */
package com.everlightsz.ncoutput.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.everlightsz.ncoutput.util.MyLog;
import com.everlightsz.ncoutput.util.PropertiesUnit;

/**
 * @author Administrator
 *
 */
@Aspect
public class Log {
	
    static	Logger logger=null;
    static  Logger allLogger=null;
	
	static{
		String logPath=PropertiesUnit.getValue("logPath"); 
		logger=MyLog.getLogger("nc", logPath);
		allLogger=MyLog.getLogger("ncAll", logPath);
	}
	
	@AfterThrowing(value="execution(* com.everlightsz.datahub.imple.*.*(..))",throwing="e")
	public void all(Throwable e){
		allLogger.error("出错",e);
	}
	
	@AfterThrowing(value="execution(* com.everlightsz.datahub.imple.FtpDataGetter.get(..))",throwing="e")
	public void getFileE(Throwable e){
		logger.error("获取数据出错",e);
	}
	
	@After(value="execution(* com.everlightsz.datahub.imple.SingleFtpGetter.get(..))&&args(index,obj)")
	public void downFtpFile(Object index,Object obj){
		String[] args=(String[])obj;
		logger.info("下载"+args[1]+"目录中的"+index.toString()+"到"+args[0]);
	}
	
	@AfterThrowing(value="execution(* com.everlightsz.datahub.imple.SingleFtpGetter.get(..))&&args(index,obj)",throwing="e")
	public void downFtpFileS(Object index,Object obj,Throwable e){
		logger.error("文件"+index.toString()+"下载失败"+"\t",e);
	}
	
	@Before(value="execution(* com.everlightsz.datahub.imple.CombiteNcProcess.process(..))&&args(obj)")
	public void buildNCB(Object obj){
		logger.info("正在合并"+obj+"下的nc文件");
	}
	
	@AfterThrowing(value="execution(* com.everlightsz.datahub.imple.CombiteNcProcess.process(..))&&args(obj)",throwing="e")
	public void buildNC(Object obj,Throwable e){
		logger.error("合并"+obj+"下的nc文件出错",e);
	}
	
	@Before(value="execution(* com.everlightsz.datahub.imple.CreateImgProcess.process(..))&&args(obj)")
	public void outputNcToImg(Object obj){
		logger.info("正在将"+obj+"输出成图片");
	}

	
	@Before(value="execution(* com.everlightsz.datahub.imple.SingleImgProcess.process(..))&&args(index,obj)")
	public void getGifB(Object index,Object obj){
		logger.info("时间"+index+"正在输出图片");
	}
	
	@AfterThrowing(value="execution(* com.everlightsz.datahub.imple.SingleImgProcess.process(..))&&args(index,obj)",throwing="e")
	public void getGif(Object index,Object obj,Throwable e){
		logger.error("时间"+index+"输出图片出错",e);
	}
	
	@Before(value="execution(* com.everlightsz.datahub.imple.HttpSender.send(..))&&args(obj)")
	public void senderB(Object obj){
		logger.info(obj+"正在传输中……");
	}
	
	@AfterReturning(value="execution(* com.everlightsz.datahub.imple.HttpSender.send(..))",returning="r")
	public void senderR(String r){
		logger.info("传输结果："+r);
	}
	
	@AfterThrowing(value="execution(* com.everlightsz.datahub.imple.HttpSender.send(..))&&args(obj)",throwing="e")
	public void getGif(Object obj,Throwable e){
		logger.error(obj+"传输出错",e);
	}
}
