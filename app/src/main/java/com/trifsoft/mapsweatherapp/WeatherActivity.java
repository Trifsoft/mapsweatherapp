package com.trifsoft.mapsweatherapp;

import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trifsoft.mapsweatherapp.databinding.ActivityWeatherBinding;
import com.trifsoft.mapsweatherapp.models.WeatherData;

import java.util.Locale;

public class WeatherActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityWeatherBinding binding;

    AppRepository appRepository;

    Intent intent;

    TextView temp, city, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        setTitle(intent.getStringExtra("locationName"));

        temp = findViewById(R.id.temp);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);


        appRepository = new AppRepository(getApplication());
        appRepository.getMutableLiveData(
                intent.getDoubleExtra("latitude", 0),
                intent.getDoubleExtra("longitude", 0)
        ).observe(this, new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                temp.setText(String.format(
                        Locale.getDefault(),
                        "%.2f",
                        weatherData.getMain().getTemp() - 273.15
                ));
                city.setText(weatherData.getName());
                country.setText(weatherData.getSys().getCountry());
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng markerPosition = new LatLng(
                intent.getDoubleExtra("latitude", 0),
                intent.getDoubleExtra("longitude", 0)
        );
        Toast.makeText(this, ""+markerPosition, Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions()
                .position(markerPosition)
                .title(intent.getStringExtra("locationName"))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15));
    }
}