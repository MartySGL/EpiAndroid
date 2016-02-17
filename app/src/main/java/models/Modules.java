package models;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

/**
 * Created by packa on 10/11/2015.
 */
public class Modules {
    @SerializedName("title")
    String title;
    @SerializedName("credits")
    String credits;
    @SerializedName("semester")
    String semester;
    @SerializedName("scolaryear")
    String scolaryear;
    @SerializedName("codemodule")
    String codemodule;
    @SerializedName("codeinstance")
    String codeinstance;
    @SerializedName("code")
    String code;

    public Modules(String title, String crd, String sem, String scolar, String codem, String codei) {
        this.title = title;
        this.credits = crd;
        this.semester = sem;
        this.scolaryear = scolar;
        this.codemodule = codem;
        this.code = codem;
        this.codeinstance = codei;
    }

    public void setTitle(String tit) {
        title = tit;
    }

    public String getTitle() {
        return title;
    }

    public void setCredits(String cred) {
        credits = cred;
    }

    public String getCredits() {
        return credits;
    }

    public void setSemester(String sem) {
        semester = sem;
    }

    public String getSemester() {
        return semester;
    }

    public void setScolaryear(String scol) {
        scolaryear = scol;
    }

    public String getScolaryear() {
        return scolaryear;
    }

    public String getCodemodule() {
        return codemodule;
    }

    public String getCode() {
        return code;
    }

    public void setCodemodule(String codem) {
        codemodule = codem;
    }

    public void setCodeinstance(String codei) {
        codeinstance = codei;
    }

    public String getCodeinstance() { return codeinstance; }

}

