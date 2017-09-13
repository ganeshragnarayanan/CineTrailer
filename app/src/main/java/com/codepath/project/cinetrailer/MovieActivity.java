package com.codepath.project.cinetrailer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.project.cinetrailer.adapters.MovieArrayAdapter;
import com.codepath.project.cinetrailer.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//public class MovieActivity extends YouTubeBaseActivity {
public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);



        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        //copy(start)
        lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                Object o = lvItems.getItemAtPosition(position);
                Log.d("debug", "onItemClick success");
                Intent i = new Intent(MovieActivity.this, MovieActivityDetail.class);

                i.putExtra("username", "foobar");
                i.putExtra("in_reply_to", "george");
                i.putExtra("code", 400);

                startActivity(i);

            }
        });
        //copy(end)


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

        Log.d("debug", "here2");
    }


}
