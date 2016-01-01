package it.jaschke.alexandria.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Gurmeet on 01-01-2016.
 */
public class Utility {
    /**
     * Utility function to check if network is available on device
     * @param c Context
     * @return boolean true if network is available else false
     */
    public static boolean isNetworkAvailable(Context c){
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
