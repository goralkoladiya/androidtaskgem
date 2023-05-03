package com.taskgem.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taskgem.Modal.MyListData;
import com.taskgem.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    ArrayList<MyListData> listdata;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<MyListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_transaction, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData myListData = listdata.get(position);

        String dtStart = myListData.getTimestamp();
        holder.date.setText(parseDateToddMMyyyy(dtStart));

        if(myListData.getReason().equals("rewards debited"))
        {
            holder.title.setText("Coin Debited");
            holder.coin.setText("-"+myListData.getRewards());
            if(myListData.getStatus()==0)
            {
                holder.reason.setText("pending");
            }
            else {
                holder.reason.setText("paid");
            }

        }
        else
        {
            holder.title.setText("Coin Credited");
            holder.coin.setText(myListData.getRewards());
            if(myListData.getReason().equals("credited"))
            {
                holder.reason.setText("Credited via Spin");
            }
            else {
                holder.reason.setText(myListData.getReason());
            }

        }
    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMM dd,yyyy - h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView reason;
        public TextView date;
        public TextView coin;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.reason = itemView.findViewById(R.id.reason);
            this.date =  itemView.findViewById(R.id.date);
            this.coin =  itemView.findViewById(R.id.coin);
            this.title =  itemView.findViewById(R.id.title);
        }
    }
}
