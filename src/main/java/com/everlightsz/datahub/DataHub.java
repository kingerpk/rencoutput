package com.everlightsz.datahub;

public abstract class DataHub {
	
	private DataGeter geter;
	private DataProcess getProcesser;
	private DataProcess sendProcesser;
	private DataSend sender;

	public DataProcess getGetProcesser() {
		return getProcesser;
	}
	public void setGetProcesser(DataProcess getProcesser) {
		this.getProcesser = getProcesser;
	}
	public DataProcess getSendProcesser() {
		return sendProcesser;
	}
	public void setSendProcesser(DataProcess sendProcesser) {
		this.sendProcesser = sendProcesser;
	}
	public DataGeter getGeter() {
		return geter;
	}
	public void setGeter(DataGeter geter) {
		this.geter = geter;
	}

	public DataSend getSender() {
		return sender;
	}
	public void setSender(DataSend sender) {
		this.sender = sender;
	}
	
	public abstract Object get() throws Exception;
	public abstract Object processGet(Object obj) throws Exception;
	public abstract Object processSend(Object obj) throws Exception;
	public abstract void send(Object obj) throws Exception;
	
	public abstract void hub() throws Exception;
	
	
}
