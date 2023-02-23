package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.orderList.Datum;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.databinding.ItemOrderListBinding;
import com.teamx.vevae.onClick.OnOrderClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private Context context;
    private ArrayList<Datum> orderListArrayList;
    private OnOrderClickListener onOrderClickListener;

    public OrderListAdapter(Context context, ArrayList<Datum> orderListArrayList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orderListArrayList = orderListArrayList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemOrderListBinding itemOrderListBinding = ItemOrderListBinding.inflate(inflater,parent,false);
        return new OrderListViewHolder(itemOrderListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderListViewHolder holder, int position) {

        Datum orderList = orderListArrayList.get(position);
        holder.itemOrderListBinding.orderId.setText("id #"+orderList.getOrderCode());
        holder.itemOrderListBinding.orderAmount.setText(orderList.getPrice()+" AED");

        String orderDate =  orderList.getOrderDate();
        String[] newOrderDate = orderDate.split(" ");
        holder.itemOrderListBinding.orderDate.setText(newOrderDate[0]);

      /*  Date date = new Date();
        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {
            SimpleDateFormat arDate = new SimpleDateFormat("d MMM yyyy", new Locale("ar"));


        }else {
            SimpleDateFormat arDate = new SimpleDateFormat("d MMM yyyy", new Locale("en"));

            holder.itemOrderListBinding.orderDate.setText(newOrderDate[0]);
        }*/



        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {

            holder.itemOrderListBinding.btnBack.setImageDrawable(context.getResources().getDrawable(R.drawable.back, context.getTheme()));

        }else{
            holder.itemOrderListBinding.btnBack.setImageDrawable(context.getResources().getDrawable(R.drawable.orderback, context.getTheme()));

        }


    }

    @Override
    public int getItemCount() {
        return  orderListArrayList == null ? 0 : orderListArrayList.size();
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        ItemOrderListBinding itemOrderListBinding;
        public OrderListViewHolder(@NonNull ItemOrderListBinding itemOrderListBinding ) {
            super(itemOrderListBinding.getRoot());

            this.itemOrderListBinding=itemOrderListBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }
}
