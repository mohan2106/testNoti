package com.example.testnoti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class profileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Button btn;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser != null){
            //startActivity(new Intent(LoginActivity.this,MainActivity.class));
            //finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(new Intent(profileActivity.this,MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(profileActivity.this).toBundle());
                finish();
            }
            else{
                startActivity(new Intent(profileActivity.this,MainActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn = findViewById(R.id.fake_noti);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationHealper.displayNotification(profileActivity.this,"title","fake notification");
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "SUccessfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                        Toast.makeText(profileActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful()){
                    String token = task.getResult().getToken();
                    saveToken(token);
                }else{
                    Toast.makeText(profileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveToken(String token){
        String Email = mAuth.getCurrentUser().getEmail();
        Map<Object,String> data = new HashMap<>();
        data.put("username",Email);
        data.put("token",token);
        String uid = mAuth.getCurrentUser().getUid();
        firestore.collection("user").document(uid).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(profileActivity.this, "token Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
