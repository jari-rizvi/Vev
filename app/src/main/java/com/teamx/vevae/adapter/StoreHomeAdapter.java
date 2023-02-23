package com.teamx.vevae.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.teamx.vevae.Models.storeprofile.Category;
import com.teamx.vevae.Models.storeprofile.Listbycategory;
import com.teamx.vevae.R;
import com.teamx.vevae.databinding.ItemHomeStoreBinding;
import com.teamx.vevae.onClick.OnOrderClickListener;
import com.teamx.vevae.onClick.OnProductClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StoreHomeAdapter extends RecyclerView.Adapter<StoreHomeAdapter.HomeViewHolder> implements OnProductClickListener {

    private Context context;
    private ArrayList<Category> storeHomeArrayList;
    private OnOrderClickListener onOrderClickListener;
    private ArrayList<Listbycategory> listbycategories = new ArrayList<>();
    private Category category;

    public StoreHomeAdapter(Context context, ArrayList<Category> storeHomeArrayList, OnOrderClickListener onOrderClickListener) {

        this.context = context;
        this.storeHomeArrayList = storeHomeArrayList;
        this.onOrderClickListener = onOrderClickListener;

    }


    @NonNull
    @NotNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemHomeStoreBinding itemHomeStoreBinding = ItemHomeStoreBinding.inflate(inflater,parent,false);
        return new HomeViewHolder(itemHomeStoreBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeViewHolder holder, int position) {

        category  = storeHomeArrayList.get(position);
        holder.itemHomeStoreBinding.categoryName.setText(category.getCategoriesName());

        listbycategories = category.getListbycategory();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        holder.itemHomeStoreBinding.itemRecyclerview.setLayoutManager(layoutManager);

        if (listbycategories != null){
            CategoryAdapter categoryAdapter = new CategoryAdapter(context, category ,this);
            holder.itemHomeStoreBinding.itemRecyclerview.setAdapter( categoryAdapter);
        }




    }

    @Override
    public int getItemCount() {

        return  storeHomeArrayList == null ? 0 : storeHomeArrayList.size();
    }

    @Override
    public void OnProductClick(Category category, int postion) {

        int productId = category.getListbycategory().get(postion).getId();
        Bundle bundle = new Bundle();
        bundle.putInt("id",productId);

        NavOptions.Builder navBuilder =  new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_out);

        if (category.getListbycategory().get(postion).getStoreType().equalsIgnoreCase("services")){
            NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_Fragmnet);
            navController.navigate(R.id.servicesFragment, bundle,navBuilder.build());
        }else {
            NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_Fragmnet);
            navController.navigate(R.id.productPreviewFragment, bundle,navBuilder.build());
        }

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ItemHomeStoreBinding itemHomeStoreBinding;
        public HomeViewHolder(@NonNull @NotNull ItemHomeStoreBinding itemHomeStoreBinding) {
            super(itemHomeStoreBinding.getRoot());
            this.itemHomeStoreBinding=itemHomeStoreBinding;

            itemHomeStoreBinding.btnSeeAll.setOnClickListener(v -> {
                onOrderClickListener.onItemClickListener(getAdapterPosition());
            });
        }
    }
}
