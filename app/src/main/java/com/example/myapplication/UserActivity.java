package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    private ImageView ivPrev;
    private Button btnDN, btnDK;
    private ImageView btnLogout;
    private TextView userName, status;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    ActivityResultLauncher<Intent> callIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==200) {
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                        Intent caller = new Intent();
                        caller.putExtra("user", fAuth.getCurrentUser().getDisplayName());
                        Log.d("res", "200");
                        setResult(RESULT_OK, caller);
                        finish();
                        return;
                    }
                    if(result.getResultCode()==201) {
                        Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_LONG).show();
                        Intent caller = new Intent();
                        caller.putExtra("user", fAuth.getCurrentUser().getDisplayName());

                        setResult(RESULT_OK, caller);
                        finish();
                        return;
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ivPrev = findViewById(R.id.ivPrev);
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent caller = new Intent();
                setResult(RESULT_CANCELED, caller);
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
                    Intent myIntent = new Intent(UserActivity.this, UserActivity.class);
                    startActivity(myIntent);
                    finish();
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
                if(getCallingActivity()!=null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    callIntent.launch(intent);
                }
                else {
                    Intent myIntent = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });


        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getCallingActivity()!=null) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    callIntent.launch(intent);
                }
                else {
                    Intent myIntent = new Intent(UserActivity.this, SignupActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });
    }
}