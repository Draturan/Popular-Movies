package com.example.simone.popularmovies.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.simone.popularmovies.Utils.ApiNetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


/**
 * Created by Simone on 28/03/2018 for Popular-Movies project
 */
public class RetrieveMoviesInformationsTask extends AsyncTask<URL, Void, String> {

    private static final String TAG = RetrieveMoviesInformationsTask.class.toString();

    private Context context;
    private AsyncTaskCompleteListener<String> listener;


    public RetrieveMoviesInformationsTask(Context ctx, AsyncTaskCompleteListener<String> listener){
        this.context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreTaskExecute();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL requestUrl= urls[0];
        String result = null;
        try{
            String answer = ApiNetworkUtils.getResponseFromHttpUrl(requestUrl);
            try{
                JSONObject jsonObject = new JSONObject(answer);
                result = jsonObject.getString("results");
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onTaskComplete(s);
    }
}
