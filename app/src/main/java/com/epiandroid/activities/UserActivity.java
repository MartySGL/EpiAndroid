package com.epiandroid.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epiandroid.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import models.User;

public class UserActivity extends AppCompatActivity {

    TextView txtname;
    TextView txtemail;
    TextView txtnum;
    TextView txtgpa;
    TextView txtlog;
    ImageView pic;
    String nom;
    String prenom;
    String picture;
    FloatingActionButton fabtn;
    FloatingActionButton fabtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.title_activity_usr));

        fabtn = (FloatingActionButton) findViewById(R.id.fab);
        fabtn2 = (FloatingActionButton) findViewById(R.id.fab2);

        String login = getIntent().getExtras().getString(getString(R.string.login));
        nom = getIntent().getExtras().getString(getString(R.string.nom));
        prenom = getIntent().getExtras().getString(getString(R.string.prenom));
        picture = getIntent().getExtras().getString(getString(R.string.picture));

        txtemail = (TextView) findViewById(R.id.mail);
        txtname = (TextView) findViewById(R.id.name_usr);
        txtnum = (TextView) findViewById(R.id.numero);
        txtgpa = (TextView) findViewById(R.id.gpaval);
        txtlog = (TextView) findViewById(R.id.logval);
        pic = (ImageView) findViewById(R.id.picture_usr);


        SharedPreferences settings = getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");

        String url = getString(R.string.url_usr);
        url = url + "?" + "token" + "=" + token + "&user=" + login ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(this).add(stringRequest);
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Gson gson = new Gson();

            try {

                Log.d("", response);

                JsonParser jp = new JsonParser();
                JsonObject jo = (JsonObject)jp.parse(response);
                final JsonElement email = jo.get(getString(R.string.mail));
                JsonElement gpa = jo.getAsJsonArray(getString(R.string.gpa)).get(0).getAsJsonObject().get(getString(R.string.gpa));
                if (response.contains(getString(R.string.nsstat))) {
                    JsonElement log = jo.getAsJsonObject(getString(R.string.nsstat)).get(getString(R.string.active));
                    txtlog.setText(log.getAsString());
                }
                else
                    txtlog.setText(getString(R.string.none));
                if (response.contains(getString(R.string.userinfo))) {
                    try {
                        if (jo.getAsJsonObject(getString(R.string.userinfo)).get(getString(R.string.telephone)) != null) {
                            final JsonElement num = jo.getAsJsonObject(getString(R.string.userinfo)).get(getString(R.string.telephone)).getAsJsonObject().get(getString(R.string.value));
                            txtnum.setText(num.getAsString());
                            if (num.getAsString() != null) {
                                fabtn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent callusr = new Intent(Intent.ACTION_CALL);
                                        callusr.setData(Uri.parse(getString(R.string.tel) + num.getAsString()));
                                        startActivity(callusr);
                                    }
                                });

                                fabtn2.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        Intent add = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, ContactsContract.Contacts.CONTENT_URI);
                                        add.setData(Uri.parse(getString(R.string.tel) + num.getAsString()));
                                        add.putExtra(ContactsContract.Intents.Insert.NAME, prenom + " " + nom);
                                        add.putExtra(ContactsContract.Intents.Insert.EMAIL, email.getAsString());
                                        startActivity(add);
                                        return true;
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String str = prenom + " " + nom;
                txtgpa.setText(gpa.getAsString());
                txtname.setText(str);
                txtemail.setText(email.getAsString());
                Picasso.with(getApplicationContext()).load(picture).into(pic);
                fabtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] TO = {txtemail.getText().toString()};
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType(getString(R.string.txtp));
                        intent.putExtra(Intent.EXTRA_EMAIL, TO);

                        startActivity(Intent.createChooser(intent, getString(R.string.contact)));
                    }
                });

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

}
