package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.services.ServiceImage;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemServicesBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private Context context;
    private ArrayList<ServiceImage> arrayList;

    public ServicesAdapter(Context context, ArrayList<ServiceImage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }



    @NonNull
    @NotNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemServicesBinding itemServicesBinding = ItemServicesBinding.inflate(layoutInflater,parent,false);
        return new ServiceViewHolder(itemServicesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceViewHolder holder, int position) {
        Picasso.get().load(AppConstants.imagePath(arrayList.get(position).getServicesImage())).into(holder.itemServicesBinding.imageView);
    }

    @Override
    public int getItemCount() {
        return  arrayList == null ? 0 : arrayList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        ItemServicesBinding itemServicesBinding;
        public ServiceViewHolder(@NonNull @NotNull ItemServicesBinding itemServicesBinding) {
            super(itemServicesBinding.getRoot());

            this.itemServicesBinding=itemServicesBinding;
        }
    }
}
