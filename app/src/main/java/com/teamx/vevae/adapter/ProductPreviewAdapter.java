package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.preview.ProductImage;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemProductPreviewBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductPreviewAdapter extends RecyclerView.Adapter<ProductPreviewAdapter.ProductPreviewViewHolder> {

    private Context context;
    private ArrayList<ProductImage> arrayList;

    public ProductPreviewAdapter(Context context, ArrayList<ProductImage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }



    @NonNull
    @NotNull
    @Override
    public ProductPreviewViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemProductPreviewBinding itemProductPreviewBinding = ItemProductPreviewBinding.inflate(layoutInflater,parent,false);
        return new ProductPreviewViewHolder(itemProductPreviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductPreviewViewHolder holder, int position) {
//        holder.itemProductPreviewBinding.imageView.setImageResource(arrayList.get(position));

        ProductImage productImage = arrayList.get(position);
        Picasso.get().load(AppConstants.imagePath(productImage.getProductImages())).into(holder.itemProductPreviewBinding.imageView);
    }

    @Override
    public int getItemCount() {
        return  arrayList == null ? 0 : arrayList.size();
    }

    public class ProductPreviewViewHolder extends RecyclerView.ViewHolder {
        ItemProductPreviewBinding itemProductPreviewBinding;
        public ProductPreviewViewHolder(@NonNull @NotNull ItemProductPreviewBinding  itemProductPreviewBinding) {
            super( itemProductPreviewBinding.getRoot());

            this. itemProductPreviewBinding= itemProductPreviewBinding;
        }
    }
}
