package com.trifsoft.mapsweatherapp;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trifsoft.mapsweatherapp.db.AppDatabase;
import com.trifsoft.mapsweatherapp.db.LokacijaDAO;
import com.trifsoft.mapsweatherapp.models.Lokacija;
import com.trifsoft.mapsweatherapp.models.WeatherData;
import com.trifsoft.mapsweatherapp.service.WeatherDataService;
import com.trifsoft.mapsweatherapp.service.RetrofitInstance;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class AppRepository {

    private final LokacijaDAO lokacijaDAO;
    private final MutableLiveData<WeatherData> mutableLiveData = new MutableLiveData<>();

    private static final String API_KEY = "c2650ae331385d33132778bcaa9cdb78";

    public AppRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        lokacijaDAO = appDatabase.lokacijaDAO();
    }

    public LiveData<List<Lokacija>> getAllLokacijas() {
        return lokacijaDAO.getLokacijas();

    }	public void insertLokacija(Lokacija lokacija){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {

                lokacijaDAO.insert(lokacija);
            }
        });
    }
    public void deleteLokacija(Lokacija lokacija){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> lokacijaDAO.delete(lokacija));
    }
    public void updateLokacija(Lokacija lokacija){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> lokacijaDAO.update(lokacija));
    }

    public MutableLiveData<WeatherData> getMutableLiveData(double lat, double lon){
        WeatherDataService weatherDataService = RetrofitInstance.getWeatherDataService();

        Call<WeatherData> call = weatherDataService.getWeatherData(lat, lon, API_KEY);

        call.enqueue(new Callback<WeatherData>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                WeatherData weatherData = response.body();
                mutableLiveData.setValue(weatherData);
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });

        return mutableLiveData;
    }

}
