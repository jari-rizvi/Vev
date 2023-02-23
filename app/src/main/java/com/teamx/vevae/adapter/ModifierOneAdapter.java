package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.preview.Modifiers1;
import com.teamx.vevae.R;
import com.teamx.vevae.databinding.ItemModifierBinding;
import com.teamx.vevae.onClick.OnModifierOneClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ModifierOneAdapter extends RecyclerView.Adapter<ModifierOneAdapter.ModifierOneViewHolder> {

    private Context context;
    private ArrayList<Modifiers1> modifierOneArrayList;
    private OnModifierOneClickListener onModifierOneClickListener;

   /* private  ArrayList<Integer> selectedItemArrayList =  new ArrayList<>();
    ArrayList<Integer> row_index = new ArrayList<>();*/

    int row_index = -1;

    public ModifierOneAdapter(Context context, ArrayList<Modifiers1> modifierOneArrayList, OnModifierOneClickListener onModifierOneClickListener) {
        this.context = context;
        this.modifierOneArrayList = modifierOneArrayList;
        this.onModifierOneClickListener = onModifierOneClickListener;
    }



    @NonNull
    @NotNull
    @Override
    public ModifierOneViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemModifierBinding itemModifierBinding = ItemModifierBinding.inflate(layoutInflater,parent,false);
        return new ModifierOneViewHolder(itemModifierBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ModifierOneViewHolder holder, int position) {
        Modifiers1 modifierOne = modifierOneArrayList.get(position);
        holder.itemModifierBinding.modifier.setText(modifierOne.getValue());


       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                AppConstants.SELECTED_MODIFIER_1 = modifierOneArrayList.get(position);
                onModifierOneClickListener.onClickModifierOne(position);
                notifyDataSetChanged();
            }
        });
*/

        holder.itemModifierBinding.modifier.setOnClickListener(v -> {
            if (position == row_index) {
                holder.itemModifierBinding.modifier.setChecked(false);
                row_index = -1;
            } else {
                row_index = position;
                notifyDataSetChanged();
            }

            onModifierOneClickListener.onClickModifierOne(position);
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

    public class ModifierOneViewHolder extends RecyclerView.ViewHolder {
        ItemModifierBinding itemModifierBinding;
        public ModifierOneViewHolder(@NonNull @NotNull ItemModifierBinding itemModifierBinding) {
            super(itemModifierBinding.getRoot());
            this.itemModifierBinding=itemModifierBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onModifierOneClickListener.onClickModifierOne(getAdapterPosition());
                }
            });



        }
    }
}
