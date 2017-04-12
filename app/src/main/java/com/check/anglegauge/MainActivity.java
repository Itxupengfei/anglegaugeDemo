package com.check.anglegauge;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.check.anglegauge.view.AngleView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnTouchListener {
    private float[] gravity = null;
    private AngleView mAngleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }
    SensorManager mSensorManager;
    TextView tvSensors;
    TextView tvAccelerometer;
    private void init() {
        tvSensors = (TextView) findViewById(R.id.tv_sensors);
        tvAccelerometer = (TextView) findViewById(R.id.tv_data);
        mAngleview = (AngleView) findViewById(R.id.angleview);
        tvAccelerometer.setOnTouchListener(this);
        //获取传感器SensorManager对象
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //显示有多少个传感器
       // tvSensors.setText("经检测该手机有" + sensors.size() + "个传感器，他们分别是：\n");
       /* //显示每个传感器的具体信息
        for (Sensor s : sensors) {

            String tempString = "\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："
                    + s.getVendor() + "\n";

            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 加速度传感器accelerometer" + tempString);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 陀螺仪传感器gyroscope" + tempString);
                    break;
                case Sensor.TYPE_LIGHT:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 环境光线传感器light" + tempString);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 电磁场传感器magnetic field" + tempString);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 方向传感器orientation" + tempString);
                    break;
                case Sensor.TYPE_PRESSURE:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 压力传感器pressure" + tempString);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 距离传感器proximity" + tempString);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 温度传感器temperature" + tempString);
                    break;
                default:
                    tvSensors.setText(tvSensors.getText().toString() + s.getType() + " 未知传感器" + tempString);
                    break;
            }


    }*/
}

    @Override
    protected void onResume() {
        super.onResume();
        //加速度传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),//传感器TYPE类型
                SensorManager.SENSOR_DELAY_UI);//采集频率
        //注册重力传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_FASTEST);
        //方向传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }
    boolean timerFlag=true;
    /**
       传感器数据变化时回调
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(final SensorEvent event) {
        //判断传感器类别
        switch (event.sensor.getType()) {
           /* case Sensor.TYPE_ACCELEROMETER: //加速度传感器
                final float alpha = (float) 0.8;
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                String accelerometer = "加速度传感器\n" + "x:"
                        + (event.values[0] - gravity[0]) + "\n" + "y:"
                        + (event.values[1] - gravity[1]) + "\n" + "z:"
                        + (event.values[2] - gravity[2]);
                //tvAccelerometer.setText(accelerometer);
                //重力加速度9.81m/s^2，只受到重力作用的情况下，自由下落的加速度
                break;
            case Sensor.TYPE_GRAVITY://重力传感器
                gravity[0] = event.values[0];//单位m/s^2
                gravity[1] = event.values[1];
                gravity[2] = event.values[2];

                break;*/
            case Sensor.TYPE_ORIENTATION://方向传感器,已过时
                if(gravity==null)
                    gravity = new float[3];
                //原本是打算根据下次与上次获取的值相减的绝对值达到某个值,再去改变UI,以此来减少UI的绘制
                //但是在安卓高级编程中是用的是线程去绘制,我觉得我的方法跟用线程绘制各有优势
                float absX = Math.abs(event.values[0] - gravity[0]);
                final float absY = Math.abs(event.values[1] - gravity[1]);
                float absZ = Math.abs(event.values[2] - gravity[2]);
                gravity[0] = event.values[0];
                gravity[1] = event.values[1];
                gravity[2] = event.values[2];
                if (absY>1&&touchflage)
                    mAngleview.setAngle(Math.abs(event.values[1]));

                if (timerFlag){
                Timer timer = new Timer("a");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (gravity==null)
                                    return ;
                                    if(absY>1&&touchflage){
                                        tvAccelerometer.clearAnimation();
                                        RotateAnimation rotateAnimation= new RotateAnimation(timerFlag?0:event.values[1]+90,gravity[1]+90, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                                        rotateAnimation.setDuration(100);
                                        rotateAnimation.setFillAfter(true);
                                        tvAccelerometer.setText(Math.abs((int) gravity[1])+"");
                                        tvAccelerometer.setAnimation(rotateAnimation);
                                        tvAccelerometer.startAnimation(rotateAnimation);
                                        timerFlag=false;
                                    }


                            }
                        });
                    }
                },0,100);
                }
               /* if(absY>1){
                    tvAccelerometer.setText((int) event.values[1]+"");
                }*/
                String accelerometerG = "方向传感器\n" + "x:"
                        + (event.values[0]) + "\n" + "y:"
                        + (event.values[1]) + "\n" + "z:"
                        + (event.values[2]);
                tvSensors.setText(accelerometerG);
            default:
                break;
        }
    }

    /**
     传感器精度变化时回调
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

   boolean touchflage=true;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touchflage){
                    touchflage=false;
                }else{
                    touchflage=true;
                }
        }
        return false;
    }
}
