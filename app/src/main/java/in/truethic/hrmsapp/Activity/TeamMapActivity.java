package in.truethic.hrmsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import in.truethic.hrmsapp.R;
import in.truethic.hrmsapp.Utils.DisplayDialogBox;
import in.truethic.hrmsapp.Utils.Globals;
import in.truethic.hrmsapp.Utils.SessionManager;

public class TeamMapActivity extends AppCompatActivity implements LocationListener{

    TextView tv_check_in;
    boolean canTakeAttedance = false;
    SessionManager sessionManager;
    SupportMapFragment supportMapFragment;
    ImageView iv_back,iv_refresh;
    LocationManager locationManager;
    TextView tv_map_details, tv_map_distance;
    FusedLocationProviderClient client;
    Location currentLocation;
    float distance = 10;
    public ProgressDialog progressDialog;
    boolean mapFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_map);
        sessionManager = new SessionManager(TeamMapActivity.this);
        tv_check_in = findViewById(R.id.tv_teamcheck_in);
        iv_back = findViewById(R.id.iv_back);
        iv_refresh = findViewById(R.id.iv_refresh);
        tv_map_details = findViewById(R.id.tv_map_details);
        tv_map_distance = findViewById(R.id.tv_map_distance);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Detecting GPS Location...");
        progressDialog.show();


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        client = LocationServices.getFusedLocationProviderClient(TeamMapActivity.this);

        String sr = Globals.siteRadius;
        float site_R = Float.parseFloat(String.valueOf(sr.isEmpty() ? "0.0" : sr));


        Intent i = getIntent();
        mapFlag = i.getBooleanExtra("mapFlag",false);
        Log.e("MapFlageProfile==>>",mapFlag+"");
        if(mapFlag)
        {
            tv_check_in.setVisibility(View.GONE);
        }
        else
        {
            tv_check_in.setVisibility(View.VISIBLE);
        }

        if (Globals.att_type.equals(true) ){
            tv_check_in.setText("Check In");
        }else {
            tv_check_in.setText("Check Out");
        }

        tv_check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canTakeAttedance)
                {
                    Globals.onclick_or_not = true;
                    Intent i = new Intent(TeamMapActivity.this,TeamMemberListActivity .class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    if(Globals.emp_username.equalsIgnoreCase("1234567890"))
                    {
                        Globals.onclick_or_not = true;
                        finish();
                    }
                    else {
                        Globals.onclick_or_not = false;
                        //ShowToast("You are exceeding site radius of " + site_R + " Mtrs");
                        DisplayDialogBox.showDialogBox(TeamMapActivity.this,"Your are out of office location By "+site_R+" Meters","GPS Alert");
                    }
                }
            }
        });
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (ActivityCompat.checkSelfPermission(TeamMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TeamMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestSingleUpdate(LocationManager.FUSED_PROVIDER, (LocationListener) TeamMapActivity.this, null);
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.FUSED_PROVIDER, (LocationListener) TeamMapActivity .this, null);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Map_Activity.this, HomeActivity.class);
                startActivity(i);*/
                finish();
            }
        });


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        progressDialog.dismiss();
        currentLocation = location;
        Log.e("LOCCCH", location.getLatitude() + "," + location.getLongitude());
        loadMap();
    }

    private void loadMap() {
        if (ActivityCompat.checkSelfPermission(TeamMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (currentLocation != null) {
                getCurrentLocation();
            } else {
                // Handle the case where currentLocation is null
                Toast.makeText(TeamMapActivity.this, "Unable to get current location", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(TeamMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
    }

    private void getCurrentLocation() {
        // ... (Your existing code)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(TeamMapActivity.this,"No location permission granted",Toast.LENGTH_LONG).show();

            // Ensure that currentLocation is not null before using it
            if (currentLocation != null) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        Log.e("MPR", "Mapready");
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                        if (currentLocation != null) {
                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            Log.e("LatLng", latLng.toString());

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                            Location locationA = new Location("point A");
                            locationA.setLatitude(currentLocation.getLatitude());
                            locationA.setLongitude(currentLocation.getLongitude());

                            Log.e("LLOC1", "lat=" + currentLocation.getLatitude() + ",lon=" + currentLocation.getLongitude());

                            Location locationB = new Location("point B");

                            String lat1 = sessionManager.getStringData(SessionManager.siteLatitude);
                            String lon1 = sessionManager.getStringData(SessionManager.siteLongitude);

                            Log.e("siteB", "" + lat1 + " - " + lon1);

                            String sr = sessionManager.getStringData(SessionManager.siteRadius);
                            float siteRadius = Float.parseFloat(String.valueOf(sr.isEmpty() ? "0.0" : sr));

                            Log.e("siteR", sr);
                            Log.e("LLOC2", "lat=" + lat1 + ",lon=" + lon1);

                            if (!lat1.isEmpty() && !lon1.isEmpty()) {
                                locationB.setLatitude(Double.parseDouble(lat1));
                                locationB.setLongitude(Double.parseDouble(lon1));

                                distance = locationA.distanceTo(locationB);
                                Log.e("DistanceR", distance + "");
                            }

                            tv_map_distance.setVisibility(View.VISIBLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                tv_map_details.setText(Html.fromHtml("Your distance is <p style=\"color:#0362fe;\">" + String.format("%.2f", distance) + " Mtrs</p> from site location", Html.FROM_HTML_MODE_COMPACT));
                                tv_map_distance.setText(Html.fromHtml("Maximum distance allowed is <p style=\"color:#0362fe;\">" + String.format("%.2f", siteRadius) + " Mtrs</p>", Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_map_details.setText(Html.fromHtml("Your distance is <p style=\"color:#0362fe;\">" + String.format("%.2f", distance) + " Mtrs</p> from site location"));
                                tv_map_distance.setText(Html.fromHtml("Maximum distance allowed is <p style=\"color:#0362fe;\">" + String.format("%.2f", siteRadius) + " Mtrs</p>"));
                            }

                            if (distance < siteRadius)
                                canTakeAttedance = true;
                            else {
                                canTakeAttedance = false;
                            }
                        } else {
                            // Log a message or handle the case where currentLocation is null
                            Log.e("Map_Activity", "Current location is null in onMapReady");
                            Toast.makeText(TeamMapActivity.this, "Unable to get current location for the map", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                // Handle the case where currentLocation is null
                Toast.makeText(TeamMapActivity.this, "Unable to get current location", Toast.LENGTH_LONG).show();
            }
            return;
        }

        // Ensure that currentLocation is not null before using it
        if (currentLocation != null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    // ... (Your existing code)
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {

                            Log.e("MPR", "Mapready");
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());


                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                            Location locationA = new Location("point A");
                            locationA.setLatitude(currentLocation.getLatitude());
                            locationA.setLongitude(currentLocation.getLongitude());

                            Log.e("LLOC1", "lat=" + currentLocation.getLatitude() + ",lon=" + currentLocation.getLongitude());

                            Location locationB = new Location("point B");

                            String lat1 = Globals.teamlat;
                            String lon1 = Globals.teamlong;

                            Log.e("siteB", ""+lat1+" - "+lon1);

                            String sr = Globals.siteRadius;
                            float siteRadius = Float.parseFloat(String.valueOf(sr.isEmpty() ? "0.0" : sr));

                            Log.e("siteR", sr);
                            Log.e("LLOC2","lat="+lat1+",lon="+lon1);

                            if (lat1.isEmpty() || lon1.isEmpty()) {

                            }
                            else
                            {
                                locationB.setLatitude( Double.parseDouble( lat1 ) );
                                locationB.setLongitude( Double.parseDouble( lon1 ) );

                                distance = locationA.distanceTo( locationB );
                                Log.e("DistanceR",distance+"");
                            }

                            tv_map_distance.setVisibility(View.VISIBLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                tv_map_details.setText(Html.fromHtml("Your distance is <p style=\"color:#0362fe;\">" + String.format("%.2f", distance) + " Mtrs</p> from site location", Html.FROM_HTML_MODE_COMPACT));
                                tv_map_distance.setText(Html.fromHtml("Maximum distance allowed is <p style=\"color:#0362fe;\">" + String.format("%.2f", siteRadius) + " Mtrs</p>", Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_map_details.setText(Html.fromHtml("Your distance is <p style=\"color:#0362fe;\">" + String.format("%.2f", distance) + " Mtrs</p> from site location"));
                                tv_map_distance.setText(Html.fromHtml("Maximum distance allowed is <p style=\"color:#0362fe;\">" + String.format("%.2f", siteRadius) + " Mtrs</p>"));
                            }

                            if(distance<siteRadius)
                                //finish();
                                canTakeAttedance = true;
                            else {
                                //ShowToast("You are exceeding site radius of " + siteRadius + " Mtrs");
                                canTakeAttedance = false;
                            }

                        }
                    });
                }
            });
        } else {
            // Handle the case where currentLocation is null
            Toast.makeText(TeamMapActivity.this, "Unable to get current location", Toast.LENGTH_LONG).show();
        }
    }
}