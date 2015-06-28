package com.geovictoria.supervisor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import Model.Employee;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DashboardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MapView mMapView;
    private GoogleMap googleMap;
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    ArrayList<LatLng> list = new ArrayList<LatLng>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DashboardFragment() {
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
        View v = inflater.inflate(R.layout.fragment_dashboard, container,
                false);

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Perform any camera updates here
        final List<EmployeeCurloc> employees = basicTest();
        setupMap(employees);

        ArrayAdapter<EmployeeCurloc> adapter = new ArrayAdapter<EmployeeCurloc>(getActivity(), android.R.layout.simple_list_item_1, employees);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmployeeCurloc emp = employees.get(position);
                LatLng loc = new LatLng(emp.loc.getLatitude(), emp.loc.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12.0f));
            }
        });

        return v;
    }

    private void setupMap(List<EmployeeCurloc> employees) {
        googleMap = mMapView.getMap();

        for(EmployeeCurloc employee : employees) {
            if (employee.loc != null) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(
                        employee.loc.getLatitude(), employee.loc.getLongitude()))
                        .title(employee.userName));
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private List<EmployeeCurloc> basicTest() {
        // TODO Auto-generated method stub
        List<EmployeeCurloc> employees = new ArrayList<EmployeeCurloc>();
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
        return employees;
    }

    public Location create_Location(double lat, double lon, double err, String date) {
        Location res = new Location("");
        res.setLatitude(lat);
        res.setLongitude(lon);
        return res;

    }

}
