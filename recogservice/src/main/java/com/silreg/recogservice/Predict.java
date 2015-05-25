package com.silreg.recogservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class Predict {
	private double Probability;
	String modelName;
	File inputFile;
	SerializationHelper sh;
	Classifier cfs;
	int numberOfAttr;
	String updateFile;
	public String path = "/data/data/com.silreg/";

	Predict(String path){
		Probability = 0.0;
		modelName = "LibSVM.model";
		numberOfAttr = 10;
        this.path = path;
	}
	
	
	protected double getProbability(String appName,Instance ins) throws Exception {
//		Instance ins = null;
//		ins.
		if(ins == null)
		{
			this.result();
		}
		else{
		 try {
			switch(Integer.parseInt(ins.toString().split(",")[1]))
			 {
                 case 2:
                     modelName = path + appName.hashCode() + "Tap.model";
                     break;
                 case 1:
                     modelName = path + appName.hashCode() + "Scrool.model";
                     break;
                 case 0:
                     modelName = path + appName.hashCode() + "Fling.model";
                     break;

			 }
				cfs = (Classifier) sh.read(modelName);
		} catch (FileNotFoundException e) {
			switch(Integer.parseInt(ins.toString().split(",")[1]))
			 {
			 case 2:
				 modelName = path+"Tap.model";
				 break;
			 case 1:
				 modelName = path+ "Scrool.model";
				 break;
			 case 0:
				 modelName =  path+"Fling.model";
				 break;
			 }
			cfs = (Classifier) sh.read(modelName);	
		}
		 this.result(ins);
		}
		return Probability;
	}

	private void result(Instance ins) {
		try{
		
//		
////		inputFile = new File("D:\\test.arff");
////		ArffLoader atf = new ArffLoader();
////		atf.setFile(inputFile);
//		
//		Instances instancesTest = atf.getDataSet();
//		instancesTest.setClassIndex(instancesTest.numAttributes()-1);
		
		
//		Evaluation eval = new Evaluation(instancesTrain);
//		eval.evaluateModel(cfs, instancesTrain);
//		Probability = eval.errorRate();
		
	//		double tmp = cfs.distributionForInstance(instancesTest.instance(0))[0];
			ArrayList<Attribute> valueList = new ArrayList<Attribute>(numberOfAttr-1);
			ArrayList<String> actionTag = new ArrayList<String>(3);
			actionTag.add("Tap");
			actionTag.add("Scroll");
			actionTag.add("Fling");
			Attribute attr = new Attribute("actionType",actionTag);
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
			instances.setClassIndex(instances.numAttributes()-1);
			ins.setDataset(instances);
			Probability = cfs.distributionForInstance(ins)[0];
		
		}
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println("oh,no!");
			
		}
	}
	
	private void result() {
		try{
		
		
		inputFile = new File(path +"Test.arff");
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		
		Instances instanceUpdate = null;
		Instances instancesTest;
		try {
			instancesTest = atf.getDataSet();
			instanceUpdate = new Instances(instancesTest);
			instanceUpdate.clear();
		} catch (IOException e) {
			instancesTest = null;
		}
		if(instancesTest == null)
		{
			System.out.println("Test data Not Found!");
			return;
		}
		instancesTest.setClassIndex(instancesTest.numAttributes()-1);
		
		double right,wrong;
			right = 0;
			wrong = 0;
		for(int i = 0; i<instancesTest.numInstances();i++){
			
//			if(instancesTest.instance(i).stringValue(1).trim().equals("Tap"))
//				instancesTest.instance(i).setValue(1,2);
//			else if(instancesTest.instance(i).stringValue(1).trim().equals("Scroll"))
//				instancesTest.instance(i).setValue(1,1);
//			else if(instancesTest.instance(i).stringValue(1).trim().equals("Fling"))
//				instancesTest.instance(i).setValue(1,0);
			
			this.getProbability(instancesTest.instance(i).stringValue(0), Judge.getInstance(instancesTest.instance(i).toString()));
			
			if(Probability > 0.5)
			{
				right += Probability;
			}
			else
				wrong ++;
			if(Probability< 0.8 && Probability >0.2)
			{
				instanceUpdate.add(instancesTest.instance(i));
			}
		}
		Probability = right / instancesTest.numInstances();
		if(instanceUpdate.numInstances()!=0)
			this.UpdateModel(instanceUpdate);
		}
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println("oh,no! Test failed.");
			
		}
	}
	
	private void UpdateModel(Instances ins) throws Exception{
		updateFile = path +"Train.arff";
		File inputFile = new File(updateFile);
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		
		Instances instanceUpdate = null;
		
		try {
			instanceUpdate = atf.getDataSet();
			instanceUpdate.addAll(ins);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Update failed.");
		}
		print(instanceUpdate.toString().split("@data")[1].substring(1),inputFile);
		Train update = new Train(path);
		update.train(updateFile);
		System.out.println("update success!");
	}
	
	public void print(String Data, File dir) throws IOException {
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bw = null;
		FileOutputStream fileOutputStream = null;
		fileOutputStream = new FileOutputStream(dir, true);
		outputStreamWriter = new OutputStreamWriter(fileOutputStream);
		bw = new BufferedWriter(outputStreamWriter);
		// CreateText();
		// for (int temp=0;temp<9;temp++)
		bw.write(Data);
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
