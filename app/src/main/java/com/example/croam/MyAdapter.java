package com.example.croam;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private List<File> mediaList;
    public Context mContext;
    private OnItemListener mOnItemListener;

    public MyAdapter(List<File> modelList, Context context, OnItemListener mOnItemListener) {
        mediaList = modelList;
        mContext = context;
        this.mOnItemListener = mOnItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // Return a new view holder
        return new MyViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(mediaList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    // View holder class whose objects represent each list item

    public interface OnItemListener {
        void onLinkClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView linkTextView;
        public ImageView image;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull final View itemView, final OnItemListener onItemListener) {
            super(itemView);
            this.onItemListener = onItemListener;
            linkTextView = itemView.findViewById(R.id.text_view);
            image=itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);

        }


        public void bindData(final File file, Context context) {
            linkTextView.setText(file.getName());
            if(file.getName().endsWith(".mp4")){
                Picasso.get().load(R.drawable.play_button).resize(100,100).centerCrop().into(image);
            }else{
                Picasso.get().load(file).resize(100,100).centerCrop().into(image);
            }

        }

        @Override
        public void onClick(View v) {
            onItemListener.onLinkClick(getAdapterPosition());
        }
    }

}


