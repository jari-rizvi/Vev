package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.storeprofile.Category;
import com.teamx.vevae.Models.storeprofile.Listbycategory;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemCategoryBinding;
import com.teamx.vevae.onClick.OnProductClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatgeoryViewHolder> {

    private Context context;
    private Category category;
    private OnProductClickListener onProductClickListener;

    public CategoryAdapter(Context context, Category category, OnProductClickListener onProductClickListener) {
        this.context = context;
        this.category = category;
        this.onProductClickListener = onProductClickListener;
    }


    @NonNull
    @NotNull
    @Override
    public CatgeoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding.inflate(inflater,parent,false);
        return new CatgeoryViewHolder(itemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CatgeoryViewHolder holder, int position) {


        Listbycategory product = category.getListbycategory().get(position);
        if (product.getProductImages() != null){
            Picasso.get().load(AppConstants.imagePath(product.getProductImages())).into(holder.itemCategoryBinding.image);
        }else {
            Picasso.get().load(AppConstants.imagePath(product.getServicesImage())).into(holder.itemCategoryBinding.image);
        }

        holder.itemCategoryBinding.txtTitle.setText(product.getName());
        if (product.getPrice() != null){
            holder.itemCategoryBinding.txtPrice.setText("AED "+product.getPrice().toString());
        }else {
            holder.itemCategoryBinding.txtPrice.setText("AED 0");

        }

    }

    @Override
    public int getItemCount() {
        return  category == null ? 0 : category.getListbycategory().size();
    }

    public class CatgeoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding itemCategoryBinding;
        public CatgeoryViewHolder(@NonNull @NotNull ItemCategoryBinding itemCategoryBinding) {
            super(itemCategoryBinding.getRoot());
            this.itemCategoryBinding =itemCategoryBinding;


            itemView.setOnClickListener(v -> {

              onProductClickListener.OnProductClick(category,getAdapterPosition());
            });
        }
    }
}
