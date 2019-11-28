package com.curryoutseller.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryoutseller.NewOrderFragment;
import com.curryoutseller.R;
import com.curryoutseller.model.OrderDisplayModel;

import java.util.ArrayList;

public class OrderDisplayAdapter extends RecyclerView.Adapter<OrderDisplayAdapter.ViewHolder> {

    ArrayList<OrderDisplayModel> listdata;
    OrderDisplayModel orderListData;
    Context context;

    public OrderDisplayAdapter(Context context,ArrayList<OrderDisplayModel> listdata){
        this.context = context;
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public OrderDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.my_order_recycler, parent, false);
        OrderDisplayAdapter.ViewHolder viewHolder = new OrderDisplayAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        orderListData = listdata.get(position);
        String dateBreak = orderListData.getOrder_datetime();
        String db[] = dateBreak.split(" ");
        Log.e("Test",db[0]);
        holder.txtOrder.setText(orderListData.getOrderID());
        //holder.txtDuration.setText(orderListData.getOrder_datetime());
        holder.txtDuration.setText(db[0]);
        holder.txt_FoodStatus.setText(orderListData.getOrder_status());
        holder.txtShowDuration.setText(db[1]);
        final String oid = orderListData.getOrderID();
        final String duration = orderListData.getOrder_datetime();
        holder.ln_OrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Testing", Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new NewOrderFragment(oid,duration);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDuration;
        public TextView txtOrder;
        public TextView txt_FoodStatus,txtShowDuration;
        LinearLayout ln_OrderId;

        public ViewHolder(View itemView) {
            super(itemView);

            this.txtDuration =  (TextView) itemView.findViewById(R.id.txtDuration);
            this.txtOrder = (TextView) itemView.findViewById(R.id.txtOrder);
            this.txt_FoodStatus = (TextView) itemView.findViewById(R.id.txt_FoodStatus);
            this.txtShowDuration = (TextView) itemView.findViewById(R.id.txtShowDuration);
            this.ln_OrderId = itemView.findViewById(R.id.ln_OrderId);
        }
    }


}
