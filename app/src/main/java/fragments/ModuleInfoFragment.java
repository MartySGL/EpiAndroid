package fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epiandroid.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Project;
import models.ProjectAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleInfoFragment extends Fragment {

    TextView cred;
    TextView grade;
    TextView desc;
    String credits;
    Button btnsub;
    Button partage;
    String Scolaryear;
    String codemodule;
    String codeinstance;
    String title;

    public ModuleInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module_info, container, false);

        title = getActivity().getIntent().getExtras().getString(getString(R.string.title));
        Scolaryear = getActivity().getIntent().getExtras().getString(getString(R.string.scolaryear));
        codemodule = getActivity().getIntent().getExtras().getString(getString(R.string.codemodule));
        codeinstance = getActivity().getIntent().getExtras().getString(getString(R.string.codeinstance));
        credits = getActivity().getIntent().getExtras().getString(getString(R.string.crd));
        getActivity().setTitle(title);

        cred = (TextView) view.findViewById(R.id.crd_nb);
        desc = (TextView) view.findViewById(R.id.desc);
        grade = (TextView) view.findViewById(R.id.grd_value);
        btnsub = (Button) view.findViewById(R.id.susmod);
        partage = (Button) view.findViewById(R.id.partagegrd);

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");

        String url = getString(R.string.url_getmodule);
        url = url + "?" + "token" + "=" + token + "&scolaryear=" + Scolaryear + "&codemodule=" + codemodule +"&codeinstance=" + codeinstance;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(getContext()).add(stringRequest);

        // Inflate the layout for this fragment
        return view;
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        public void onResponse(String response) {
            Gson gson = new Gson();
            String grd = null;
            String debut = null;
            String lim = null;
            String regist = null;
            String allow = null;

            try {
                JsonParser jp = new JsonParser();

                JsonObject jo = (JsonObject)jp.parse(response);

                if (jo.get(getString(R.string.student_grade)).isJsonNull())
                    grd = getString(R.string.none);
                else
                    grd = jo.get(getString(R.string.student_grade)).getAsString();

                String description = jo.get(getString(R.string.description)).getAsString();

                if (jo.get(getString(R.string.begin)).isJsonNull() == false)
                    debut = jo.get(getString(R.string.begin)).getAsString();

                if (jo.get(getString(R.string.end_register)).isJsonNull() == false)
                    lim = jo.get(getString(R.string.end_register)).getAsString();

                if (jo.get(getString(R.string.student_registered)).isJsonNull() == false)
                    regist = jo.get(getString(R.string.student_registered)).getAsString();

                cred.setText(credits);
                grade.setText(grd);
                desc.setText(description);


                String curr = new SimpleDateFormat(getString(R.string.format)).format(new Date());

                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.format));

                if (jo.get(getString(R.string.allow)).isJsonNull() == false) {

                    final Date current = sdf.parse(curr);
                    final Date begin = (Date) sdf.parse(debut);
                    final Date limite = (Date) sdf.parse(lim);

                    if (current.compareTo(begin) < 0)
                        btnsub.setText(getString(R.string.Inscription));
                    else if (current.compareTo(begin) >= 0 && current.compareTo(limite) < 0) {
                        if (regist.equals(getString(R.string.nb1)))
                            btnsub.setText(getString(R.string.Desinscription));
                        else
                            btnsub.setText(getString(R.string.Inscription));
                    } else if (current.compareTo(limite) > 0) {
                        if (regist.equals(getString(R.string.nb1)))
                            btnsub.setText(getString(R.string.Desinscription));
                        else
                            btnsub.setText(getString(R.string.Inscription));
                    }

                    btnsub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (btnsub.getText().equals(getString(R.string.Inscription)) && current.compareTo(begin) > 0 && current.compareTo(limite) < 0)
                                Suscribe(Scolaryear, codemodule, codeinstance);
                            else if (btnsub.getText().equals(getString(R.string.Desinscription)) && current.compareTo(begin) > 0 && current.compareTo(limite) < 0)
                                Unsuscribe(Scolaryear, codemodule, codeinstance);
                        }

                        public void Suscribe(String scoly, String codemodule, String codeinstance) {
                            SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
                            String token = settings.getString("token", "default");

                            String url = getString(R.string.url_getmodule);
                            url = url + "?" + "token" + "=" + token + "&scolaryear=" + scoly + "&codemodule=" + codemodule + "&codeinstance=" + codeinstance;

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener2, responseError2);
                            Volley.newRequestQueue(getContext()).add(stringRequest);

                        }

                        public void Unsuscribe(String scoly, String codemodule, String codeinstance) {
                            SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
                            String token = settings.getString("token", "default");

                            String url = getString(R.string.url_getmodule);
                            url = url + "?" + "token" + "=" + token + "&scolaryear=" + scoly + "&codemodule=" + codemodule + "&codeinstance=" + codeinstance;

                            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, responseListener2, responseError2);
                            Volley.newRequestQueue(getContext()).add(stringRequest);
                        }

                        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                            public void onResponse(String response) {

                                try {

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        };

                        Response.ErrorListener responseError2 = new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        };

                    });
                }
                else if (jo.get(getString(R.string.allow)).isJsonNull()) {
                    btnsub.setText(getString(R.string.Inscription));
                    btnsub.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), getString(R.string.no),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                                              }


                            partage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ((grade.getText().toString()).equals(getString(R.string.none)) == false) {
                                        Uri uri = Uri.parse(getString(R.string.smsto));
                                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                        it.putExtra(getString(R.string.sms_body), getString(R.string.strmod) + " \n\n" + title + " : " + grade.getText());
                                        it.setType(getString(R.string.itsms));
                                        startActivity(it);
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.strpmod),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    Response.ErrorListener responseError = new Response.ErrorListener() {
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };
}
