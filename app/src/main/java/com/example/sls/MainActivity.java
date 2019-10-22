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

public class MainActivity extends AppCompatActivity {
    EditText emailId, password;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        tvSignIn = findViewById(R.id.signInTextView);
    }

    public void signUpButtonClick(View view){
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
            Toast.makeText(MainActivity.this,"Fields are empty",Toast.LENGTH_SHORT);
        }
        else if(!(email.isEmpty() && pwd.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(MainActivity.this,"Sign Up Unsuccessful, Please Try Again",Toast.LENGTH_SHORT);
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                }
            });
        }
        else{
            Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_SHORT);
        }
    }

    public void signInTextViewClick(View view){
        Intent intent = new Intent(MainActivity.this,SigninActivity.class);
        startActivity(intent);
    }
}
