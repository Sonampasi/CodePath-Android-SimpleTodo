package com.codepath.simpletodo;

import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * Created by Sonam on 9/25/2016.
 */

public class TodoApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
