package com.example.myapplication;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

    final ArrayList<Message> listMessages;

    MessageAdapter(ArrayList<Message> listMessages) {
        Log.d("count", String.valueOf(listMessages.size()));
        this.listMessages = listMessages;
    }


    @Override
    public int getCount() {
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
        TextView tvUser = viewMessage.findViewById(R.id.message_user);
        TextView tvMessage = viewMessage.findViewById(R.id.message_text);
        TextView tvTime = viewMessage.findViewById(R.id.message_time);
        tvUser.setText(message.getMessageUser());
        tvMessage.setText(message.getMessageText());
        tvTime.setText(new SimpleDateFormat("HH:mm:ss").format(message.getMessageTime()));
        Log.d("name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        if(message.getMessageUser().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
            LinearLayout l = (LinearLayout) viewMessage.findViewById(R.id.item_layout);
            l.setGravity(Gravity.END);
            tvUser.setText("Bạn");
        }
        else {
            LinearLayout l = (LinearLayout) viewMessage.findViewById(R.id.item_layout);
            l.setGravity(Gravity.CENTER_VERTICAL);
        }


        return viewMessage;
    }
}
