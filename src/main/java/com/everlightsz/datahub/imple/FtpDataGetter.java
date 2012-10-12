package com.everlightsz.datahub.imple;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.everlightsz.datahub.DataGeter;
import com.everlightsz.datahub.DataSingleGetter;
import com.everlightsz.ncoutput.util.FtpUtil;
import com.everlightsz.ncoutput.util.PropertiesUnit;

public class FtpDataGetter implements DataGeter {
	
	DataSingleGetter singleGetter=null;
	
	public DataSingleGetter getSingleGetter() {
		return singleGetter;
	}

	public void setSingleGetter(DataSingleGetter singleGetter) {
		this.singleGetter = singleGetter;
	}

	public Object get() throws Exception {
		String baseDownPath=PropertiesUnit.getValue("baseDownPath");
		String ftpUrl=PropertiesUnit.getValue("ftpUrl");
		String ftpUser=PropertiesUnit.getValue("ftpUser");
		String ftpPw=PropertiesUnit.getValue("ftpPw");
		javax.swing.JOptionPane.showMessageDialog(null,ftpPw);
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String parentDayDirPath=baseDownPath+"/"+df.format(cal.getTime());
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		String childDayDirPath=parentDayDirPath+"/hours/"+(hour<10?"0"+hour:hour);
		File childDayDir=new File(childDayDirPath);
		if(!childDayDir.isDirectory()){
			childDayDir.mkdirs();
		}
		login(ftpUrl,ftpUser,ftpPw);
		Calendar cal2=Calendar.getInstance();
		cal2.setTime(cal.getTime());
		List<String> ns=getDownFileNames(cal);
		String dir=getFtpDir(cal2);
		int i=0;
		for(String n:ns){
			downFile(n,childDayDirPath, dir);
			singleGetter.get(n, new String[]{childDayDirPath,dir});
			i++;
		}
		return childDayDirPath;
	}
	
	public Boolean login(String url ,String user,String pw) throws SocketException, IOException{
		return FtpUtil.login(url, user, pw);
	}
	
	public void downFile(String fileName,String localpath,String ftpDir) throws Exception{
		FtpUtil.downFtpFile(fileName, localpath, ftpDir);
	}
	
	private List<String> getDownFileNames(Calendar cal){
		cal.add(Calendar.HOUR_OF_DAY, -8);
		String year=cal.get(Calendar.YEAR)+"";
		
		int monthInt=cal.get(Calendar.MONTH)+1;
		int dayInt=cal.get(Calendar.DAY_OF_MONTH);
		int hourInt=cal.get(Calendar.HOUR_OF_DAY);
		String month=(monthInt<10?"0"+monthInt:monthInt+"");
		String day=(dayInt<10?"0"+dayInt:dayInt+"");
		String hour=(hourInt<10?"0"+hourInt:hourInt+"");
		String name1="ar"+year+month+day+hour+".netacc01_0";

		List<String> names=new ArrayList<String>();
		for(int i=1;i<=24;i++){
			int tem=i*3600;
			String hourSecond=(String) (tem<10000?"0"+tem:tem+"");
			names.add(name1+hourSecond);
		}
		return names;
	}
	
	private String getFtpDir(Calendar cal) {
		cal.add(Calendar.HOUR_OF_DAY, -8);
		String year=cal.get(Calendar.YEAR)+"";
		int monthInt=cal.get(Calendar.MONTH)+1;
		int dayInt=cal.get(Calendar.DAY_OF_MONTH);
		int hourInt=cal.get(Calendar.HOUR_OF_DAY);
		String month=(monthInt<10?"0"+monthInt:monthInt+"");
		String day=(dayInt<10?"0"+dayInt:dayInt+"");
		String hour=(hourInt<10?"0"+hourInt+"00":hourInt+"00");
		return "/program/"+year+month+day+"/"+hour+"/data2d/";
	}
	
	
}
