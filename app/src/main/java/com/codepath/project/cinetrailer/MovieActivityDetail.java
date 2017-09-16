package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivityDetail  extends YouTubeBaseActivity  implements  YouTubePlayer.OnInitializedListener {

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

        String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivYoutubeDetail);
        youTubePlayerView.setVisibility(View.INVISIBLE);
        youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", MovieActivityDetail.this);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MovieActivityDetail.this, YoutubeActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

    }

    public void fetchTrailerId(String url) {
        Log.d("debug url ", url);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", "onSuccess in fetchTrailerId");
                JSONArray movieJsonResults = null;

                try {
                    Log.d("debug", "onSuccess in try 1");
                    movieJsonResults = response.getJSONArray("results");
                    JSONObject jsonObject = movieJsonResults.getJSONObject(0);
                    Log.d("debug", "onSuccess in try 2");
                    youtubeTrailerID = jsonObject.getString("key");
                    Log.d("debug", "onSuccess in try 3");
                    Log.d("debug trailer id ", youtubeTrailerID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void fetchMovieDataOKHttp(String url) {

        Log.d("debug", "using OK HTTP");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MovieActivityDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            Log.d("debug", "received success");
                            JSONArray movieJsonResults = null;

                            try {
                                movieJsonResults = json.getJSONArray("results");
                                JSONObject jsonObject = movieJsonResults.getJSONObject(0);
                                youtubeTrailerID = jsonObject.getString("key");

                                YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivYoutubeDetail);
                                youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", MovieActivityDetail.this);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });
    }


    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        Log.d("debug", "on init success");
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!wasRestored) {
            player.cueVideo(youtubeTrailerID);
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
        }

    private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
            Log.d("debug", "on init success");
        }

        @Override
        public void onPaused() {
            Log.d("debug", "onPaused");
        }

        @Override
        public void onPlaying() {
            Log.d("debug", "onPlaying");
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
            Log.d("debug", "onStopped");
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
    Log.d("debug", "onLoaded");
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
