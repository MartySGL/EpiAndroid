package models;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.epiandroid.R;
import com.epiandroid.activities.ModuleActivity;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by packa on 11/11/2015.
 */
public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private List<Modules> modulelist;

    public ModuleAdapter (List<Modules> modulelist) {
        this.modulelist = modulelist;
    }

    public int getItemCount() {
        return modulelist.size();
    }

    public void onBindViewHolder(ModuleViewHolder moduleViewHolder, int i) {
        Modules mod = modulelist.get(i);
        moduleViewHolder.vTitle.setText(mod.title);
        moduleViewHolder.vCredits.setText(mod.credits);
    }

    public ModuleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_modules, viewGroup, false);

        return new ModuleViewHolder(itemView, modulelist);
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vTitle;
        protected TextView vCredits;
        CardView cv;
        List<Modules> modlist;

        public ModuleViewHolder (View v, List<Modules> modulelist) {
            super(v);
            modlist = modulelist;
            v.setClickable(true);
            v.setOnClickListener(this);
            cv = (CardView) v.findViewById(R.id.cv);
            vTitle = (TextView) v.findViewById(R.id.module_title);
            vCredits = (TextView) v.findViewById(R.id.module_cred);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(v.getContext(), ModuleActivity.class);
            intent.putExtra(v.getContext().getString(R.string.title), modlist.get(getPosition()).getTitle());
            intent.putExtra(v.getContext().getString(R.string.crd), modlist.get(getPosition()).getCredits());
            intent.putExtra(v.getContext().getString(R.string.scolaryear), modlist.get(getPosition()).getScolaryear());
            intent.putExtra(v.getContext().getString(R.string.codemodule), modlist.get(getPosition()).getCodemodule());
            intent.putExtra(v.getContext().getString(R.string.codeinstance), modlist.get(getPosition()).getCodeinstance());
            v.getContext().startActivity(intent);
        }
    }

}
