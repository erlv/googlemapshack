package com.geovictoria.supervisor.ExampleFragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.geovictoria.supervisor.Connectivity.NetworkHelper;
import com.geovictoria.supervisor.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import Model.Employee;

public class EmployeeListFragment extends ListFragment {


    private Employee[] employees;
    private ProgressDialog progressDialog;


    /******************************************************************************************
     * This Fragment is an example  on how to retrieve all employees for the company
     *
     * String response = NetworkHelper.downloadUrl(getString(R.string.employeeUrl), null, "GET");
     * List<Employee> employees =new Gson().fromJson(response,Employee[].class);
     *
     ********************************************************************************************/

    public EmployeeListFragment() {
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
        ArrayAdapter<Employee> myCustomAdapter = new ArrayAdapter<Employee>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, employees){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv1= (TextView) view.findViewById(android.R.id.text1);
                TextView tv2= (TextView) view.findViewById(android.R.id.text2);
                Employee item = getItem(position);
                tv1.setText(item.userName);
                tv2.setText("ID: " + item.userId);
                return view;
            }
        };
        setListAdapter(myCustomAdapter);
    }

    public void loadDataFromApi() {
        EmployeeFetcher employeeFetcher = new EmployeeFetcher();
        employeeFetcher.execute();
    }

    class EmployeeFetcher extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                String response = NetworkHelper.downloadUrl(getString(R.string.employeeUrl), null, "GET");
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
                if(response==null){
                    showError();
                    return;}
                try {
                    employees=new Gson().fromJson(response,Employee[].class);
                } catch (Exception e) {
                    showError();return;
                }

                setMyListAdapter();

        } // protected void onPostExecute(Void v)
    } //class EmployeeFetcher extends AsyncTask<String, String, Void>

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
