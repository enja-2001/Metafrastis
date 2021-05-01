package com.example.myapplication.Network;
import com.example.myapplication.Model.CustomBody;
import com.example.myapplication.Model.CustomOutput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL="https://metafrastis-backend.herokuapp.com";
    @POST("/translate")
    Call<CustomOutput> getData(@Body CustomBody customBody);
}
