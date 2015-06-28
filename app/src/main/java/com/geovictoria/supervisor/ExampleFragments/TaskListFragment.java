package com.geovictoria.supervisor.ExampleFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.geovictoria.supervisor.R;
import com.google.gson.Gson;

import java.io.IOException;

import Model.Task;

/******************************************************************************************
 * This Fragment is an example  on how to retrieve all tasks for the company
 *
 * String response = NetworkHelper.downloadUrl(getString(R.string.taskUrl), null, "GET");
 * List<Task> tasks =new Gson().fromJson(response,Task[].class);
 *
 ********************************************************************************************/
public class TaskListFragment extends ListFragment {


    private ProgressDialog progressDialog;
    private Task[] tasks;

    public TaskListFragment() {
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
        ArrayAdapter<Task> myCustomAdapter = new ArrayAdapter<Task>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, tasks){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv1= (TextView) view.findViewById(android.R.id.text1);
                TextView tv2= (TextView) view.findViewById(android.R.id.text2);
                Task item = getItem(position);
                tv1.setText(item.name);
                tv2.setText("ID: " + item.id);
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
                String response = NetworkHelper.downloadUrl(getString(R.string.taskUrl), null, "GET");
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
                tasks=new Gson().fromJson(response,Task[].class);
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
