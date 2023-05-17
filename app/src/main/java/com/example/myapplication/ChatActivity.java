package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private EditText edtMessage;
    private Button btnSend;

    private TextView tvCurrentAccout;
    private Button btnSwitchAccount;

    private ListView lvMessages;

    private ArrayList<Message> mListMessage;

    private MessageAdapter messagesListViewAapter;
    public static int currentUserId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setContentView();

        mListMessage = new ArrayList<>();
        messagesListViewAapter = new MessageAdapter(mListMessage);
        lvMessages.setAdapter(messagesListViewAapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentMessage();
            }
        });

        edtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkKeyboard();
            }
        });

        btnSwitchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUserId == 1) {
                    currentUserId = 2;
                }
                else {
                    currentUserId = 1;
                }
                messagesListViewAapter.notifyDataSetChanged();
                tvCurrentAccout.setText("Current Account ID: "+ String.valueOf(currentUserId));
            }
        });
    }

    private void sentMessage() {
        String strMessage = edtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(strMessage)) {
            return;
        }
        mListMessage.add(new Message(strMessage, currentUserId));

        messagesListViewAapter.notifyDataSetChanged();
        lvMessages.smoothScrollToPosition(mListMessage.size()-1);
        edtMessage.setText("");
    }

    private void setContentView() {
        edtMessage = findViewById(R.id.edt_message);
        btnSend = findViewById(R.id.btn_send);
        lvMessages =findViewById(R.id.lv_message);
        btnSwitchAccount = findViewById(R.id.btn_switch_account);
        tvCurrentAccout = findViewById(R.id.tv_current_account_id);
        tvCurrentAccout.setText("Current Account ID: "+ String.valueOf(currentUserId));
    }

    private void checkKeyboard() {
        final View activityRootView = findViewById(R.id.activityRoot);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

                activityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = activityRootView.getRootView().getHeight() - r.height();
                if (heightDiff > 0.25*activityRootView.getRootView().getHeight()) {
                    if (mListMessage.size() > 0) {
                        lvMessages.smoothScrollToPosition(mListMessage.size() - 1);
                        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }
}