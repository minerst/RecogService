package com.silreg.recogservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import weka.classifiers.Classifier;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.filters.unsupervised.attribute.Remove;

public class Train {
	SerializationHelper sh;
	J48 cfs;
	String inputFileName;
	String outputFileName;
	ArrayList<InstancesApp> appList;
	HashSet hash;
	public String path ;
	Train(String path)
	{
		this.path = path;
	}

	public boolean train() throws Exception {
		inputFileName = path+"Train.arff";
		File input;
		input = new File(inputFileName);
		ArffLoader atf = new ArffLoader();
		atf.setFile(input);
		appList = new ArrayList<InstancesApp>();
		hash = new HashSet();
		
		Instances instances;
		try {
			instances = atf.getDataSet();
		} catch (IOException e) {
			System.out.println("Not enough data!");
			return false;
		}
		
		Instances instancesTap = new Instances(instances);
		instancesTap.clear();
		Instances instancesScroll = new Instances(instances);
		instancesScroll.clear();
		Instances instancesFling = new Instances(instances);
		instancesFling.clear();
		instances.setClassIndex(instances.numAttributes()-1);
		for(int i = 0;i < instances.numInstances();i++)
		{
			String appName = instances.instance(i).stringValue(0).trim();
			if(!hash.contains(appName)){
				hash.add(appName);
				InstancesApp app = new InstancesApp(appName,instances);
				appList.add(app);
			}
			for(InstancesApp app : appList)
			{
				if(app.getAppName().equals(appName))
				{
					if(instances.instance(i).stringValue(1).equals("Tap"))
					{
						instancesTap.add(instances.instance(i));
						app.instancesTap.add(instances.instance(i));
					}
					else if(instances.instance(i).stringValue(1).equals("Scroll"))
					{
						instancesScroll.add(instances.instance(i));
						app.instancesScroll.add(instances.instance(i));
					}
					else if(instances.instance(i).stringValue(1).equals("Fling"))
					{
						instancesFling.add(instances.instance(i));
						app.instancesFling.add(instances.instance(i));
					}
				}
			}
		}
		instancesTap.setClassIndex(instancesTap.numAttributes()-1);
		instancesScroll.setClassIndex(instancesScroll.numAttributes()-1);
		instancesFling.setClassIndex(instancesFling.numAttributes()-1);
		
		cfs = new J48();
		//String[] options=weka.core.Utils.splitOptions("-S 0 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.0010 -P 0.1 -B 0");
		//cfs.setOptions(options);
		 Remove rm = new Remove();
		 rm.setAttributeIndices("1");
		 
		 FilteredClassifier fc = new FilteredClassifier();
		 fc.setFilter(rm);
		 fc.setClassifier(cfs);
		 
		fc.buildClassifier(instancesTap);
		SerializationHelper.write(path+"Tap.model", fc);
		fc.buildClassifier(instancesScroll);
		SerializationHelper.write(path+"Scrool.model", fc);
		fc.buildClassifier(instancesFling);
		SerializationHelper.write(path+"Fling.model", fc);
		
		for(InstancesApp app : appList)
		{
			app.instancesTap.setClassIndex(instancesTap.numAttributes()-1);
			app.instancesScroll.setClassIndex(instancesScroll.numAttributes()-1);
			app.instancesFling.setClassIndex(instancesFling.numAttributes()-1);
			fc.buildClassifier(app.instancesTap);
			SerializationHelper.write(path+app.getFileName()+"Tap.model", fc);
			fc.buildClassifier(app.instancesScroll);
			SerializationHelper.write(path+app.getFileName()+"Scrool.model", fc);
			fc.buildClassifier(instancesFling);
			SerializationHelper.write(path+app.getFileName()+"Fling.model", fc);
		}
		return true;
		

	}
	
	public boolean train(String inputF) throws Exception{
		inputFileName =inputF;
		File input = new File(inputFileName);
		ArffLoader atf = new ArffLoader();
		atf.setFile(input);
		appList = new ArrayList<InstancesApp>();
		hash = new HashSet();
		
		Instances instances;
		try {
			instances = atf.getDataSet();
		} catch (IOException e) {
			System.out.println("Not enough data!");
			return false;
		}
		Instances instancesTap = new Instances(instances);
		instancesTap.clear();
		Instances instancesScroll = new Instances(instances);
		instancesScroll.clear();
		Instances instancesFling = new Instances(instances);
		instancesFling.clear();
		instances.setClassIndex(instances.numAttributes()-1);
		for(int i = 0;i < instances.numInstances();i++)
		{
			String appName = instances.instance(i).stringValue(0).trim();
			if(!hash.contains(appName)){
				hash.add(appName);
				InstancesApp app = new InstancesApp(appName,instances);
				appList.add(app);
			}
			for(InstancesApp app : appList)
			{
				if(app.getAppName().equals(appName))
				{
					if(instances.instance(i).stringValue(1).equals("Tap"))
					{
						instancesTap.add(instances.instance(i));
						app.instancesTap.add(instances.instance(i));
					}
					else if(instances.instance(i).stringValue(1).equals("Scroll"))
					{
						instancesScroll.add(instances.instance(i));
						app.instancesScroll.add(instances.instance(i));
					}
					else if(instances.instance(i).stringValue(1).equals("Fling"))
					{
						instancesFling.add(instances.instance(i));
						app.instancesFling.add(instances.instance(i));
					}
				}
			}
		}
		instancesTap.setClassIndex(instancesTap.numAttributes()-1);
		instancesScroll.setClassIndex(instancesScroll.numAttributes()-1);
		instancesFling.setClassIndex(instancesFling.numAttributes()-1);
		
		cfs = new J48();
//		String[] options=weka.core.Utils.splitOptions("-S 0 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.0010 -P 0.1 -B 0");
//		cfs.setOptions(options);
		 Remove rm = new Remove();
		 rm.setAttributeIndices("1");
		 
		 FilteredClassifier fc = new FilteredClassifier();
		 fc.setFilter(rm);
		 fc.setClassifier(cfs);
		 
		fc.buildClassifier(instancesTap);
		SerializationHelper.write(path+"Tap.model", fc);
		fc.buildClassifier(instancesScroll);
		SerializationHelper.write(path+"Scrool.model", fc);
		fc.buildClassifier(instancesFling);
		SerializationHelper.write(path+"Fling.model", fc);
		
		for(InstancesApp app : appList)
		{
			app.instancesTap.setClassIndex(instancesTap.numAttributes()-1);
			app.instancesScroll.setClassIndex(instancesScroll.numAttributes()-1);
			app.instancesFling.setClassIndex(instancesFling.numAttributes()-1);
			fc.buildClassifier(app.instancesTap);
			SerializationHelper.write(path+app.getFileName()+"Tap.model", fc);
			fc.buildClassifier(app.instancesScroll);
			SerializationHelper.write(path+app.getFileName()+"Scrool.model", fc);
			fc.buildClassifier(instancesFling);
			SerializationHelper.write(path+app.getFileName()+"Fling.model", fc);
		}
		return true;
	}
	
}
