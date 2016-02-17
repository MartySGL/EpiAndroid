package models;

import com.google.gson.annotations.SerializedName;

/*
 * Created by Marty on 23/11/2015.
 */
public class User {

    @SerializedName("lastname")
    String name;
    @SerializedName("firstname")
    String firstname;
    @SerializedName("internal_email")
    String email;
    @SerializedName("picture")
    String picture;

    @SerializedName("title")
    String completeName;

    @SerializedName("url")
    String url;

    @SerializedName("credits")
    String credits;

    public User(String name, String firstname, String email, String picture) {
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.picture = picture;
    }

    public User(String completeName, String picture, String url) {
        this.completeName = completeName;
        this.picture = picture;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public String getCompleteName() {
        return completeName;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return name + " " + firstname;
    }

    public String getCredits() {
        return credits;
    }
}
