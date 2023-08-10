package com.trifsoft.mapsweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trifsoft.mapsweatherapp.adapters.CustomAdapter;
import com.trifsoft.mapsweatherapp.databinding.ActivityMapsBinding;
import com.trifsoft.mapsweatherapp.models.Lokacija;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton fab;

    ActivityMapsBinding binding = null;

    AlertDialog alertDialog;

    GoogleMap map;
    LatLng selectedLocation;

    ArrayList<Lokacija> lokacije;
    CustomAdapter customAdapter;
    RecyclerView recyclerView;
    AppRepository appRepository;

    EditText imeLokacije;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appRepository = new AppRepository(getApplication());
        LiveData<List<Lokacija>> lokacijaList = appRepository.getAllLokacijas();
        lokacijaList.observe(this, new Observer<List<Lokacija>>() {
            @Override
            public void onChanged(List<Lokacija> lokacijas) {
                lokacije = (ArrayList<Lokacija>) lokacijas;
                customAdapter = new CustomAdapter(lokacije);
                customAdapter.setClickListener(new CustomAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int pos) {
                        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                        Lokacija lokacija = lokacije.get(pos);
                        intent.putExtra("locationName", lokacija.getIme_kolone());
                        intent.putExtra("latitude", lokacija.getLatitude());
                        intent.putExtra("longitude", lokacija.getLongitude());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(customAdapter);

                lokacijaList.removeObserver(this);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Lokacija lokacija = lokacije.get(position);
                appRepository.deleteLokacija(lokacija);

                lokacije.remove(position);
                customAdapter.notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this).setView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            googleMap.setOnMapClickListener(latLng -> {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                );
                selectedLocation = latLng;
            });
        });
        
        selectedLocation = null;

        alertDialog = alertDialogBuilder
                .setTitle("Add a location")
                .setCancelable(true)
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("ADD", null)
                .create();
        alertDialog.setOnShowListener(dialogInterface ->
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                imeLokacije = binding.getRoot().findViewById(R.id.imeLokacijeEditText);
                if(selectedLocation == null || imeLokacije.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "You need to select a location first!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Lokacija lokacija = new Lokacija(
                            imeLokacije.getText().toString(),
                            selectedLocation.longitude,
                            selectedLocation.latitude
                    );
                    lokacije.add(lokacija);
                    customAdapter.notifyItemInserted(lokacije.size() - 1);

                    appRepository.insertLokacija(lokacija);

                    dialogInterface.dismiss();
                }
            })
        );
        alertDialog.setOnDismissListener((dialogInterface -> {
            map.clear();
            imeLokacije.setText("");
            selectedLocation = null;
        }));
        
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> alertDialog.show());
    }
}