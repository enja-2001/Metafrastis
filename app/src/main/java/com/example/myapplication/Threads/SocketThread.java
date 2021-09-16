package com.example.myapplication.Threads;

import android.util.Log;

import com.example.myapplication.Model.SocketModel;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketThread extends Thread{

    private int task;
    private static Socket socket;
    private static final String URL = "http://yoururl.com";

    public SocketThread(int task){
        try {
            socket = IO.socket(URL);        //connect to the socket
            socket.connect();
        } catch (URISyntaxException e) {
            Log.d("Error - ",e.getMessage());
        }
        this.task = task;
    }

    @Override
    public void run() {
        super.run();

        //all the background service logic should be written here

        if(socket!=null) {
            switch (task) {
                case SocketModel.MAKE_CALL:
                    socketMakeCall();
                    break;

                case SocketModel.CALL_REJECTED:
                    socketCallRejected();
                    break;

                case SocketModel.CALL_ACCEPTED:
                    socketCallAccepted();
                    break;

                case SocketModel.SEND_VOICE_DATA:
                    socketSendVoiceData();
                    break;

                case SocketModel.RECEIVE_VOICE_DATA:
                    socketReceiveVoiceData();
                    break;
            }
        }
    }

    private void socketMakeCall(){

        while(socket.connected()){

            JSONObject obj = new JSONObject();

            try {
                obj.put("msg","Hello there !");
                socket.emit("first",obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            socket.on("second",(msg)->{
                Log.d("secondSocket",""+msg);
            });
        }
        socket.close();                 //close the socket connections
//        socketThread.interrupt();       //stop the thread
    }
    private void socketCallRejected(){

    }
    private void socketCallAccepted(){

    }
    private void socketSendVoiceData(){

    }
    private void socketReceiveVoiceData(){

    }
}
