package com.teamx.vevae.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.teamx.vevae.Models.notification.Notification;
import com.teamx.vevae.Models.updatednotification.Datum;
import com.teamx.vevae.Models.updatednotification.Notifications;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.Utils.CustomTypefaceSpan;
import com.teamx.vevae.databinding.ItemNotificationBinding;
import com.teamx.vevae.onClick.OnOrderClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NoticationViewHolder> {

    private Context context;
    private ArrayList<Datum> notificationArrayList;
    private OnOrderClickListener onOrderClickListener;
    private  List<String> wordCollectionList;

    public NotificationAdapter(Context context, ArrayList<Datum> notificationArrayList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public NoticationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemNotificationBinding itemNotificationBinding = ItemNotificationBinding.inflate(layoutInflater,parent,false);
        return new NoticationViewHolder(itemNotificationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NoticationViewHolder holder, int position) {

        Datum notification = notificationArrayList.get(position);
        Picasso.get().load(AppConstants.imagePath(notification.getDisplayImage())).into(holder.itemNotificationBinding.notificationImage);

        holder.itemNotificationBinding.notificationDescription.setText(linkifyHashtags(notification.getMessage()));

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat f1 = new SimpleDateFormat("dd/MM/yyyy");
        String d = f.format(date);
        String d1 = f1.format(date);

        holder.itemNotificationBinding.notificationTime.setText(d1);


//            boldText(holder.itemNotificationBinding.notificationContent,notifications.getNotificationContent());

           /* if (recent.getBoldWords().contains(notificationFullMessage)){

                holder.itemNotificationBinding.notificationContent.setText(notificationFullMessage);
            }*/
        wordCollectionList = Arrays.asList(notification.getBoldWords().split(","));

        final SpannableStringBuilder sb = new SpannableStringBuilder(notification.getMessage());
        Typeface CUSTOM_TYPEFACE = ResourcesCompat.getFont(context, R.font.neusa_next_medium);


        for (int q=0; q < wordCollectionList.size(); q++){
            if (notification.getMessage().contains(wordCollectionList.get(q))) {

                int start = notification.getMessage().indexOf(wordCollectionList.get(q));
                int end = notification.getMessage().indexOf(wordCollectionList.get(q)) + wordCollectionList.get(q).length();

                sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new CustomTypefaceSpan("",CUSTOM_TYPEFACE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.itemNotificationBinding.notificationDescription.setText(sb);

            }
        }





    }

    @Override
    public int getItemCount() {

        return  notificationArrayList == null ? 0 : notificationArrayList.size();
    }

    public class NoticationViewHolder extends RecyclerView.ViewHolder {

        ItemNotificationBinding itemNotificationBinding;
        public NoticationViewHolder(@NonNull @NotNull ItemNotificationBinding itemNotificationBinding) {
            super(itemNotificationBinding.getRoot());
            this.itemNotificationBinding=itemNotificationBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClickListener.onItemClickListener(getAdapterPosition());
                }
            });

        }
    }

    public CharSequence linkifyHashtags(String text) {
        SpannableStringBuilder linkifiedText = new SpannableStringBuilder(text);
        Pattern pattern = Pattern.compile("@\\w");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String hashtag = text.substring(start, end);
            ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
            linkifiedText.setSpan(span, 0, hashtag.length(), 0);
        }
        return linkifiedText;
    }
}
