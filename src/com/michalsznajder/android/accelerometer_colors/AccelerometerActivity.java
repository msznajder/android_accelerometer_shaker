package com.michalsznajder.android.accelerometer_colors;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class AccelerometerActivity extends Activity implements SensorEventListener {
	
	private SensorManager sensorManager;
	private boolean backgroundColor = true;
	private View backgroundView;
	private long lastUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accelerometer);
		backgroundView = findViewById(R.id.background);
		backgroundView.setBackgroundColor(Color.YELLOW);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lastUpdate = System.currentTimeMillis();
	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			processAccelerometer(event);
		}
	}
	
	private void processAccelerometer (SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		float accRoot = (x*x+y*y+z*z) 
				/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
		long currentTime = System.currentTimeMillis();
		
		if (accRoot >= 2) {
			if (currentTime - lastUpdate < 195) {
				return;
			}
			lastUpdate = currentTime;
			Toast.makeText(this,  "You shaked!", Toast.LENGTH_SHORT).show();
			if (backgroundColor) {
				backgroundView.setBackgroundColor(Color.GREEN);
			} else {
				backgroundView.setBackgroundColor(Color.YELLOW);
			}
			backgroundColor = !backgroundColor;
		}
		
		
	}
	
	@Override
	protected void onResume() {
		super.onPause();
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(
				Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}
	

}
