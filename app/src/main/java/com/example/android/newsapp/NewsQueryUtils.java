package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * Created by Ramesh on 10/28/2016.
 */

public final class NewsQueryUtils {


    private String mURL;

    /*
     * Create a private constructor because no one should ever create a {@link NewsQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NewsQueryUtils (and an object instance of NewsQueryUtils is not needed).
     */
    private NewsQueryUtils()
    {

    }
    //
    public static ArrayList<News> fetchBookData(String stringUrl) {

        // Create URL object
        URL url = createUrl(stringUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.v("LOG_TAG", "Error with getting JSON response");

        }

        //Extract fields from JSON Response
        return extractNews(jsonResponse);

    }
    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<News> extractNews(String jsonResponse) {

        // Create an empty ArrayList that we can start adding news items
        ArrayList<News> newsArray = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of News item objects with the corresponding data.
            JSONObject newsJson = new JSONObject(jsonResponse);
            JSONObject responseJson = newsJson.getJSONObject("response");//get response JSON Object
            JSONArray results = responseJson.getJSONArray("results"); // get results array in response JSON object
            int length = results.length();

            for (int i = 0; i<length; i++) {
                JSONObject newsItem = results.getJSONObject(i);
                String section = newsItem.getString("sectionName");
                String title = newsItem.getString("webTitle");
                String date = newsItem.getString("webPublicationDate");
                String url = newsItem.getString("webUrl");
                Log.v("NewsQueryUtils: webUrl", url);

                newsArray.add(new News(section, title, date, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NewsQueryUtils", "Problem parsing the News JSON results", e);
        }

        // Return the list of books
        return newsArray;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("LOG_TAG", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.v("Response Code:", String.valueOf(responseCode));
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.v("IOException message: ", e.getMessage().toString());
            Log.v("IOException cause: ", e.getCause().toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


} //NewsQueryUtils Class