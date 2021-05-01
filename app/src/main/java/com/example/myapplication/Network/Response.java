package com.example.myapplication.Network;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.EditText;

import com.example.myapplication.Model.CustomBody;
import com.example.myapplication.Model.CustomOutput;

import retrofit2.Call;
import retrofit2.Callback;

public class Response {

    public String output;

    public void setResponse(String inputString, EditText editText,TextToSpeech textToSpeech) {

        CustomBody customBody = new CustomBody();
        customBody.setText(inputString);
        customBody.setLan("hi");


        Call<CustomOutput> call = ApiClient.getClient().create(Api.class).getData(customBody);


        call.enqueue(new Callback<CustomOutput>() {
            @Override
            public void onResponse(Call<CustomOutput> call, retrofit2.Response<CustomOutput> response) {

                try {
                    if (response.body() != null) {
                        Log.d("response", response.body().getOutput());
                        output = response.body().getOutput();
                        editText.setText(""+output);
                        textToSpeech.speak("" + output, TextToSpeech.QUEUE_FLUSH, null);

                    } else {
                        Log.d("response", "null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CustomOutput> call, Throwable t) {
                t.printStackTrace();
                call.cancel();
            }
        });
    }
}
