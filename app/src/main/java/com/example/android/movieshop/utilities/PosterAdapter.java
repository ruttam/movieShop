package com.example.android.movieshop.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieshop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    private ArrayList<Movie> movies;

    final private PosterClickListener onPosterClickListener;

    public PosterAdapter(PosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
        movies = new ArrayList<>();
    }

    public interface PosterClickListener {
        void onPosterClick(int clickedPosterId);
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterImageView;

        PosterViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onPosterClickListener.onPosterClick(clickedPosition);
        }
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutIdForListItem = R.layout.movie_poster;

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Picasso.with(holder.posterImageView.getContext())
                .load(NetworkUtils.composeImageUrl(movie.getThumbnailURL()))
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMoviePosters(ArrayList<Movie> m) {
        movies = m;
        notifyDataSetChanged();
    }

    public Movie getMovieInfo(int id) {
        return movies.get(id);
    }
}
