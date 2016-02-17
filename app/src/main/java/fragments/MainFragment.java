package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epiandroid.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.MessagesAdapter;
import models.Message;
import models.Modules;
import models.User;

public class MainFragment extends Fragment {

    RecyclerView rv;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        getActivity().setTitle(getString(R.string.app_name));

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString(getString(R.string.token), getString(R.string.default_shared));

        String url = getString(R.string.url_messages);
        url = url + "?" + getString(R.string.token) + "=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(getContext()).add(stringRequest);
        return view;
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Gson gson = new Gson();
            try {
                response = response.substring(2);
                response = "{ \"messages\":[\n" + response  + "}";
                JsonParser jp = new JsonParser();
                JsonObject jo = (JsonObject)jp.parse(response);
                JsonArray ja = jo.getAsJsonArray(getString(R.string.messages));
                Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
                List<Message> msgs = gson.fromJson(ja, listType);
                MessagesAdapter moduleAdapter = new MessagesAdapter(msgs);
                rv.setAdapter(moduleAdapter);
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    };

    Response.ErrorListener responseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Toast.makeText(getContext(), getString(R.string.errormessages), Toast.LENGTH_SHORT).show();
        }
    };
}
