package com.everlightsz.ncoutput.netcdf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.geoloc.ProjectionImpl;


/**
 * @author Administrator
 *
 */
public class NetcdfToGIF {
	
	GridDataset gridDataSet;
	List<GridDatatype> gridDataTypes;
	
	double offx=0;
	double offy=0;
	boolean drawBox=false;
	double dataWidth;
	double dataHeight;
	static ColorScale deflutCS=null;
	
	static {
		Color[] cs = new Color[] { 
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

		List<ColorRank> deflutCRS=new ArrayList<ColorRank>();
	    deflutCRS.add(new ColorRank(-100, 0.1, 0));
	    deflutCRS.add(new ColorRank(0.1, 1,1));
	    deflutCRS.add(new ColorRank(1,5,2));
	    deflutCRS.add(new ColorRank(5,10,3));
	    deflutCRS.add(new ColorRank(10,15,4));
	    deflutCRS.add(new ColorRank(15,20,5));
	    deflutCRS.add(new ColorRank(20,25,6));
	    deflutCRS.add(new ColorRank(25,30,7));
	    deflutCRS.add(new ColorRank(30,40,8));
	    deflutCRS.add(new ColorRank(40,50,9));
	    deflutCRS.add(new ColorRank(50,75,10));
	    deflutCRS.add(new ColorRank(75,100,11));
	    deflutCRS.add(new ColorRank(100,200,12));
	    deflutCRS.add(new ColorRank(200,250,13));
	    deflutCS=new ColorScale("", cs, deflutCRS);
	}
	
	/**
	 * 
	 * @param ofx custom Coordinate origin x
	 * @param ofy  ^^^^^^^^^
	 * @param drawbox weather draw the dataBox
	 * @param dataWidth the data width eg:(East lon-west lon) or (120-104)
	 * @param dataHeight ^^^^^^^^^^^^^^^^^^^^
	 * @param ncFilename the localtion of nc need to draw
	 * @throws IOException
	 */
	public NetcdfToGIF(double ofx,double ofy,boolean drawbox,double dataWidth,double dataHeight,String ncFilename) throws IOException{
		offx=ofx;
		offy=ofy;
		drawBox=drawbox;
		this.dataWidth=dataWidth;
		this.dataHeight=dataHeight;
		gridDataSet=getDataset(ncFilename);
		gridDataTypes=gridDataSet.getGrids();
	}
	
	public NetcdfToGIF(){
		
	}
	
	
	public void setGridDataSet(String filepath) throws IOException{
		gridDataSet=getDataset(filepath);
		gridDataTypes=gridDataSet.getGrids();
	}
	
	public int getTimeLength(int gridtype){
		return gridDataTypes.get(gridtype).getTimeDimension().getLength();
	}
	/**
	 * 
	 *lrq
	 *2012-9-11
	 *TODO
	 * @param width result image width
	 * @param height ^^^^
	 * @param dataWidth the data width eg:(East lon-west lon) or (120-104)
	 * @param dataHeight  ^^^^^^^^^^
	 * @param time the time index eg:in(13:00,15:00,19:00,21:00),the index of 15:00 is 1
	 * @param var the Variable index
	 * @param cs 
	 * @param min the value below it will be draw with the first color of colorscale
	 * @param max  ^^^^^^^
	 * @param resulttype  
	 * @return
	 */
	public BufferedImage getGIF(int width, int height,int time,int var,ColorScale cs,double min,double max,int resulttype){
		if(cs==null){
			cs=deflutCS;
		}
		GridDatatype gridType=gridDataTypes.get(var);
		GridRenderer gridrender=new GridRenderer(null,drawBox);
		ProjectionImpl dataProjection = gridType.getProjection();
		gridrender.setProjection(dataProjection);
		gridrender.setGeoGrid(gridType);
		cs.setMinMax(min, max);
		gridrender.setColorScale(cs);
		gridrender.setTime(time);
		
		BufferedImage processDiagram = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_INDEXED,createIndexColorModel());
	    Graphics2D myg = (Graphics2D) processDiagram.createGraphics();
	    AffineTransform transform = new   AffineTransform(1,0,0,-1,0,height-1);
	    myg.setTransform(transform);
	    transform.scale(width/dataWidth,height/dataHeight);
	   //((double)width)/proRect.getWidth(),((double)height)/proRect.getHeight()

	    myg.setTransform(transform);
	    myg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    myg.setStroke(new BasicStroke(0.1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,2)); 
	    myg.translate(offx,offy);
	    gridrender.renderPlanView(myg, null,resulttype);
	    
	    BufferedImageOp   op=new   AffineTransformOp(transform,AffineTransformOp.TYPE_BILINEAR); 
	    BufferedImage   filteredImage=new   BufferedImage(width,height,BufferedImage.TYPE_BYTE_INDEXED,createIndexColorModel()); 
	    op.filter(processDiagram,   filteredImage); 
	    myg.dispose();
	    return processDiagram;
	}
	
	public BufferedImage getGIF_width(int width,int time,int var,ColorScale cs,double min,double max,int resulttype){
		int height=(int) ((Integer)width*dataHeight/dataWidth);
		return getGIF(width, height,time, var, cs, min, max,resulttype);
	}
	
	public BufferedImage getGIF_height(int height,int time,int var,ColorScale cs,double min,double max,int resulttype){
		int width=(int) ((Integer)height*dataWidth/dataHeight);
		return getGIF(width, height,time, var, cs, min, max,resulttype);
	}
	
	public GridDataset getDataset(String filepath) throws IOException{
		NetcdfFile netcdffile=NetcdfFile.open(filepath);
		NetcdfDataset netcdfDataset=new NetcdfDataset(netcdffile);
		GridDataset gridsdataset=new ucar.nc2.dt.grid.GridDataset(netcdfDataset);
		return gridsdataset;
	}
	
    static IndexColorModel createIndexColorModel() {
        BufferedImage ex = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_INDEXED);
        IndexColorModel icm = (IndexColorModel) ex.getColorModel();
        int SIZE = 256;
        byte[] r = new byte[SIZE];
        byte[] g = new byte[SIZE];
        byte[] b = new byte[SIZE];
        byte[] a = new byte[SIZE];
        icm.getReds(r);
        icm.getGreens(g);
        icm.getBlues(b);
        java.util.Arrays.fill(a, (byte)255);
        r[0] = g[0] = b[0] = a[0] = 0; //transparent
        return  new IndexColorModel(8, SIZE, r, g, b, a);
    }
	
}
