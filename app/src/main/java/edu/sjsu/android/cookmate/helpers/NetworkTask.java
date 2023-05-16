package edu.sjsu.android.cookmate.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.preference.PreferenceManager;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// The Network Tasks extend the AsyncTask and takes care of the external network calls to be made.
public class NetworkTask extends AsyncTask<String, Void, String> {

    public interface NetworkTaskCallback {
        void onTaskFinished(String result);
    }

    private NetworkTaskCallback callback;
    private final SharedPreferences sharedPreferences;


    public NetworkTask(NetworkTaskCallback callback, Context context) {
        this.callback = callback;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        if(getFromCache(urls[0]) != null) {
            return getFromCache(urls[0]);
        }

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urls[0])
                    .build();

            Response okhttpResponse = client.newCall(request).execute();

            if (okhttpResponse.isSuccessful()) {
                response = okhttpResponse.body().string();
            }
            addToCache(urls[0], response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        callback.onTaskFinished(response);
    }

    // Add a response string to the cache map
    public void addToCache(String url, String response) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(url, response);
        editor.apply();

    }

    // Retrieve a response string from the cache map
    public String getFromCache(String url) {
        return sharedPreferences.getString(url, null);

    }
}

