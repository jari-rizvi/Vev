package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.teamx.vevae.DummyModel.PaymentMethod;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemPaymentMethodBinding;
import com.teamx.vevae.onClick.OnPaymentMethodClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.PaymentViewholder> {

    private Context context;
    private ArrayList<PaymentMethod> paymentMethodArrayList;
    private OnPaymentMethodClickListener onPaymentMethodClickListener;

    int row_index = -1;

    public PaymentMethodAdapter(Context context, ArrayList<PaymentMethod> paymentMethodArrayList, OnPaymentMethodClickListener onPaymentMethodClickListener) {
        this.context = context;
        this.paymentMethodArrayList = paymentMethodArrayList;
        this.onPaymentMethodClickListener = onPaymentMethodClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public PaymentViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemPaymentMethodBinding itemPaymentMethodBinding = ItemPaymentMethodBinding.inflate(inflater,parent,false);
        return new PaymentViewholder(itemPaymentMethodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PaymentViewholder holder, int position) {

        PaymentMethod paymentMethod = paymentMethodArrayList.get(position);
        holder.itemPaymentMethodBinding.paymentName.setText(paymentMethod.getPaymentName());
        holder.itemPaymentMethodBinding.paymentImage.setImageResource(paymentMethod.getPaymentImage());
        holder.itemPaymentMethodBinding.paymentType.setChecked(row_index == position);


        String  method = Prefs.getString("paymentMethod",null);
        if (method != null){
            if (method.equalsIgnoreCase(context.getString(R.string.debit_credit))){
                row_index = 0;
                Prefs.putString("paymentMethod",context.getString(R.string.debit_credit));
            }else{
                row_index = 1;
                Prefs.putString("paymentMethod",context.getString(R.string.cash));

            }
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                AppConstants.SELECTED_METHOD = paymentMethodArrayList.get(position);
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

        return  paymentMethodArrayList == null ? 0 : paymentMethodArrayList.size();
    }

    public class PaymentViewholder extends RecyclerView.ViewHolder {
        ItemPaymentMethodBinding itemPaymentMethodBinding;

        public PaymentViewholder(@NonNull @NotNull ItemPaymentMethodBinding itemPaymentMethodBinding) {
            super(itemPaymentMethodBinding.getRoot());

            this.itemPaymentMethodBinding = itemPaymentMethodBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPaymentMethodClickListener.onPaymentClick(getAdapterPosition());
                }
            });
        }
    }
}
