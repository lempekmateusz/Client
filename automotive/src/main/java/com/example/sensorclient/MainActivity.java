package com.example.sensorclient;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sensorserver.IVehicleAidlInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable;
    private IVehicleAidlInterface mIVehicleAidlInterface;
    TextView tv_magneticFieldData;
    private final String TAG = MainActivity.class.getSimpleName();

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mIVehicleAidlInterface = IVehicleAidlInterface.Stub.asInterface(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_magneticFieldData = findViewById(R.id.tv_magneticFieldData);


        Intent intent = new Intent("MyService"); // pass the same action we have defined in Service Registration
        intent.setPackage("com.example.sensorserver"); //add service app
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                pollData();
                handler.postDelayed(runnable,1000);
            }
        },1000);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @SuppressLint("SetTextI18n")
    private void pollData()
    {
        try {
            float magneticFieldValueX = mIVehicleAidlInterface.getMagneticFieldValueX();
            float magneticFieldValueY = mIVehicleAidlInterface.getMagneticFieldValueY();
            float magneticFieldValueZ = mIVehicleAidlInterface.getMagneticFieldValueZ();
            tv_magneticFieldData.setText("Magnetic Field: X:" + String.valueOf(magneticFieldValueX) + " ,Y:" + String.valueOf(magneticFieldValueY) +
                    " ,Z:" + String.valueOf(magneticFieldValueZ));
            Log.d(TAG,"Magnetic Field- X: " + magneticFieldValueX + " ,Y: "+ magneticFieldValueY + " ,Z: "+ magneticFieldValueZ);
        } catch (Exception e) {

        }
    }
}