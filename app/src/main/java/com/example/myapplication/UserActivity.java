package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    private ImageView ivPrev;
    private Button btnDN, btnDK;
    private ImageView btnLogout;
    private TextView userName, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ivPrev = findViewById(R.id.ivPrev);
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        userName = findViewById(R.id.tvUserName);
        status = findViewById(R.id.tvStatus);
        btnLogout = findViewById(R.id.ivLogout);
        btnDN = findViewById(R.id.bntDN);
        btnDK = findViewById(R.id.btnDK);
        //
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            userName.setText(user.getDisplayName());
            btnLogout.setVisibility(View.VISIBLE);
            btnDN.setVisibility(View.INVISIBLE);
            btnDK.setVisibility(View.INVISIBLE);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    userName.setText("Khách");
                    status.setText("Chưa đăng nhập");
                    btnLogout.setVisibility(View.INVISIBLE);
                    btnDN.setVisibility(View.VISIBLE);
                    btnDK.setVisibility(View.VISIBLE);
                }
            });
            status.setText("");
        }
        else {
            userName.setText("Khách");
            btnLogout.setVisibility(View.INVISIBLE);
            btnDN.setVisibility(View.VISIBLE);
            btnDK.setVisibility(View.VISIBLE);
            status.setText("Chưa đăng nhập");
        }


        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });


        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UserActivity.this, SignupActivity.class);
                startActivity(myIntent);
            }
        });
    }
}