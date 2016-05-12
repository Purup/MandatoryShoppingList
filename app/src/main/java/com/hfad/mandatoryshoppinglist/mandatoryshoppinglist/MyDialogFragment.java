package com.hfad.mandatoryshoppinglist.mandatoryshoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Jesper on 14-Mar-16.
 */
public class MyDialogFragment extends DialogFragment {

    public Dialog onCreatDialog(Bundle savedInstance){
        AlertDialog.Builder alert
                = new AlertDialog.Builder(getActivity());
        alert.setTitle("Confirmation");
        alert.setMessage("Clear the list?");
        alert.setPositiveButton("Yes", pListener);
        alert.setNegativeButton("No", nListener);

        return alert.create();
    }

    DialogInterface.OnClickListener pListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            // these will be executed when user click Yes button
            positiveClick();
        }
    };

    DialogInterface.OnClickListener nListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            // these will be executed when user click No button
            negativeClick();
        }
    };

    protected void positiveClick()
    {

    }
    protected void negativeClick()
    {

    }
}

