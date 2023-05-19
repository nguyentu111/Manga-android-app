package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText edtMessage;
    private Button btnSend;

    private TextView tvCurrentAccout;


    private ListView lvMessages;

    private ArrayList<Message> mListMessage;
    private MessageAdapter messagesListViewAapter;
    public static String currentUserName;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Db");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
        else {
            currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setContentView();

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
        displayMessage();
    }

    private void displayMessage() {
        mListMessage = new ArrayList<>();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListMessage.clear();
                for(DataSnapshot dt : snapshot.getChildren()) {
                    HashMap dtObject = (HashMap) dt.getValue();
                    Log.d("count", dt.getValue().toString());
                    mListMessage.add(new Message((String) dtObject.get("messageUser"), (String) dtObject.get("messageText"), (Long) dtObject.get("messageTime")));
                }
                Log.d("mList", String.valueOf(mListMessage.size()));
                messagesListViewAapter = new MessageAdapter(mListMessage);
                lvMessages.setAdapter(messagesListViewAapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(ChatActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void sentMessage() {
        String strMessage = edtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(strMessage)) {
            return;
        }
        mListMessage.add(new Message(currentUserName, strMessage));

        messagesListViewAapter.notifyDataSetChanged();
        lvMessages.smoothScrollToPosition(mListMessage.size()-1);

        Log.d("name", currentUserName);
        dbRef.push().setValue(new Message(currentUserName, strMessage));
        edtMessage.setText("");
    }

    private void setContentView() {
        edtMessage = findViewById(R.id.edt_message);
        btnSend = findViewById(R.id.btn_send);
        lvMessages =findViewById(R.id.lv_message);

        tvCurrentAccout = findViewById(R.id.tv_current_account_id);
        tvCurrentAccout.setText("Current Account Name: "+currentUserName);
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