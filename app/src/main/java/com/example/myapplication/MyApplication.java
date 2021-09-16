package com.example.myapplication;

import android.app.Application;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.myapplication.Services.SocketService;

public class MyApplication extends Application {

    boolean mBounded;
    SocketService socketService;
    private ServiceConnection mConnection;

    public MyApplication(){
        super();

        mBounded = false;

//        mConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                Log.d("status","Service connected !");
//                mBounded = true;
//                socketService = ((SocketService.MyLocalBinder)service).getService();
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                Log.d("status","Service disconnected !");
//                mBounded=false;
//            }
//        };
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Intent mIntent = new Intent(getApplicationContext(), SocketService.class);
//        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        ContextCompat.startForegroundService(getApplicationContext(),new Intent(getApplicationContext(), SocketService.class));
//        startService(mIntent);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d("status","App terminated !");
        if(mBounded) {
            Intent mIntent = new Intent(getApplicationContext(), SocketService.class);
            stopService(mIntent);       //the onDestroy() method in the Service class is called
            mBounded = false;
        }
    }
}
