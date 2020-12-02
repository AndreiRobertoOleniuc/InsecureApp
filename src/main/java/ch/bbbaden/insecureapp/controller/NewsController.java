package ch.bbbaden.insecureapp.controller;

import ch.bbbaden.insecureapp.model.News;
import ch.bbbaden.insecureapp.model.NewsDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
@Named(value = "newsController")
@SessionScoped
public class NewsController implements Serializable {

    @Inject
    private LoginController loginController;

    private News current;
    private int deleteid;

    public NewsController() {
    }

    public News getCurrent() {
        if (current == null) {
            current = new News();
        }
        return current;
    }

    public List<News> getNews() throws SQLException {
        return new NewsDAO().getAll();
    }

    public long getDate() {
        return new Date().getTime();
    }

    public String prepareCreate() {
        current = new News();
        return "/secured/news/create";
    }

    public String create() throws SQLException {
        if (loginController.getUser().isIsAdmin()) {
            current.setIsAdminNews(true);
        } else {
            current.setIsAdminNews(false);
        }
        Date date = new Date();
        current.setAuthor(loginController.getUser().getUsername());
        current.setPosted(date);

        new NewsDAO().insert(current);
        current = null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "News created!", null));
        return "/secured/index?faces-redirect=true";
    }

    public String prepareEdit(News news) {
        current = news;
        return "/secured/news/edit?faces-redirect=true";
    }

    public String edit() throws SQLException {
        if (loginController.getUser().isIsAdmin()) {
            current.setIsAdminNews(true);
        } else {
            current.setIsAdminNews(false);
        }
        Date date = new Date();
        current.setAuthor(loginController.getUser().getUsername());
        current.setPosted(date);

        new NewsDAO().edit(current);
        current = null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "News changed!", null));
        return "/secured/index?faces-redirect=true";
    }

    public String delete(News news) throws SQLException {
        new NewsDAO().delete(news);
        current = null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "News deleted!", null));

        return "/secured/index?faces-redirect=true";
    }

}
