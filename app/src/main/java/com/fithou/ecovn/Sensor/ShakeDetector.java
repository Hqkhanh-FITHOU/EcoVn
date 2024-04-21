package com.fithou.ecovn.Sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {
        //ngưỡng lắc
        private static final float SHAKE_THRESHOLD =500f;
        //tg min giữa các lần lắc
        private static final int SHAKE_TIMEOUT = 500;
        // tg min của một lần lắc
        private static final int SHAKE_DURATION = 1000;
        // Số lần lắc cần
        public static final int SHAKE_COUNT = 2;

        private SensorManager sensorManager;
        private Sensor accelerometer;
        private OnShakeListener listener;
        private long lastShakeTime;
        private long lastUpdateTime;
        private int shakeCount;

    public ShakeDetector(Context context) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.listener = listener;
    }

    public interface OnShakeListener {
        void onShake(int count);
    }

    public void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastUpdateTime;

            if (deltaTime > SHAKE_TIMEOUT) {
                shakeCount = 0;
            }
            if (deltaTime > 0) {
                float speed = Math.abs(x + y + z - lastShakeTime) / deltaTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    if (++shakeCount >= SHAKE_COUNT) {
                        listener.onShake(shakeCount);
                        shakeCount = 0;
                    }
                    lastShakeTime = currentTime;
                }
                lastUpdateTime = currentTime;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
