package com.example.acs;

import android.app.Activity;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//This class performs the internet requests (demonstrated in one of the lecture videos)
public class PHPRequest {

    //Method for internet requests with no parameters
    public void doRequest(Activity a, String url, RequestHandler rh){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rh.processResponse(responseData);
                    }
                });
            }
        });
    }

    //Method for internet requests with one parameter
    public void doRequestWithParameters(Activity a, String url, String name, String value, RequestHandler rh){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.get(url).newBuilder();
        urlBuilder.addQueryParameter(name, value);

        url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rh.processResponse(responseData);
                    }
                });
            }
        });
    }

    //Method for internet requests with two parameters
    public void doRequestWithParameters2(Activity a, String url, String name1, String value1, String name2, String value2, RequestHandler rh){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.get(url).newBuilder();
        urlBuilder.addQueryParameter(name1, value1);
        urlBuilder.addQueryParameter(name2, value2);

        url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rh.processResponse(responseData);
                    }
                });
            }
        });
    }

    //Method for internet requests with four parameters
    public void doRequestWithParameters4(Activity a, String url, String name1, String value1, String name2, String value2, String name3, String value3, String name4, String value4, RequestHandler rh){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.get(url).newBuilder();
        urlBuilder.addQueryParameter(name1, value1);
        urlBuilder.addQueryParameter(name2, value2);
        urlBuilder.addQueryParameter(name3, value3);
        urlBuilder.addQueryParameter(name4, value4);

        url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rh.processResponse(responseData);
                    }
                });
            }
        });
    }

    //Method for internet requests with an unknown number of parameters
    public void doRequestWithManyParameters(Activity a, String url, ArrayList<Parameter> parameters, RequestHandler rh){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.get(url).newBuilder();

        //for the size of the array
        for(int i = 0; i < parameters.size(); i++){
            urlBuilder.addQueryParameter(parameters.get(i).name, parameters.get(i).value);
        }

        url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rh.processResponse(responseData);
                    }
                });
            }
        });
    }
}
