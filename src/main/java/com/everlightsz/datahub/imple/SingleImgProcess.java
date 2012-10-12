package com.everlightsz.datahub.imple;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.everlightsz.datahub.DataSingleProcess;
import com.everlightsz.ncoutput.netcdf.ColorRank;
import com.everlightsz.ncoutput.netcdf.ColorScale;
import com.everlightsz.ncoutput.netcdf.GridRenderer;
import com.everlightsz.ncoutput.netcdf.NetcdfToGIF;

public class SingleImgProcess implements DataSingleProcess {


	ColorScale cs = null;
    String imgPath=null;
    NetcdfToGIF netcdfOP=null;
    public SingleImgProcess(){
    	 List<ColorRank> crs=new ArrayList<ColorRank>();
        crs.add(new ColorRank(-100, 0.1, 0));
        crs.add(new ColorRank(0.1, 1,1));
        crs.add(new ColorRank(1,5,2));
        crs.add(new ColorRank(5,10,3));
        crs.add(new ColorRank(10,15,4));
        crs.add(new ColorRank(15,20,5));
        crs.add(new ColorRank(20,25,6));
        crs.add(new ColorRank(25,30,7));
        crs.add(new ColorRank(30,40,8));
        crs.add(new ColorRank(40,50,9));
        crs.add(new ColorRank(50,75,10));
        crs.add(new ColorRank(75,100,11));
        crs.add(new ColorRank(100,200,12));
        crs.add(new ColorRank(200,250,13));
        
    	Color[] colors = new Color[] { 
    			Color.black, 
    			new Color(0, 234, 235),
    			new Color(0, 160, 244), 
    			new Color(0, 0, 246),
    			new Color(0, 254, 0), 
    			new Color(0, 200, 0),
    			new Color(0, 144, 0), 
    			new Color(254, 254, 0),
    			new Color(231, 192, 0), 
    			new Color(254, 144, 0),
    			new Color(254, 0, 0), 
    			new Color(212, 0, 0),
    			new Color(192, 0, 0), 
    			new Color(254, 0, 254)
    			};
    	cs = new ColorScale("default", colors,crs);
        
    }
	
	@Override
	public Object process(Object index, Object obj) throws Exception {
		String ncCombitePath=obj.toString();
		if(this.imgPath==null){
			File ncCombiteDir=new File(ncCombitePath);
			File dayDir=ncCombiteDir.getParentFile().getParentFile();
			imgPath= dayDir.getPath()+"/img/"+ncCombiteDir.getName();
			File imgDir=new File(imgPath);
			if(!imgDir.isDirectory()){
				imgDir.mkdirs();
			}
		}
		
		if(netcdfOP==null){
			netcdfOP =new NetcdfToGIF(-103.17303486509098, -17.207625610448765,false,17.653930269818048,13.14446231397691,ncCombitePath);
		}
		BufferedImage processDiagram = netcdfOP.getGIF_height(800,Integer.parseInt(index.toString()), 0, cs, 0.1, 50, GridRenderer.contoursTyp);
		if (processDiagram == null) {
			return null;
		}
		File outfilepng = new File(imgPath+"/"+index+".gif");
		ImageIO.write(processDiagram, "gif", outfilepng);
		return null;
	}

}
