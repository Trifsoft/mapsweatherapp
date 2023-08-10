package com.trifsoft.mapsweatherapp.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.trifsoft.mapsweatherapp.models.Lokacija;
import com.trifsoft.mapsweatherapp.models.WeatherData;
import com.trifsoft.mapsweatherapp.service.WeatherDataService;
import com.trifsoft.mapsweatherapp.service.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

@Database(entities = {Lokacija.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

	public abstract LokacijaDAO lokacijaDAO();

	private static AppDatabase instance;

	private ArrayList<WeatherData> movies = new ArrayList<>();
	private MutableLiveData<WeatherData> mutableLiveData = new MutableLiveData<>();

	private static final String API_KEY = "c2650ae331385d33132778bcaa9cdb78";


	public MutableLiveData<WeatherData> getMutableLiveData(double lat, double lon){
		WeatherDataService weatherDataService = RetrofitInstance.getWeatherDataService();

		Call<WeatherData> call = weatherDataService.getWeatherData(lat, lon, API_KEY);

		call.enqueue(new retrofit2.Callback<WeatherData>() {
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

	public static synchronized AppDatabase getInstance(Context context){
		if (instance == null){
			instance = Room.databaseBuilder(context.getApplicationContext(),
				AppDatabase.class, "database")
				.fallbackToDestructiveMigration()
				.addCallback(roomCallback)
				.build();
		}
		return instance;
	}

	private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);

			LokacijaDAO lokacijaDAO = instance.lokacijaDAO();

			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(new Runnable() {
				@Override
				public void run() {

					//TODO Ubaciti pocetne elemente baze podataka u odgovarajuce tabele

				}
			});
		}
	};
}