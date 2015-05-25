package com.silreg.recogservice;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Judge {
	double threshold;
	double P;
	Predict pp;
	static int numberOfAttr;
	Judge(String path)
	{
		threshold = 0.8;
		P = 0;
		pp = new Predict(path);
		numberOfAttr = 10;
	}
	protected double getProbility() {
		return P;
	}
	boolean isOwner(String instance) throws Exception
	{
		//AppName, Fling, 432, 864, 150, 287, 26, 0.23137257, 0.33333334, yes
		String[] data;
		data = instance.trim().split(",");
		P = pp.getProbability(data[0].trim(),this.getInstance(instance));
		if(P>=threshold)
			return true;
		return false;
		
	} 
	static Instance getInstance(String instance){
		Instance ins = new DenseInstance(numberOfAttr);
		ArrayList<Attribute> valueList = new ArrayList<Attribute>(numberOfAttr);
		String[] data;
		data = instance.trim().split(",");
		Attribute attr = new Attribute("appName") ;
		valueList.add(attr);
	
		
		ArrayList<String> actionTag = new ArrayList<String>(3);
		actionTag.add("Tap");
		actionTag.add("Scroll");
		actionTag.add("Fling");
		
		attr = new Attribute("actionType",actionTag);
		valueList.add(attr);
		

		attr = new Attribute("coordinateX");
		valueList.add(attr);
		
		attr = new Attribute("coordinateY");
		valueList.add(attr);
		
		attr = new Attribute("velocityX");
		valueList.add(attr);
		
		attr = new Attribute("velocityY");
		valueList.add(attr);
		
		attr = new Attribute("duration");
		valueList.add(attr);
		
		attr = new Attribute("pressure");
		valueList.add(attr);
		
		attr = new Attribute("size");
		valueList.add(attr);
		
	
		ArrayList<String> cls = new ArrayList<String>(3);
		cls.add("yes");
		cls.add("no");
		cls.add("?");
		attr = new Attribute("isOwner",cls);
		valueList.add(attr);
	  
		Instances instances = new Instances("Test", valueList, 1);
		ins.setValue(valueList.get(0), 0);
		if(data[1].trim().equals("Tap"))
			ins.setValue(valueList.get(1),0);
		else if(data[1].trim().equals("Scroll"))
			ins.setValue(valueList.get(1),1);
		else if(data[1].trim().equals("Fling"))
			ins.setValue(valueList.get(1),2);
		
		
		for(int i = 2;i<9;i++)
			ins.setValue(valueList.get(i), Double.parseDouble(data[i]));
		ins.setValue(valueList.get(valueList.size()-1), data[numberOfAttr-1].trim());
		return ins;
	}
	boolean isOwner() throws Exception
	{

		P = pp.getProbability(null,null);
		if(P>=threshold)
			return true;
		return false;
		
	} 
}
