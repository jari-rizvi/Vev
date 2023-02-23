package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.cart.Datum;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemCartBinding;
import com.teamx.vevae.onClick.OnCartClickLister;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


    private Context context;
    private ArrayList<Datum> cartArrayList;
    private OnCartClickLister onCartClickLister;

    public CartAdapter(Context context, ArrayList<Datum> cartArrayList, OnCartClickLister onCartClickLister) {
        this.context = context;
        this.cartArrayList = cartArrayList;
        this.onCartClickLister = onCartClickLister;
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCartBinding itemCartBinding = ItemCartBinding.inflate(inflater,parent,false);
        return new CartViewHolder(itemCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position) {

        Datum cart = cartArrayList.get(position);
        cart.setTotalAmount(cart.getPrice() * cart.getQuantity());

        holder.itemCartBinding.productName.setText(cart.getName());
        holder.itemCartBinding.productModifier.setText(cart.getModifier1()+", "+cart.getModifier2());
        holder.itemCartBinding.productPrice.setText(cart.getTotalAmount()+" AED");
        holder.itemCartBinding.productQuantity.setText(cart.getQuantity()+"");


        Picasso.get().load(AppConstants.imagePath(cart.getProductImages())).into(holder.itemCartBinding.productPicture);

    }

    @Override
    public int getItemCount() {
        return  cartArrayList == null ? 0 : cartArrayList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding itemCartBinding;
        public CartViewHolder(@NonNull @NotNull ItemCartBinding itemCartBinding) {
            super(itemCartBinding.getRoot());

            this.itemCartBinding= itemCartBinding;

            itemCartBinding.addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartClickLister.addProductClick(getAdapterPosition(),itemCartBinding.productQuantity,itemCartBinding.productPrice);
                }
            });


            itemCartBinding.subProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartClickLister.subProductClick(getAdapterPosition(),itemCartBinding.productQuantity,itemCartBinding.productPrice);

                }
            });

            itemCartBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartClickLister.onDeleteProductClick(getAdapterPosition());

                }
            });


        }
    }
}
