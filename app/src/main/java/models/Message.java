package models;

import com.google.gson.annotations.SerializedName;

/*
 * Created by Marty on 24/11/2015.
 */
public class Message {

    @SerializedName("title")
    String title;
    @SerializedName("content")
    String content;
    @SerializedName("date")
    String date;
    @SerializedName("user")
    User user;

    public Message(String title, String content, String date, User user) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }
}
