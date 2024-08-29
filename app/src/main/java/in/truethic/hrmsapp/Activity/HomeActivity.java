package in.truethic.hrmsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import in.truethic.hrmsapp.Fragment.AttendanceFragment;
import in.truethic.hrmsapp.Fragment.BreakFragment;
import in.truethic.hrmsapp.Fragment.HomeDashboardFragment;
import in.truethic.hrmsapp.Fragment.MoreFragment;
import in.truethic.hrmsapp.Fragment.TeamFragment;
import in.truethic.hrmsapp.R;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startCheckingPermission();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_dashboard) {
                    item.setChecked(true);
                    loadFragment(new HomeDashboardFragment(),item);
                    Log.e("dashboard Fragment==>>", "Dashboard Fragment");
                }
                else if(id == R.id.navigation_teamattendance) {
                    item.setChecked(true);
                    loadFragment(new TeamFragment(),item);
                    Log.e("Team Fragment==>>", "Team Fragment");
                }

                else if(id == R.id.navigation_attendance) {
                    item.setChecked(true);
                    loadFragment(new AttendanceFragment(),item);
                    Log.e("Attendance Fragment==>>", "Attendance Fragment");
                }
                else if(id == R.id.navigation_break){
                    item.setChecked(true);
                    loadFragment(new BreakFragment(),item);
                    Log.e("Break Fragment==>>","Break fragment");
                }
                else if(id == R.id.navigation_more) {
                    item.setChecked(true);
                    loadFragment(new MoreFragment(),item);
                    Log.e("More Fragment==>>","More Fragment");
                }
                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        for (Fragment fragment : fragments) {
            if (fragment != null) {
                if (fragment instanceof AttendanceFragment || fragment instanceof BreakFragment || fragment instanceof MoreFragment) {
                    loadFragment(new HomeDashboardFragment(), null);
                    navigationView.setSelectedItemId(R.id.navigation_dashboard);
                    return;
                }
            }
        }

        for (Fragment fragment : fragments) {
            if (fragment instanceof AttendanceFragment || fragment instanceof BreakFragment) {
                loadFragment(new HomeDashboardFragment(), null);
                navigationView.setSelectedItemId(R.id.navigation_dashboard);
                return;
            }
        }

        int item = navigationView.getSelectedItemId();
        if (item != R.id.navigation_dashboard) {
            navigationView.setSelectedItemId(R.id.navigation_dashboard);
            loadFragment(new HomeDashboardFragment(), null);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // "Yes" button clicked
                            finish(); // This will finish the current activity and exit the app
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // "No" button clicked, do nothing or handle accordingly
                            dialog.dismiss();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // If the dialog is canceled (e.g., by pressing the back button), do nothing or handle accordingly
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }


    private void startCheckingPermission() {
        if (ContextCompat.checkSelfPermission( HomeActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale( HomeActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION ))
            {
                ActivityCompat.requestPermissions( HomeActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_MEDIA_LOCATION,
                                Manifest.permission.CAMERA,
                                //android.Manifest.permission.READ_PHONE_NUMBERS,
                                //android.Manifest.permission.RECORD_AUDIO,
                                //android.Manifest.permission.SCHEDULE_EXACT_ALARM,

                        }, 1122 );
            }
            else
            {
                ActivityCompat.requestPermissions( HomeActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_MEDIA_LOCATION,
                                Manifest.permission.CAMERA
                        }, 1122 );
            }
        }
        else
        {
            isGPSEnabled();
        }
    }

    private void isGPSEnabled() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        Log.e("GPP",""+ LocationManagerCompat.isLocationEnabled(manager));
        if (!LocationManagerCompat.isLocationEnabled(manager))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder( this );

            builder.setMessage( "Your GPS seems to be disabled, do you want to enable it?" )
                    .setCancelable( false )
                    .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                        {
                            startActivityForResult( new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS ),1122 );
                        }
                    } )
                    .setNegativeButton( "Skip", new DialogInterface.OnClickListener()
                    {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                        {
                            dialog.cancel();
                          //  gotoNext();

                            Log.e("GPS Permission==>>","User skip the GPS permission Knowingly");
                        }
                    } );
            final AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            Log.e("GPS Permission==>>","User has PGS permission already");
        }
    }



    public boolean loadFragment(Fragment mFragment,MenuItem item) {
        if (mFragment != null) {

            if(item!=null){
                item.setChecked(true);
            }else{
                if(navigationView==null)
                    Log.e("NavView"," is null");
                item=navigationView.getMenu().getItem(3);
                item.setChecked(true);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, mFragment)
                    .commitNow();
            return true;
        }
        return false;
    }


}

