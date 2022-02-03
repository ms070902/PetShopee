package com.example.petshopee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    Button btnLogIn;

    EditText editTextLogInEmail;
    EditText editTextLogInPassword;
    TextView signUp;
    TextView resetPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btnLogIn = findViewById(R.id.btnLogIn);
        editTextLogInEmail = findViewById(R.id.editTextLogInEmail);
        editTextLogInPassword = findViewById(R.id.editTextLogInPassword);
        signUp = findViewById(R.id.signUpText);
        resetPassword = findViewById(R.id.resetPassword);





        firebaseAuth = FirebaseAuth.getInstance();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,RegisterUserActivity.class));
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailId = new EditText(v.getContext());
                AlertDialog.Builder changePassword = new AlertDialog.Builder(v.getContext());
                changePassword.setTitle("Reset Password");
                changePassword.setMessage("Enter your email to reset the password");
                changePassword.setView(emailId);
                changePassword.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = emailId.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(),"Password reset link sent to given email successfully.",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Error occurred in sending reset link: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                changePassword.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                changePassword.create().show();
            }
        });
    }

    private void logInUser() {
        String email = editTextLogInEmail.getText().toString();
        String password = editTextLogInPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            editTextLogInEmail.setError("Email required");
            editTextLogInEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            editTextLogInPassword.setError("Password required");
            editTextLogInPassword.requestFocus();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LogInActivity.this,"Log in successful.",Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getApplicationContext()
                                        .getSharedPreferences("emailId",MODE_PRIVATE)
                                        .edit();

                                editor.putString("email",email);
                                editor.apply();
                                startActivity(new Intent(LogInActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("profile",0));
                            }
                            else{
                                Toast.makeText(LogInActivity.this,"Something went wrong: "
                                        +task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

}