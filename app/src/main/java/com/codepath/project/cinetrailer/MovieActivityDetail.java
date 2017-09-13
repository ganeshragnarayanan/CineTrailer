package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieActivityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_movie_detail);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String image_portrait = intent.getStringExtra("image_portrait");
        String image_landscape = intent.getStringExtra("image_landscape");
        String title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");

        // Capture the layout's TextView and set the string as its text
        ImageView ivImage = (ImageView) findViewById(R.id.ivMovieImageDetail);
        //copy start
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(this).load(image_portrait).into(ivImage);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(this).load(image_landscape).into(ivImage);
        }
        // copy end
        ivImage.setImageResource(0);

        TextView textView = (TextView) findViewById(R.id.tvTitleDetail);
        textView.setText(title);

        TextView tvOverview = (TextView) findViewById(R.id.tvOverviewDetail);
        tvOverview.setText(overview);

    }

}
