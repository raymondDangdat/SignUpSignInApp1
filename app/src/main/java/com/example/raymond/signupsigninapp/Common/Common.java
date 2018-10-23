package com.example.raymond.signupsigninapp.Common;

import com.example.raymond.signupsigninapp.Model;
import com.example.raymond.signupsigninapp.StaffUser;

public class Common {
    public static Model currentUser;

    public static StaffUser staffUser;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static String converCodeToStatus(String code){
        if (code.equals("0")){
            return "Placed";
        }
        else  if (code.equals("1")){
            return "On my way";
        }
        else {
            return "Shipped";
        }

    }




}
