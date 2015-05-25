package com.silreg.recogservice;

import weka.core.Instances;

public class InstancesApp {
	private String appName;
	private String fileName;
	public Instances instancesTap;
	public Instances instancesScroll;
	public Instances instancesFling;
	public InstancesApp(String appName,Instances ins){
		this.appName = appName;
		instancesTap = new Instances(ins);
		instancesTap.clear();
		instancesScroll = new Instances(ins);
		instancesScroll.clear();
		instancesFling = new Instances(ins);
		instancesFling.clear();
		fileName = ""+appName.hashCode();
	}
	protected String getAppName() {
		return appName;
	}
	protected void setAppName(String appName) {
		this.appName = appName;
	}
	protected String getFileName() {
		return fileName;
	}
	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
