package com.trifsoft.mapsweatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trifsoft.mapsweatherapp.R;
import com.trifsoft.mapsweatherapp.models.Lokacija;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

	private final ArrayList<Lokacija> listData;

	private ItemClickListener clickListener;

	public CustomAdapter(ArrayList<Lokacija> listData){
		this.listData = listData;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View listItem = inflater.inflate(R.layout.recyclerview_item, parent, false);
		return new MyViewHolder(listItem);
	}

	@Override
	public int getItemCount() {
		return listData.size();
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		final Lokacija myListData = listData.get(position);
		holder.ime_lokacije.setText(myListData.getIme_kolone());
		holder.longitude.setText(String.format(Locale.getDefault(), "%f", myListData.getLongitude()));
		holder.latitude.setText(String.format(Locale.getDefault(), "%f", myListData.getLatitude()));
	}

	public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

		public TextView ime_lokacije;
		public TextView longitude;
		public TextView latitude;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);

			this.ime_lokacije = itemView.findViewById(R.id.ime_lokacije);
			this.longitude = itemView.findViewById(R.id.longitude);
			this.latitude = itemView.findViewById(R.id.latitude);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			if(clickListener != null){
				clickListener.onClick(view, getAdapterPosition());
			}
		}
	}

	public void setClickListener(ItemClickListener clickListener) {
		this.clickListener = clickListener;
	}
	public interface ItemClickListener{
		void onClick(View view, int pos);
	}
}