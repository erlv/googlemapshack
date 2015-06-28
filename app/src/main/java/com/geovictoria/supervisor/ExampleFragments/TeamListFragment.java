package com.geovictoria.supervisor.ExampleFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.geovictoria.supervisor.Connectivity.NetworkHelper;
import com.geovictoria.supervisor.Helper.DateHelper;
import com.geovictoria.supervisor.R;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import Model.Employee;
import Model.Event;
import Model.Team;

/******************************************************************************************
 * This Fragment is an example  on how to retrieve all teams for the company
 *
 * String response = NetworkHelper.downloadUrl(getString(R.string.teamUrl), null, "GET");
 * List<Team> teams =new Gson().fromJson(response,Team[].class);
 *
 ********************************************************************************************/
public class TeamListFragment extends ListFragment {


    private ProgressDialog progressDialog;
    private Team[] teams;

    public TeamListFragment() {
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
        textView.setText("This example displays all the teams of the company");
        getListView().addHeaderView(textView);
        loadDataFromApi();
    }

    private void setMyListAdapter() {

        ArrayAdapter<Team> myCustomAdapter = new ArrayAdapter<Team>(getActivity(), R.layout.mysimplerow, R.id.rowTitle, teams){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Team item = getItem(position);
                LinearLayout linearLayout=new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView tv=new TextView(getActivity());
                tv.setText(item.name);
                tv.setTextSize(30);
                linearLayout.addView(tv);
                TextView tv2=new TextView(getActivity());
                tv2.setText("ID :" + item.id);
                linearLayout.addView(tv2);
                for(Employee e:item.employees){
                    LinearLayout ll=new LinearLayout(getActivity());
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    TextView name=new TextView(getActivity());
                    name.setText(e.userName);
                    ll.addView(name);

                    TextView userId=new TextView(getActivity());
                    userId.setText(" ID: "+e.userId);
                    ll.addView(userId);

                    linearLayout.addView(ll);
                }
                return linearLayout;
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
                String response = NetworkHelper.downloadUrl(getString(R.string.teamUrl), null, "GET");
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
                teams =new Gson().fromJson(response,Team[].class);
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

