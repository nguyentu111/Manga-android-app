package com.example.myapplication;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

    final ArrayList<Message> listMessages;

    MessageAdapter(ArrayList<Message> listMessages) {
        this.listMessages = listMessages;
    }


    @Override
    public int getCount() {
        if (listMessages == null){
            return 0;
        }
        return listMessages.size();
    }

    @Override
    public Object getItem(int i) {
        if (listMessages == null){
            return null;
        }
        return listMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewMessage;
        if (view == null) {
            viewMessage = View.inflate(viewGroup.getContext(), R.layout.item_message, null);
        } else viewMessage = view;

        Message message = (Message) getItem(i);

        TextView tvMessage = viewMessage.findViewById(R.id.tv_message);
        TextView tvTime = viewMessage.findViewById(R.id.time);
        LinearLayout llItemMessage = viewMessage.findViewById(R.id.item_message);

        if (message.getId() != ChatActivity.currentUserId ){
            tvMessage.setBackgroundResource(R.drawable.bg_white_corner_16);
            tvMessage.setTextColor(Color.parseColor("#000000"));
            llItemMessage.setGravity(Gravity.START);
        }
        else {
            tvMessage.setBackgroundResource(R.drawable.bg_orange_corner_16);
            tvMessage.setTextColor(Color.parseColor("#ffffff"));
            llItemMessage.setGravity(Gravity.END);
        }
        if (i > 0){
            if (!message.isSameTime(listMessages.get(i-1).getTime())){
                tvTime.setMaxHeight(100);
                tvTime.setText(message.getTimeString());
            }
            else{
                tvTime.setMaxHeight(0);
            }
        }
        else {
            tvTime.setMaxHeight(100);
            tvTime.setText(message.getTimeString());
        }

        tvMessage.setText(message.getMessage());

        return viewMessage;
    }
}
