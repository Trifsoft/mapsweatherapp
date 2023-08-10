package com.trifsoft.mapsweatherapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lokacija", foreignKeys = {
})
public class Lokacija {

	@Ignore
	public Lokacija(){

	}

	@ColumnInfo(name = "lokacija_id")
	@PrimaryKey(autoGenerate = true)
	private int lokacija_id;

	@ColumnInfo(name = "ime_kolone")
	private String ime_kolone;

	@ColumnInfo(name = "longitude")
	private double longitude;

	@ColumnInfo(name = "latitude")
	private double latitude;

	public Lokacija(String ime_kolone, double longitude, double latitude) {
		this.ime_kolone = ime_kolone;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public int getLokacija_id() {
		return lokacija_id;
	}
	public void setLokacija_id(int lokacija_id) {
		this.lokacija_id = lokacija_id;
	}

	public String getIme_kolone(){
		return ime_kolone;
	}
	public void setIme_kolone(String ime_kolone){
		this.ime_kolone = ime_kolone;
	}

	public double getLongitude(){
		return longitude;
	}
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLatitude(){
		return latitude;
	}
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

}