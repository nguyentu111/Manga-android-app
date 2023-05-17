package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    private ImageView ivPrev;
    private Button btnDNSubmit;
    private EditText emailEdt, passwordEdt;
    private TextView forgotEdt;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivPrev = findViewById(R.id.ivPrev);
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //submit
        emailEdt = findViewById(R.id.edtEmailSignup);
        passwordEdt = findViewById(R.id.edtPasswordSignup);
        btnDNSubmit = findViewById(R.id.btnSubmitDK);
        btnDNSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(emailEdt.getText().toString())) {
                    emailEdt.setError("Bắt buộc");
                    return;
                }
                if(TextUtils.isEmpty(passwordEdt.getText().toString())) {
                    passwordEdt.setError("Bắt buộc");
                    return;
                }
                if(!emailEdt.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    emailEdt.setError("Email không hợp lệ");
                    return;
                }
                fAuth.signInWithEmailAndPassword(emailEdt.getText().toString(), passwordEdt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            try
                            {
                                throw task.getException();
                            }
                            catch (Exception e)
                            {
                                emailEdt.setError("Thông tin không đúng");
                                passwordEdt.setError("Thông tin không đúng");
                                Toast.makeText(getApplicationContext(),"Đăng nhập không thành công!",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        Toast.makeText(getApplicationContext(),"Welcome back!",Toast.LENGTH_LONG).show();
                        Intent main = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(main);
                    }
                });
            }
        });

        //forgot
        forgotEdt = findViewById(R.id.tvForgot);
        forgotEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(forgot);
            }
        });

    }
}