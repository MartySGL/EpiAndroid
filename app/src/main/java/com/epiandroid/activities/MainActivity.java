package com.epiandroid.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epiandroid.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import fragments.AllmodulesFragment;
import fragments.MainFragment;
import fragments.ModulesFragment;
import fragments.PlanningFragment;
import fragments.ProfileFragment;
import fragments.TabsFragment;
import fragments.TrombiFragment;
import models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String default_shared;
    TextView txtName;
    TextView txtMail;
    TextView txtLog;
    ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        default_shared = getString(R.string.default_shared);
        SharedPreferences settings = getSharedPreferences(getString(R.string.preferences), 0);
        String st = settings.getString(getString(R.string.token), default_shared);

        if (st.equals(default_shared)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_main);

            View header = navigationView.getHeaderView(0);
            txtName = (TextView) header.findViewById(R.id.txtName);
            txtMail = (TextView) header.findViewById(R.id.txtMail);
            txtLog = (TextView) header.findViewById(R.id.txtLog);
            imgProfile = (ImageView) header.findViewById(R.id.imgProfile);

            getCurrentUser();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment mf = new MainFragment();
            ft.replace(R.id.main_content, mf);
            ft.commit();
        }
    }

    public void getCurrentUser() {

        SharedPreferences settings = getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString(getString(R.string.token), getString(R.string.default_shared));
        String login = settings.getString(getString(R.string.login), getString(R.string.default_shared));

        String url = getString(R.string.url_user);
        url = url + "?" + getString(R.string.token) + "=" + token + "&" + getString(R.string.user) + "=" + login;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(this).add(stringRequest);
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Gson gson = new Gson();

            try {
                JsonParser jp = new JsonParser();
                JsonObject jo = (JsonObject)jp.parse(response);
                User user = gson.fromJson(jo, User.class);

                JsonObject log = jo.getAsJsonObject(getString(R.string.nsstat));

                String logtime = "0";

                if (log != null)
                    logtime = log.get(getString(R.string.active)).getAsString();

                if (user != null) {
                    SharedPreferences settings = getSharedPreferences(getString(R.string.preferences), 0);
                    SharedPreferences.Editor edit = settings.edit();
                    String scolaryear = jo.get(getString(R.string.scolaryear)).getAsString();
                    String location = jo.get(getString(R.string.location)).getAsString();
                    String course = jo.get(getString(R.string.coursecode)).getAsString();
                    edit.putString(getString(R.string.scl), scolaryear);
                    edit.putString(getString(R.string.loca), location);
                    edit.putString(getString(R.string.course), course);
                    edit.apply();
                    String str = user.getFirstname() + " " + user.getName();
                    txtName.setText(str);
                    txtMail.setText(user.getEmail());
                    String logg = getString(R.string.log) + " " + logtime + "h";
                    txtLog.setText(logg);
                    Picasso.with(getApplicationContext()).load(user.getPicture()).into(imgProfile);
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    };

    Response.ErrorListener responseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences settings = getSharedPreferences(getString(R.string.preferences), 0);
            SharedPreferences.Editor edit = settings.edit();
            edit.putString(getString(R.string.token), default_shared);
            edit.apply();
            startActivity(new Intent(getApplication(), LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Fragment mf = new MainFragment();
            ft.replace(R.id.main_content, mf);
            ft.commit();

        } else if (id == R.id.nav_planning) {
            Fragment pf = new PlanningFragment();
            ft.replace(R.id.main_content, pf);
            ft.commit();
        } else if (id == R.id.nav_profile) {
            Fragment pf = new ProfileFragment();
            ft.replace(R.id.main_content, pf);
            ft.commit();
        } else if (id == R.id.nav_mmodules) {
            Fragment pf = new ModulesFragment();
            ft.replace(R.id.main_content, pf);
            ft.commit();
        } else if (id == R.id.nav_modules) {
            Fragment pf = new AllmodulesFragment();
            ft.replace(R.id.main_content, pf);
            ft.commit();
        } else if (id == R.id.nav_trombi) {
            Fragment pf = new TrombiFragment();
            ft.replace(R.id.main_content, pf);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
