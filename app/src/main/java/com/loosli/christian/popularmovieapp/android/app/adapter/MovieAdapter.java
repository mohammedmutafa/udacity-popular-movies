package com.loosli.christian.popularmovieapp.android.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loosli.christian.popularmovieapp.android.app.R;
import com.loosli.christian.popularmovieapp.android.app.entity.Movie;
import com.loosli.christian.popularmovieapp.android.app.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ChristianL on 06.02.16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements View.OnClickListener {

    private static final double TMDB_POSTER_SIZE_RATIO = 185.0 / 277.0;

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<Movie> mDataset;
    private final int mPosterWidth;
    private final int mPosterHeight;
    private final OnMovieClickListener mItemClickListener;

    public MovieAdapter(Context context, List<Movie> data, int posterWidth, OnMovieClickListener itemClickListener) {
        mContext = context;
        mDataset = data;
        mPosterWidth = posterWidth;
        mPosterHeight = (int) (posterWidth / TMDB_POSTER_SIZE_RATIO);
        mItemClickListener = itemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        v.setOnClickListener(this);
        // set the view's size, margins, paddings and layout parameters
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) v.getLayoutParams();
        lp.width = mPosterWidth;
        lp.height = mPosterHeight;
        v.setLayoutParams(lp);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        Movie movie = mDataset.get(position);
        // - replace the contents of the view with that element
        holder.itemView.setTag(movie);
        String posterUrl = Util.buildPosterUrl(movie.getPosterPath(), mPosterWidth);
        Log.d(LOG_TAG, posterUrl);
        Picasso picasso = Picasso.with(mContext);
        picasso.load(posterUrl)
                .resize(mPosterWidth, mPosterHeight)
                .placeholder(R.color.colorBlueGrey300)
                .centerCrop()
                .into(holder.mImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onClick(View v) {
        Movie movie = (Movie)v.getTag();
        int position = mDataset.indexOf(movie);
        mItemClickListener.onMovieClicked(movie, v, position);
    }

    public interface OnMovieClickListener {
        void onMovieClicked(@NonNull final Movie movie, View view, int position);

        OnMovieClickListener DUMMY = new OnMovieClickListener() {
            @Override
            public void onMovieClicked(@NonNull Movie movie, View view, int position) {
            }
        };
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @Bind(R.id.movie_item_image)
        ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
