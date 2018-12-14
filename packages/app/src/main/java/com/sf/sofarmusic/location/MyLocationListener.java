package com.sf.sofarmusic.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class MyLocationListener implements LocationListener{


    @Override
    public void onLocationChanged(Location location) {
        //位置发生变化时调用,在这里更新位置信息
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
