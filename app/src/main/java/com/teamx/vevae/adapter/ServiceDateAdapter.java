package com.teamx.vevae.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.services.ServicesDate;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemServiceDateBinding;

import com.teamx.vevae.onClick.OnSearchResultClickListener;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ServiceDateAdapter extends RecyclerView.Adapter<ServiceDateAdapter.ServiceDateViewHolder> {

    private Context context;
    private ArrayList<ServicesDate> servicesDateArrayList;
    private OnSearchResultClickListener onSearchResultClickListener;

    int row_index = -1;


    public ServiceDateAdapter(Context context, ArrayList<ServicesDate> servicesDateArrayList, OnSearchResultClickListener onSearchResultClickListener) {
        this.context = context;
        this.servicesDateArrayList = servicesDateArrayList;
        this.onSearchResultClickListener = onSearchResultClickListener;
    }

    @NonNull
    @Override
    public ServiceDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemServiceDateBinding itemServiceDateBinding = ItemServiceDateBinding.inflate(layoutInflater, parent, false);
        return new ServiceDateViewHolder(itemServiceDateBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ServiceDateViewHolder holder, int position) {

        ServicesDate servicesDate = servicesDateArrayList.get(position);

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = (Date)formatter.parse(servicesDate.getServicesDate());
            DateFormat dateFormat = new SimpleDateFormat("dd");
            String mydate = dateFormat.format(date);
            holder.itemServiceDateBinding.date.setText(mydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.itemServiceDateBinding.day.setText(getWeekDayName(servicesDate.getServicesDate()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                AppConstants.Current_Item = servicesDateArrayList.get(position);
                onSearchResultClickListener.OnSearchedItemClick(position);
                notifyDataSetChanged();

            }
        });

        if (row_index == position) {
            holder.itemServiceDateBinding.layoutBackground.setBackground(ContextCompat.getDrawable(context, R.drawable.date_filled_background));
            holder.itemServiceDateBinding.date.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.itemServiceDateBinding.day.setTextColor(ContextCompat.getColor(context, R.color.white));

        } else {
            holder.itemServiceDateBinding.layoutBackground.setBackground(ContextCompat.getDrawable(context, R.drawable.date_background_boarder));
            holder.itemServiceDateBinding.date.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.itemServiceDateBinding.day.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }

    }


    @Override
    public int getItemCount() {
        return servicesDateArrayList == null ? 0 : servicesDateArrayList.size();
    }


    public class ServiceDateViewHolder extends RecyclerView.ViewHolder {
        ItemServiceDateBinding itemServiceDateBinding;

        public ServiceDateViewHolder(@NonNull ItemServiceDateBinding itemServiceDateBinding) {
            super(itemServiceDateBinding.getRoot());
            this.itemServiceDateBinding = itemServiceDateBinding;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getWeekDayName(String s) {
        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("u-M-d", Locale.ENGLISH);
        return LocalDate.parse(s, dtfInput).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }
}
