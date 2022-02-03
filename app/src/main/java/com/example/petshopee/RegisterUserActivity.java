package com.example.petshopee;

import static com.google.firebase.auth.GoogleAuthProvider.getCredential;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterUserActivity extends AppCompatActivity {
    private static final String TAG = "LogIn";
    Button btnSignUp;
    EditText editTextRegEmail;
    EditText editTextRegPassword;
    TextView logIn;
    SignInButton btnRegSignIn;

    FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        btnSignUp = findViewById(R.id.btnSignUp);
        editTextRegEmail = findViewById(R.id.editTextSignUpEmail);
        editTextRegPassword = findViewById(R.id.editTextSignUpPassword);
        logIn = findViewById(R.id.logInText);
        btnRegSignIn = findViewById(R.id.googleRegSignIn);


        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUserActivity.this,LogInActivity.class));
            }
        });

        requestGoogleSignIn();

        btnRegSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    private void registerUser() {
        String email = editTextRegEmail.getText().toString();
        String password = editTextRegPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            editTextRegEmail.setError("Email required!");
            editTextRegEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            editTextRegPassword.setError("Password required!");
            editTextRegPassword.requestFocus();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterUserActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterUserActivity.this,LogInActivity.class));
                            }
                            else{
                                Toast.makeText(RegisterUserActivity.this,"Something went wrong: "
                                        +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void requestGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_1))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void googleSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("MyPrefs",MODE_PRIVATE)
                        .edit();
                editor.putString("userName",account.getDisplayName());
                editor.putString("userEmail",account.getEmail());
                editor.putString("userProfile",account.getPhotoUrl().toString());
                editor.apply();

            }
            catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterUserActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterUserActivity.this,MainActivity.class).putExtra("profile",1));
                        }
                        else{
                            Toast.makeText(RegisterUserActivity.this,"Something went wrong: "
                                    +task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}