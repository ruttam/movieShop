package com.example.android.movieshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieshop.utilities.Movie;
import com.example.android.movieshop.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import static com.example.android.movieshop.MainActivity.NAME_OF_EXTRA;

public class DetailsActivity extends AppCompatActivity {

    Movie movie = null;

    ImageView moviePoster;
    TextView movieTitle;
    TextView movieRating;
    TextView movieReleaseDate;
    TextView movieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        moviePoster = (ImageView) findViewById(R.id.poster_thumbnail);
        movieTitle = (TextView) findViewById(R.id.movie_title);
        movieRating = (TextView) findViewById(R.id.movie_rate);
        movieReleaseDate = (TextView) findViewById(R.id.movie_releasedate);
        movieOverview = (TextView) findViewById(R.id.movie_overview);

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
