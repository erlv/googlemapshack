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

import Model.Location;
import Model.Punch;
/******************************************************************************************
 * This Fragment work as an example  on how to retrieve all punches within a date range.
 * To retrieve user punches between $date1 and $date2 for user $user
 * Uri.Builder builder = new Uri.Builder();
 * builder.encodedPath(getString(R.string.punchUrl))
 * .appendQueryParameter("initialDate", DateHelper.format($date1))
 * .appendQueryParameter("finalDate", DateHelper.format($date2))
 * .appendQueryParameter("userId","$user");
 * String response = NetworkHelper.downloadUrl(builder.toString(), null, "GET");
 * List<Punch> punches =new Gson().fromJson(response,Punch[].class);
 *
 ********************************************************************************************/
public class PunchListFragment extends ListFragment {


    private ProgressDialog progressDialog;
    private Punch[] punches ;

    public PunchListFragment() {
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
        textView.setText("This example displays all locations reported by a user the 25'th of june.\n " );
        getListView().addHeaderView(textView);
        loadDataFromApi();
    }

    private void setMyListAdapter() {

        ArrayAdapter<Punch> myCustomAdapter = new ArrayAdapter<Punch>(getActivity(), R.layout.punchrow, R.id.isShiftPunch, punches){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Punch item = getItem(position);
                ((TextView) view.findViewById(R.id.isShiftPunch)).setText(item.isShiftPunch?"Shift punch":"Activity punch");
                ((TextView) view.findViewById(R.id.lat)).setText("Lat : "+item.lat);
                ((TextView) view.findViewById(R.id.lon)).setText("Lon : "+item.lon);
                ((TextView) view.findViewById(R.id.type)).setText(item.type);
                ((TextView) view.findViewById(R.id.userId)).setText("Id :"+item.userId);
                ((TextView) view.findViewById(R.id.taskId)).setText("Task id : " + item.taskId);
                ((TextView) view.findViewById(R.id.projectId)).setText("Project id: " + item.projectId);
                return view;
            }
        };
        setListAdapter(myCustomAdapter);
    }

    public void loadDataFromApi() {
        PunchFetcher punchFetcher = new PunchFetcher();
        punchFetcher.execute();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
    class PunchFetcher extends AsyncTask<Void, String, String> {
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
                builder.encodedPath(getString(R.string.punchUrl))
                        .appendQueryParameter("initialDate", DateHelper.formatCalendar(calendar1))
                        .appendQueryParameter("finalDate", DateHelper.formatCalendar(calendar2))
                        .appendQueryParameter("userId","82045");
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
                punches =new Gson().fromJson(response,Punch[].class);
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