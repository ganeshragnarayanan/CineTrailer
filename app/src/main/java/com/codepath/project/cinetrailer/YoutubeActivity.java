package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeActivity extends YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener{

    String youtubeTrailerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        fetchMovieDataOKHttp(url);
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        /** add listeners to YouTubePlayer instance **/
        /*int controlFlags = player.getFullscreenControlFlags();
        setRequestedOrientation(PORTRAIT_ORIENTATION);
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);
        player.setFullscreen(true);*/

        //fetchTrailerId("https://api.themoviedb.org/3/movie/" + "10" + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed");
        Log.d("debug, trailer id = ", youtubeTrailerID);
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /** Start buffering **/
        if (!wasRestored) {
            Log.d("debug", "playing");
            //player.cueVideo("ACA_yL0lDA4");
            player.cueVideo(youtubeTrailerID);
            player.play();

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

                YoutubeActivity.this.runOnUiThread(new Runnable() {
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

                                YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivYoutube);
                                youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", YoutubeActivity.this);

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
}
