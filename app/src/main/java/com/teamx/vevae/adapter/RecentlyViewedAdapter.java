package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.recentsearch.Datum;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemRecentlyViewedBinding;
import com.teamx.vevae.onClick.OnOrderClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewHolder> {

    private Context context;
    private ArrayList<Datum> recentlyViewArrayList;
    private OnOrderClickListener onOrderClickListener;

    public RecentlyViewedAdapter(Context context, ArrayList<Datum> recentlyViewArrayList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.recentlyViewArrayList = recentlyViewArrayList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public RecentlyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemRecentlyViewedBinding itemRecentlyViewedBinding = ItemRecentlyViewedBinding.inflate(layoutInflater,parent,false);
        return new RecentlyViewHolder(itemRecentlyViewedBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentlyViewHolder holder, int position) {
        Datum recentlyView = recentlyViewArrayList.get(position);

        holder.itemRecentlyViewedBinding.productName.setText(recentlyView.getName());
        Picasso.get().load(AppConstants.imagePath(recentlyView.getImage())).into(holder.itemRecentlyViewedBinding.productPicture);



    }

    @Override
    public int getItemCount() {
        return  recentlyViewArrayList == null ? 0 : recentlyViewArrayList.size();
    }

    public class RecentlyViewHolder extends RecyclerView.ViewHolder {
        ItemRecentlyViewedBinding itemRecentlyViewedBinding;
        public RecentlyViewHolder(@NonNull @NotNull ItemRecentlyViewedBinding itemRecentlyViewedBinding) {
            super(itemRecentlyViewedBinding.getRoot());
            this.itemRecentlyViewedBinding = itemRecentlyViewedBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }
}
