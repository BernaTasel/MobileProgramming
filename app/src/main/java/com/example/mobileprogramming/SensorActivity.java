
package com.example.mobileprogramming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Service;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SensorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView;
    private RelativeLayout layout;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private float maxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        iniView();

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor == null){
            Toast.makeText(SensorActivity.this,"There is no light sensor!",Toast.LENGTH_SHORT).show();
            finish();
        }
        maxValue = lightSensor.getMaximumRange();
        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float value = event.values[0];
                getSupportActionBar().setSubtitle(value+" lx");
                int newValue = (int) (255f * value/ maxValue);
                layout.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
                if (value < 20000){
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                }
                else{
                    textView.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }

    private void iniView(){
        toolbar = findViewById(R.id.tbSensor);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setTitle("Sensor");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView = findViewById(R.id.tvText);
        String str = "Sessiz Gemi\n"+
                "Artık demir almak günü gelmişse zamandan,\n" +
                "Meçhule giden bir gemi kalkar bu limandan.\n" +
                "Hiç yolcusu yokmuş gibi sessizce alır yol;\n" +
                "Sallanmaz o kalkışta ne mendil ne de bir kol.\n" +
                "Rıhtımda kalanlar bu seyahatten elemli,\n" +
                "Günlerce siyah ufka bakar gözleri nemli.\n" +
                "Biçare gönüller! Ne giden son gemidir bu!\n" +
                "Hicranlı hayatın ne de son matemidir bu!\n" +
                "Dünyada sevilmiş ve seven nafile bekler;\n" +
                "Bilmez ki giden sevgililer dönmeyecekler.\n" +
                "Birçok gidenin her biri memnun ki yerinden,\n" +
                "Birçok seneler geçti; dönen yok seferinden.\n"+
                "Yahya Kemal BEYATLI";
        textView.setText(str);
        layout = findViewById(R.id.layoutSensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(accelerometerEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
        sensorManager.unregisterListener(accelerometerEventListener);
    }

}
