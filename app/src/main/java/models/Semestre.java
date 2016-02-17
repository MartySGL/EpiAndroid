package models;

import java.util.List;

/**
 * Created by packa on 23/11/2015.
 */
public class Semestre {

    int nb;
    List<Modules> listmod;

    public Semestre (int nb, List<Modules> list) {
        this.nb = nb;
        this.listmod = list;
    }

    public void setListmod(List<Modules> lst) {
        listmod = lst;
    }

    public List<Modules> getListmod() {
        return listmod;
    }

    public void setNb(int number) {
        nb = number;
    }

    public int getNb() {
        return nb;
    }
}
