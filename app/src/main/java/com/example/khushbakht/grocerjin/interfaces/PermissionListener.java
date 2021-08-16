package com.example.khushbakht.grocerjin.interfaces;

import java.util.ArrayList;

/**
 * Created by khush on 10/11/2017.
 */

public interface PermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(ArrayList<String> deniedPermissions);

}