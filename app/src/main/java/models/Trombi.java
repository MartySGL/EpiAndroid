package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by packa on 01/12/2015.
 */
public class Trombi {
    @SerializedName("nom")
    String nom;
    @SerializedName("prenom")
    String prenom;
    @SerializedName("picture")
    String picture;
    @SerializedName("login")
    String login;

    public Trombi(String login, String name, String firstname, String picture) {
        this.login = login;
        this.nom = name;
        this.prenom = firstname;
        this.picture = picture;
    }

    public String getLogin() {
        return this.login;
    }

    public String getNom () {
        return this.nom;
    }

    public String getPrenom () {
        return this.prenom;
    }

    public String getPicture() {
        return this.picture;
    }
}
