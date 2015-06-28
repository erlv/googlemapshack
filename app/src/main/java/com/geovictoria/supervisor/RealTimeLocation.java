package com.geovictoria.supervisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONTokener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RealTimeLocation extends Activity {

    private GoogleMap map;
    private EditText location;
    private ArrayList<EmployeeCurloc> employees;
    LinearLayout ll;

    //static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    //ArrayList<LatLng> list = new ArrayList<LatLng>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_current_location);
        ll = (LinearLayout) (findViewById(R.id.l1));
        addTest();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        location = (EditText) findViewById(R.id.location);
        for (EmployeeCurloc employee : employees) {
            if (employee.loc != null) {
                map.addMarker(new MarkerOptions().position(new LatLng(
                        employee.loc.getLatitude(), employee.loc.getLongitude()))
                        .title(employee.userName));
            }
        }
    }

    private void addTest() {
        // TODO Auto-generated method stub
        employees = new ArrayList<EmployeeCurloc>();
        Location loc1 = this.create_Location(37.391641,
                -122.0427257, 7, "2015-02-23 19:57:30");
        EmployeeCurloc tmp1 = new EmployeeCurloc(1, "Name Hello", loc1);
        Location loc2 = this.create_Location(37.391641,
                -122.0427257, 30, "2015-02-22 19:57:30");
        EmployeeCurloc tmp2 = new EmployeeCurloc(2, "Name World", loc2);

        Location loc3 = this.create_Location(37.3852647,
                -122.0373613, 7, "2015-02-23 19:57:30");
        EmployeeCurloc tmp3 = new EmployeeCurloc(3, "Name Hell0o", loc3);
        Location loc4 = this.create_Location(37.3852647,
                -122.0373613, 30, "2015-02-22 19:57:30");
        EmployeeCurloc tmp4 = new EmployeeCurloc(4, "Name World0", loc4);
        Location loc5 = this.create_Location(37.3898679,
                -122.0182639, 7, "2015-02-23 19:57:30");
        EmployeeCurloc tmp5 = new EmployeeCurloc(5, "Name Hello2", loc5);
        Location loc6 = this.create_Location(37.3898679,
                -122.0182639, 30, "2015-02-22 19:57:30");
        EmployeeCurloc tmp6 = new EmployeeCurloc(6, "Name World3", loc6);
        employees.add(tmp1);
        employees.add(tmp2);
        employees.add(tmp3);
        employees.add(tmp4);
        employees.add(tmp5);
        employees.add(tmp6);
    }

    public void showInfo(ArrayList<Distance> dist) {
        for (Distance d : dist) {
            TextView txt = new TextView(this);
            txt.setText(d.employee.userName + "time :" + d.time2 +
                    "distance :" + d.distance2);
            ll.addView(txt);
        }
        for (EmployeeCurloc employee : employees) {
            if (employee.loc == null) {
                TextView txt = new TextView(this);
                txt.setText(employee.userName + "is free");
                ll.addView(txt);
            }
        }
    }

    public Location create_Location(double lat, double lon, double err, String date) {
        Location res = new Location("as");
        res.setLatitude(lat);
        res.setLongitude(lon);
        return res;
    }

    public void click(View view) {
        ll.removeAllViews();
        LatLng dest = getLocationFromAddress(location.getText().toString().trim());
        if (dest == null) {
            destError();
            location.setText("");
            return;
        }
        map.addMarker(new MarkerOptions().position(dest).
                icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Destination"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(dest, 20.0f));
        //calcDistance(dest);
        GetDistanceTask gdt = new GetDistanceTask();
        gdt.passData(dest, employees, this);
        gdt.execute();
    }

    private void destError() {
        // TODO Auto-generated method stub
        Context context = getApplicationContext();
        CharSequence text = "Sorry your location could not be found please try again!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /*public void click (View view){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 12.0f));
        Thread t = new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    HttpClient client = new DefaultHttpClient();
                    double lat1 = HAMBURG.latitude;
                    double long1 = HAMBURG.longitude;
                    double lat2 = 52.5167;
                    double long2 = 13.3833;
                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                            "37.386051,-122.083855&destinations=Sunnyvale&mode=driving&key=AIza" +
                            "SyCVexmd9R_RhrcIwv9Z-nZYRxH5ZnZEq6c";
                    HttpGet request = new HttpGet(url);
                    Log.d("Reached Here", "HAHA");
                    HttpResponse response = client.execute(request);
                    Log.d("Response of GET request", response.toString());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONArray finalResult = new JSONArray(tokener);
                }catch (Exception e){

                }
            }

        });
        t.start();
    }*/
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            return (new LatLng(location.getLatitude(), location.getLongitude()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}