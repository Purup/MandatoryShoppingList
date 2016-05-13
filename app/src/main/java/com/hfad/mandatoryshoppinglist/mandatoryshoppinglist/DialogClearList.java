package com.hfad.mandatoryshoppinglist.mandatoryshoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Puriiqt on 09/05/2016.
 */
public class DialogClearList extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Clear");
        alert.setMessage("Are you sure you want to clear the list?");
        alert.setPositiveButton("Yes", pListener);
        alert.setNegativeButton("No", nListener);

        return alert.create();
    }

    DialogInterface.OnClickListener pListener = new DialogInterface.OnClickListener()
    {
        @Override
    public void onClick(DialogInterface arg0, int arg1)
        {
            positiveClick();
        }
    };

    DialogInterface.OnClickListener nListener = new DialogInterface.OnClickListener()
    {
        @Override
    public void onClick(DialogInterface arg0, int arg1)
        {
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
