package com.everlightsz.datahub.imple;

public class DataHub extends com.everlightsz.datahub.DataHub {

	@Override
	public Object get() throws Exception {
		
		return this.getGeter().get();
	}

	@Override
	public Object processGet(Object obj) throws Exception {
		return this.getGetProcesser().process(obj);
	}

	@Override
	public Object processSend(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return this.getSendProcesser().process(obj);
	}

	@Override
	public void send(Object obj) throws Exception {
		this.getSender().send(obj);
	}

	@Override
	public void hub() throws Exception {
		javax.swing.JOptionPane.showMessageDialog(null,"nihao11");
		Object ncsPath=get();
		Object combitePath=processGet(ncsPath);
		Object imgsPath=processSend(combitePath);
		send(imgsPath);
	}
	
	
	
}
