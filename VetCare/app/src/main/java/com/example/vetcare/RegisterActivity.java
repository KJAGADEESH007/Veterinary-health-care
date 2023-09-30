package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText fname, femail, fcontact, fpassword;
    Button createAccount;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       mAuth = FirebaseAuth.getInstance();


        fname = findViewById(R.id.editTextNameRegister);
        femail = findViewById(R.id.editTextEmailRegister);
        fcontact = findViewById(R.id.editTextContactRegister);
        fpassword = findViewById(R.id.editTextPasswordRegister);
        createAccount = findViewById(R.id.buttonRegisterRegister);
        loadingBar = new ProgressDialog(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = fname.getText().toString();
                final String email = femail.getText().toString();
                final String contact = fcontact.getText().toString();
                final String password = fpassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !contact.isEmpty() && !password.isEmpty()) {

                    loadingBar.setTitle("Register");
                    loadingBar.setMessage("Please wait... Your account is being created.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        String userID = mAuth.getCurrentUser().getUid();
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                                        FirebaseMessaging.getInstance().subscribeToTopic("all");

                                        HashMap<String,Object> userMap = new HashMap<>();
                                        userMap.put("name",name);
                                        userMap.put("email",email);
                                        userMap.put("contact",contact);
                                        userMap.put("password",password);

                                        databaseReference.setValue(userMap);


                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();


                                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingBar.dismiss();
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
                if (email.isEmpty()) {
                    femail.setError("Email required");
                    femail.requestFocus();
                }
                if (password.isEmpty()) {
                    fpassword.setError("Password required");
                    fpassword.requestFocus();
                }
                if (contact.isEmpty()) {
                    fcontact.setError("Contact number required");
                    fcontact.requestFocus();
                }
                if (name.isEmpty()) {
                    fname.setError("Full name required");
                    fname.requestFocus();
                }

            }
        });
   }
}