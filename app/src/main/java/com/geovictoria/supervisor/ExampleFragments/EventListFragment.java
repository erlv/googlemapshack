package com.geovictoria.supervisor.ExampleFragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.style.TtsSpan;
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
import java.util.Date;

import Model.Event;
import Model.Task;

/******************************************************************************************
 * This Fragment is an example  on how to retrieve all events programmed for a date range.
 * To retrieve events between $date1 and $date2
 *
 * Uri.Builder builder = new Uri.Builder();\n" +
 * builder.encodedPath(getString(R.string.eventUrl))
 * .appendQueryParameter("initialDate", DateHelper.format($date1))
 * .appendQueryParameter("finalDate", DateHelper.format($date2));
 * String response = NetworkHelper.downloadUrl(builder.toString(), null, "GET");
 * List<Punch> punches =new Gson().fromJson(response,Punch[].class);
 *
 ********************************************************************************************/
public class EventListFragment extends ListFragment {


    private ProgressDialog progressDialog;
    private Event[] events;

    public EventListFragment() {
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
        textView.setText("This example displays all events programmed for the month on JUNE");
        getListView().addHeaderView(textView);
        loadDataFromApi();
    }

    private void setMyListAdapter() {

        ArrayAdapter<Event> myCustomAdapter = new ArrayAdapter<Event>(getActivity(), R.layout.mysimplerow, R.id.rowTitle, events){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv1= (TextView) view.findViewById(R.id.rowTitle);
                TextView tv2= (TextView) view.findViewById(R.id.textView2);
                TextView tv3= (TextView) view.findViewById(R.id.textView3);
                TextView tv4= (TextView) view.findViewById(R.id.textView4);
                Event item = getItem(position);
                tv1.setText(item.dateString);
                tv2.setText("ID: " + item.id);
                tv3.setText("Team id: " + item.teamId);
                tv4.setText("Project id: " + item.projectId);
                return view;
            }
        };
        setListAdapter(myCustomAdapter);
    }

    public void loadDataFromApi() {
        TaskFetcher projectFetcher = new TaskFetcher();
        projectFetcher.execute();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
    class TaskFetcher extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // DUMMY DATE
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(2015, Calendar.JUNE, 1);
                Calendar calendar2=Calendar.getInstance();
                calendar2.set(2015, Calendar.JUNE, 30);
                NetworkHelper.DateRange argument =new NetworkHelper.DateRange();
                Uri.Builder builder = new Uri.Builder();
                builder.encodedPath(getString(R.string.eventUrl))
                .appendQueryParameter("initialDate",DateHelper.formatCalendar(calendar1))
                .appendQueryParameter("finalDate", DateHelper.formatCalendar(calendar2));
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
                events =new Gson().fromJson(response,Event[].class);
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

