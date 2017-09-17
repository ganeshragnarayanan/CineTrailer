package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieActivityDetail  extends AppCompatActivity {

    String youtubeTrailerID;
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

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
        String voteAverage = intent.getStringExtra("vote_average");
        String releaseDate = intent.getStringExtra("release_date");
        final String id = intent.getStringExtra("id");

        // Capture the layout's TextView and set the string as its text
        final ImageView ivImage = (ImageView) findViewById(R.id.ivMovieImageDetail);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(this).load(image_landscape).into(ivImage);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(this).load(image_landscape).into(ivImage);
        }
        ivImage.setImageResource(0);

        final TextView textView = (TextView) findViewById(R.id.tvTitleDetail);
        textView.setText(title);

        final TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvReleaseDate.setText("Release Date: " + releaseDate);

        final RatingBar tvRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        tvRatingBar.setRating(Float.parseFloat(voteAverage));

        final TextView tvOverview = (TextView) findViewById(R.id.tvOverviewDetail);
        tvOverview.setText(overview);

        String url = "https://api.themoviedb.org/3/movie/" + id +
                "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MovieActivityDetail.this, YoutubeActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

    }
}
