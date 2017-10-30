package com.zara.pl.io.server;

import com.zara.pl.util.Constant;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.zara.pl.util.Constant.Util.UTF_8;

public class HttpRequestManager {

    public static HttpURLConnection executeRequest(String apiUrl, String requestMethod, String postData) {
        HttpURLConnection connection = null;

        try {
            URL ulr = new URL(apiUrl);
            connection = (HttpURLConnection) ulr.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setUseCaches(false);

            switch (requestMethod) {
                case Constant.RequestMethod.PUT:
                case Constant.RequestMethod.POST:
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.connect();
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(postData != null ? postData.getBytes(UTF_8) : new byte[]{0});
                    outputStream.flush();
                    outputStream.close();
                    break;

                case Constant.RequestMethod.GET:
                    connection.connect();
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
