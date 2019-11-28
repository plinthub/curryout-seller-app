package com.curryoutseller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curryoutseller.NewOrderFragment;
import com.curryoutseller.R;
import com.curryoutseller.model.OrderDisplayModel;
import com.curryoutseller.model.OrderItemModel;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>  {

    ArrayList<OrderItemModel> listdata;
    OrderItemModel itemModel;
    Context context;

    public OrderItemAdapter(Context context,ArrayList<OrderItemModel> listdata){
        this.context = context;
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.new_order_recycler, parent, false);
        OrderItemAdapter.ViewHolder viewHolder = new OrderItemAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        itemModel = listdata.get(position);
        holder.txtListProduct.setText(itemModel.getProduct_name());
        holder.txtListQty.setText(itemModel.getOrdered_item_quantity());
        holder.txtListPrice.setText(itemModel.getOrdered_item_price());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtListProduct;
        public TextView txtListQty;
        public TextView txtListPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            this.txtListProduct =  (TextView) itemView.findViewById(R.id.txtListProduct);
            this.txtListQty = (TextView) itemView.findViewById(R.id.txtListQty);
            this.txtListPrice = (TextView) itemView.findViewById(R.id.txtListPrice);

        }
    }

}
