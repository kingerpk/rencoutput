package com.everlightsz.datahub.imple;

import com.everlightsz.datahub.DataSend;
import com.everlightsz.ncoutput.util.FileUtil;
import com.everlightsz.ncoutput.util.PropertiesUnit;

public class HttpSender implements DataSend {

	@Override
	public Object send(Object obj) throws Exception {
		
		String imgsPath=obj.toString();
		String url= PropertiesUnit.getValue("receiveHost");
		String resultStr= FileUtil.uploadFile(url,imgsPath);
		return resultStr;
	}
	
}
