package com.trifsoft.mapsweatherapp.service;

import com.trifsoft.mapsweatherapp.models.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherDataService {

    @GET("data/2.5/weather")
    Call<WeatherData> getWeatherData(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);

}