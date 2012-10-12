/**
 * 
 */
package com.everlightsz.ncoutput;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.everlightsz.datahub.DataHub;
import com.everlightsz.ncoutput.util.AnimatedGifEncoder;
import com.everlightsz.ncoutput.util.FileUtil;

/**
 * @author Administrator
 *
 */
public class testf {
	
	@Test
	public void t() throws Exception{	
		
		String imgDirPath="F:/temdata/ncputput/ncdata/20121012/img/14/";
		AnimatedGifEncoder e = new AnimatedGifEncoder(); 
		e.setRepeat(0); 
		e.start("d:/laoma.gif"); 
		e.setTransparent(new Color(0, 0, 0, 0));
		for(int i=0;i<24;i++){
			String imgPath=imgDirPath+i+".gif";
			File img=new File(imgPath);
			if(!img.exists()){
				System.out.println(imgPath+"不存在");
				continue;
			}
			BufferedImage src=ImageIO.read(img);
			e.setDelay(600); 
			e.setTransparent(new Color(0, 0, 0, 0));
			e.addFrame(src); 
		}
		e.finish();
	}
	
	 public static void main(String[] args) throws Exception{
		FileUtil.uploadFile("http://192.168.1.100:8080/FileUpLoad/ReceiveFile", "F:/temdata/ncputput/ncdata/20121012/img/13.zip");
	 }
}
