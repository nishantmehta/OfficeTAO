package com.tao.unitClass;


public class Logs {
    //this fields contains the time entered by the user
	private long timeStamp;

    //the form type used to create the form
	private long formID;

    //this is the time stamp generated for the first time
	private long logID;

    //data in json string
	private String data;

    //currently not used
	private int flag;

	public Logs(){}
	
	public Logs(long formID,String data){
		this.data=data;
		this.formID=formID;
	}
    public Logs(Logs log){
        this.timeStamp=log.timeStamp;
        this.formID=log.getFormID();
        this.logID=log.getLogID();
        this.data=log.getData();
        this.flag=log.getFlag();
    }

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getFormID() {
		return formID;
	}

	public void setFormID(long formID) {
		this.formID = formID;
	}

	public long getLogID() {
		return logID;
	}

	public void setLogID(long logID) {
		this.logID = logID;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
