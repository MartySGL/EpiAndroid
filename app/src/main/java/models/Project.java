package models;

/**
 * Created by packa on 21/11/2015.
 */
public class Project {

    String year;
    String title;
    String note;
    String codemodule;
    String codeacti;
    String nb_notes;
    String register;
    String end_register;
    String start;
    String codein;

    public Project(String code, String yr, String p_title, String mk, String codem, String codeact, String tp, String reg, String e_register, String st) {
        codein = code;
        year = yr;
        title = p_title;
        note = mk;
        codemodule = codem;
        codeacti = codeact;
        nb_notes = tp;
        register = reg;
        end_register = e_register;
        start = st;
    }

    public String getCodein () {
        return codein;
    }

    public String getYear() {
        return year;
    }

    public String getRegister () {
        return register;
    }

    public  String getCodemodule() {
        return codemodule;
    }

    public String getTitle() {
        return title;
    }

    public String getNote(){
        return note;
    }

    public String getCodeacti(){
        return codeacti;
    }

    public String getNb_notes() {
        return nb_notes;
    }

    public String getEnd_register() {
        return end_register;
    }

   public String getStart() {
       return start;
   }

}
