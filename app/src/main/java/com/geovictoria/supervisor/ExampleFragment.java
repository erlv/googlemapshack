package com.geovictoria.supervisor;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geovictoria.supervisor.ExampleFragments.EmployeeListFragment;
import com.geovictoria.supervisor.ExampleFragments.EventListFragment;
import com.geovictoria.supervisor.ExampleFragments.LocationListFragment;
import com.geovictoria.supervisor.ExampleFragments.ProjectListFragment;
import com.geovictoria.supervisor.ExampleFragments.PunchListFragment;
import com.geovictoria.supervisor.ExampleFragments.TaskListFragment;
import com.geovictoria.supervisor.ExampleFragments.TeamListFragment;


public class ExampleFragment extends Fragment {

    private MyViewPagerAdapter adapter;

    // TODO: Rename and change types and number of parameters
    public static ExampleFragment newInstance(String param1, String param2) {
        ExampleFragment fragment = new ExampleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExampleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_example, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        return view;
    }

    public class MyViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        final int PAGE_COUNT = 7;
        // Tab Titles
        private String tabtitles[] = new String[]{"Project example", "Task example","Employee example", "Location example","Schedule example","Teams example","Punch example"};
        Context context;

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ProjectListFragment();
                case 1:
                    return new TaskListFragment();
                case 2:
                    return new EmployeeListFragment();
                case 3:
                    return new LocationListFragment();
                case 4:
                    return new EventListFragment();
                case 5:
                    return new TeamListFragment();
                case 6:
                    return new PunchListFragment();
            }
            return null;
        }
        private int lastPosition = -1;

        @Override
        public CharSequence getPageTitle(int position) {
            return tabtitles[position];
        }
    }
}

