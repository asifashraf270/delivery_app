package com.example.khushbakht.grocerjin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by SAG-E-ATTAR on 8/18/2017.
 */

public class Checks {

  private ProgressDialog dialog;



    public void showAlert(Context context, String message, String btn)
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        //  builder1.setTitle("Dialog Box");
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();



    }



}
