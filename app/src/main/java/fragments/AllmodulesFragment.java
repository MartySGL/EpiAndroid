package fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import models.ModuleAdapter;
import models.Modules;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllmodulesFragment extends Fragment {

    public AllmodulesFragment() {
        // Required empty public constructor
    }

    TextView txt;
    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allmodules, container, false);

        getActivity().setTitle(R.string.action_modules);

        txt = (TextView) view.findViewById(R.id.txtAllmodules);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        rv = (RecyclerView) view.findViewById(R.id.rvam);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");
        String scolaryear = settings.getString("scl", "default");
        String location = settings.getString("loca", "default");
        String course = settings.getString("course", "default");

        String url = getString(R.string.url_modules);
        url = url + "?" + "token" + "=" + token + "&" + "scolaryear" + "=" + scolaryear + "&" + "location" + "=" + location + "&" + "course" + "=" + course;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(getContext()).add(stringRequest);

        return view;
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        public void onResponse(String response) {
            Gson gson = new Gson();
            List<Modules> modulesList = new ArrayList<>();
            String nstr = null;
            try {
                nstr = response;

                JsonParser jp = new JsonParser();

                JsonObject jo = (JsonObject)jp.parse(nstr);

                JsonArray ja = jo.getAsJsonArray(getString(R.string.items));

                Modules[] modules = gson.fromJson(ja, Modules[].class);

                for (int i = 0; i < modules.length; i++) {
                    Modules mod = new Modules(modules[i].getTitle(), modules[i].getCredits(), modules[i].getSemester(), modules[i].getScolaryear(), modules[i].getCode(), modules[i].getCodeinstance());
                    modulesList.add(mod);
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
