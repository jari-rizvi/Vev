package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemCategoryBinding;
import com.teamx.vevae.onClick.OnOrderClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.AllCategoriesViewHolder> {

    private Context context;
    private ArrayList<com.teamx.vevae.Models.AllCategories.Listbycategory> categoriesArrayList;
    private OnOrderClickListener onOrderClickListener;

    public AllCategoriesAdapter(Context context, ArrayList<com.teamx.vevae.Models.AllCategories.Listbycategory> categoriesArrayList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.categoriesArrayList = categoriesArrayList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public AllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding.inflate(inflater,parent,false);
        return new AllCategoriesViewHolder(itemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoriesViewHolder holder, int position) {
        com.teamx.vevae.Models.AllCategories.Listbycategory product = categoriesArrayList.get(position);
        Picasso.get().load(AppConstants.imagePath(product.getProductImages())).into(holder.itemCategoryBinding.image);
        holder.itemCategoryBinding.txtTitle.setText(product.getName());
        if (product.getPrice() != null){

            holder.itemCategoryBinding.txtPrice.setText("AED "+product.getPrice().toString());
        }else {
            holder.itemCategoryBinding.txtPrice.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return  categoriesArrayList == null ? 0 : categoriesArrayList.size();
    }

    public class AllCategoriesViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding itemCategoryBinding;
        public AllCategoriesViewHolder(@NonNull   ItemCategoryBinding itemCategoryBinding) {
            super(itemCategoryBinding.getRoot());
            this.itemCategoryBinding =itemCategoryBinding;

            itemView.setOnClickListener(v -> {
                onOrderClickListener.onItemClickListener(getAdapterPosition());
            });
        }
    }

}
