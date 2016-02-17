package models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.epiandroid.activities.ModuleActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fragments.ProjectsFragment;

/**
 * Created by packa on 24/11/2015.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ModuleViewHolder> {

    private List<Project> projectlist;
    private ProjectsFragment fragment;
    private View vw;

    public ProjectAdapter(Context context, List<Project> projectlist, ProjectsFragment pf, View view) {
        this.projectlist = projectlist;
        this.fragment = pf;
        this.vw = view;
    }

    public int getItemCount() {
        return projectlist.size();
    }

    public void onBindViewHolder(final ModuleViewHolder moduleViewHolder, int i) {
        final Project proj = projectlist.get(i);
        moduleViewHolder.vTitle.setText(proj.title);
        moduleViewHolder.vNotes.setText(proj.note);

        moduleViewHolder.vShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proj.note != null) {
                    Uri uri = Uri.parse(v.getContext().getString(R.string.smsto));
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    it.putExtra(v.getContext().getString(R.string.sms_body), v.getContext().getString(R.string.strpro)+" \n\n" + moduleViewHolder.vTitle.getText() + " : " + moduleViewHolder.vNotes.getText());
                    it.setType(v.getContext().getString(R.string.itsms));
                    fragment.startActivity(it);
                }
                else {
                    Toast.makeText(fragment.getContext(), v.getContext().getString(R.string.strppro),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        if (proj.register.equals(vw.getContext().getString(R.string.nb1))) {
            moduleViewHolder.vCheck.setText(vw.getContext().getString(R.string.Inscription));
            moduleViewHolder.vCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date_debut;
                    Date date_limite;
                    Date current = new Date();
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat(v.getContext().getString(R.string.format2));

                    try {
                        if (proj.end_register == null)
                            Toast.makeText(fragment.getContext(), v.getContext().getString(R.string.toast1), Toast.LENGTH_LONG).show();
                        else {
                            date_debut = format.parse(proj.start);
                            date_limite = format.parse(proj.end_register);
                            if (current.compareTo(date_debut) > 0 && current.compareTo(date_limite) < 0)
                                fragment.Suscribe(proj.year, proj.codemodule, proj.codein, proj.codeacti);
                            else
                                Toast.makeText(fragment.getContext(), v.getContext().getString(R.string.toast2),
                                        Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        else {
            moduleViewHolder.vCheck.setText(vw.getContext().getString(R.string.Desinscription));
            moduleViewHolder.vCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date_debut;
                    Date date_limite;
                    Date current = new Date();
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat(v.getContext().getString(R.string.format2));

                    try {
                        if (proj.end_register == null)
                            Toast.makeText(fragment.getContext(), v.getContext().getString(R.string.toast1),
                                    Toast.LENGTH_LONG).show();
                        else {
                            date_debut = format.parse(proj.start);
                            date_limite = format.parse(proj.end_register);
                            if (current.compareTo(date_debut) > 0 && current.compareTo(date_limite) < 0)
                                fragment.Unsuscribe(proj.year, proj.codemodule, proj.codein, proj.codeacti);
                            else
                                Toast.makeText(fragment.getContext(), v.getContext().getString(R.string.toast2),
                                        Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public ModuleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_projects, viewGroup, false);

        return new ModuleViewHolder(itemView, projectlist, fragment);
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vTitle;
        protected TextView vNotes;
        protected Button vCheck;
        protected Button vShare;
        CardView cv2;
        List<Project> projlist;
        ProjectsFragment frag;

        public ModuleViewHolder(View v, List<Project> projectlist, ProjectsFragment fr) {
            super(v);
            v.setClickable(true);
            v.setOnClickListener(this);
            frag = fr;
            projlist = projectlist;
            cv2 = (CardView) v.findViewById(R.id.cv2);
            vTitle = (TextView) v.findViewById(R.id.project_title);
            vNotes = (TextView) v.findViewById(R.id.project_note);
            vCheck = (Button) v.findViewById(R.id.checks);
            vShare = (Button) v.findViewById(R.id.share);
        }

        @Override
        public void onClick(View v) {
            String scloar = projlist.get(getPosition()).year;
            String codemod = projlist.get(getPosition()).codemodule;
            String codein = projlist.get(getPosition()).codein;
            String codeact = projlist.get(getPosition()).codeacti;

            frag.Download(scloar, codemod, codein, codeact);
        }
    }
}