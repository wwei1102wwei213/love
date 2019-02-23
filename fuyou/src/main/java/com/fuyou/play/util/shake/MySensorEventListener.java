package com.fuyou.play.util.shake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.fuyou.play.util.LogCustom;

/**
 * Created by issuser on 2017/2/4.
 */

public class MySensorEventListener implements SensorEventListener {

    private Shakeable mActivity;

    public MySensorEventListener(Shakeable mActivity){
        this.mActivity = mActivity;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            //获取传感器类型
            int sensorType = event.sensor.getType();
            //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = event.values;
            //如果传感器类型为加速度传感器，则判断是否为摇一摇
            if(sensorType == Sensor.TYPE_ACCELEROMETER){
                if ((Math.abs(values[0]) > 12 || Math.abs(values[1]) > 12 || Math
                        .abs(values[2]) > 12))
                {
                    LogCustom.show("sensor x ============ values[0] = " + values[0] + ",sensor y ============ values[1] = "
                            + values[1] + ",sensor z ============ values[2] = " + values[2]);
                    //摇一摇的回调方法
                    mActivity.onShake(values);
                }
            }
        } catch (Exception e){
            LogCustom.e(e, "onSensorChanged");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
