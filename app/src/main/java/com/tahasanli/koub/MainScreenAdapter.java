package com.tahasanli.koub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tahasanli.koub.databinding.MainScreenRecyclerHolderBinding;

public class MainScreenAdapter extends RecyclerView.Adapter<MainScreenAdapter.AdapterHolder> {

    @NonNull
    @Override
    public MainScreenAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainScreenRecyclerHolderBinding binding = MainScreenRecyclerHolderBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new AdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainScreenAdapter.AdapterHolder holder, int position) {
        Picasso.get().load(MainScreen.instance.photos.get(position).photo).into(holder.binding.RecyclerHolderImage);
        holder.binding.RecyclerHolderUser.setText(MainScreen.instance.photos.get(position).user);
        holder.binding.RecyclerHolderName.setText(MainScreen.instance.photos.get(position).name);


    }

    @Override
    public int getItemCount() {
        return MainScreen.instance.photos.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder{
        MainScreenRecyclerHolderBinding binding;

        public AdapterHolder(@NonNull MainScreenRecyclerHolderBinding vie) {
            super(vie.getRoot());
            binding = vie;

        }
    }
}
