package me.ghui.AMS.domain;

/**
 * Created by ghui on 3/25/14.
 */
public class User {
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Password;
}
