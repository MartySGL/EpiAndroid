package adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epiandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import models.Message;

/**
 * Created by Marty on 24/11/2015.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Message> messages;

    public MessagesAdapter (List<Message> messages) {
        this.messages = messages;
    }

    public int getItemCount() {
        return messages.size();
    }

    public void onBindViewHolder(MessageViewHolder messageViewHolder, int i) {
        Message mess = messages.get(i);
        messageViewHolder.vTitle.setText(removeHtml(mess.getTitle()));
        messageViewHolder.vContent.setText(removeHtml(mess.getContent()));
        if (mess.getUser().getPicture() != null)
            Picasso.with(messageViewHolder.viewGroup.getContext()).load(mess.getUser().getPicture()).into(messageViewHolder.vImg);
    }

    public String removeHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_message, viewGroup, false);

        return new MessageViewHolder(itemView, viewGroup);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vContent;
        protected ImageView vImg;
        protected ViewGroup viewGroup;
        CardView cv;

        public MessageViewHolder (View v, ViewGroup viewGroup) {
            super(v);
            v.setClickable(true);
            cv = (CardView) v.findViewById(R.id.cv_message);
            vTitle = (TextView) v.findViewById(R.id.txtTitle);
            vContent = (TextView) v.findViewById(R.id.txtContent);
            vImg = (ImageView) v.findViewById(R.id.imgMessage);
            this.viewGroup = viewGroup;
        }
    }
}

