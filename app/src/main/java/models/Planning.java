package models;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

/*
 * Created by Marty on 30/11/2015.
 */
public class Planning {

    @SerializedName("event_registered")
    String registered;
    @SerializedName("module_registered")
    boolean module;
    @SerializedName("acti_title")
    String title;
    @SerializedName("start")
    String datestart;
    @SerializedName("end")
    String dateend;
    @SerializedName("allow_token")
    boolean allowToken;
    @SerializedName("scolaryear")
    String scolaryear;

    @SerializedName("allow_register")
    boolean allowRegister;

    @SerializedName("codemodule")
    String codemodule;

    @SerializedName("codeinstance")
    String codeinstance;

    @SerializedName("codeacti")
    String codeacti;

    @SerializedName("codeevent")
    String codeevent;

    Calendar dateStart;
    Calendar dateEnd;

    public boolean getAllowToken() {
        return allowToken;
    }

    public boolean getAllowRegister() {
        return allowRegister;
    }

    public String getRegistered() {
        return registered;
    }

    public boolean isModule() {
        return module;
    }

    public String getTitle() {
        return title;
    }

    public String getDatestart() {
        return datestart;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDate(Calendar date) {
        this.dateStart = date;
    }

    public Calendar getDate() {
        return dateStart;
    }

    public void setDateEnd(Calendar date) {
        this.dateEnd = date;
    }

    public Calendar getDateEnd() {
        return dateEnd;
    }

    public String getScolaryear() {
        return scolaryear;
    }

    public String getCodemodule() {
        return codemodule;
    }

    public String getCodeinstance() {
        return codeinstance;
    }

    public String getCodeacti() {
        return codeacti;
    }

    public String getCodeevent() {
        return codeevent;
    }

}
