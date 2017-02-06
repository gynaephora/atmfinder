package com.apps.mustango.atmfinder;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.mustango.atmfinder.map_listeners.MyMarkerLongClickListener;
import com.apps.mustango.atmfinder.map_listeners.MyMarkerOnInfoAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    String type;
    String fullAddressRu;
    String placeRu;
    String latitude;
    String longitude;
    String tw;

    EditText editText;
    Button button;


    public static final String URL_0 = "https://api.privatbank.ua/p24api/infrastructure?json&";
    public static final String URL_1 = "&address=&city=";

    public static String LOG_TAG = "my_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
                                      public boolean onKey(View v, int keyCode, KeyEvent event) {
                                          if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                                  (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                              // text before clicking Enter
                                              if (editText.getText().toString().equals("")) {
                                              // code if EditText is empty
                                                  Toast.makeText(MapsActivity.this, getString(R.string.text1), Toast.LENGTH_SHORT).show();

                                              } else {
                                              // if there is a text, there is a different code
                                                  String strCity = editText.getText().toString();
                                                  mMap.clear();
                                                  String[] atm1 = {"atm", strCity};
                                                  new ParseTask().execute(atm1);
                                                  String[] atm2 = {"tso", strCity};
                                                  new ParseTask().execute(atm2);

                                              }
                                              return true;
                                          }
                                          return false;
                                      }
                                  }
        );
        button = (Button) findViewById(R.id.btnTest);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (editText.getText().toString().equals("")) {
                    // code if EditText is empty
                    Toast.makeText(MapsActivity.this, getString(R.string.text1), Toast.LENGTH_SHORT).show();

                } else {
                    // if there is a text, there is a different code
                    String strCity = editText.getText().toString();
                    mMap.clear();
                    String[] atm1 = {"atm", strCity};
                    new ParseTask().execute(atm1);
                    String[] atm2 = {"tso", strCity};
                    new ParseTask().execute(atm2);

                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //   mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

    }


    private class ParseTask extends AsyncTask<String, Void, Atminfo> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected Atminfo doInBackground(String... params) {

            // obtain data from an external resource
            try {

                /*
                URL url = new URL(URL_0 + params[0] + URL_1 + URLEncoder.encode(params[1], "UTF-8"));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();*/
              //  final String url = URL_0 + params[0] + URL_1 + URLEncoder.encode(params[1], "UTF-8");

                String url ="https://api.privatbank.ua/p24api/infrastructure?json&atm&address=&city=Днепропетровск";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Atminfo atminfo = restTemplate.getForObject(url, Atminfo.class);
                return atminfo;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Atminfo strJson) {
           super.onPostExecute(strJson);
          /*  if (mMap != null) {
                mMap.setInfoWindowAdapter(new MyMarkerOnInfoAdapter(MapsActivity.this));
                mMap.setOnInfoWindowLongClickListener(new MyMarkerLongClickListener(MapsActivity.this));
            }*/
            // derive json-string
            Log.d(LOG_TAG, strJson.getCity());
            JSONObject dataJsonObj;
/*
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray devices = dataJsonObj.getJSONArray("devices");

                if(devices.length()==0){
                    Log.d(LOG_TAG, "null");
                    Toast.makeText(MapsActivity.this, getString(R.string.text2), Toast.LENGTH_SHORT).show();
                }
                else {
                    for (int i = 0; i < devices.length(); i++) {
                        JSONObject atm = devices.getJSONObject(i);
                        JSONObject shedule = atm.getJSONObject("tw");
                        tw = getString(R.string.monday) + " " + shedule.getString("mon") + "\n" +
                                getString(R.string.tuesday) + " " + shedule.getString("tue") + "\n" +
                                getString(R.string.wednesday) + " " + shedule.getString("wed") + "\n" +
                                getString(R.string.thursday) + " " + shedule.getString("thu") + "\n" +
                                getString(R.string.friday) + " " + shedule.getString("fri") + "\n" +
                                getString(R.string.saturday) + " " + shedule.getString("sat") + "\n" +
                                getString(R.string.sunday) + " " + shedule.getString("sun") + "\n" +
                                getString(R.string.holiday) + " " + shedule.getString("hol");

                        type = atm.getString("type");
                        fullAddressRu = atm.getString("fullAddressRu");
                        placeRu = atm.getString("placeRu");
                        latitude = atm.getString("latitude");
                        longitude = atm.getString("longitude");

                        String snippet = fullAddressRu + "\n" + placeRu + "\n" + getString(R.string.shedule) + "\n" + tw;
                        if (i == 0) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Float.valueOf(latitude), Float.valueOf(longitude)), 10));
                        }
                        if (type.equals("ATM")) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Float.valueOf(latitude), Float.valueOf(longitude)))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(getString(R.string.atm)).snippet(snippet));
                        } else {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Float.valueOf(latitude), Float.valueOf(longitude)))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                    .title(getString(R.string.tso)).snippet(snippet));
                        }
                        Log.d(LOG_TAG, "type: " + type);
                        Log.d(LOG_TAG, "fullAdressRu: " + fullAddressRu);
                        Log.d(LOG_TAG, "placeRu: " + placeRu);
                        Log.d(LOG_TAG, "shedule: " + tw);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }
}
