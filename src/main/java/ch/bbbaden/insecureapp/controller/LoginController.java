package ch.bbbaden.insecureapp.controller;

import ch.bbbaden.insecureapp.model.UserDAO;
import ch.bbbaden.insecureapp.model.User;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author Alexander Flick / adapted by Michael Schneider
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @Size(min = 2, message = "Your Username to Short")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Bitte gebe richtige Zeichen ein")
    private String username;
    @Size(min = 8, message = "Your Username to Short")
    private String password;
    @Size(min = 8, message = "Your Username to Short")
    private String newPassword;

    private User user;

    public String doLogin() {
        UserDAO udao = new UserDAO();
        this.user = udao.check(username, password);
        if (this.user != null) {
            return "/secured/index?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed!", null));
        return "/index";
    }

    public String changePassword() {
        UserDAO udao = new UserDAO();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Password changed!", null));
        udao.changePassword(user, newPassword);
        this.user = udao.check(username, newPassword);
        newPassword = "";
        return "/secured/index";

    }

    public boolean isLoggedIn() {
        return user != null;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public User getUser() {
        return user;
    }
}
