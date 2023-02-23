package com.teamx.vevae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamx.vevae.Models.storeprofile.Rating;
import com.teamx.vevae.R;
import com.teamx.vevae.Utils.AppConstants;
import com.teamx.vevae.databinding.ItemReviewBinding;
import com.teamx.vevae.onClick.OnOrderClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private ArrayList<Rating> reviewArrayList;
    private OnOrderClickListener onOrderClickListener;

    public ReviewAdapter(Context context, ArrayList<Rating> reviewArrayList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemReviewBinding itemReviewBinding = ItemReviewBinding.inflate(layoutInflater,parent,false);
        return new ReviewViewHolder(itemReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReviewViewHolder holder, int position) {
        Rating review = reviewArrayList.get(position);
        holder.itemReviewBinding.rating.setRating(Float.parseFloat(review.getRating()));
        holder.itemReviewBinding.productName.setText(review.getUserName());
        holder.itemReviewBinding.productDescription.setText(review.getComments());
        Picasso.get().load(AppConstants.imagePath(review.getUserImage())).placeholder(R.drawable.icon_placeholder).into(holder.itemReviewBinding.productPicture);

//
//        holder.itemReviewBinding.productPicture.setImageResource(review.getThumbnail());


        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = (Date)formatter.parse(review.getCreatedAt());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String mydate = dateFormat.format(date);
            holder.itemReviewBinding.reviewDate.setText(mydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return  reviewArrayList == null ? 0 : reviewArrayList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ItemReviewBinding itemReviewBinding;
        public ReviewViewHolder(@NonNull @NotNull ItemReviewBinding itemReviewBinding) {
            super(itemReviewBinding.getRoot());

            this.itemReviewBinding=itemReviewBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }
}
