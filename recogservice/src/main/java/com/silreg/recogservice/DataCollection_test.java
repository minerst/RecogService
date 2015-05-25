package com.silreg.recogservice;

import android.os.Bundle;
import android.view.GestureDetector;
import android.widget.TextView;

public class DataCollection_test extends DataCollectActivity {

	public DataCollection_test(boolean b) {
		super(b);
	}

	@Override
	public void onCreate(Bundle savedInst) {
		super.onCreate(savedInst);
		setContentView(R.layout.empty_layout);
		getIntent();
		textView = (TextView) findViewById(R.id.tv);
		if (FileUtil.path_test == null)
			FileUtil.path_test = getFilesDir() + FileUtil.PATH_TEST;
		if (FileUtil.path_train == null)
			FileUtil.path_train = getFilesDir() + FileUtil.PATH_TRAIN;
		dir = FileUtil.createFile(1);
		detector = new GestureDetector(this,(GestureDetector.OnGestureListener) this);
	}

}
