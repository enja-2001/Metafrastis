package com.example.myapplication.HelperClasses;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketClient {

    private static SocketClient socketClient;
    private static Socket socket;
    private static final String URL = "http://yoururl.com";


    public static SocketClient getInstance(){
        if(socketClient == null){
            socketClient = new SocketClient();
            connect();
        }
        return socketClient;
    }

    public boolean isConnected() {
        return false;
    }

    public static void connect(){
        try {
            socket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void isClosed(){

    }

    public void close(){

    }
}