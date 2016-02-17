package fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import models.ModuleAdapter;
import models.Modules;


public class TabsFragment extends Fragment {

    RecyclerView rv;
    String nb;

    public TabsFragment() {
        // Required empty public constructor
    }

    public static TabsFragment newInstance(String nb){
        TabsFragment f = new TabsFragment();
        Bundle args = new Bundle();
        args.putString("sem", nb);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);

        Bundle arg = getArguments();

        if (arg != null)
            nb = arg.getString(getString(R.string.sem));
        else
            nb = getString(R.string.nb1);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");

        String url = getString(R.string.url_mymodules);
        url = url + "?" + "token" + "=" + token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(getContext()).add(stringRequest);

        // Inflate the layout for this fragment
        return view;
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        public void onResponse(String response) {
            Gson gson = new Gson();
            List<Modules> modulesList = new ArrayList<>();

            try {
                JsonParser jp = new JsonParser();

                JsonObject jo = (JsonObject)jp.parse(response);

                JsonArray ja = jo.getAsJsonArray(getString(R.string.modules));

                Modules[] modules = gson.fromJson(ja, Modules[].class);

                for (int i = 0; i < modules.length; i++) {
                    if (modules[i].getSemester() == nb) {
                        Modules mod = new Modules(modules[i].getTitle(), modules[i].getCredits(), modules[i].getSemester(), modules[i].getScolaryear(), modules[i].getCodemodule(), modules[i].getCodeinstance());
                        modulesList.add(mod);
                    }
                }

                ModuleAdapter moduleAdapter = new ModuleAdapter(modulesList);
                rv.setAdapter(moduleAdapter);

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
