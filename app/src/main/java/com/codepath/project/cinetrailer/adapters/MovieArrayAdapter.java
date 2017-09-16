package com.codepath.project.cinetrailer.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.cinetrailer.R;
import com.codepath.project.cinetrailer.models.Movie;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by GANESH on 9/12/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    static boolean isPopular;

    private static class ViewHolderNonPopular {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivMovieImage;
    }

    private static class ViewHolderPopular {
        ImageView ivMovieImage;
        YouTubePlayerView youTubePlayerView;
        TextView tvTitleLandscape;
        TextView tvOverviewLandscape;
        ImageView ivPlay;
    }


    public MovieArrayAdapter(Context context, List<Movie>movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for position
        Movie movie = getItem(position);
        boolean popularMovie = false;

        if (popularMovie(movie)) {
            popularMovie = true;
        } else {
            popularMovie = false;
        }

        int type = this.getItemViewType(position);

        switch (type) {
            case 0: {  //popular movie

                View v = convertView;
                ViewHolderPopular viewHolderPopular;
                if (v == null) {
                    // If there's no view to re-use, inflate a brand new view for row
                    viewHolderPopular = new ViewHolderPopular();

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    v = getInflatedLayoutForType(popularMovie);


                    viewHolderPopular.ivMovieImage = (ImageView)
                            v.findViewById(R.id.ivMovieImagePopular);

                    int orientation = parent.getResources().getConfiguration().orientation;

                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        viewHolderPopular.youTubePlayerView = (YouTubePlayerView)
                                v.findViewById(R.id.ivYoutubePopularMovie);
                        viewHolderPopular.youTubePlayerView.setVisibility(View.INVISIBLE);

                    }

                    viewHolderPopular.tvTitleLandscape = (TextView) v.findViewById(R.id.tvTitle);
                    viewHolderPopular.tvOverviewLandscape = (TextView)
                            v.findViewById(R.id.tvOverview);

                    v.setTag(viewHolderPopular);

                } else {
                    viewHolderPopular = (ViewHolderPopular) v.getTag();
                }

                viewHolderPopular.ivMovieImage.setImageResource(0);

                String image;
                int orientation = parent.getResources().getConfiguration().orientation;

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Picasso.with(getContext()).load(movie.getBackdropPath()).fit().centerCrop()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.loading)
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into((ImageView)
                                    viewHolderPopular.ivMovieImage);

                    viewHolderPopular.ivMovieImage.setMaxWidth(100);

                } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Picasso.with(getContext()).load(movie.getBackdropPath())
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.loading)
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into((ImageView)
                                    viewHolderPopular.ivMovieImage);

                    viewHolderPopular.tvTitleLandscape.setText(movie.getOriginalTitle());
                    viewHolderPopular.tvOverviewLandscape.setText(movie.getOverview());

                }

                return v;
            }

            case 1: { // less popular movie
                View v = convertView;
                ViewHolderNonPopular viewHolderNonPopular;
                if (v == null) {
                    viewHolderNonPopular = new ViewHolderNonPopular();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    v = getInflatedLayoutForType(false);
                    viewHolderNonPopular.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                    viewHolderNonPopular.tvOverview = (TextView) v.findViewById(R.id.tvOverview);
                    viewHolderNonPopular.ivMovieImage = (ImageView)
                            v.findViewById(R.id.ivMovieImage);
                    v.setTag(viewHolderNonPopular);
                } else {
                    viewHolderNonPopular = (ViewHolderNonPopular) v.getTag();
                }
                viewHolderNonPopular.tvTitle.setText(movie.getOriginalTitle());
                viewHolderNonPopular.tvOverview.setText(movie.getOverview());
                viewHolderNonPopular.ivMovieImage.setImageResource(0);

                String image;
                int orientation = parent.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Picasso.with(getContext()).load(movie.getPosterPath()).fit().centerCrop()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.loading)
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into((ImageView)
                                    viewHolderNonPopular.ivMovieImage);
                } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Picasso.with(getContext()).load(movie.getBackdropPath())
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.loading)
                            .transform(new RoundedCornersTransformation(10, 10))
                            .into((ImageView)
                                    viewHolderNonPopular.ivMovieImage);
                }

                return v;
            }

        }

        return null;
    }

    // check if the input movie is popular
    private boolean popularMovie(Movie movie) {
        String voteAverage = movie.getVoteAverage();
        float f = Float.parseFloat(voteAverage);

        // check if the vote average is more than 5
        if (f > 5.0 ) {
            return true;
        } else {
            return false;
        }
    }

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    // we have 2 types - popular and not so popular movies
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        // Return an integer here representing the type of View.
        // Note: Integers must be in the range 0 to getViewTypeCount() - 1
        Movie movie = getItem(position);

        if (popularMovie(movie)) {
            return 0;
        } else {
            return 1;
        }
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(boolean popularMovie) {
        if (popularMovie) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, null);
        } else  {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        }
    }
}
