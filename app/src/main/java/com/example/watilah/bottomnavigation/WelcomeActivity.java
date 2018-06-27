package com.example.watilah.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    TextView title, desc;
    ImageView welcomeLogo;
    Animation upToDown, downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread myThread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        title = findViewById(R.id.tv_title);
        desc = findViewById(R.id.tv_desc);
        welcomeLogo = findViewById(R.id.welcome_logo);

        upToDown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        title.setAnimation(upToDown);
        welcomeLogo.setAnimation(upToDown);
        desc.setAnimation(downToUp);

        myThread.start();

    }

}