package com.geovictoria.supervisor;

/**
 * Created by trijones on 6/27/15.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.util.Log;

public class GetDistanceTask extends AsyncTask {
    LatLng dest;
    ArrayList<EmployeeCurloc> employees;
    ArrayList<Distance> distance;
    RealTimeLocation a;

    @Override
    protected Object doInBackground(Object... params) {
        // TODO Auto-generated method stub
        for (EmployeeCurloc e : employees) {
            try {
                if (e.loc == null) continue;
                HttpClient client = new DefaultHttpClient();
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                        e.loc.getLatitude() + "," + e.loc.getLongitude() + "&destinations=" +
                        dest.latitude + "," + dest.longitude +
                        "&mode=driving&key=AIzaSyBS4ozqNqmjgqwKkyzSQJP8X3Hhp-UwjSQ";
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }
                JSONTokener tokener = new JSONTokener(builder.toString());
                //JSONArray finalResult = new JSONArray(tokener);
                JSONObject finalResult2 = new JSONObject(tokener);
                JSONArray row = finalResult2.getJSONArray("rows");
                JSONObject elements = row.getJSONObject(0);
                JSONArray dj = elements.getJSONArray("elements");
                JSONObject dj2 = dj.getJSONObject(0);
                JSONObject dj3 = dj2.getJSONObject("distance");
                JSONObject dj4 = dj2.getJSONObject("duration");
                Distance d = new Distance(dj3.getDouble("value"), dj4.getDouble("value"),
                        dj3.getString("text"), dj4.getString("text"), e);
                Log.d("jo", "asd");
                distance.add(d);
            } catch (Exception e100) {
            }
        }
        Collections.sort(distance);
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        a.showInfo(distance);
    }

    public ArrayList<Distance> getSorted() {
        return distance;
    }

    public void passData(LatLng dest, ArrayList<EmployeeCurloc> employees, RealTimeLocation a) {
        // TODO Auto-generated method stub
        this.dest = dest;
        this.employees = employees;
        this.a = a;
        distance = new ArrayList<Distance>();
    }
}
