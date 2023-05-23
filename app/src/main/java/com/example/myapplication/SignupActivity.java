package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
    private ImageView ivPrev;
    private Button btnSignupSubmit;
    private EditText userNameEdt, emailEdt, passwordEdt;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ivPrev = findViewById(R.id.ivPrev);
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //submit
        userNameEdt = findViewById(R.id.edtUserNameSignup);
        emailEdt = findViewById(R.id.edtEmailSignup);
        passwordEdt = findViewById(R.id.edtPasswordSignup);
        btnSignupSubmit = findViewById(R.id.btnSubmitDK);
        btnSignupSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(userNameEdt.getText().toString())) {
                    userNameEdt.setError("Bắt buộc");
                    return;
                }
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
                }
                fAuth.createUserWithEmailAndPassword(emailEdt.getText().toString(), passwordEdt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            try
                            {
                                Toast.makeText(getApplicationContext(),"Đã xảy ra lỗi, hãy thử lại!",Toast.LENGTH_LONG).show();
                                throw task.getException();
                            }

                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                            {
                                return;
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                emailEdt.setError("Email đã được sử dụng");
                                return;
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Đã xảy ra lỗi, hãy thử lại!",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userNameEdt.getText().toString())
                                .build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()) {
                                    user.delete();
                                    Toast.makeText(getApplicationContext(),"Đã xảy ra lỗi, hãy thử lại!",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(getCallingActivity()!=null) {
                                    Intent caller = new Intent();
                                    caller.putExtra("user", fAuth.getCurrentUser().getDisplayName());

                                    setResult(201, caller);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_LONG).show();
                                    Intent main = new Intent(SignupActivity.this, UserActivity.class);
                                    startActivity(main);
                                }
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Log.d("on key down", String.valueOf(keyCode));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
