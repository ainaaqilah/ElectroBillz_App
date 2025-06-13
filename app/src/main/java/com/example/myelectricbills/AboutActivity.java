package com.example.myelectricbills;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textViewGithub = findViewById(R.id.textViewGithub);
        textViewGithub.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
