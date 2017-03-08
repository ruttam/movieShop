package com.example.android.movieshop.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String MOVIEDB_BASE_URL =
            "http://api.themoviedb.org/3/movie/";
    private final static String MOVIEDB_IMAGE_URL =
            "http://image.tmdb.org/t/p/";

    private static String sortBy = "";

    private final static String PARAM_API = "api_key";
    private final static String api = "";

    public static URL buildUrl(Sort sortParam) {
        setSortParams(sortParam);
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(sortBy)
                .appendQueryParameter(PARAM_API, api)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static void setSortParams(Sort s) {
        switch (s) {
            case HIGHEST_RATED:
                sortBy = "top_rated";
                break;
            case POPULAR:
                sortBy = "popular";
                break;
            default:
                break;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String composeImageUrl(String path) {
        String imageSize = "w342";
        return MOVIEDB_IMAGE_URL + imageSize + path;
    }
}
