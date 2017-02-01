package com.apps.mustango.atmfinder.map_listeners;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.apps.mustango.atmfinder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class MyMarkerLongClickListener implements GoogleMap.OnInfoWindowLongClickListener {

    private Activity mActivity;

    public MyMarkerLongClickListener(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+marker.getPosition().latitude+","+marker.getPosition().longitude+"&mode=w");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(mActivity.getString(R.string.url_maps));
        if (mapIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(mapIntent);
        }
    }
}
