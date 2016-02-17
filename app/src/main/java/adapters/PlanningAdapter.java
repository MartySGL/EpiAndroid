package adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epiandroid.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Planning;

/*
 * Created by Marty on 30/11/2015.
 */
public class PlanningAdapter extends RecyclerView.Adapter<PlanningAdapter.PlanningViewHolder> {

    private List<Planning> planning;
    ProgressDialog mProgressDialog;

    public PlanningAdapter (List<Planning> planning) {
        this.planning = planning;
    }

    private String getMonth(int dt, PlanningViewHolder planningViewHolder) {

        dt++;
        switch (dt) {
            case 1:
                return planningViewHolder.view.getContext().getString(R.string.janvier);
            case 2:
                return planningViewHolder.view.getContext().getString(R.string.fevrier);
            case 3:
                return planningViewHolder.view.getContext().getString(R.string.mars);
            case 4:
                return planningViewHolder.view.getContext().getString(R.string.avril);
            case 5:
                return planningViewHolder.view.getContext().getString(R.string.mai);
            case 6:
                return planningViewHolder.view.getContext().getString(R.string.juin);
            case 7:
                return planningViewHolder.view.getContext().getString(R.string.juillet);
            case 8:
                return planningViewHolder.view.getContext().getString(R.string.aout);
            case 9:
                return planningViewHolder.view.getContext().getString(R.string.septembre);
            case 10:
                return planningViewHolder.view.getContext().getString(R.string.octobre);
            case 11:
                return planningViewHolder.view.getContext().getString(R.string.novembre);
            case 12:
                return planningViewHolder.view.getContext().getString(R.string.decembre);
            default:
                return "";
        }
    }

    private String getHour(Calendar calst, Calendar calend) {

        String str = "";
        int i =  calst.get(Calendar.HOUR_OF_DAY);
        if (i < 10)
            str = str + "0";
        str = str + Integer.toString(i) + "h:";
        i = calst.get(Calendar.MINUTE);
        if (i < 10)
            str = str + "0";
        str = str + Integer.toString(i) + " à ";

        i =  calend.get(Calendar.HOUR_OF_DAY);
        if (i < 10)
            str = str + "0";
        str = str + Integer.toString(i) + "h:";
        i = calend.get(Calendar.MINUTE);
        if (i < 10)
            str = str + "0";

        str = str + Integer.toString(i);
        return str;
    }

    public int getItemCount() {
        return planning.size();
    }

