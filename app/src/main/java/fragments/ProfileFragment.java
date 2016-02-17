package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import models.User;

public class ProfileFragment extends Fragment {

    TextView txtName;
    TextView txtMail;
    TextView txtGpa;
    TextView txtCredits;
    ImageView imgProfile;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle(R.string.action_profil);

        txtName = (TextView) view.findViewById(R.id.txtCompleteName);
        txtMail = (TextView) view.findViewById(R.id.txtMail);
        txtGpa = (TextView) view.findViewById(R.id.txtGpa);
        txtCredits = (TextView) view.findViewById(R.id.txtCredits);
        imgProfile = (ImageView) view.findViewById(R.id.imgProfile);

        getCurrentUser();

        return view;
    }

    public void getCurrentUser() {

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString(getString(R.string.token), getString(R.string.default_shared));
        String login = settings.getString(getString(R.string.login), getString(R.string.default_shared));

        String url = getString(R.string.url_user);
        url = url + "?" + getString(R.string.token) + "=" + token + "&" + getString(R.string.user) + "=" + login;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Gson gson = new Gson();

            try {
                JsonParser jp = new JsonParser();
                JsonObject jo = (JsonObject)jp.parse(response);
                JsonArray ggpa = jo.getAsJsonArray(getString(R.string.gpa));

                JsonObject gpa = ggpa.get(0).getAsJsonObject();

                User user = gson.fromJson(jo, User.class);
                if (user != null) {
                    String str = user.getFirstname() + " " + user.getName();
                    txtName.setText(str);
                    txtMail.setText(user.getEmail());
                    String GPA = getString(R.string.GPA) + " " + gpa.get(getString(R.string.gpa)).getAsString() + " " + getString(R.string.cycle) + " : " + gpa.get(getString(R.string.cycle2)).getAsString();
                    txtGpa.setText(GPA);
                    String creds = txtCredits.getText() + " : " + user.getCredits();
                    txtCredits.setText(creds);
                    Picasso.with(getContext()).load(user.getPicture()).into(imgProfile);
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
}
