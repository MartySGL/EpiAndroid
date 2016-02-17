package fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epiandroid.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.CustomOnItemSelectedListener;
import models.ModuleAdapter;
import models.Modules;
import models.Trombi;
import models.TrombiAdapter;
import models.User;

public class TrombiFragment extends Fragment {

   //GridView gridView;
    Spinner spinner_v;
    Spinner spinner_y;
    Button btnSubmit;
    RecyclerView rv;
    RecyclerView.LayoutManager lm;
    RecyclerView.Adapter mAdapter;

    public TrombiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trombi, container, false);

        getActivity().setTitle(getString(R.string.Trombi));
        spinner_v = (Spinner) view.findViewById(R.id.spinner_country);
        spinner_y = (Spinner) view.findViewById(R.id.spinner_year);
        Calendar calendar = Calendar.getInstance();
        int CurrentYear = calendar.get(Calendar.YEAR);
        List<String> years_list = new ArrayList<String>();

        for (int i = 2010; i <= CurrentYear; i++) {
            years_list.add(String.valueOf(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_y.setAdapter(dataAdapter);

        addListenerOnSpinnerItemSelection();

        rv = (RecyclerView) view.findViewById(R.id.rv_trombi);
        rv.setHasFixedSize(true);

        lm = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(lm);

        addListenerOnButton(view, rv);

        // Inflate the layout for this fragment
        return view;
    }

    public void addListenerOnSpinnerItemSelection(){

        spinner_y.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void addListenerOnButton(View v, RecyclerView rv) {

        spinner_v = (Spinner) v.findViewById(R.id.spinner_country);

        spinner_y = (Spinner) v.findViewById(R.id.spinner_year);

        btnSubmit = (Button) v.findViewById(R.id.button_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String city = String.valueOf(spinner_v.getSelectedItem());
                String year = String.valueOf(spinner_y.getSelectedItem());

                switch (city) {
                    case "Lyon" :
                        city = getString(R.string.Lyon);
                        break;
                    case "Paris" :
                        city = getString(R.string.Paris);
                        break;
                    case "Marseille" :
                        city = getString(R.string.Marseille);
                        break;
                    case "Bordeaux" :
                        city = getString(R.string.Bordeaux);
                        break;
                    case "Lille" :
                        city = getString(R.string.Lille);
                        break;
                    case "Montpellier" :
                        city = getString(R.string.Montpellier);
                        break;
                    case "Nancy" :
                        city = getString(R.string.Nancy);
                        break;
                    case "Nantes" :
                        city = getString(R.string.Nantes);
                        break;
                    case "Nice" :
                        city = getString(R.string.Nice);
                        break;
                    case "Rennes" :
                        city = getString(R.string.Rennes);
                        break;
                    case "Strasbourg" :
                        city = getString(R.string.Strasbourg);
                        break;
                    case "Toulouse" :
                        city = getString(R.string.Toulouse);
                        break;
                }

                SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
                String token = settings.getString("token", "default");

                String url = getString(R.string.url_trombi);
                url = url + "?" + "token" + "=" + token + "&year=" + year + "&location=" + city;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
                Volley.newRequestQueue(getContext()).add(stringRequest);

            }

        });

    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        public void onResponse(String response) {
            Gson gson = new Gson();
            List<Trombi> usersList = new ArrayList<>();
            String newstr = null;

            try {

                JsonParser jp = new JsonParser();
                JsonObject jo = (JsonObject)jp.parse(response);

                JsonArray ja = jo.getAsJsonArray(getString(R.string.items));

                Trombi[] users = gson.fromJson(ja, Trombi[].class);

                for (int i = 0; i < users.length; i++) {
                    Trombi usr = new Trombi(users[i].getLogin(), users[i].getNom(), users[i].getPrenom(), users[i].getPicture());
                    usersList.add(usr);
                }

                mAdapter = new TrombiAdapter(usersList);
                rv.setAdapter(mAdapter);

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
