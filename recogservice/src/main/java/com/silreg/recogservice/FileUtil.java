package com.silreg.recogservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {
	public static final String PATH_TRAIN = "Train.arff";
	public static final String PATH_TEST = "Test.arff";
	public static String path_train = null;
	public static String path_test = null;
	static String filehead_train = "@relation Train\n\n@attribute appName string\n@attribute actionType {Fling,Scroll,Tap}\n@attribute coordinateX numeric\n@attribute coordinateY numeric\n@attribute velocityX numeric\n@attribute velocityY numeric\n@attribute duration numeric\n@attribute pressure numeric\n@attribute size numeric\n@attribute isOwner {yes,no,?}\n\n@data\n";
	static String filehead_test = "@relation Test\n\n@attribute appName string\n@attribute actionType {Fling,Scroll,Tap}\n@attribute coordinateX numeric\n@attribute coordinateY numeric\n@attribute velocityX numeric\n@attribute velocityY numeric\n@attribute duration numeric\n@attribute pressure numeric\n@attribute size numeric\n@attribute isOwner {yes,no,?}\n\n@data\n";

	/**
	 * type = 0 代表Train.arff type = 1 代表Test.arff
	 * 
	 * @param type
	 * @return
	 */
	public static File createFile(int type) {
		File file;
		if (type == 0) {
			file = new File(path_train);
			if (!file.exists()) {
				try {
					if (file.createNewFile()) {
						write(filehead_train, file);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		} else {
			file = new File(path_test);
			if (!file.exists()) {
				try {
					if (file.createNewFile()) {
						write(filehead_test, file);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}
	}

	/**
	 * 将数据写到文件里
	 * @param Data
	 * @param dir
	 * @throws IOException
	 */
	public static void write(String Data, File dir) throws IOException {
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bw = null;
		FileOutputStream fileOutputStream = null;
		fileOutputStream = new FileOutputStream(dir, true);
		outputStreamWriter = new OutputStreamWriter(fileOutputStream);
		bw = new BufferedWriter(outputStreamWriter);
		bw.write(Data);
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
