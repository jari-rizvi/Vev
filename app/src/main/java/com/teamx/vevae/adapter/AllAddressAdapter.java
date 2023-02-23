package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.App.App;
import com.teamx.vevae.Models.address.Address;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.teamx.vevae.databinding.ItemAllAddressBinding;
import com.teamx.vevae.onClick.OnAddressListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllAddressAdapter extends RecyclerView.Adapter<AllAddressAdapter.AddressViewHolder> {
    
    private Context context;
    private ArrayList<Address> showAllAddressArrayList;
    private OnAddressListener onAddressListener;

    int row_index;

    public AllAddressAdapter(Context context, ArrayList<Address> showAllAddressArrayList, OnAddressListener onAddressListener) {
        this.context = context;
        this.showAllAddressArrayList = showAllAddressArrayList;
        this.onAddressListener = onAddressListener;
    }

    @NonNull
    @NotNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemAllAddressBinding itemAllAddressBinding = ItemAllAddressBinding.inflate(inflater,parent,false);
        return new AddressViewHolder(itemAllAddressBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddressViewHolder holder, int position) {
        Address  address =showAllAddressArrayList.get(position);
        holder.itemAllAddressBinding.txtdeliveryAddressHouse.setText(address.getHouseNo()+", "+address.getStreetNo());
        holder.itemAllAddressBinding.txtdeliveryAddressCityProvince.setText(address.getCity()+", "+address.getState());
        holder.itemAllAddressBinding.txtdeliveryAddressCityCountry.setText(address.getCountry());

        if (address.getIsdefault()==1){
            holder.itemAllAddressBinding.defaultAddress.setChecked(true);

        }else {
            holder.itemAllAddressBinding.defaultAddress.setChecked(false);
        }

        holder.itemAllAddressBinding.defaultAddress.setOnClickListener(v -> {
            if (position == row_index) {
                holder.itemAllAddressBinding.defaultAddress.setChecked(false);
                row_index = -1;
            } else {
                row_index = position;
                notifyDataSetChanged();
            }

            onAddressListener.onDefaultClick(position);
        });

        if (row_index == position) {
            holder.itemAllAddressBinding.defaultAddress.setChecked(true);
            holder.itemAllAddressBinding.defaultAddress.setText(context.getString(R.string.default_));

        } else {
            holder.itemAllAddressBinding.defaultAddress.setChecked(false);
            holder.itemAllAddressBinding.defaultAddress.setText("");
        }


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.getLANGUAGE_ENGLISH())) {
           holder.itemAllAddressBinding.addressBackIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.arabic_setting_back, context.getTheme()));
        }else{
            holder.itemAllAddressBinding.addressBackIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.edit_arrow, context.getTheme()));



        }


    }

    @Override
    public int getItemCount() {

        return  showAllAddressArrayList == null ? 0 : showAllAddressArrayList.size();

    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        ItemAllAddressBinding itemAllAddressBinding;

        public AddressViewHolder(@NonNull @NotNull ItemAllAddressBinding itemAllAddressBinding ) {
            super(itemAllAddressBinding.getRoot());

            this.itemAllAddressBinding=itemAllAddressBinding;

            itemAllAddressBinding.defaultAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onAddressListener.onDefaultClick(getAdapterPosition());
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onAddressListener.onAddressClick(getAdapterPosition());
                }
            });

        }
    }
}
