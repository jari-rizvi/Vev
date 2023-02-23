package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.services.ServicesDatum;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemServiceTypeBinding;
import com.teamx.vevae.onClick.OnPaymentMethodClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServicesTypeAdaper extends RecyclerView.Adapter<ServicesTypeAdaper.ServiceTypeViewHolder> {

    private Context context;
    private ArrayList<ServicesDatum> serviceTypeArrayList;
    private OnPaymentMethodClickListener onPaymentMethodClickListener;

    int row_index = -1;

    public ServicesTypeAdaper(Context context, ArrayList<ServicesDatum> serviceTypeArrayList, OnPaymentMethodClickListener onPaymentMethodClickListener) {
        this.context = context;
        this.serviceTypeArrayList = serviceTypeArrayList;
        this.onPaymentMethodClickListener = onPaymentMethodClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ServiceTypeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemServiceTypeBinding itemServiceTypeBinding = ItemServiceTypeBinding.inflate(layoutInflater,parent,false);
        return new ServiceTypeViewHolder(itemServiceTypeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceTypeViewHolder holder, int position) {

        ServicesDatum servicesDatum = serviceTypeArrayList.get(position);
        holder.itemServiceTypeBinding.title.setText(servicesDatum.getServicesduration()+" "+servicesDatum.getServiceType());
        holder.itemServiceTypeBinding.amount.setText(servicesDatum.getPrice().toString()+ " AED");
        holder.itemServiceTypeBinding.totalOrder.setText(servicesDatum.getTotalCount().toString());
        holder.itemServiceTypeBinding.serviceType.setChecked(row_index == position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                AppConstants.SELECTED_SERVICE = serviceTypeArrayList.get(position);
                onPaymentMethodClickListener.onPaymentClick(position);
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
//            holder.itemPaymentMethodBinding.methodLayout.setBackgroundResource(R.drawable.other_users_answer_background);

        } else {
//            holder.itemPaymentMethodBinding.methodLayout.setBackgroundResource(R.drawable.card_background_white);

        }
    }

    @Override
    public int getItemCount() {
        return  serviceTypeArrayList == null ? 0 : serviceTypeArrayList.size();
    }

    public class ServiceTypeViewHolder extends RecyclerView.ViewHolder {
        ItemServiceTypeBinding itemServiceTypeBinding;
        public ServiceTypeViewHolder(@NonNull @NotNull ItemServiceTypeBinding itemServiceTypeBinding ) {
            super(itemServiceTypeBinding.getRoot());

            this.itemServiceTypeBinding=itemServiceTypeBinding;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPaymentMethodClickListener.onPaymentClick(getAdapterPosition());
                }
            });
        }
    }
}
