package com.everlightsz.ncoutput.util;

import ucar.nc2.NetcdfFileWriteable;

@SuppressWarnings("deprecation")
public class NcAttrUtil {
	
	public static void addTimeAttr(NetcdfFileWriteable nc_w,String timeName){
//		Attribute calendar=new Attribute("calendar", "noleap");
//		Attribute standard_name=new Attribute("standard_name", "time");
//		Attribute axis=new Attribute("axis", "T");
//		Attribute units=new Attribute("units", "days since 0000-1-1");
//		Attribute bounds=new Attribute("bounds", "time_bnds");
//		Attribute long_name=new Attribute("long_name", "time");
		nc_w.addVariableAttribute(timeName, "calendar", "noleap");
		nc_w.addVariableAttribute(timeName, "standard_name", "time");
		nc_w.addVariableAttribute(timeName,"axis", "T");
		nc_w.addVariableAttribute(timeName, "units", "seconds since 1970-01-01");
		nc_w.addVariableAttribute(timeName, "bounds", "time_bnds");
		nc_w.addVariableAttribute(timeName, "long_name", "time");
	}
	
	public static void addLatAttr( NetcdfFileWriteable nc_w,String latName){
//		Attribute calendar=new Attribute("calendar", "noleap");
//		Attribute standard_name=new Attribute("standard_name", "time");
//		Attribute axis=new Attribute("axis", "T");
//		Attribute units=new Attribute("units", "days since 0000-1-1");
//		Attribute bounds=new Attribute("bounds", "time_bnds");
//		Attribute long_name=new Attribute("long_name", "time");
		nc_w.addVariableAttribute(latName, "long_name", "latitude");
		nc_w.addVariableAttribute(latName, "units", "degrees_north");
		nc_w.addVariableAttribute(latName,"axis", "Y");
		nc_w.addVariableAttribute(latName, "standard_name", "latitude");
		nc_w.addVariableAttribute(latName, "bounds", "lat_bnds");
	}
	
	public static void addLonAttr(NetcdfFileWriteable nc_w,String lonName){
//		Attribute calendar=new Attribute("calendar", "noleap");
//		Attribute standard_name=new Attribute("standard_name", "time");
//		Attribute axis=new Attribute("axis", "T");
//		Attribute units=new Attribute("units", "days since 0000-1-1");
//		Attribute bounds=new Attribute("bounds", "time_bnds");
//		Attribute long_name=new Attribute("long_name", "time");
		nc_w.addVariableAttribute(lonName, "long_name", "longitude");
		nc_w.addVariableAttribute(lonName, "units", "degrees_east");
		nc_w.addVariableAttribute(lonName,"axis", "X");
		nc_w.addVariableAttribute(lonName, "standard_name", "longitude");
		nc_w.addVariableAttribute(lonName, "bounds", "lon_bnds");
	}
	
}
