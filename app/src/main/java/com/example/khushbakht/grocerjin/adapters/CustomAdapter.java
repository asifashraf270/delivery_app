package com.example.khushbakht.grocerjin.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.viewHolders.ItemTouchHelperViewHolder;
import com.example.khushbakht.grocerjin.listeners.OnItemClickListener;
import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.model.OrderList.OrderList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Khushbakht on 7/14/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    // private ArrayList<Data> dataset;
    public List<OrderList> responseList;
    public static String order_no;
    public OnItemClickListener onItemClickListener = null;
    Context context;
    public int rowLayout;

    public List<OrderList> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<OrderList> responseList) {
        this.responseList = responseList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView orderNo;
        TextView dateTime;
        TextView status;
        TextView timeOfDelivery;
        TextView adress;
        CardView cardView;
        Button pick;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.orderNo = (TextView) itemView.findViewById(R.id.orderNo);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.dateTime = (TextView) itemView.findViewById(R.id.DateTime);
            this.pick = (Button) itemView.findViewById(R.id.pickbtn);
            this.timeOfDelivery = (TextView) itemView.findViewById(R.id.timeOfDilevery);
            this.adress = (TextView) itemView.findViewById(R.id.address);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {
                        getOnItemClickListener().onItemClick(itemView, getLayoutPosition());
                    }
                }
            });


            pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {

                        getOnItemClickListener().onPickCick(itemView, getLayoutPosition());

                    }
                }
            });


        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
            itemView.setFocusable(true);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
        // in my view holder
    }

    public CustomAdapter(List<OrderList> responseList, Context context) {
        this.responseList = responseList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        OrderList orderList = responseList.get(position);
        holder.orderNo.setText("Order no: " + orderList.getOrderno());
        holder.status.setText(orderList.orderStatusName);
        String date = orderList.getDateTime();
        if (date != null && date.length() > 0) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            Date newDate = null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("MMM dd, yyyy   hh:mm");
            if (newDate != null) {
                date = spf.format(newDate);
            }
            holder.dateTime.setText(date.toString());

        }
        holder.adress.setText("Address: " + orderList.getAddress());
        order_no = orderList.getOrderno();

        if (orderList.orderStatusName.equalsIgnoreCase("New")) {
            holder.status.setTextColor(Color.rgb(250, 89, 14));
//            holder.orderDetail.setBackgroundColor(Color.rgb(250, 89, 14));
//            holder.orderNo.setTextColor(Color.rgb(250, 89, 14));
//            holder.dateTime.setTextColor(Color.rgb(250, 89, 14));
//            holder.timeOfDelivery.setTextColor(Color.rgb(250, 89, 14));
//            holder.adress.setTextColor(Color.rgb(250, 89, 14));
        } else if (orderList.orderStatusName.equalsIgnoreCase("In Transit")) {
            holder.status.setTextColor(Color.rgb(202, 217, 20));
//            holder.orderDetail.setBackgroundColor(Color.rgb(202, 217, 20));
//            holder.orderNo.setTextColor(Color.rgb(202, 217, 20));
//            holder.dateTime.setTextColor(Color.rgb(202, 217, 20));
//            holder.timeOfDelivery.setTextColor(Color.rgb(202, 217, 20));
//            holder.adress.setTextColor(Color.rgb(202, 217, 20));
//
        }
//
        else if (orderList.orderStatusName.equalsIgnoreCase("Delivered")) {
            holder.status.setTextColor(Color.rgb(34, 134, 12));
//            holder.orderDetail.setBackgroundColor(Color.rgb(34, 134, 12));
//            holder.orderNo.setTextColor(Color.rgb(34, 134, 12));
//            holder.dateTime.setTextColor(Color.rgb(34, 134, 12));
//            holder.timeOfDelivery.setTextColor(Color.rgb(34, 134, 12));
//            holder.adress.setTextColor(Color.rgb(34, 134, 12));
//
        }
//
        else {
            holder.status.setTextColor(Color.rgb(217, 32, 20));
//            holder.orderNo.setTextColor(Color.rgb(217,32,20));
//            holder.dateTime.setTextColor(Color.rgb(217,32,20));
//            holder.timeOfDelivery.setTextColor(Color.rgb(217,32,20));
//            holder.adress.setTextColor(Color.rgb(217,32,20));
//            holder.orderDetail.setBackgroundColor(Color.rgb(217,32,20));
        }


    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(responseList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override

    public void onItemDismiss(int position) {
        responseList.remove(position);
        notifyItemRemoved(position);
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
