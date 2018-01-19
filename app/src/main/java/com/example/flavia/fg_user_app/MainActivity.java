package com.example.flavia.fg_user_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    AccountFragment.OnFragmentInteractionListener,
                    OldNotificationFragment.OnNotificationsPreferencesSet{

    private final String FRAG_TAG = "swap fragment";


    private final String url = "https://api.festigeek.ch/v1";



    private String token = null;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bundle recieved, get the token
        Intent i = getIntent();
        token =  i.getStringExtra("token");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fragment launch
        changeFragment(AccountFragment.class, false, true);

        // Just load default pref one
        OldNotificationPreference.getInstance();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            changeFragment(AccountFragment.class, false, true);
        } else if (id == R.id.nav_notifications) {
            changeFragment(OldNotificationFragment.class, false, true);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        //check if the token is valid and redirect to login if not


        Map<String, String> jsonParams = new HashMap<>();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url + "/users/me", new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("/me RESPONSE", response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("token invalid", error.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), "Session expired, please reconnect", Toast.LENGTH_SHORT);
                        toast.show();
                        startActivity(getIntent());
                    }
                }) {  // needs headers for the request, override header function of this object (not the general class)
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };


        queue.add(jsObjRequest);

    }
    /**
     * Change the current displayed fragment by a new one.
     * - if the fragment is in backstack, it will pop it
     * - if the fragment is already displayed (trying to change the fragment with the same), it will not do anything
     *
     * @param fragmentClass   the class of the new fragment to display
     * @param saveInBackstack if we want the fragment to be in backstack
     * @param animate         if we want a nice animation or not
     */
    protected void changeFragment(Class fragmentClass, boolean saveInBackstack, boolean animate) {
        Fragment frag = null;

        try {
            frag = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();

        }

        String backStateName = ((Object) frag).getClass().getName();

        try {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
                //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();

                if (animate) {
                    Log.d(FRAG_TAG, "Change Fragment: animate");
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }

                transaction.replace(R.id.content, frag, backStateName);

                if (saveInBackstack) {
                    Log.d(FRAG_TAG, "Change Fragment: addToBackTack " + backStateName);
                    transaction.addToBackStack(backStateName);
                } else {
                    Log.d(FRAG_TAG, "Change Fragment: NO addToBackStack");
                }

                transaction.commit();
            } else {
                // custom effect if fragment is already instanciated
            }
        } catch (IllegalStateException exception) {
            Log.w(FRAG_TAG, "Unable to commit fragment, could be activity as been killed in background. " + exception.toString());
        }
    }
    public String getToken() {
        return token;
    }

    // OldNotificationFragment
    @Override
    public void upadateNotificationRequest(final List<String> listNotifications) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
