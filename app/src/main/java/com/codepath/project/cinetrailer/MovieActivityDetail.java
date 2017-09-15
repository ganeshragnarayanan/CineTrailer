package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

public class MovieActivityDetail  extends YouTubeBaseActivity  implements  YouTubePlayer.OnInitializedListener {

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

        // Capture the layout's TextView and set the string as its text
        final ImageView ivImage = (ImageView) findViewById(R.id.ivMovieImageDetail);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(this).load(image_portrait).into(ivImage);
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

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivYoutubeDetail);
        youTubePlayerView.setVisibility(View.INVISIBLE);
        //youTubePlayerView.setOnClickListener(new View.OnClickListener() {
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivYoutubeDetail);
                //youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", MovieActivityDetail.this);
                youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", MovieActivityDetail.this);

                Log.d("debug", "testing youtube");
                youTubePlayerView.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.INVISIBLE);
                tvOverview.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                /** Initializing YouTube player view **/
                //YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivYoutubeDetail);
                youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", MovieActivityDetail.this);
            }
        });

    }



    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
            /** add listeners to YouTubePlayer instance **/
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /** Start buffering **/
        if (!wasRestored) {
        //player.cueVideo("ACA_yL0lDA4");
            player.loadVideo("ACA_yL0lDA4");
            //player.play();

        }
        //player.play();

        }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
        }

    private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

        };

private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {

@Override
public void onAdStarted() {
        }

@Override
public void onError(ErrorReason arg0) {
        }

@Override
public void onLoaded(String arg0) {
        }

@Override
public void onLoading() {
        }

@Override
public void onVideoEnded() {
        }

@Override
public void onVideoStarted() {
        }
        };
}
