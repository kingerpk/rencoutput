/**
 * 
 */
package com.everlightsz.ncoutput.util;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * @author Administrator
 *
 */
public class CoordinateConvent {
	
	static CoordinateReferenceSystem cs=null;
	static MathTransform t=null;
	static {
		try {
			cs = CRS.parseWKT(
					"PROJCS[\"Asia_Lambert_Conformal_Conic\","+
					"GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\","+
					"SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],"+
					"PRIMEM[\"Greenwich\",0.0],"+
					"UNIT[\"Degree\",0.0174532925199433]],"+
					"PROJECTION[\"Lambert_Conformal_Conic\"],"+
					"PARAMETER[\"False_Easting\",0.0],"+
					"PARAMETER[\"False_Northing\",0.0],"+
					"PARAMETER[\"Central_Meridian\",112.0],"+
					"PARAMETER[\"Standard_Parallel_1\",32.0],"+
					"PARAMETER[\"Standard_Parallel_2\",20.0],"+
					"PARAMETER[\"Latitude_Of_Origin\",24.0],"+
					"UNIT[\"Meter\",1.0]]");
			t = CRS.findMathTransform(cs,DefaultGeographicCRS.WGS84,true); 
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double[] convent(double x,double y) {
		double[] result = new double[2];

		try {
			result=new double[]{0,0};  
			t.transform(new double[] {x,y}, 0, result, 0, 1);
		}
		 catch (org.opengis.referencing.operation.TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  return result;
	}
	
	public static float[] convent_float(double x,double y){
		double[] tem=convent(x, y);
		float[] result={(float)x,(float)y};
		return result;
	}
	
}
