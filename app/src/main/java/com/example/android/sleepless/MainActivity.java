package com.example.android.sleepless;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.Random;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    final Context context=this;
    int count=0;
    private boolean init;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private float x1,y1,z1;
    private static final float ERROR=(float)12.0;
    TimePicker time;
    PendingIntent pending_intent;
    Button set_alarm,unset_alarm;
    AlarmManager alarmmanager;
    Calendar cal;
    String questions[]={"6*18+86","23+89*3","34*21+7","123+456+2","987+345+1","45*3+93","234-76+29","1234-987+12","38*5+88","12*12*4"};
    String answers[]={"194","290","721","581","1333","228","187","259","278","576"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        init=false;
        mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        cal = Calendar.getInstance();
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        time=(TimePicker) findViewById(R.id.timePicker);
        set_alarm=(Button)findViewById(R.id.set);
        unset_alarm=(Button)findViewById(R.id.unset);

        final Intent myintent=new Intent(this,Alarmreceiver.class);

        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(Calendar.HOUR_OF_DAY,time.getHour());
                cal.set(Calendar.MINUTE,time.getMinute());
                pending_intent=PendingIntent.getBroadcast(MainActivity.this,0,myintent,0);
                alarmmanager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pending_intent);
                Toast.makeText(v.getContext(),"Alarm set for "+time.getHour()+":"+time.getMinute(),Toast.LENGTH_LONG).show();

            }
        });
        unset_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random=new Random();
                final int num=random.nextInt(9-0)+0;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final TextView question=(TextView)mView.findViewById(R.id.question);
                question.setText(questions[num]);
                final EditText answer=(EditText)mView.findViewById(R.id.answer);
                Button ok=(Button)mView.findViewById(R.id.ok);
                mBuilder.setView(mView);
                final AlertDialog dialog=mBuilder.create();
                dialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ans=answer.getText().toString().trim();
                        if(ans.equals(answers[num])){
                            alarmmanager.cancel(pending_intent);
                            stopService(new Intent(v.getContext(),MyService.class));
                            Toast.makeText(v.getContext(),"Alarm dismissed",Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //Nothing
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x,y,z;
        x=event.values[0];
        y=event.values[1];
        z=event.values[2];
        if(!init){
            x1=x;
            y1=y;
            z1=z;
            init=true;
        }
        else{
            float diffX=Math.abs(x1-x);
            float diffY=Math.abs(y1-y);
            float diffZ=Math.abs(z1-z);
            if (diffX < ERROR) {

                diffX = (float) 0.0;
            }
            if (diffY < ERROR) {
                diffY = (float) 0.0;
            }
            if (diffZ < ERROR) {

                diffZ = (float) 0.0;
            }
            x1=x;
            y1=y;
            x1=z;
            if(diffX>diffY){
                count++;
            }
        }
        if(count>=40){
            count=0;
            alarmmanager.cancel(pending_intent);
            stopService(new Intent(context,MyService.class));
            Toast.makeText(context,"Alarm dismissed",Toast.LENGTH_LONG).show();
            //dialog.dismiss();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
