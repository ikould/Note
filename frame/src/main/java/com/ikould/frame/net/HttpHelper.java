package com.ikould.frame.net;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Okhttp封装Helper
 */
@Keep
public class HttpHelper {
    private static OkHttpClient client = new OkHttpClient();

    private HttpHelper() {
        throw new AssertionError("No instances");
    }

    /**
     * 同步get
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        } else {
            return response.body().string();
        }

    }

    /**
     * 异步get
     *
     * @param map
     * @param url
     * @param callback
     */
    public static void getSync(Map <String, Object> map, @NonNull String url, @NonNull final OkhttpCallBack callback) {
        final Request request;
        String urlStr = "?";
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                urlStr += entry.getKey().toString() + "=" + entry.getValue().toString() + "&";
            }
        }
        urlStr.substring(0, urlStr.length() - 1);
        request = new Request.Builder().url(url + urlStr).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        callback.onResponse(new JSONObject(response.body().string()));
                    } else {
                        throw new IOException("can't get the receive");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(response.request(), e);
                }
            }
        });

    }

    /**
     * 异步post
     *
     * @param map
     * @param url
     * @param callback
     */
    public static void postfrom(Map <String, Object> map, @NonNull String url, @NonNull final OkhttpCallBack callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                builder.add(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        RequestBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        callback.onResponse(jsonObject);
                    } else {
                        throw new IOException("can't get the receive");
                    }
                } catch (Exception e) {
                    callback.onFailure(response.request(), e);
                }
            }
        });


    }

    public interface OkhttpCallBack {
        void onFailure(Request request, Exception e);

        void onResponse(JSONObject jsonObject);
    }
}
