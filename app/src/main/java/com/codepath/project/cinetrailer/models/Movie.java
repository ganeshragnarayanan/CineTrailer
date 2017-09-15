package com.codepath.project.cinetrailer.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by GANESH on 9/12/17.
 */

public class Movie {
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getId() {
        return id;
    }

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    String voteAverage;
    String releaseDate;
    String id;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getString("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
        this.id = jsonObject.getString("id");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int x=0;x<array.length();x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    // check if the input movie is popular
    public boolean popularMovie(Movie movie) {
        String voteAverage = movie.getVoteAverage();
        Log.d("debug", voteAverage);
        float f = Float.parseFloat(voteAverage);

        if (f > 5.0 ) {
            return true;
        } else {
            return false;
        }
    }
}
