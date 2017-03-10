package com.example.android.movieshop.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.movieshop.MainActivity;
import com.example.android.movieshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MovieDBQueryTask extends AsyncTask<Sort, Void, ArrayList<Movie>> {
    private AsyncTaskListener<ArrayList<Movie>> listener;

    private Sort sortBy = Sort.POPULAR;
    private ProgressBar mProgressBar;

    public MovieDBQueryTask(Context c, MainActivity.MovieDBQueryTaskListener listener) {
        this.listener = listener;
        mProgressBar = (ProgressBar) ((Activity) c).findViewById(R.id.progress_bar);
    }

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
        listener.onTaskComplete(movies);
    }

}
