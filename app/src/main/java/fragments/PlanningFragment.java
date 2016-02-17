package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epiandroid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.PlanningAdapter;
import models.Planning;

public class PlanningFragment extends Fragment {

    RecyclerView rv;
    TextView txt;

    public PlanningFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning, container, false);

        getActivity().setTitle(R.string.action_planning);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv = (RecyclerView) view.findViewById(R.id.calendar);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preferences), 0);
        String token = settings.getString(getString(R.string.token), getString(R.string.default_shared));
        String url = getString(R.string.url_planning);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String st = df.format(cal.getTime());

        Calendar calend = Calendar.getInstance();
        calend.add(Calendar.DATE, +7);
        SimpleDateFormat dfend = new SimpleDateFormat("yyyy/MM/dd");
        String stend = dfend.format(calend.getTime());


        url = url + "?" + getString(R.string.token) + "=" + token + "&" + getString(R.string.start) + "=" + st + "&" + getString(R.string.end) + "=" + stend;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseError);
        Volley.newRequestQueue(getContext()).add(stringRequest);

        return view;
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {

                Type fooType = new TypeToken<ArrayList<Planning>>() {}.getType();
                List<Planning> array = new Gson().fromJson(response, fooType);
                List<Planning> planning = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {

                    if (array.get(i).isModule())
                        planning.add(array.get(i));
                }

                for (int i = 0; i < planning.size(); i++) {

                    Calendar c = null;
                    Calendar cend = null;
                    String dtStart = planning.get(i).getDatestart();
                    String dtEnd = planning.get(i).getDateend();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat formatE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        format.parse(dtStart);
                        c = format.getCalendar();
                        formatE.parse(dtEnd);
                        cend = formatE.getCalendar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    planning.get(i).setDate(c);
                    planning.get(i).setDateEnd(cend);
                }

                Collections.sort(planning, new Comparator<Planning>() {
                    public int compare(Planning o1, Planning o2) {
                        if (o1.getDate() == null || o2.getDate() == null)
                            return 0;
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });

                PlanningAdapter planningAdapter = new PlanningAdapter(planning);
                rv.setAdapter(planningAdapter);

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
