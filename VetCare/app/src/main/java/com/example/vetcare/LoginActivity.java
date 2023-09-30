package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button login, register;
    EditText femail, fpassword;
    ProgressDialog loadingBar;
    TextView forgotpw;

    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        femail = findViewById(R.id.editTextEmailLogin);
        fpassword = findViewById(R.id.editTextPasswordLogin);
        login = findViewById(R.id.buttonLogin);
        register = findViewById(R.id.buttonRegisterLogin);
        forgotpw = findViewById(R.id.textViewForgotPwLogin);
        loadingBar = new ProgressDialog(this);

        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() != null) {

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = femail.getText().toString();
                String pwd = fpassword.getText().toString();

                if(!mail.isEmpty() && !pwd.isEmpty())
                {
                    loadingBar.setTitle("Login");
                    loadingBar.setMessage("Please wait...Authenticating");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    fbAuth.signInWithEmailAndPassword(mail,pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(getApplicationContext(),"Login successfull",Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(mail.isEmpty())
                {
                    femail.setError("Enter email");
                    femail.requestFocus();
                }
                if(pwd.isEmpty())
                {
                    fpassword.setError("Enter password");
                    fpassword.requestFocus();
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }
}