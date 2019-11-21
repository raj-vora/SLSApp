package com.example.sls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;
import android.widget.Toast;

import java.nio.charset.Charset;

import io.chirp.chirpsdk.ChirpSDK;
import io.chirp.chirpsdk.models.ChirpError;
import io.chirp.chirpsdk.interfaces.ChirpEventListener;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private ChirpSDK chirp;
    String email, uid, uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        uname = intent.getStringExtra("name");
        //Chirp
        chirp = new ChirpSDK(this, "a7cbAC032bad0FBbCA0bAE528", "ac0E3e41c3AfFBE3CED431e5CE4Eee8aC1e793BF353a42bd7E");
        ChirpError error = chirp.setConfig("WYDrwoPtZlpHZ1sr0ooixtey0XpxmszJv8mNH89KCf9zVccne5oKKqataryJASDQ1RPMMiKOVzv38rG7iQEjM8ApT9Ls0UZ9NYRBWId9c7IWgUwwyiIp7t2Zl76rI6Q5nArnb8OXpiEKnl/F41EgBhRwW1/cYJ1CN5W8O+ba4lfrPXdzqI7M/vaoic4IbI8rUjh70nNY5rZP3NXeK0Xb+qokdBdatFzBpzCNDq3SKMWtVUR8FZA/16tXuGqqC9B6iyjhMN8ttsaPz68ihz4DA3z3W7Btn5HTCTbGDUv+pZTmlgGEJVbwm3azyVzFoZiclxzlX9lxEqrA97SeNH0wu/vdaP85sZABwiKO+6rtS9pT24MvKSGqNWRSDUWIKW6A+jsW6rdZ4zOQdnpaX6MS3vRvJC5bHlZUhuIbVgLAKKGw7V9KC9dWs9FYi21bO8DhsvtvuDHFQZwX7P4EPj2Drvi/I1TlRVwEQft6Q89jnpHVX8jxFETYiWtTwXWW9uZOP6K7btW5gXHtmyyLxoLmC9mV0z0jqmQSdCkkFpU/qWxWUf7qs63H1DoFs6akx6cSUh/0Ydajnq85jIjPs2WkZ8aAn3k+sflaXgE6gmtTUIUBjO2sKxU9Wan25/tQdNKDoGrZjGxJLgaNuAlWCqR7JsBil8bZXxWfQTPumYIjq7iVEa9N31H+t1PQHhyNW51hJPd9KGVkqn9E6c3ywDYeyWhcl+4cG1Nd0gJprckj/fNRk+N82EP0qgH2aNI3+EqPf3uF99PlW3R9MmGyqM5YzJFnmaiveIJz+oRxy0qio/ajf1pUGlXmU7Wv/gAIrtPthJfLlspQADDNmwFhDm0hpkhPAOr6WRb7rB7yxb5nSfc=" + "");
        if (error.getCode() == 0) {
            Log.v("ChirpSDK: ", "Configured ChirpSDK");
        } else {
            Log.e("ChirpError: ", error.getMessage());
        }
        //Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        uid  = user.getUid();
        String display = "Hi "+ uname + "\nWelcome to Smart Lock!";
        TextView textView = findViewById(R.id.homeTextView);
        textView.setText(display);
    }

    public void logoutButtonClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, SigninActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start ChirpSDK sender and receiver, if no arguments are passed both sender and receiver are started
        ChirpError error = chirp.start(true, false  );
        if (error.getCode() > 0) {
            Log.e("ChirpError: ", error.getMessage());
        } else {
            Log.v("ChirpSDK: ", "Started ChirpSDK");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chirp.stop();
        try {
            chirp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chirpSendButtonClick(View view){
        String identifier = uid;
        byte[] payload = identifier.getBytes(Charset.forName("UTF-8"));

        ChirpError error = chirp.send(payload);
        if (error.getCode() > 0) {
            Log.e("ChirpError: ", error.getMessage());
            Toast.makeText(HomeActivity.this,"Chirp Error",Toast.LENGTH_SHORT);
        } else {
            Log.v("ChirpSDK: ", "Sent " + identifier);
            Toast.makeText(HomeActivity.this,"Chirp Sent",Toast.LENGTH_SHORT);
        }
    }
}