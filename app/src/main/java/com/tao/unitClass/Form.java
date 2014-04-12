package com.tao.unitClass;

public class Form {
	
	//form data variables
	private long timeStamp;
	private long formID;
	private String formTemplate;
	private String formName;
	private int flag;
	public Form()
	{}
	public Form(String formName,String formTemplate,int flag)
	{
		this.formName=formName;
		this.formTemplate=formTemplate;
		this.flag = flag;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setFormID(long formID) {
		this.formID = formID;
	}

	public String getFormTemplate() {
		return formTemplate;
	}

	public void setFormTemplate(String formTemplate) {
		this.formTemplate = formTemplate;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	
	
}
