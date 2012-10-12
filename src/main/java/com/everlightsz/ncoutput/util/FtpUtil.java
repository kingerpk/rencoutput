package com.everlightsz.ncoutput.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.junit.Test;


public class FtpUtil {
	
	static FTPClient ftp=new FTPClient();
	static Logger log=Logger.getLogger(FtpUtil.class);

	/**
	 * down all files in ftpDir
	 *lrq
	 *2012-9-12
	 *TODO
	 * @param dirName
	 * @param localPath
	 * @throws IOException
	 */
	public static void downFtpFile(String dirName,String localPath) throws IOException{
		ftp.changeToParentDirectory();
		ftp.changeWorkingDirectory(dirName);
		FTPFile[] ftpfiles=ftp.listFiles();
		File localDir=new File(localPath);
		if(!localDir.isDirectory()){
			localDir.mkdir();
		}
		for(FTPFile ftpfile:ftpfiles){
			File file=new File(localPath+"/"+ftpfile.getName());
			file.createNewFile();
			FileOutputStream fileoutput=new FileOutputStream(file);
			ftp.retrieveFile(dirName+"/"+ftpfile.getName(), fileoutput);
			fileoutput.close();
		}
	}

	/**
	 * down single ftp file
	 *lrq
	 *2012-9-12
	 *TODO
	 * @param filename
	 * @param localPath
	 * @param dirName
	 * @throws Exception
	 */
	public static void downFtpFile(String filename,String localPath,String dirName) throws Exception{
		ftp.changeToParentDirectory();
		ftp.changeWorkingDirectory(dirName);
		
		if(ftp.list(filename)<1){
			throw new Exception("there are not such file in "+dirName+"\t:"+filename);
		}
		
		File file=new File(localPath+"/"+filename);
		file.createNewFile();
		FileOutputStream fileoutput=new FileOutputStream(file);
		ftp.retrieveFile(dirName+"/"+filename, fileoutput);
		fileoutput.close();
	}
	
	/**
	 * 根据提供的文件列表和ftp路径以及本地的目标路径，下载文件
	 * @param filenames 要下载的文件列表
	 * @param downPath 本地的目标路径
	 * @param ftpPath ftp的路径
	 */
	public static void downFtpFiles(List<String> filenames,String downPath,String ftpPath){
		
		try {
			ftp.changeWorkingDirectory(ftpPath);
			for(String filename:filenames){
				if(ftp.listFiles(filename)!=null){
					File folder=new File(downPath);
					if(!folder.isDirectory())folder.mkdir();//不存在则创建
					File file=new File(downPath+filename);
					file.createNewFile();
					FileOutputStream fileOutPut=new FileOutputStream(file);
					ftp.retrieveFile(ftpPath+filename,fileOutPut);
					fileOutPut.close();
				}
				else{
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	

	
	public static void upLoadFiles(String localfilename,String uploadPath){
		try {
			int uploadCount=0;//上传文件计数器
			ftp.changeWorkingDirectory(uploadPath);//切换到指定目录
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			File parentDir=new File(localfilename);//获取本地的文件目录
			//如果本地的文件不存在或者不是目录就返回
			if(!parentDir.exists()||!parentDir.isDirectory()){
				log.error("本地目录不存在或不是目录");
				return;
			}					
			
			File[] dirs= parentDir.listFiles();			
			File dir_tem=getLastModifyFile(dirs);//获取最后修改的文件夹
			File dir=getLastModifyFile(dir_tem.listFiles());//获取最后修改的文件夹
				
			//获取该本地文件夹中，最后修改的文件夹中的文件列表
			File[] files=dir.listFiles();
			log.info("文件夹名："+dir.getPath());
			log.info("总共"+files.length+"个文件");
			
			FTPFile[] ftpFiles=ftp.listFiles();
			
//			//删除原来的所有文件
//			for(int i=ftpFiles.length-1;i>=0;i--){
//				if(ftp.deleteFile(ftpFiles[i].getName())){
//					
//				}
//			}			
			
			//遍历文件夹，上传文件
			for(int i=files.length-1;i>=0;i--){
				File file=files[i];				
				if(file.exists()){
					
					FileInputStream inputstream=new FileInputStream(file);
					Boolean uploadState=ftp.storeFile(file.getName().split("#")[3], inputstream);
					if(!uploadState){
						log.info("第"+i+"个文件："+file.getPath()+"上传结果："+uploadState);
					}
					else{
						uploadCount++;
					}
					inputstream.close();
				}
				else{
					log.error("第"+i+"个文件："+file.getPath()+"不存在");
				}				
			}
			//service();
			log.info("成功上传"+uploadCount+"个文件至"+ftp.getLocalAddress());
			
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public  void service(){
		String serviceUrl="http://10.153.123.163/InfoManageTest/ProgramUpload/ProgramUploadService.asmx?op=UpdateNotify";
		String soapActionURI="http://server-lcd/";
		try {
			Call call=(Call)(new Service()).createCall();
			call.setTargetEndpointAddress(serviceUrl);
			call.setOperationName(new QName(soapActionURI,"UpdateNotify"));
			call.addParameter(new QName(soapActionURI,"programIDs"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapActionURI,"userName"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapActionURI,"pwd"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.setUseSOAPAction(true);
            call.setSOAPActionURI(soapActionURI+"UpdateNotify");
			call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_STRING);  
			String res=(String) call.invoke("UpdateNotify",new Object[]{"1228","admin","admin@lcd"});
			System.out.println(res);
			log.info("执行http://10.153.123.163/InfoManageTest/ProgramUpload/ProgramUploadService.asmx完毕");
		} catch (ServiceException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (RemoteException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static File getLastModifyFile(File[] dirs){
		Arrays.sort(dirs, new Comparator<File>() {
			public int compare(File o1, File o2) {
				if(o1.lastModified()>o2.lastModified()){
					return 1;
				}
				else{
					return -1;
				}
			}
		});			
		
		File dir=null;//获取最后修改的文件夹
		for(int i=dirs.length-1;i>=0;i--){
			if(dirs[i].isDirectory()){
				dir=dirs[i];
				break;
			}	
		}	
		return dir;
	}
	
	public static void disconnect(){
		try {
			ftp.disconnect(); 
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String ttt(int i) throws Exception{
		if(i<2){throw new Exception("ni");}
		return "f";
	}
	
	public static Boolean login(String ftpurl,String user,String psw) throws SocketException, IOException{
		Boolean loginState=false;
			ftp.connect(ftpurl);
			loginState=ftp.login(user, psw);
			loginState=true;
		return loginState;
		
	}
	
}
