package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Model.CustomBody;
import com.example.myapplication.Model.CustomOutput;
import com.example.myapplication.Network.Api;
import com.example.myapplication.Network.ApiClient;
import com.example.myapplication.Network.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    EditText editTextResult;
    ImageButton imageButton;
    Button butInstall;
    TextToSpeech textToSpeech;

    final SpeechRecognizer mSpeechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
    final Intent mSpeechRecognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.etSpeech);
        imageButton=findViewById(R.id.butMicrophone);
        butInstall=findViewById(R.id.butInstall);
        editTextResult=findViewById(R.id.etResult);

        butInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installVoiceData();
            }
        });

        imageButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                checkPermission();

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    editText.setText("");
                    editText.setHint("Listening...");
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    textToSpeech.stop();
                    editTextResult.setText("");
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    editText.setHint("Input will appear here...");
                    mSpeechRecognizer.stopListening();

                }
                return false;
            }

        });

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> al=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Response ob=new Response();

                if(!al.isEmpty()) {
                    editText.setText("" + al.get(0));
                    ob.setResponse(al.get(0),editTextResult,textToSpeech);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.setSpeechRate(0.8f);

                    Locale arr[]=Locale.getAvailableLocales();

                    Log.d("TTS",""+Arrays.toString(arr));

                    ArrayList<Locale> al=new ArrayList<>();
                    for (Locale locale : arr) {
                        int res = textToSpeech.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            al.add(locale);
                        }
                    }

                    Log.d("TTS",""+al.toString());
                }
            }
        });
    }

    private void checkPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},200);
    }

    private void installVoiceData() {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.tts");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "Failed to install voice data ! ", Toast.LENGTH_SHORT).show();
        }
    }
}