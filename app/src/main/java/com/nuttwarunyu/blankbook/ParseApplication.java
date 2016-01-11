package com.nuttwarunyu.blankbook;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Dell-NB on 28/12/2558.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        ParseFacebookUtils.initialize(this);

    }
}
