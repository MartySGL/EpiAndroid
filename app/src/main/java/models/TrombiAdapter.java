package models;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epiandroid.R;
import com.epiandroid.activities.UserActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrombiAdapter extends RecyclerView.Adapter<TrombiAdapter.ModuleViewHolder> {

    private List<Trombi> userlist;

    public TrombiAdapter(List<Trombi> lst) {
        this.userlist = lst;
    }

    public int getItemCount() {
        return userlist.size();
    }

    public void onBindViewHolder(ModuleViewHolder moduleViewHolder, int i) {
        Trombi usr = userlist.get(i);
        moduleViewHolder.vName.setText(usr.getLogin());
        Picasso.with(moduleViewHolder.ctx).load(usr.picture).into(moduleViewHolder.vPicture);
    }

    public ModuleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_trombi, viewGroup, false);

        return new ModuleViewHolder(itemView, userlist);
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vName;
        protected ImageView vPicture;
        CardView cv3;
        List<Trombi> list;
        Context ctx;

        public ModuleViewHolder(View v, List<Trombi> lst) {
            super(v);
            v.setClickable(true);
            v.setOnClickListener(this);
            list = lst;
            cv3 = (CardView) v.findViewById(R.id.cv3);
            vName = (TextView) v.findViewById(R.id.name);
            vPicture = (ImageView) v.findViewById(R.id.picture);
            ctx = v.getContext();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(v.getContext(), UserActivity.class);
            intent.putExtra(v.getContext().getString(R.string.login), list.get(getPosition()).getLogin());
            intent.putExtra(v.getContext().getString(R.string.nom), list.get(getPosition()).getNom());
            intent.putExtra(v.getContext().getString(R.string.prenom), list.get(getPosition()).getPrenom());
            intent.putExtra(v.getContext().getString(R.string.picture), list.get(getPosition()).getPicture());
            v.getContext().startActivity(intent);
        }
    }
}