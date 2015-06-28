package com.geovictoria.supervisor.ExampleFragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.geovictoria.supervisor.Connectivity.NetworkHelper;
import com.geovictoria.supervisor.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import Model.Project;


/******************************************************************************************
 * This Fragment is an example  on how to retrieve all projects for the company
 *
 * String response = NetworkHelper.downloadUrl(getString(R.string.projectUrl), null, "GET");
 * List<Project> projects =new Gson().fromJson(response,Project[].class);
 *
 ********************************************************************************************/

public class ProjectListFragment extends ListFragment {
    private Project[] projects;
    private ProgressDialog progressDialog;

    public ProjectListFragment() {
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
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("Downloading");
                progressDialog.show();
                loadDataFromApi();
            }
        });
        getListView().addHeaderView(button);
        loadDataFromApi();
    }

    private void setMyListAdapter() {
        ArrayAdapter<Project> myCustomAdapter = new ArrayAdapter<Project>(getActivity(), R.layout.mysimplerow, R.id.rowTitle, projects){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv1= (TextView) view.findViewById( R.id.rowTitle);
                LinearLayout mLayout= (LinearLayout) view.findViewById(R.id.myLinearLayout);
                Project item = getItem(position);
                tv1.setText(item.name);
                TextView tv2= (TextView) view.findViewById(R.id.textView2);
                tv2.setText("Lat: "+item.lat);
                TextView tv3=(TextView) view.findViewById(R.id.textView3);
                tv3.setText("Lon: "+item.lon);
                TextView tv4=(TextView) view.findViewById(R.id.textView4);
                tv4.setText("id: " + item.id);
                return view;
            }
        };
        setListAdapter(myCustomAdapter);
    }

    public void loadDataFromApi() {
        ProjectFetcher projectFetcher = new ProjectFetcher();
        projectFetcher.execute();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
    class ProjectFetcher extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                String response = NetworkHelper.downloadUrl(getString(R.string.projectUrl), null, "GET");
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
                projects=new Gson().fromJson(response,Project[].class);
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
                new String[] { "NO DATA" }));
    }



}
