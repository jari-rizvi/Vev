package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.orderDetails.Orderitem;
import com.teamx.vevae.R;
import com.teamx.vevae.databinding.ItemOrderDetailBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private Context context;
    private ArrayList<Orderitem> orderDetailsArrayList;

    public OrderDetailAdapter(Context context, ArrayList<Orderitem> orderDetailsArrayList) {
        this.context = context;
        this.orderDetailsArrayList = orderDetailsArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemOrderDetailBinding itemOrderDetailBinding  = ItemOrderDetailBinding.inflate(inflater,parent,false);
        return new OrderDetailViewHolder(itemOrderDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderDetailViewHolder holder, int position) {

        Orderitem orderitem = orderDetailsArrayList.get(position);
        holder.itemOrderDetailBinding.txtitemName.setText(orderitem.getName());

        if (orderitem.getModifier1() != null){
            holder.itemOrderDetailBinding.txtItemIdentifier.setVisibility(View.VISIBLE);
            holder.itemOrderDetailBinding.txtItemIdentifier.setText(orderitem.getModifier1()+", "+orderitem.getModifier2());
        }else {
            holder.itemOrderDetailBinding.txtItemIdentifier.setVisibility(View.GONE);
        }


        if (orderitem.getQuantity() != null){
            holder.itemOrderDetailBinding.txtItemQuantity.setVisibility(View.VISIBLE);
            holder.itemOrderDetailBinding.txtItemQuantity.setText(context.getString(R.string.qty)+" "+orderitem.getQuantity()+context.getString(R.string.price)+orderitem.getItemPrice());
        }else {
            holder.itemOrderDetailBinding.txtItemQuantity.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return  orderDetailsArrayList == null ? 0 : orderDetailsArrayList.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ItemOrderDetailBinding itemOrderDetailBinding;
        public OrderDetailViewHolder(@NonNull @NotNull ItemOrderDetailBinding itemOrderDetailBinding) {
            super(itemOrderDetailBinding.getRoot());
            this.itemOrderDetailBinding = itemOrderDetailBinding;
        }
    }
}
