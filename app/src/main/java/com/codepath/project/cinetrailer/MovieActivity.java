package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.project.cinetrailer.adapters.MovieArrayAdapter;
import com.codepath.project.cinetrailer.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import android.telecom.Call;
//import com.squareup.picasso.Request;

//public class MovieActivity extends AppCompatActivity {
public class MovieActivity  extends YouTubeBaseActivity  implements  YouTubePlayer.OnInitializedListener {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    String youtubeTrailerID;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        //ActionBar actionBar = getSupportActionBar();



        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        /*YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivYoutubePopularMovie);
        youTubePlayerView.setVisibility(View.INVISIBLE);*/

        //copy(start)
        lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                /** Initializing YouTube player view **/
                /*YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ivMovieImagePopular);
                youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", MovieActivity);*/

                Movie movie = (Movie) lvItems.getItemAtPosition(position);

                // fetch trailer id
                url = "https://api.themoviedb.org/3/movie/" + movie.getId()+ "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
                fetchTrailerId(url);


                if (!movie.popularMovie(movie)) {
                    Intent i = new Intent(MovieActivity.this, MovieActivityDetail.class);

                    i.putExtra("image_portrait", movie.getPosterPath());
                    i.putExtra("image_landscape", movie.getBackdropPath());
                    i.putExtra("title", movie.getOriginalTitle());
                    i.putExtra("overview", movie.getOverview());
                    i.putExtra("vote_average", movie.getVoteAverage());
                    i.putExtra("release_date", movie.getReleaseDate());
                    i.putExtra("id", movie.getId());

                    startActivity(i);
                } else {
                    Log.d("debug", "popular movie click");

                    Intent i = new Intent(MovieActivity.this, YoutubeActivity.class);
                    i.putExtra("id", movie.getId());
                    startActivity(i);

                    //((YouTubePlayerView)v.findViewById(R.id.ivYoutubePopularMovie)).setVisibility(View.VISIBLE);

                    /*YouTubePlayerView youTubePlayerView = (YouTubePlayerView) v.findViewById(R.id.ivYoutubePopularMovie);
                    youTubePlayerView.setVisibility(View.VISIBLE);
                    ImageView ivMovieImage = (ImageView) v.findViewById(R.id.ivMovieImagePopular);
                    ivMovieImage.setVisibility(View.GONE);
                    youTubePlayerView.initialize("a07e22bc18f5cb106bfe4cc1f83ad8ed", MovieActivity.this);*/
                }

            }
        });
        //copy(end)

        //fetchMovieDataAsyncHttp(url);
        fetchMovieDataOKHttp(url);

        Log.d("debug", "here2");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public void fetchTrailerId(String url) {
        Log.d("debug url ", url);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", "received success for Trailer id");
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    JSONObject jsonObject = movieJsonResults.getJSONObject(0);
                    youtubeTrailerID = jsonObject.getString("key");
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

    public void fetchMovieDataAsyncHttp(String url) {
        //Log.d("debug", "here1");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", "received success");
                //super.onSuccess(statusCode, headers, response);
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    //movies = Movie.fromJSONArray(movieJsonResults);
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.d("debug", "success1");
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

                MovieActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            Log.d("debug", "received success");
                            //super.onSuccess(statusCode, headers, response);
                            JSONArray movieJsonResults = null;

                            try {
                                movieJsonResults = json.getJSONArray("results");
                                //movies = Movie.fromJSONArray(movieJsonResults);
                                movies.addAll(Movie.fromJSONArray(movieJsonResults));
                                movieAdapter.notifyDataSetChanged();
                                Log.d("debug", "success1");
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
        Log.d("debug", "youtube onInitializationSuccess");
        /** add listeners to YouTubePlayer instance **/
        /*int controlFlags = player.getFullscreenControlFlags();
        setRequestedOrientation(PORTRAIT_ORIENTATION);
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);
        player.setFullscreen(true);*/

        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("debug", "youtube onInitializationSuccess before if");
        /** Start buffering **/
        if (!wasRestored) {
            //player.cueVideo("ACA_yL0lDA4");
            //player.loadVideo(youtubeTrailerID);
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
