package me.ghui.AMS.domain;

/**
 * Created by ghui on 3/25/14.
 */
public class User {
    private String ID;

    public String getID() {
        return ID;
    }

    public User setID(String ID) {
        this.ID = ID;
        return this;
    }

    public String getPassword() {
        return Password;
    }

    public User setPassword(String password) {
        Password = password;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", Password='" + Password + '\'' +
                ", ValidateCode='" + ValidateCode + '\'' +
                '}';
    }

    private String Password;

    private String ValidateCode;

    public User setValidateCode(String validateCode) {
        ValidateCode = validateCode;
        return this;
    }

    public String getValidateCode() {
        return ValidateCode;
    }
}