    public void onBindViewHolder(final PlanningViewHolder planningViewHolder, int i) {
        final Planning plan = this.planning.get(i);
        planningViewHolder.vTitle.setText(plan.getTitle());

        if (plan.getDate() != null) {
            planningViewHolder.dateday.setText(Integer.toString(plan.getDate().get(Calendar.DAY_OF_MONTH)));
            planningViewHolder.date.setText(getMonth(plan.getDate().get(Calendar.MONTH), planningViewHolder));
            planningViewHolder.txtHour.setText(getHour(plan.getDate(), plan.getDateEnd()));
        }

        String reg = planning.get(i).getRegistered();

        if (reg == null) {
            planningViewHolder.token.setVisibility(View.GONE);
        } else if (!plan.getAllowRegister()) {
            if (reg.equals("present")) {
                planningViewHolder.token.setBackgroundResource(R.drawable.ic_token_valid);
                planningViewHolder.token.setTag(R.drawable.ic_token_valid);
                planningViewHolder.token.setVisibility(View.VISIBLE);
            } else
                planningViewHolder.token.setVisibility(View.GONE);
        } else if (reg.equals("false")) {
            planningViewHolder.token.setBackgroundResource(R.drawable.ic_add);
            planningViewHolder.token.setTag(R.drawable.ic_add);
            planningViewHolder.token.setVisibility(View.VISIBLE);
        } else if (reg.equals("present")) {
            planningViewHolder.token.setBackgroundResource(R.drawable.ic_token_valid);
            planningViewHolder.token.setTag(R.drawable.ic_token_valid);
            planningViewHolder.token.setVisibility(View.VISIBLE);
        } else {
            if (planning.get(i).getAllowToken()) {
                planningViewHolder.token.setBackgroundResource(R.drawable.ic_token_invalid);
                planningViewHolder.token.setTag(R.drawable.ic_token_invalid);
                planningViewHolder.token.setVisibility(View.VISIBLE);
            }
            else {
                planningViewHolder.token.setBackgroundResource(R.drawable.ic_remove);
                planningViewHolder.token.setTag(R.drawable.ic_remove);
                planningViewHolder.token.setVisibility(View.VISIBLE);
            }
        }

        final Context context = planningViewHolder.view.getContext();

        if (reg.equals("present")) {
            planningViewHolder.token.setOnClickListener(null);
        } else {

            planningViewHolder.token.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer tag = (Integer) planningViewHolder.token.getTag();

                    switch (tag) {
                        case R.drawable.ic_add:
                            add_event(planningViewHolder.view.getContext(), plan);
                            planningViewHolder.token.setBackgroundResource(R.drawable.ic_remove);
                            planningViewHolder.token.setTag(R.drawable.ic_remove);
                            break;
                        case R.drawable.ic_remove:
                            remove_event(planningViewHolder.view.getContext(), plan);
                            planningViewHolder.token.setBackgroundResource(R.drawable.ic_add);
                            planningViewHolder.token.setTag(R.drawable.ic_add);
                            break;
                        case R.drawable.ic_token_invalid:
                            final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);
                            inputAlert.setTitle("Entrez votre token :");
                            final EditText userInput = new EditText(context);
                            inputAlert.setView(userInput);
                            inputAlert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = userInput.getText().toString();
                                    if (!input.equals("")) {
                                        showProgress(true);
                                        validToken(plan, context, input);
                                    }
                                }
                            });
                            inputAlert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = inputAlert.create();
                            alertDialog.show();
                            break;
                    }
                }
            });
        }
    }

    public PlanningViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_planning, viewGroup, false);

        PlanningViewHolder plvh = new PlanningViewHolder(itemView, planning);

        mProgressDialog = new ProgressDialog(plvh.view.getContext());
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return plvh;
    }

    public static class PlanningViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView dateday;
        protected TextView date;
        protected TextView txtHour;
        protected View view;
        protected Button token;
        CardView cv;
        List<Planning> planning;

        public PlanningViewHolder (View v, List<Planning> planning) {
            super(v);
            v.setClickable(true);
            view = v;
            this.planning = planning;
            cv = (CardView) v.findViewById(R.id.cv_planning);
            vTitle = (TextView) v.findViewById(R.id.txtTitle);
            dateday = (TextView) v.findViewById(R.id.txtDate);
            date = (TextView) v.findViewById(R.id.txtDateSec);
            txtHour = (TextView) v.findViewById(R.id.txtHour);
            token = (Button) v.findViewById(R.id.btnToken);
        }
    }

    private void showProgress(final boolean show) {

        if (show) {
            mProgressDialog.show();
        } else
            mProgressDialog.dismiss();
    }

    Context ctx;

    private void validToken(final Planning planning, final Context context, final String vToken) {

        ctx = context;
        String url = context.getString(R.string.url_token);
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.preferences), 0);
        final String token = settings.getString(context.getString(R.string.token), context.getString(R.string.default_shared));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener, responseError) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(context.getString(R.string.token), token);
                params.put(context.getString(R.string.scolaryear), planning.getScolaryear());
                params.put(context.getString(R.string.codemodule), planning.getCodemodule());
                params.put(context.getString(R.string.codeinstance), planning.getCodeinstance());
                params.put(context.getString(R.string.codeacti), planning.getCodeacti());
                params.put(context.getString(R.string.codeevent), planning.getCodeevent());
                params.put(context.getString(R.string.tokenvalidationcode), vToken);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            showProgress(false);
            Toast.makeText(ctx, ctx.getString(R.string.tokenok), Toast.LENGTH_SHORT).show();
        }
    };

    Response.ErrorListener responseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showProgress(false);
            error.printStackTrace();
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void add_event(final Context context, final Planning planning) {

        ctx = context;
        String url = context.getString(R.string.url_event);
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.preferences), 0);
        final String token = settings.getString(context.getString(R.string.token), context.getString(R.string.default_shared));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseaddListener, responseaddError) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(context.getString(R.string.token), token);
                params.put(context.getString(R.string.scolaryear), planning.getScolaryear());
                params.put(context.getString(R.string.codemodule), planning.getCodemodule());
                params.put(context.getString(R.string.codeinstance), planning.getCodeinstance());
                params.put(context.getString(R.string.codeacti), planning.getCodeacti());
                params.put(context.getString(R.string.codeevent), planning.getCodeevent());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    Response.Listener<String> responseaddListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            Toast.makeText(ctx, ctx.getString(R.string.), Toast.LENGTH_SHORT).show();
        }
    };

    Response.ErrorListener responseaddError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void remove_event(final Context context, final Planning planning) {

        ctx = context;
        String url = context.getString(R.string.url_event);
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.preferences), 0);
        final String token = settings.getString(context.getString(R.string.token), context.getString(R.string.default_shared));

        url = url + "?" + context.getString(R.string.token) + "=" + token + "&" + context.getString(R.string.scolaryear) + "=" + planning.getScolaryear()
                + "&" + context.getString(R.string.codemodule) + "=" + planning.getCodemodule() + "&" + context.getString(R.string.codeinstance) + "=" + planning.getCodeinstance() +
                "&" + context.getString(R.string.codeacti) + "=" + planning.getCodeacti() + "&" + context.getString(R.string.codeevent) + "=" + planning.getCodeevent();


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, responseremoveListener, responseremoveError) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(context.getString(R.string.token), token);
                params.put(context.getString(R.string.scolaryear), planning.getScolaryear());
                params.put(context.getString(R.string.codemodule), planning.getCodemodule());
                params.put(context.getString(R.string.codeinstance), planning.getCodeinstance());
                params.put(context.getString(R.string.codeacti), planning.getCodeacti());
                params.put(context.getString(R.string.codeevent), planning.getCodeevent());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    Response.Listener<String> responseremoveListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            Toast.makeText(ctx, ctx.getString(R.string.), Toast.LENGTH_SHORT).show();
        }
    };

    Response.ErrorListener responseremoveError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}
