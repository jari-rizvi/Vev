package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.home.Data;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemSearchResultBinding;
import com.teamx.vevae.onClick.OnSearchResultClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private Context context;
    private ArrayList<Data> homeArrayList;
    private OnSearchResultClickListener onSearchResultClickListener;

    public HomeAdapter(Context context, ArrayList<Data> homeArrayList, OnSearchResultClickListener onSearchResultClickListener) {
        this.context = context;
        this.homeArrayList = homeArrayList;
        this.onSearchResultClickListener = onSearchResultClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemSearchResultBinding itemSearchResultBinding = ItemSearchResultBinding.inflate(inflater,parent,false);
        return new HomeViewHolder(itemSearchResultBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeViewHolder holder, int position) {
        Data data = homeArrayList.get(position);
        holder.itemSearchResultBinding.txtname.setText(data.getStore_name());
        holder.itemSearchResultBinding.txtaddress.setText(data.getStore_address());

        //reduce digit after decimal (1.333333)
        String strDouble = String.format("%.1f", data.getDistance());
        holder.itemSearchResultBinding.txtDistance.setText(strDouble +" km");
        holder.itemSearchResultBinding.rating.setRating(data.getRating());
        Picasso.get().load(AppConstants.imagePath(data.getBanner())).into(holder.itemSearchResultBinding.image);
    }

    @Override
    public int getItemCount() {

        return  homeArrayList == null ? 0 : homeArrayList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ItemSearchResultBinding itemSearchResultBinding;
        public HomeViewHolder(@NonNull @NotNull  ItemSearchResultBinding itemSearchResultBinding) {
            super(itemSearchResultBinding.getRoot());
            this.itemSearchResultBinding=itemSearchResultBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSearchResultClickListener.OnSearchedItemClick(getAdapterPosition());
                }
            });
        }
    }
}
