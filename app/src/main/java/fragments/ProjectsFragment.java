package fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import models.ModuleAdapter;
import models.Modules;
import models.Myfile;
import models.Project;
import models.ProjectAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectsFragment extends Fragment {

    TextView txt;
    RecyclerView rv2;
    String codemodule;
    String scolaryear;
    String codeinstance;
    View vw;

    public ProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        vw = view;

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        rv2 = (RecyclerView) view.findViewById(R.id.rv2);
        rv2.setHasFixedSize(true);
        rv2.setLayoutManager(llm);

        codemodule = getActivity().getIntent().getExtras().getString(getString(R.string.codemodule));
        scolaryear = getActivity().getIntent().getExtras().getString(getString(R.string.scolaryear));
        codeinstance = getActivity().getIntent().getExtras().getString(getString(R.string.codeinstance));

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");

        String url = getString(R.string.url_getmodule);
        url = url + "?" + "token" + "=" + token + "&scolaryear=" + scolaryear + "&codemodule=" + codemodule + "&codeinstance=" +codeinstance;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(getContext()).add(stringRequest);

        // Inflate the layout for this fragment
        return view;
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        public void onResponse(String response) {
            Gson gson = new Gson();
            List<Project> projectList = new ArrayList<>();

            try {
                JsonParser jp = new JsonParser();

                JsonObject jo = (JsonObject)jp.parse(response);

                JsonElement registered = jo.get(getString(R.string.student_registered));

                JsonArray ja = jo.getAsJsonArray(getString(R.string.activites));

                Project[] projects = gson.fromJson(ja, Project[].class);

                if (registered.getAsString().equals(getString(R.string.nb1))){
                    for (int i = 0; i < projects.length; i++) {
                        if (projects[i].getNb_notes() != null) {
                            Project proj = new Project(codeinstance, scolaryear, projects[i].getTitle(), projects[i].getNote(), codemodule, projects[i].getCodeacti(), projects[i].getNb_notes(), projects[i].getRegister(), projects[i].getEnd_register(), projects[i].getStart());
                            projectList.add(proj);
                        }
                    }
                }

                ProjectAdapter projectAdapter = new ProjectAdapter(getContext(), projectList, ProjectsFragment.this, vw);

                rv2.setAdapter(projectAdapter);
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

    public void Suscribe (String scoly, String codemodule, String codeinstance, String codeacti) {
        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");

        String url = getString(R.string.url_susproj);
        url = url + "?" + "token" + "=" + token + "&scolaryear=" + scoly + "&codemodule=" + codemodule + "&codeinstance=" + codeinstance + "&codeacti=" +codeacti;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener2, responseError2);
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

    public void Unsuscribe (String scoly, String codemodule, String codeinstance, String codeacti) {
        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");

        String url = getString(R.string.url_susproj);
        url = url + "?" + "token" + "=" + token + "&scolaryear=" + scoly + "&codemodule=" + codemodule + "&codeinstance=" + codeinstance + "&codeacti=" +codeacti;

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

    public void Download (String scoly, String codemodule, String codeinstance, String codeacti) {
        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString("token", "default");

        String url = getString(R.string.url_pdf);
        url = url + "?" + "token" + "=" + token + "&scolaryear=" + scoly + "&codemodule=" + codemodule + "&codeinstance=" + codeinstance + "&codeacti=" +codeacti;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener3, responseError3);
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    Response.Listener<String> responseListener3 = new Response.Listener<String>() {
        public void onResponse(String response) {

            try {
                Type fooType = new TypeToken<ArrayList<Myfile>>(){}.getType();
                List<Myfile> array = new Gson().fromJson(response, fooType);

                for (int i = 0; i < array.size(); i++)
                    Downloadondevice(array.get(i).getFullpath(), array.get(i).getTitle());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    Response.ErrorListener responseError3 = new Response.ErrorListener() {
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };

    public void Downloadondevice(String url, String filename) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_epitech)+url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
