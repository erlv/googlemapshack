package com.geovictoria.supervisor.ExampleFragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.geovictoria.supervisor.Connectivity.NetworkHelper;
import com.geovictoria.supervisor.Helper.DateHelper;
import com.geovictoria.supervisor.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import Model.Event;
import Model.Location;
/******************************************************************************************
 * This Fragment is an example  on how to retrieve all Locations programmed for a date range.
 * To retrieve locations between $date1 and $date2 for user $user
 *
 * Uri.Builder builder = new Uri.Builder();
 * builder.encodedPath(getString(R.string.locationUrl))
 * .appendQueryParameter("initialDate", DateHelper.format($date1))
 * .appendQueryParameter("finalDate", DateHelper.format($date2))
 * .appendQueryParameter("userId","$user");
 * String response = NetworkHelper.downloadUrl(builder.toString(), null, "GET");
 * List<Location> locations =new Gson().fromJson(response,Location[].class);
 *
 ********************************************************************************************/

public class LocationListFragment extends ListFragment {


    private ProgressDialog progressDialog;
    private Location[] locations;

    public LocationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = new Button(getActivity());
        button.setText("RELOAD");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Downloading");
                progressDialog.show();
                loadDataFromApi();
            }
        });
        getListView().addHeaderView(button);
        TextView textView = new TextView(getActivity());
        textView.setText("This example displays all locations reported by a user the 25'th of june");
        getListView().addHeaderView(textView);
        loadDataFromApi();
    }

    private void setMyListAdapter() {

        ArrayAdapter<Location> myCustomAdapter = new ArrayAdapter<Location>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, locations){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv1= (TextView) view.findViewById(android.R.id.text1);
                TextView tv2= (TextView) view.findViewById(android.R.id.text2);
                Location item = getItem(position);
                tv1.setText(item.date);
                String rowContent = String.format("lat: %f, lon %f, err %f", item.lat, item.lon, item.err);
                tv2.setText(rowContent);
                return view;
            }
        };
        setListAdapter(myCustomAdapter);
    }

    public void loadDataFromApi() {
        LocationFetcher locationFetcher = new LocationFetcher();
        locationFetcher.execute();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
    class LocationFetcher extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // DUMMY DATE
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(2015, Calendar.JUNE, 25,0,0);
                Calendar calendar2=Calendar.getInstance();
                calendar2.set(2015, Calendar.JUNE, 26,0,0);

                NetworkHelper.DateRange argument =new NetworkHelper.DateRange();
                Uri.Builder builder = new Uri.Builder();
                builder.encodedPath(getString(R.string.locationUrl))
                        .appendQueryParameter("initialDate", DateHelper.formatCalendar(calendar1))
                        .appendQueryParameter("finalDate", DateHelper.formatCalendar(calendar2))
                        .appendQueryParameter("userId", "82045");
                String response = NetworkHelper.downloadUrl(builder.toString(), null, "GET");
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(String response) {
            if(progressDialog!=null&&progressDialog.isShowing())
                progressDialog.dismiss();
            if(response==null){showError();return;}
            try {
                locations =new Gson().fromJson(response,Location[].class);
            } catch (Exception e) {
                showError();return;
            }
            setMyListAdapter();

        }
    }
    private void showError() {
        new AlertDialog.Builder(getActivity())
                .setTitle("ERROR")
                .setMessage("The data couldn't be retrieved from the server")
                .setPositiveButton("Ok", null)
                .show();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                new String[]{"NO DATA"}));
    }
}