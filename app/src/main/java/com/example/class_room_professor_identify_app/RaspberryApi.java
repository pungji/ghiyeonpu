package com.example.class_room_professor_identify_app;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;


public interface RaspberryApi {
    @GET("hello")
    Call<JsonObject> getHello();
}

