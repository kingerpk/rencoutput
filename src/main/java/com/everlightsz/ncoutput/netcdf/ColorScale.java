/*
 * Copyright 1998-2009 University Corporation for Atmospheric Research/Unidata
 *
 * Portions of this software were developed by the Unidata Program at the
 * University Corporation for Atmospheric Research.
 *
 * Access and use of this software shall impose the following obligations
 * and understandings on the user. The user is granted the right, without
 * any fee or cost, to use, copy, modify, alter, enhance and distribute
 * this software, and any derivative works thereof, and its supporting
 * documentation for any purpose whatsoever, provided that this entire
 * notice appears in all copies of the software, derivative works and
 * supporting documentation.  Further, UCAR requests that the user credit
 * UCAR/Unidata in any publications that result from the use of this
 * software or in any product that includes this software. The names UCAR
 * and/or Unidata, however, may not be used in any advertising or publicity
 * to endorse or promote any products or commercial entity unless specific
 * written permission is obtained from UCAR/Unidata. The user also
 * understands that UCAR/Unidata is not obligated to provide the user with
 * any support, consulting, training or assistance of any kind with regard
 * to the use, operation and performance of this software nor to provide
 * the user with any updates, revisions, new versions or "bug fixes."
 *
 * THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 * INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 * WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 * getIndexFromValue
 */
package com.everlightsz.ncoutput.netcdf;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import ucar.nc2.dt.GridDatatype;
import ucar.nc2.util.ListenerManager;

/**
 * A ColorScale is used for false-color data mapping. It contains an array of java.awt.Color,
 * along with an assignment of a data interval to each Color.
 * <p/>
 * ColorScale.Panel handles the displaying of a ColorScale. It also works with ColorScaleManager
 * to allow editing and defining new ColorScales.
 *
 * @author caron
 */

public class ColorScale implements Cloneable, java.io.Serializable {
  public static final int VERTICAL = 0;
  public static final int HORIZONTAL = 1;

  private static final long serialVersionUID = -1L;       // disconnect java version checking
  private static final int objectVersion = 1;             // our version control
  private static final boolean debugColors = false;
  private static int sigfig = 4;

  // this is all that needs to be serialized
  private String name;
  private int ncolors;
  private Color[] colors;
  static Color bColor=new Color(0, 0, 0, 0);

  // reset after deserializing
  private Color[] useColors;
  private ListenerManager lm;

  // this is set for each grid
  private GridDatatype gg;
  private double[] edge;
  private int[] hist;
  private double min, max, interval;
  private boolean hasMissingData = false;
  private Color missingDataColor = Color.white;


  static final public Color[] redBlue = {
	  	  bColor,
          new java.awt.Color(1, 57, 255),
          new java.awt.Color(0, 140, 255),
          new java.awt.Color(1, 209, 255),
          new java.awt.Color(1, 255, 232),
          new java.awt.Color(1, 255, 171),
          new java.awt.Color(1, 255, 79),
          new java.awt.Color(43, 255, 0),
          new java.awt.Color(166, 255, 2),
          new java.awt.Color(227, 255, 1),
          new java.awt.Color(255, 198, 0),
          new java.awt.Color(255, 168, 1),
          new java.awt.Color(255, 145, 1),
          new java.awt.Color(255, 130, 1),
          new java.awt.Color(255, 107, 0),
          new java.awt.Color(255, 84, 0),
          new java.awt.Color(255,255, 255)          
  };


  static final private Color[] defaultColors = redBlue;
  List<ColorRank> crs=null;
  /* Constructor.
   * @param name of this colorscale.
   * @param c array of colors.
   */
  public ColorScale(String name, Color [] c,List<ColorRank> crs) {
    this.name = new String(name);
    this.ncolors = c.length;
    this.crs=crs;
    colors = new Color[ ncolors];
    for (int i = 0; i < ncolors; i++)
      colors[i] = c[i];

    constructTransient();
  }

  // rest of stuff for construction/deserialization
  private void constructTransient() {
    useColors = colors;

    edge = new double[ ncolors];
    hist = new int[ ncolors + 1];
    lm = new ListenerManager(
            "java.beans.PropertyChangeListener",
            "java.beans.PropertyChangeEvent",
            "propertyChange");
    missingDataColor = Color.white;
  }

  /**
   * add action event listener
   */
  public void addPropertyChangeListener(PropertyChangeListener l) {
    lm.addListener(l);
  }

