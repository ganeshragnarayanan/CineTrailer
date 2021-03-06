package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.project.cinetrailer.adapters.MovieArrayAdapter;
import com.codepath.project.cinetrailer.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;

    String url = "";

    @BindView(R.id.lvMovies) ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                Movie movie = (Movie) lvItems.getItemAtPosition(position);

                url = "https://api.themoviedb.org/3/movie/" + movie.getId()+
                        "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

                if (!movie.popularMovie(movie)) {
                    //start a new activity for not popular movies
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
                    // open youtube activity for the trailer
                    Intent i = new Intent(MovieActivity.this, YoutubeActivity.class);
                    i.putExtra("id", movie.getId());
                    startActivity(i);
                }

            }
        });

        fetchMovieDataOKHttp(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
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
                            JSONArray movieJsonResults = null;

                            try {
                                movieJsonResults = json.getJSONArray("results");
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
}
