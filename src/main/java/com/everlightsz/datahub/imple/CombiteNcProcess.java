package com.everlightsz.datahub.imple;

import java.io.File;

import com.everlightsz.datahub.DataProcess;
import com.everlightsz.ncoutput.netcdf.BuiltNetcdf;

public class CombiteNcProcess implements DataProcess {

	@Override
	public Object process(Object obj) throws Exception {
		String ncsPath=obj.toString();
		File ncsDir=new File(ncsPath);
		File dayDir=ncsDir.getParentFile().getParentFile();
		String combitePath= dayDir.getPath()+"/combite/";
		File combiteDir=new File(combitePath);
		if(!combiteDir.isDirectory()){
			combiteDir.mkdirs();
		}
		BuiltNetcdf.built(ncsPath,combitePath+"/"+ncsDir.getName());
		return combitePath+"/"+ncsDir.getName();
	}
	
	
}
