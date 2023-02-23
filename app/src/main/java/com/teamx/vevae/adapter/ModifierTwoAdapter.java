package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.preview.Modifiers2;
import com.teamx.vevae.R;
import com.teamx.vevae.databinding.ItemModifierBinding;
import com.teamx.vevae.onClick.OnModifierTwoClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ModifierTwoAdapter extends RecyclerView.Adapter<ModifierTwoAdapter.ModifierTwoViewHolder> {

    private Context context;
    private ArrayList<Modifiers2> modifierOneArrayList;
    private OnModifierTwoClickListener onModifierTwoClickListener;

    int row_index=-1;


    public ModifierTwoAdapter(Context context, ArrayList<Modifiers2> modifierOneArrayList, OnModifierTwoClickListener onModifierTwoClickListener) {
        this.context = context;
        this.modifierOneArrayList = modifierOneArrayList;
        this.onModifierTwoClickListener = onModifierTwoClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ModifierTwoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemModifierBinding itemModifierBinding = ItemModifierBinding.inflate(layoutInflater,parent,false);
        return new ModifierTwoViewHolder(itemModifierBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ModifierTwoViewHolder holder, int position) {
        Modifiers2 modifierOne = modifierOneArrayList.get(position);
        holder.itemModifierBinding.modifier.setText(modifierOne.getValue());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                AppConstants.SELECTED_MODIFIER_2 = modifierOneArrayList.get(position);
                onModifierTwoClickListener.onClickModifierTwo(position);
                notifyDataSetChanged();
            }
        });*/

        holder.itemModifierBinding.modifier.setOnClickListener(v -> {
            if (position == row_index) {
                holder.itemModifierBinding.modifier.setChecked(false);
                row_index = -1;
            } else {
                row_index = position;
                notifyDataSetChanged();
            }

            onModifierTwoClickListener.onClickModifierTwo(position);
        });

        if (row_index == position) {
            holder.itemModifierBinding.modifier.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

        } else {
            holder.itemModifierBinding.modifier.setTextColor(ContextCompat.getColor(context,R.color.secondaryText));
        }


    }

    @Override
    public int getItemCount() {
        return  modifierOneArrayList == null ? 0 : modifierOneArrayList.size();
    }

    public class ModifierTwoViewHolder extends RecyclerView.ViewHolder {
        ItemModifierBinding itemModifierBinding;
        public ModifierTwoViewHolder(@NonNull @NotNull ItemModifierBinding itemModifierBinding) {
            super(itemModifierBinding.getRoot());
            this.itemModifierBinding=itemModifierBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onModifierTwoClickListener.onClickModifierTwo(getAdapterPosition());
                }
            });

        }
    }
}
