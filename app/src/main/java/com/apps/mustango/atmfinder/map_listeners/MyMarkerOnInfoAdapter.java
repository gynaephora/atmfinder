package com.apps.mustango.atmfinder.map_listeners;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.apps.mustango.atmfinder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class MyMarkerOnInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity mActivity;

    public MyMarkerOnInfoAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View v =mActivity.getLayoutInflater().inflate(R.layout.info_window,null);

        TextView title = (TextView) v.findViewById(R.id.type);
        TextView snipet = (TextView) v.findViewById(R.id.snippet);

        title.setText(marker.getTitle());
        title.setTextColor(Color.BLACK);

        snipet.setText(marker.getSnippet());
        snipet.setTextColor(Color.GRAY);

        return v;
    }
}
