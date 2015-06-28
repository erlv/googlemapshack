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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RealTimeLocation extends Fragment {

    private GoogleMap map;
    private EditText location;
    private ArrayList<EmployeeCurloc> employees;
    LinearLayout ll;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static RealTimeLocation newInstance(String param1, String param2) {
        RealTimeLocation fragment = new RealTimeLocation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RealTimeLocation() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_current_location, container, false);

        ll = (LinearLayout) (v.findViewById(R.id.l1));
        addTest();

        map = ((MapFragment) this.getActivity().getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        location = (EditText) v.findViewById(R.id.location);
        for (EmployeeCurloc employee : employees) {
            if (employee.loc != null) {
                map.addMarker(new MarkerOptions().position(new LatLng(
                        employee.loc.getLatitude(), employee.loc.getLongitude()))
                        .title(employee.userName));
            }
        }

        return v;
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
            TextView txt = new TextView(this.getActivity().getApplicationContext());
            txt.setText(d.employee.userName + "time :" + d.time2 +
                    "distance :" + d.distance2);
            ll.addView(txt);
        }
        for (EmployeeCurloc employee : employees) {
            if (employee.loc == null) {
                TextView txt = new TextView(this.getActivity().getApplicationContext());
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
        Context context = this.getActivity().getApplicationContext();
        CharSequence text = "Sorry your location could not be found please try again!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this.getActivity().getApplicationContext());
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