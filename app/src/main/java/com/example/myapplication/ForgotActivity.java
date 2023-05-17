package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    private ImageView ivPrev;
    private Button btnForgotSubmit;
    private EditText emailEdt;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        ivPrev = findViewById(R.id.ivPrev);
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        emailEdt = findViewById(R.id.edtEmailForgot);

        btnForgotSubmit = findViewById(R.id.btnForgotSubmit);
        btnForgotSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(emailEdt.getText().toString())) emailEdt.setError("Bắt buộc");
                if(!emailEdt.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    emailEdt.setError("Email không hợp lệ");
                    return;
                }
                else
                    fAuth.sendPasswordResetEmail(emailEdt.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ForgotActivity.this,"Kiểm tra email để đặt lại mật khẩu",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotActivity.this,"Gửi email thất bại!",Toast.LENGTH_LONG).show();
                        }
                    });
            }
        });
    }
}