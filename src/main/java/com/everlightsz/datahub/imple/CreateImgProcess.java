package com.everlightsz.datahub.imple;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.everlightsz.datahub.DataProcess;
import com.everlightsz.datahub.DataSingleProcess;
import com.everlightsz.ncoutput.netcdf.NetcdfToGIF;
import com.everlightsz.ncoutput.util.AnimatedGifEncoder;
import com.everlightsz.ncoutput.util.FileUtil;

public class CreateImgProcess implements DataProcess {
	
	DataSingleProcess simpleProcess;
	
	
	public DataSingleProcess getSimpleProcess() {
		return simpleProcess;
	}

	public void setSimpleProcess(DataSingleProcess simpleProcess) {
		this.simpleProcess = simpleProcess;
	}

	public Object process(Object obj) throws Exception {
		String ncCombitePath=obj.toString();
		File ncCombiteDir=new File(ncCombitePath);
		File dayDir=ncCombiteDir.getParentFile().getParentFile();
		String imgsPath= dayDir.getPath()+"/img/"+ncCombiteDir.getName();
		File imgDir=new File(imgsPath);
		if(!imgDir.isDirectory()){
			imgDir.mkdirs();
		}
		
		NetcdfToGIF netcdfOP =new NetcdfToGIF(-103.17303486509098, -17.207625610448765,false,17.653930269818048,13.14446231397691,ncCombitePath);
		
		int tl = netcdfOP.getTimeLength(0);
		for (int j = 0; j < tl; j++) {
			simpleProcess.process(j, ncCombitePath);
		}
		File[] imgFs=imgDir.listFiles();
		List<File> imgfLise=new ArrayList<File>();
		for(File imgf:imgFs){
			imgfLise.add(imgf);
		}
		ImgComparator ncComparator=(this).new ImgComparator();
		Collections.sort(imgfLise, ncComparator);
		AnimatedGifEncoder e = new AnimatedGifEncoder(); 
		e.setRepeat(0); 
		e.start(imgsPath+"/adimated.gif"); 
		e.setTransparent(new Color(0, 0, 0, 0));
		for(int i=0;i<24;i++){
			String imgPath=imgsPath+"/"+i+".gif";
			File img=new File(imgPath);
			if(!img.exists()){
				System.out.println(img.getPath()+"不存在");
				continue;
			}
			BufferedImage src=ImageIO.read(img);
			e.setDelay(600); 
			e.setTransparent(new Color(0, 0, 0, 0));
			e.addFrame(src); 
		}
		e.finish();
		FileUtil.zipFile(imgsPath, imgsPath+".zip");
		return imgsPath+".zip";
	}
	
	
	@Test
	public void ttt() throws Exception{
		CreateImgProcess c=new CreateImgProcess();
		c.setSimpleProcess(new SingleImgProcess());
		String p="F:/temdata/ncputput/ncdata/20121012/combite/14";
		c.process(p);
	}
	
	class ImgComparator implements Comparator<File>{

		@Override
		public int compare(File o1, File o2) {
			String name1=o1.getName();
			String name2=o2.getName();
			int a=0;
			try{
				a=Integer.parseInt(name1.substring(0,name1.lastIndexOf(".")-1));
			}
			catch (Exception e) {
				a=0;
			}
			int b=0;
			try{
				b=Integer.parseInt(name2.substring(0,name2.lastIndexOf(".")-1));
			}
			catch (Exception e) {
				b=0;
			}
			if(a<b){
				return -1;
			}
			else if(a==b){
				return 0;
			}
			else{
				return 1;
			}
		}
	}
}
