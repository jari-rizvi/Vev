package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.search.Datum;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemSearchResultBinding;
import com.teamx.vevae.onClick.OnSearchResultClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class SearchResultAdapter extends RecyclerView.Adapter <SearchResultAdapter.SearchResultViewHolder>{

    private Context context;
    private ArrayList<Datum> searchResultArrayList;
    private OnSearchResultClickListener onSearchResultClickListener;

    public SearchResultAdapter(Context context, ArrayList<Datum> searchResultArrayList, OnSearchResultClickListener onSearchResultClickListener) {
        this.context = context;
        this.searchResultArrayList = searchResultArrayList;
        this.onSearchResultClickListener = onSearchResultClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemSearchResultBinding itemSearchResultBinding = ItemSearchResultBinding.inflate(inflater,parent,false);
        return new SearchResultViewHolder(itemSearchResultBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchResultViewHolder holder, int position) {
        Datum searchResult = searchResultArrayList.get(position);
        holder.itemSearchResultBinding.txtname.setText(searchResult.getName());
       /* holder.itemSearchResultBinding.txtaddress.setText(searchResult.getAddress());
        holder.itemSearchResultBinding.txtDistance.setText(searchResult.getDistance());*/
        Picasso.get().load(AppConstants.imagePath(searchResult.getImage())).into(holder.itemSearchResultBinding.image);
        holder.itemSearchResultBinding.txtaddress.setText(searchResult.getAddress());

        //if search item is Store (product/ service)
        if (searchResult.getType().equalsIgnoreCase("product") ||  searchResult.getType().equalsIgnoreCase("services")){

            holder.itemSearchResultBinding.rating.setVisibility(View.VISIBLE);
            holder.itemSearchResultBinding.rating.setRating(searchResult.getRatings());
            String strDouble = String.format(Locale.getDefault(), "%.2f", Double.parseDouble(searchResult.getDistance()));
            holder.itemSearchResultBinding.txtDistance.setText(strDouble+" km");

        }else {
            holder.itemSearchResultBinding.rating.setVisibility(View.GONE);

            if (searchResult.getType().equalsIgnoreCase("storeproduct")){
                holder.itemSearchResultBinding.txtDistance.setText(searchResult.getPrice()+" AED");
            }

           else if (searchResult.getType().equalsIgnoreCase("storeservices")){
                holder.itemSearchResultBinding.txtDistance.setVisibility(View.GONE);

            }

        }







    }

    @Override
    public int getItemCount() {
        return  searchResultArrayList == null ? 0 : searchResultArrayList.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        ItemSearchResultBinding itemSearchResultBinding;
        public SearchResultViewHolder(@NonNull @NotNull ItemSearchResultBinding itemSearchResultBinding) {
            super(itemSearchResultBinding.getRoot());
            this.itemSearchResultBinding=itemSearchResultBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSearchResultClickListener.OnSearchedItemClick(getBindingAdapterPosition());
                }
            });
        }
    }
}
