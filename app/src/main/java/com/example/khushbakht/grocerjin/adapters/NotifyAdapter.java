package com.example.khushbakht.grocerjin.adapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.listeners.OnItemClickListener;
import com.example.khushbakht.grocerjin.sugarDB.InboxTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by khush on 11/27/2017.
 */


public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    List<InboxTable> inboxTableList;
    public OnItemClickListener onItemClickListener = null;




    Context context;

    public NotifyAdapter(List<InboxTable> inboxTableList, Context context) {

        this.inboxTableList = inboxTableList;
        this.context = context;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvNotifyMessage, tvNotifyTime;
        LinearLayout notifyCard;


        public MyViewHolder(final View view)

        {
            super(view);

            tvNotifyMessage = (TextView) view.findViewById(R.id.notify_message);
            tvNotifyTime = (TextView) view.findViewById(R.id.notify_time);
            notifyCard = (LinearLayout) view.findViewById(R.id.notifyCard);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getOnItemClickListener()!=null)
                    {
                        getOnItemClickListener().onItemClick(itemView,getLayoutPosition());
                    }
                }
            });

        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_row, parent, false);
        MyViewHolder myviewholder = new MyViewHolder(itemview);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvNotifyMessage.setText(inboxTableList.get(position).getNotifyMesssage());
        String date=inboxTableList.get(position).getNotifyTime();
        SimpleDateFormat spf=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");
        if (newDate!= null) {
            date = spf.format(newDate);
        }
        holder.tvNotifyTime.setText(date);

        if (!inboxTableList.get(position).isReadFlag())
        {
            holder.tvNotifyMessage.setTypeface(holder.tvNotifyMessage.getTypeface(), Typeface.BOLD);
            holder.tvNotifyTime.setTypeface(holder.tvNotifyTime.getTypeface(), Typeface.BOLD);
        }
        else
        {
            holder.tvNotifyMessage.setTypeface(holder.tvNotifyMessage.getTypeface(), Typeface.NORMAL);
            holder.tvNotifyTime.setTypeface(holder.tvNotifyTime.getTypeface(), Typeface.NORMAL);
        }

    }


    @Override
    public int getItemCount() {

        return inboxTableList.size();
    }
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}

