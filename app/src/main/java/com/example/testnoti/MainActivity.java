package com.example.testnoti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    //1. Notification Channel
    //2. Notification Builder
    //3. Notification Manager

    public static final String CHANNEL_ID = "mohan";
    private static final String CHANNEL_NAME = "mohan";
    private static final String CHANNEL_DESC = "mohan notification";
    private Button btn;

    private EditText email;
    private EditText password;
    private Button login;
//    private Button register;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
//    public static Activity La;
//    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser != null){
            //startActivity(new Intent(LoginActivity.this,MainActivity.class));
            //finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(new Intent(MainActivity.this,profileActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                finish();
            }
            else{
                startActivity(new Intent(MainActivity.this,profileActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_password);
        login=(Button)findViewById(R.id.login_button);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        // ON registering our app to firebase , Firebase actually generate the token to to the user which is unique. We are printing that token.


    }

    private void createUser(){
        final String username = email.getText().toString();
        final String pass = password.getText().toString();
        if(TextUtils.isEmpty(username)){
            email.setError("Username is required");
            email.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(pass.length() <= 6){
            password.setError("Minimum 6 charecter is required!!!");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(username, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startProfileActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                userLogin(username,pass);
                            }else{
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                        progressBar.setVisibility(View.INVISIBLE);

                        // ...
                    }
                });
    }
    private void userLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startProfileActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private void startProfileActivity(){
        finish();
        Intent intent = new Intent(MainActivity.this,profileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}