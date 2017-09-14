package com.codepath.project.cinetrailer.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.cinetrailer.R;
import com.codepath.project.cinetrailer.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GANESH on 9/12/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivMovieImage;
    }


    public MovieArrayAdapter(Context context, List<Movie>movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for position
        Movie movie = getItem(position);
        boolean popularMovie = false;

        int type = getItemViewType(position);

        if (popularMovie(movie)) {
            popularMovie = true;
        }

        ViewHolder viewHolder;
        if (convertView == null) {


            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            //convertView = inflater.inflate(R.layout.item_movie, parent, false);
            convertView = getInflatedLayoutForType(popularMovie);

            if (popularMovie) {
                viewHolder.ivMovieImage = (ImageView) convertView.findViewById(R.id.ivMovieImagePopular);
                convertView.setTag(viewHolder);
            } else {
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                viewHolder.ivMovieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                convertView.setTag(viewHolder);
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (popularMovie) {
            viewHolder.ivMovieImage.setImageResource(0);

            String image;
            int orientation = parent.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Picasso.with(getContext()).load(movie.getPosterPath()).into(viewHolder.ivMovieImage);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Picasso.with(getContext()).load(movie.getBackdropPath()).into(viewHolder.ivMovieImage);
            }
        } else {
            viewHolder.tvTitle.setText(movie.getOriginalTitle());
            viewHolder.tvOverview.setText(movie.getOverview());
            viewHolder.ivMovieImage.setImageResource(0);

            String image;
            int orientation = parent.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Picasso.with(getContext()).load(movie.getPosterPath()).into(viewHolder.ivMovieImage);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Picasso.with(getContext()).load(movie.getBackdropPath()).into(viewHolder.ivMovieImage);
            }
        }

        return convertView;
    }

    // check if the input movie is popular
    private boolean popularMovie(Movie movie) {
        String voteAverage = movie.getVoteAverage();
        Log.d("debug", voteAverage);
        float f = Float.parseFloat(voteAverage);

        if (f > 5.0 ) {
            return true;
        } else {
            return false;
        }
    }

    // heterogenour start
    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return 1;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        // Return an integer here representing the type of View.
        // Note: Integers must be in the range 0 to getViewTypeCount() - 1
        return 1;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(boolean popularMovie) {
        if (popularMovie) {
            Log.d("debug", "popular movie");
            return LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, null);
        } else  {
            Log.d("debug", "not popular movie");
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        }
    }


}
