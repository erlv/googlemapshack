package com.geovictoria.supervisor;

/**
 * Created by trijones on 6/27/15.
 */
public class Distance implements Comparable<Distance>{
    EmployeeCurloc employee;
    double distance;
    double time;
    String distance2;
    String time2;
    public Distance(double d, double t, String d2, String t2, EmployeeCurloc e){
        distance = d;
        time = t;
        distance2 = d2;
        time2 = t2;
        employee = e;
    }
    @Override
    public int compareTo(Distance d2) {
        // TODO Auto-generated method stub
        if(time == d2.time) return 0;
        if(time>d2.time) return 1;
        return -1;
    }
}
