package ch.bbbaden.insecureapp.model;

import java.io.Serializable;

/**
 *
 * @author Alexander Flick / adapted by Michael Schneider
 */
public class User implements Serializable {

    private final int id;
    private final String username;
    private final String password;
    private final boolean isAdmin;

    public User(int id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

}
