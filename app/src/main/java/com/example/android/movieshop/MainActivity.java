package com.example.android.movieshop;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.movieshop.utilities.Movie;
import com.example.android.movieshop.utilities.NetworkUtils;
import com.example.android.movieshop.utilities.PosterAdapter;
import com.example.android.movieshop.utilities.Sort;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new PosterAdapter(this);
        recyclerView.setAdapter(adapter);

        mErrorMessage = (TextView) findViewById(R.id.error_message_display);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        makeMovieDBSearchQuery(sortBy);
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
            new MovieDBQueryTask().execute(s);
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

    private class MovieDBQueryTask extends AsyncTask<Sort, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Sort... sort) {
            if (sort.length != 0) {
                sortBy = sort[0];
            }
            String movieDBRequestResults;
            try {
                movieDBRequestResults = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl(sortBy));
                return jsonObjectToArray(movieDBRequestResults);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Movie> jsonObjectToArray(String s) {
            ArrayList<Movie> m = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray arrayOfMovies = object.getJSONArray("results");
                for (int i = 0; i < arrayOfMovies.length(); i++) {
                    JSONObject o = arrayOfMovies.getJSONObject(i);
                    m.add(new Movie(o.getString("poster_path"),
                            o.getString("original_title"),
                            o.getString("overview"),
                            o.getString("vote_average"),
                            o.getString("release_date")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return m;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (!movies.isEmpty()) {
                showPosterDataView();
                adapter.setMoviePosters(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
