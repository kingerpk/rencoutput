package com.everlightsz.datahub.imple;

import com.everlightsz.datahub.DataSingleGetter;
import com.everlightsz.ncoutput.util.FtpUtil;

public class SingleFtpGetter implements DataSingleGetter {

	@Override
	public Object get(Object index, Object obj) throws Exception {
		String[] args=(String[])obj;
		
		FtpUtil.downFtpFile(index.toString(),args[0],args[1]);
		return null;
	}

}
