/**
 * 
 */
package com.everlightsz.ncoutput.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * @author Administrator
 *
 */
public class FileUtil {
	
	 public static final int BUFFER = 1024 ;//缓存大小  
     
	    /**
	     * zip file
	     *lrq
	     *2012-9-11
	     *TODO
	     * @param filePath the dir to zip
	     * @param resultPath the result zip file name
	     * @throws Exception
	     */
	    public static void zipFile(String filePath,String resultPath) throws Exception{  
	    	File fileDir=new File(filePath);
	    	File[] fileList=fileDir.listFiles();
	    	ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(resultPath));  
	        ZipEntry ze=null;  
	        byte[] buf=new byte[BUFFER];  
	        int readLen=0;  
	        for(int i = 0; i <fileList.length; i++) {  
	            File f=(File)fileList[i];  
	            ze=new ZipEntry(f.getName());  
	            ze.setSize(f.length());  
	            ze.setTime(f.lastModified());     
	            zos.putNextEntry(ze);  
	            InputStream is=new BufferedInputStream(new FileInputStream(f));  
	            while ((readLen=is.read(buf, 0, BUFFER))!=-1) {  
	                zos.write(buf, 0, readLen);  
	            }  
	            is.close();  
	        }  
	        zos.close();  
	    }  
	    
	    public static void upZipFile(String zipfile,String dir) throws Exception{  
	        ZipFile zfile=new ZipFile(zipfile); 
	        Enumeration zList=zfile.entries();  
	        ZipEntry ze=null;  
	        byte[] buf=new byte[1024]; 
	        File temzipFile=new File(zipfile);
	        String zipName=temzipFile.getName();
	        File zipdir=new File(dir+"/"+zipName.substring(0, zipName.lastIndexOf(".")));
	        if(!zipdir.mkdir()){
	        	throw new Exception(zipdir.getPath()+"已存在");
	        }
	        while(zList.hasMoreElements()){  
	            ze=(ZipEntry)zList.nextElement();         
	            OutputStream os=new BufferedOutputStream(new FileOutputStream(zipdir.getPath()+"/"+ze.getName()));  
	            InputStream is=new BufferedInputStream(zfile.getInputStream(ze));  
	            int readLen=0;  
	            while ((readLen=is.read(buf, 0, 1024))!=-1) {  
	                os.write(buf, 0, readLen);  
	            }  
	            is.close();  
	            os.close();   
	        }  
	        zfile.close();  
	    }  
	    
	    public static String uploadFile(String targetURL,String filename) throws HttpException, IOException{
			   File targetFile = null;
			   targetFile = new File(filename);
			   PostMethod filePost = new PostMethod(targetURL);
	            Part[] parts = { new FilePart(targetFile.getName(), targetFile) };
			    filePost.setRequestEntity(new MultipartRequestEntity(parts,filePost.getParams()));
			    HttpClient client = new HttpClient();
			    client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			    int status = client.executeMethod(filePost);
			    String responText=filePost.getResponseBodyAsString();
			    filePost.releaseConnection();
			    return "http状态码："+status+"/n"+responText;
	    }
	    
	    
	
}
