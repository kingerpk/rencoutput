/**
 * 
 */
package com.everlightsz.ncoutput.netcdf;

/**
 * @author Administrator
 *
 */
public class ColorRank {
	double min;
	double max;
	int colorIndex;
	
	public ColorRank(double min,double max,int colorindex){
		this.min=min;
		this.max=max;
		this.colorIndex=colorindex;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public int getColorIndex() {
		return colorIndex;
	}
	
	public boolean contail(double value){
		return value>min&&value<=max;
	}
	
}
