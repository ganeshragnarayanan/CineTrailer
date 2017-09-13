package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MovieActivityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra("test");

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.lvMoviesDetail);
        textView.setText("reached 2nd activity");
    }

}
