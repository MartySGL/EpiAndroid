package models;

/**
 * Created by packa on 02/12/2015.
 */
public class Myfile {
    String title;
    String fullpath;

    public Myfile(String ttl, String path) {
        this.title = ttl;
        this.fullpath = path;
    }

    public String getTitle() {
        return title;
    }

    public String getFullpath() {
        return fullpath;
    }
}
