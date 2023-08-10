package com.trifsoft.mapsweatherapp.db;

import androidx.lifecycle.LiveData;import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.trifsoft.mapsweatherapp.models.Lokacija;

import java.util.List;

@Dao
public interface LokacijaDAO {

	@Insert
	public long insert(Lokacija lokacija);

	@Update
	public void update(Lokacija lokacija);

	@Delete
	public void delete(Lokacija lokacija);

	@Query("select * from lokacija")
	public LiveData<List<Lokacija>> getLokacijas();

	@Query("select * from lokacija where lokacija_id ==:lokacijaId")
	public Lokacija getLokacija(long lokacijaId);

}