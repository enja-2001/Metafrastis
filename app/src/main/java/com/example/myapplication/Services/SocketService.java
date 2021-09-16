package com.example.myapplication.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.Model.Contact;
import com.example.myapplication.Model.CustomBody;
import com.example.myapplication.Model.SocketModel;
import com.example.myapplication.Threads.SocketThread;

public class SocketService extends Service {

    private SocketThread socketThread;
    private final String CHANNEL_ID = "my_channel_01";


    public class MyLocalBinder extends Binder {

        public SocketService getService(){         //returns the reference to the current Service object
            return SocketService.this;              //which is different from creating a new Service object
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyLocalBinder();      //iBinder is a superclass of MyLocalBinder
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.hasExtra("socketParams")){
            switch(intent.getIntExtra("socketParams",-1)){

                case SocketModel.MAKE_CALL:
                    socketThread = new SocketThread(SocketModel.MAKE_CALL);
                    socketThread.start();
                    break;

                case SocketModel.CALL_REJECTED:
                    socketThread = new SocketThread(SocketModel.CALL_REJECTED);
                    socketThread.start();
                    break;

                case SocketModel.CALL_ACCEPTED:
                    socketThread = new SocketThread(SocketModel.CALL_ACCEPTED);
                    socketThread.start();
                    break;

                case SocketModel.SEND_VOICE_DATA:
                    socketThread = new SocketThread(SocketModel.SEND_VOICE_DATA);
                    socketThread.start();
                    break;

                case SocketModel.RECEIVE_VOICE_DATA:
                    socketThread = new SocketThread(SocketModel.RECEIVE_VOICE_DATA);
                    socketThread.start();
                    break;
            }
        }
        Log.d("status","onStartCommand() executed !");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("status","onCreate() executed !");

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Socket connected")
                .setContentText("Help us get you in touch. We promise to provide a quality service...").build();

        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("status","Service destroyed !");

        socketThread.interrupt();       //stop the thread
        stopSelf();                     //stop the service
    }
}
