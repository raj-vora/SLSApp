package com.example.sls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {
    EditText emailId, password;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        tvSignUp = findViewById(R.id.signUpTextView);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Toast.makeText(SigninActivity.this,"You are logged in",Toast.LENGTH_SHORT);
                    Intent intent = new Intent(SigninActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SigninActivity.this,"Please Sign In",Toast.LENGTH_SHORT);
                }
            }
        };
    }

    public void signInButtonClick (View view){
        final String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        if(email.isEmpty()){
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        }
        else if(pwd.isEmpty()){
            password.setError("Please enter your password");
            password.requestFocus();
        }
        else if (email.isEmpty() && pwd.isEmpty()){
            Toast.makeText(SigninActivity.this,"Fields are empty",Toast.LENGTH_SHORT);
        }
        else if(!(email.isEmpty() && pwd.isEmpty())){
            mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(SigninActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT);

                    }
                    else {
                        Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                }
            });
        }
        else{
            Toast.makeText(SigninActivity.this,"Error Occurred!",Toast.LENGTH_SHORT);
        }
    }

    public void signUpTextViewClick(View view) {
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
