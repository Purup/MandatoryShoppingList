package com.hfad.mandatoryshoppinglist.mandatoryshoppinglist;

import com.firebase.client.Firebase;

/**
 * Created by Puriiqt on 27/04/2016.
 */
public class FireBaseApplication extends android.app.Application{

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