  /**
   * remove action event listener
   */
  public void removePropertyChangeListener(PropertyChangeListener l) {
    lm.removeListener(l);
  }

  /**
   * Get the colorscale name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the colorscale name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the number of colors in the colorscale.
   */
  public int getNumColors() {
    return ncolors;
  }

  /**
   * Set the number of colors in the colorscale.
   */
  public void setNumColors(int n) {
    if (n != ncolors) {
      colors = new Color[n];
      int prevn = Math.min(ncolors, n);
      for (int i = 0; i < prevn; i++)
        colors[i] = useColors[i];
      for (int i = ncolors; i < n; i++)
        colors[i] = Color.white;

      useColors = colors;
      ncolors = n;
      edge = new double[ ncolors];
      hist = new int[ ncolors + 1];
    }
  }

  /* Get the color at the given index, or the missing data color.
   * @param i index into color array, or ncolors to get missingDataColor.
   * @exception IllegalArgumentException if (i<0) || (i>ncolors)
   */
  public Color getColor(int i) {
    if (i >= 0 && i < ncolors)
      return useColors[i];
    else if (i == ncolors && hasMissingData)
      return missingDataColor;
    else
      throw new IllegalArgumentException("Color Scale getColor " + i);
  }

  /**
   * Get the edge of the ith interval. see @link setMinMax
   */
  public double getEdge(int index) {
    return edge[index];
  }

  /* Set whether there is missing data, what it is, and what color to use.
   * @param i: index into color array, or ncolors for missingDataColor.
   * @exception IllegalArgumentException if (i<0) || (i>ncolors)
   *
  public void setMissingDataColor( Color missingDataColor) {
    this.missingDataColor = missingDataColor;
  } */

  public void setGeoGrid(GridDatatype gg) {
    this.gg = gg;
    hasMissingData = gg.hasMissingData();
  }

  public Color getMissingDataColor() {
    return missingDataColor;
  }

  
  public int getIndexFromValue(double value) {
	    int index=0;
	   
	    for(ColorRank cr:crs){
	    	if(cr.contail(value)){
	    		index=cr.getColorIndex();
	    		break;
	    	}
	    }

	    hist[index]++;
	    return index;
	  }

  /**
   * Set the data min/max interval. The color intervals are set based on this.
   * A PropertyChangeEvent is sent when this is called. Currently the intervals are
   * calculated in the following way (where incr = (max-min)/(n-2)) :
   * <pre>
   * <p/>
   *      edge           data interval
   *  0    min             value <= min
   *  1    min+incr        min <= value < min + incr
   *  2    min+2*incr      min+incr <= value < min+2*incr
   *  ith  min+i*incr      min+(i-1)*incr <= value < min+i*incr
   *  n-2  max             min+(n-3)*incr <= value < max
   *  n-1  max             max < value
   *  n                    value = missingDataValue
   * </pre>
   *
   * @param min: minimum data value
   * @param max: maximum data value
   */
  public void setMinMax(double min, double max) {
    this.min = min;
    this.max = max;
    interval = (max - min) / (ncolors - 2);

    //这里注意了，颜色设置的时候是这样的，0~0.1一个颜色，0.1~1一个颜色……最后200~无穷大一个颜色，一共对应14个颜色
    for(int i=0;i<crs.size();i++){
    	edge[i]=crs.get(i).getMin();
    }
    lm.sendEvent(new PropertyChangeEvent(this, "ColorScaleLimits", null, this));
  }


  /**
   * This is an optimization for counting the number of colors in each interval.
   * the histpogram is populated by calls to getIndexFromValue().
   *
   * @return the index with the maximum histogram count.
   */
  public int getHistMax() {
    int max = 0, maxi = 0;
    for (int i = 0; i <= ncolors; i++)
      if (hist[i] > max) {
        max = hist[i];
        maxi = i;
      }
    return maxi;
  }

  /**
   * reset the histogram.
   */
  public void resetHist() {
    for (int i = 0; i <= ncolors; i++)
      hist[i] = 0;
  }

  public String toString() {
    return name;
  }

  // serialization
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    int readVer = s.readInt();
    this.name = s.readUTF();
    this.colors = (Color[]) s.readObject();
    this.ncolors = colors.length;
    constructTransient();
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
    s.writeInt(objectVersion);
    s.writeUTF(this.name);
    s.writeObject(this.colors);
  }
}