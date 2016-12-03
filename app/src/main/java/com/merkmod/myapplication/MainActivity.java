package com.merkmod.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.merkmod.achievementtoastlibrary.AchievementToast;

public class MainActivity extends AppCompatActivity {

    Button all, withicon, normal, withouticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        all = (Button) findViewById(R.id.button);
        normal = (Button) findViewById(R.id.button2);
        withicon = (Button) findViewById(R.id.button3);
        withouticon = (Button) findViewById(R.id.button4);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AchievementToast.makeAchievement(MainActivity.this, getResources(), R.string.with_all,
                        AchievementToast.LENGTH_MEDIUM, android.R.color.white, R.color.colorPrimary, getResources().getDrawable(R.mipmap.ic_launcher))
                        .show();
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AchievementToast.makeAchievement(MainActivity.this, "normal", AchievementToast.LENGTH_SHORT).show();
            }
        });

        withicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AchievementToast.makeAchievement(MainActivity.this, "with icon", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.mipmap.ic_launcher)).show();
            }
        });

        withouticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AchievementToast.makeAchievement(MainActivity.this, "without icon",
                        AchievementToast.LENGTH_MEDIUM, android.R.color.white, R.color.colorPrimary)
                        .show();
            }
        });

    }
}
