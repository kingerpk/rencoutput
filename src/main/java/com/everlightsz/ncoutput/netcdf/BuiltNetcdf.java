package com.everlightsz.ncoutput.netcdf;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.Variable;

import com.everlightsz.ncoutput.util.NcAttrUtil;

@SuppressWarnings("deprecation")
public class BuiltNetcdf {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InvalidRangeException 
	 * @throws ParseException 
	 */
	public static void built(String ncDir,String outputDir) throws IOException, InvalidRangeException, ParseException {
		// TODO Auto-generated method stub
		List<Array> arrayDataList=new ArrayList<Array>();
		List<Float> datelist=new ArrayList<Float>();
		File fdir=new File(ncDir);
		File[] fileas=fdir.listFiles();
		List<File> files=new ArrayList<File>();
		for(File f:fileas){
			files.add(f);
		}
		NcComparator ncComparator=(new BuiltNetcdf()).new NcComparator();
		Collections.sort(files, ncComparator);
		for(File f: files){
			String filepath=f.getPath();
			String fn=f.getName();
			String datestr=fn.substring(2, 12);
			String timestr=fn.substring(22, fn.length());
			int hours=Integer.parseInt(timestr)/3600;
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHH");
			Date date=format.parse(datestr);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR_OF_DAY, hours+8);
			datelist.add((float)cal.getTime().getTime());
			arrayDataList.add(getacc01_data(filepath));
		}
		
		NetcdfFileWriteable nc_w=NetcdfFileWriteable.createNew(outputDir, true);
		
		List<Dimension> dims=new ArrayList<Dimension>();
		dims.add(nc_w.addDimension("t", datelist.size()));
		dims.add(nc_w.addDimension("y", 363));
		dims.add(nc_w.addDimension("x", 423));				
		nc_w.addVariable("pr", DataType.FLOAT, dims);
		
		List<Dimension> dims_time=new ArrayList<Dimension>();
		dims_time.add(dims.get(0));
		nc_w.addVariable("t", DataType.FLOAT, dims_time);
		NcAttrUtil.addTimeAttr(nc_w, "t");		
		ArrayFloat datatime=new ArrayFloat.D1(datelist.size());
		Index timeindex=datatime.getIndex();
		Calendar c=Calendar.getInstance();
		c.set(2012, 7,21,1, 0,0);
		for(int i=0;i<datelist.size();i++){
			datatime.setFloat(timeindex.set(i),datelist.get(i)/1000);
		}
		

		List<Dimension> dims_lat=new ArrayList<Dimension>();
		dims_lat.add(dims.get(1));
		nc_w.addVariable("y", DataType.DOUBLE, dims_lat);
		NcAttrUtil.addLatAttr(nc_w, "y");		
		ArrayDouble datalat=new ArrayDouble.D1(363);
		Index lonindex=datalat.getIndex();
		for(int i=0;i<363;i++){
			datalat.setDouble(lonindex.set(i), -(181-i)*4000-2000);
		}

		List<Dimension> dims_lon=new ArrayList<Dimension>();
		dims_lon.add(dims.get(2));
		nc_w.addVariable("x",  DataType.DOUBLE, dims_lon);
		NcAttrUtil.addLonAttr(nc_w, "x");
		ArrayDouble data_lon=new ArrayDouble.D1(423);
		Index latindex=data_lon.getIndex();
		for(int i=0;i<423;i++){
			//经验证，这个式子没错
			data_lon.setDouble(latindex.set(i), -(211-i)*4000-2000);
		}
				
		nc_w.create();		
		
		for(int i=0;i<arrayDataList.size();i++){
			nc_w.write("pr",new int[]{i,0,0}, arrayDataList.get(i));
		}
		nc_w.write("t",new int[1], datatime);
		nc_w.write("x",new int[1], data_lon);
		nc_w.write("y",new int[1], datalat);
		nc_w.close();
	}
	
	public static Array getacc01_data(String filepath) throws IOException, InvalidRangeException{
		NetcdfFile nc_r=NetcdfFile.open(filepath);
		Variable v=nc_r.findVariable("acc01_");
		Array data= v.read("0:0:1, 0:362:1, 0:422:1");
		nc_r.close();
		return data;
	}
	
	/**
	 * 用于nc文件的排序
	 * @author Administrator
	 *
	 */
	class NcComparator implements Comparator<File>{

		@Override
		public int compare(File o1, File o2) {
			String name1=o1.getName();
			String name2=o2.getName();
			int a=0;
			try{
				a=Integer.parseInt(name1.substring(name1.lastIndexOf("_")+1));
			}
			catch (Exception e) {
				a=0;
			}
			int b=0;
			try{
				b=Integer.parseInt(name2.substring(name2.lastIndexOf("_")+1));
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

