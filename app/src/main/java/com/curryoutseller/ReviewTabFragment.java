package com.curryoutseller;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ReviewTabFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.review_tab_fragment, container, false);


        ReviewDataModel[] reviewDataModels= new ReviewDataModel[] {

                new ReviewDataModel("9:30", "10.04.2019", "John Doe", "On CurryOut, you will find food reviews for virtually every state across the U.S. But instead of generic restaurant reviews, you can read about individual dishes and view ratings for each."),
                new ReviewDataModel("9:30", "10.04.2019", "John Doe", "On CurryOut, you will find food reviews for virtually every state across the U.S. But instead of generic restaurant reviews, you can read about individual dishes and view ratings for each."),
                new ReviewDataModel("9:30", "10.04.2019", "John Doe", "On CurryOut, you will find food reviews for virtually every state across the U.S. But instead of generic restaurant reviews, you can read about individual dishes and view ratings for each."),
                new ReviewDataModel("9:30", "10.04.2019", "John Doe", "On CurryOut, you will find food reviews for virtually every state across the U.S. But instead of generic restaurant reviews, you can read about individual dishes and view ratings for each."),
                new ReviewDataModel("9:30", "10.04.2019", "John Doe", "On CurryOut, you will find food reviews for virtually every state across the U.S. But instead of generic restaurant reviews, you can read about individual dishes and view ratings for each."),


        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyView);
        ReviewListAdapter adapter = new ReviewListAdapter(reviewDataModels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        return view;
    }

    private class ReviewDataModel {

        private String time;
        private String date;
        private String name;
        private String comment;

        public ReviewDataModel(String time, String date, String name, String comment) {
            this.time = time;
            this.date = date;
            this.name = name;
            this.comment = comment;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    private class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
        private ReviewDataModel[] listData;

        public ReviewListAdapter(ReviewDataModel[] listData) {
            this.listData = listData;
        }

        @NonNull
        @Override
        public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.review_item_list, parent, false);
            ViewHolder viewHolder= new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewListAdapter.ViewHolder holder, int position) {
            final ReviewDataModel reviewDataModel= listData[position];
            holder.txtTime.setText(listData[position].getTime());
            holder.txtDate.setText(listData[position].getDate());
            holder.txtName.setText(listData[position].getName());
            holder.txtComment.setText(listData[position].getComment());

        }

        @Override
        public int getItemCount() {
            return listData.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txtTime, txtDate, txtName, txtComment, txt_readMore;

            public ViewHolder(View itemView) {
                super(itemView);
                this.txtTime=(TextView)itemView.findViewById(R.id.txt_time);
                this.txtDate=(TextView)itemView.findViewById(R.id.txt_date);
                this.txtName=(TextView)itemView.findViewById(R.id.txt_name);
                this.txtComment=(TextView)itemView.findViewById(R.id.txt_comment);
            }
        }
    }
}
