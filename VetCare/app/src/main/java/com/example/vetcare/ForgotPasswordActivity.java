package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText mail;
    Button recoverymailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        mail = findViewById(R.id.editTextEmailForgotPw);
        recoverymailBtn = findViewById(R.id.sendRecovoryEmailButton);
        mAuth = FirebaseAuth.getInstance();

        recoverymailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fpMail = mail.getText().toString();

                if(!fpMail.isEmpty())
                {
                    sendRecoveryMail(fpMail);
                }
                else {
                    mail.setError("Enter proper mail id");
                    mail.requestFocus();
                }
            }
        });

    }

    private void sendRecoveryMail(String fpMail) {

        mAuth.sendPasswordResetEmail(fpMail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPasswordActivity.this);
                            alert.setIcon(android.R.drawable.ic_dialog_info);
                            alert.setTitle("Success");
                            alert.setMessage("Recovery mail sent");

                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alert.show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPasswordActivity.this);
                        alert.setIcon(android.R.drawable.ic_dialog_info);
                        alert.setTitle("Error");
                        alert.setMessage(e.toString());
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                    }
                });

    }
}