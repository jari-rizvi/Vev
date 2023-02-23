package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.checkout.CartItem;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemCartBinding;
import com.teamx.vevae.databinding.ItemCheckOutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.CheckoutViewHolder> {


    private Context context;
    private ArrayList<CartItem> cartArrayList;

    public CheckoutProductAdapter(Context context, ArrayList<CartItem> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCheckOutBinding itemCartBinding = ItemCheckOutBinding.inflate(inflater,parent,false);
        return new CheckoutViewHolder(itemCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CheckoutViewHolder holder, int position) {

        CartItem cart = cartArrayList.get(position);
        holder.itemCartBinding.productName.setText(cart.getName());
        holder.itemCartBinding.productModifier.setText(cart.getModifier1()+", "+cart.getModifier2());


        int productAmount = cart.getPrice()*cart.getQuantity();
        cart.setSingleItemTotalAmount(productAmount);

        holder.itemCartBinding.productPrice.setText(productAmount+" AED");

        Picasso.get().load(AppConstants.imagePath(cart.getProductImages())).into(holder.itemCartBinding.productPicture);

    }

    @Override
    public int getItemCount() {

        return  cartArrayList == null ? 0 : cartArrayList.size();
    }



    public class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ItemCheckOutBinding itemCartBinding;
        public CheckoutViewHolder(@NonNull @NotNull ItemCheckOutBinding itemCartBinding) {
            super(itemCartBinding.getRoot());

            this.itemCartBinding= itemCartBinding;

        }
    }
}
