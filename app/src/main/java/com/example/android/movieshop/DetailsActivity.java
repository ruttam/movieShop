package com.example.android.movieshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieshop.utilities.Movie;
import com.example.android.movieshop.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.movieshop.MainActivity.NAME_OF_EXTRA;

public class DetailsActivity extends AppCompatActivity {

    private Movie movie = null;

    @BindView(R.id.poster_thumbnail) ImageView moviePoster;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_rate) TextView movieRating;
    @BindView(R.id.movie_releasedate) TextView movieReleaseDate;
    @BindView(R.id.movie_overview) TextView movieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra(NAME_OF_EXTRA)) {
            movie = parentIntent.getParcelableExtra(NAME_OF_EXTRA);
        }

        Picasso.with(this).load(NetworkUtils.composeImageUrl(movie.getThumbnailURL())).into(moviePoster);
        movieTitle.setText(movie.getTitle());
        movieRating.setText(movie.getVoteAverage());
        movieReleaseDate.setText(movie.getReleaseDate());
        movieOverview.setText(movie.getOverview());
    }
}
