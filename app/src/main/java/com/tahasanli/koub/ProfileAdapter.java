package com.tahasanli.koub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tahasanli.koub.databinding.MainScreenRecyclerHolderBinding;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.AdapterHolder> {

    @NonNull
    @Override
    public ProfileAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainScreenRecyclerHolderBinding binding = MainScreenRecyclerHolderBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new ProfileAdapter.AdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.AdapterHolder holder, int position) {
        Picasso.get().load(Profile.instance.photos.get(position).photo).into(holder.binding.RecyclerHolderImage);
        holder.binding.RecyclerHolderUser.setText(Profile.instance.photos.get(position).user);
        holder.binding.RecyclerHolderName.setText(Profile.instance.photos.get(position).name);


    }

    @Override
    public int getItemCount() {
        return Profile.instance.photos.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder{
        MainScreenRecyclerHolderBinding binding;

        public AdapterHolder(@NonNull MainScreenRecyclerHolderBinding vie) {
            super(vie.getRoot());
            binding = vie;

        }
    }
}
