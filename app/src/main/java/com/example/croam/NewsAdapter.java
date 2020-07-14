package com.example.croam;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {


    private List<ReportModel> mReportModelList;
    private Context mContext;
    private OnItemListener mOnItemListener;

    public NewsAdapter(List<ReportModel> modelList, Context context,
            OnItemListener mOnItemListener) {
        mReportModelList = modelList;
        mContext = context;
        this.mOnItemListener = mOnItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        // Return a new view holder
        return new MyViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(mReportModelList.get(position), mContext);
    }

    // View holder class whose objects represent each list item

    @Override
    public int getItemCount() {
        return mReportModelList.size();
    }


    public interface OnItemListener {
        void OnBookClick(int position);

        void OnBookLongClick(int position, View view);

        void OnButton1Click(int position, View view);

        void OnButton2Click(int position, View view);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        public ImageView cardImageView;
        public TextView titleTextView;
        public TextView subTitleTextView;
        OnItemListener onItemListener;
        Button Action1;
        Button Action2;

        public MyViewHolder(@NonNull final View itemView, final OnItemListener onItemListener) {
            super(itemView);
            Action1 = itemView.findViewById(R.id.action_button_1);
            Action2 = itemView.findViewById(R.id.action_button_2);
            this.onItemListener = onItemListener;
            cardImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.card_title);
            subTitleTextView = itemView.findViewById(R.id.card_subtitle);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            Action1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.OnButton1Click(getAdapterPosition(), itemView);
                }
            });
            Action2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemListener.OnButton2Click(getAdapterPosition(), itemView);
                }
            });


        }


        public void bindData(final ReportModel bookDataModel, Context context) {
//            titleTextView.setText(bookDataModel.getTitle());
//            subTitleTextView.setText(bookDataModel.getAuthor());
        }

        @Override
        public void onClick(View v) {
            onItemListener.OnBookClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onItemListener.OnBookLongClick(getAdapterPosition(), view);
            return true;
        }
    }

}


