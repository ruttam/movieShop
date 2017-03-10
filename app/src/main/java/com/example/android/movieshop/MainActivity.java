package com.example.android.movieshop;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.movieshop.utilities.AsyncTaskListener;
import com.example.android.movieshop.utilities.Movie;
import com.example.android.movieshop.utilities.MovieDBQueryTask;
import com.example.android.movieshop.utilities.PosterAdapter;
import com.example.android.movieshop.utilities.Sort;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterClickListener {

    public final static String NAME_OF_EXTRA = "movie";

    private RecyclerView recyclerView;
    private PosterAdapter adapter;

    private TextView mErrorMessage;
    private ProgressBar mProgressBar;

    private Sort sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sortBy = Sort.POPULAR;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_posters);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(manager);

        adapter = new PosterAdapter(this);
        recyclerView.setAdapter(adapter);

        mErrorMessage = (TextView) findViewById(R.id.error_message_display);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        makeMovieDBSearchQuery(sortBy);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 300;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.most_popular) {
            sortBy = Sort.POPULAR;

        } else if (menuItemThatWasSelected == R.id.highest_rated) {
            sortBy = Sort.HIGHEST_RATED;
        }
        makeMovieDBSearchQuery(sortBy);
        return super.onOptionsItemSelected(item);
    }

    private void makeMovieDBSearchQuery(Sort s) {
        if(isOnline()) {
            showPosterDataView();
            new MovieDBQueryTask(this, new MovieDBQueryTaskListener()).execute(s);
        } else {
            showErrorMessage();
        }
    }

    private void showPosterDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPosterClick(int clickedPosterId) {
        Movie movie = adapter.getMovieInfo(clickedPosterId);

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(NAME_OF_EXTRA, movie);
        startActivity(intent);
    }

    public class MovieDBQueryTaskListener implements AsyncTaskListener<ArrayList<Movie>> {

        @Override
        public void onTaskComplete(ArrayList<Movie> movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (!movies.isEmpty()) {
                showPosterDataView();
                adapter.setMoviePosters(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
