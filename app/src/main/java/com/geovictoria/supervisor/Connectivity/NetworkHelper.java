package com.geovictoria.supervisor.Connectivity;

import android.net.Uri;
import android.util.JsonReader;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Maximiliano on 6/25/2015.
 */
public class NetworkHelper {
    public static String downloadUrl(String myurl, Object argument, String type) throws IOException {
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(type);
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a JSONObject
            if(response==200) {
                return readIt(is);
            }
            else{
                return null;
            }
        } catch (Exception e) {
            throw new IOException();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    public static  String readIt(InputStream stream) throws IOException, UnsupportedEncodingException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        return responseStrBuilder.toString();
    }
    public static class DateRange{
        public String initialDate;
        public String finalDate;
    }
}
