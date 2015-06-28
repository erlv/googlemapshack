package com.geovictoria.supervisor;

/**
 * Created by trijones on 6/27/15.
 */
import android.location.Location;

import Model.Employee;

public class EmployeeCurloc extends Employee {

    public Location loc;

    public EmployeeCurloc(Employee curEmp) {
        this.userId = curEmp.userId;
        this.userName = curEmp.userName;
        this.loc = null;
    }
    public EmployeeCurloc() {
        //Empty
    }
    public EmployeeCurloc(int userId, String userName, Location loc) {
        this.userId = userId;
        this.userName = userName;
        this.loc = loc;
    }

    @Override
    public String toString() {
        return this.userName;
    }
}
