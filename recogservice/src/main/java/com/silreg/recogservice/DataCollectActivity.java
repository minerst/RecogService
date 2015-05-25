package com.silreg.recogservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.TextView;

import java.io.*;
import java.util.*;


public class DataCollectActivity extends Activity implements
		GestureDetector.OnGestureListener {

    private boolean train = true;
	public int MinLength = 1000;
	static int type = 0;
	public List Data = new ArrayList();
	public TextView textView;
	private VelocityTracker vt = null;
	GestureDetector detector;
	File dir;
    public DataCollectActivity(boolean b) {
        train = b;
    }
	public boolean onTouchEvent(MotionEvent event) {
		int x;
		int y;
		int vx;

		int vy;
		int tTime;
		float pre;
		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (vt == null) {
				vt = VelocityTracker.obtain();
			} else {
				vt.clear();
			}
			vt.addMovement(event);
			break;
		case MotionEvent.ACTION_MOVE:
			vt.addMovement(event);
			vt.computeCurrentVelocity(1000);
			showXY(event.getX(), event.getY());
			x = (int) event.getX();
			y = (int) event.getY();
			vx = (int) vt.getXVelocity();
			vy = (int) vt.getYVelocity();
			tTime = (int) (event.getEventTime() - event.getDownTime());
			pre = event.getPressure();
			Data.add("AppName");
			switch (type) {
			case 1:
				Data.add("Tap");
				break;
			case 2:
				Data.add("Scroll");
				break;
			case 3:
				Data.add("Fling");
				break;
			default:
				break;
			}
			Data.add(String.valueOf(x));
			Data.add(String.valueOf(y));
			Data.add(String.valueOf(vx));
			Data.add(String.valueOf(vy));
			Data.add(String.valueOf(tTime));
			Data.add(String.valueOf(pre));
			Data.add(String.valueOf(event.getSize()));
			Data.add("yes");
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}

		try {
			if (Data.size() == 10) {
				for (int temp = 0; temp < Data.size(); temp++) {
					if (temp == Data.size() - 1)
						FileUtil.write(Data.get(temp) + "\n", dir);
					else
						FileUtil.write(Data.get(temp) + ",", dir);
				}
                Intent intent = new Intent(getApplicationContext(), RecognizationService.class);
                intent.putExtra(RecognizationService.EXTRA_MODE, train);
				startService(intent);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Data.clear();
		return detector.onTouchEvent(event);
	}

	private void showXY(float x, float y) {
		textView.setText("x：" + x + "\n" + "y：" + y);
		// }
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// Toast.makeText(this,"触碰时间按下时触发该方法",Toast.LENGTH_LONG).show();
		// gesture.setText("onDown");
		return false;
	}

	/**
	 * The user has performed a down {@link android.view.MotionEvent} and not
	 * performed a move or up yet. This event is commonly used to provide visual
	 * feedback to the user to let them know that their action has been
	 * recognized i.e. highlight an element.
	 *
	 * @param e
	 *            The down motion event
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		// Toast.makeText(this,"onShowPress",Toast.LENGTH_LONG).show();
		// gesture.setText("onShowPress");
	}

	/**
	 * Notified when a tap occurs with the up {@link android.view.MotionEvent}
	 * that triggered it.
	 *
	 * @param e
	 *            The up motion event that completed the first tap
	 * @return true if the event is consumed, else false
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// gesture.setText("Tap");// 点击
		type = 1;
		Data.add("Tap");
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// Toast.makeText(this,"onScroll",Toast.LENGTH_LONG).show();
		// gesture.setText("Scroll");// 滚动

		Data.add("Scroll");
		type = 2;
		return false;
	}

	/**
	 * Notified when a long press occurs with the initial on down
	 * {@link android.view.MotionEvent} that trigged it.
	 *
	 * @param e
	 *            The initial on down motion event that started the longpress.
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		// gesture.setText("Tap");// 合并至Tap
		Data.add("Tap");
		type = 1;
	}

	/**
	 * Notified of a fling event when it occurs with the initial on down
	 * {@link android.view.MotionEvent} and the matching up
	 * {@link android.view.MotionEvent}. The calculated velocity is supplied
	 * along the x and y axis in pixels per second.
	 *
	 * @param e1
	 *            The first down motion event that started the fling.
	 * @param e2
	 *            The move motion event that triggered the current onFling.
	 * @param velocityX
	 *            The velocity of this fling measured in pixels per second along
	 *            the x axis.
	 * @param velocityY
	 *            The velocity of this fling measured in pixels per second along
	 *            the y axis.
	 * @return true if the event is consumed, else false
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// gesture.setText("Fling");// 抛
		Data.add("Fling");
		type = 3;
		return false;
	}

}
