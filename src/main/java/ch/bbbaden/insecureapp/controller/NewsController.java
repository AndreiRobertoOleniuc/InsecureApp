package ch.bbbaden.insecureapp.controller;

import ch.bbbaden.insecureapp.model.News;
import ch.bbbaden.insecureapp.model.NewsDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
@Named(value = "newsController")
@SessionScoped
public class NewsController implements Serializable {

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

    public List<News> getNews() {
        return new NewsDAO().getAll();
    }

    public long getDate() {
        return new Date().getTime();
    }

    public String prepareCreate() {
        current = new News();
        return "/secured/news/create";
    }

    public String create() {
        // Get Hidden Fields
        current.setAuthor(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("author"));
        current.setIsAdminNews("true".equals(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isAdminNews")));
        current.setPosted(new Date(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("posted"))));

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

    public String edit() {
        // Get Hidden Fields
        current.setAuthor(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("author"));
        current.setIsAdminNews("true".equals(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isAdminNews")));
        current.setPosted(new Date(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("posted"))));

        new NewsDAO().edit(current);
        current = null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "News changed!", null));
        return "/secured/index?faces-redirect=true";
    }

    public int getDeleteid() {
        return deleteid;
    }

    public void setDeleteid(int deleteid) {
        this.deleteid = deleteid;
    }

    public void delete() {
        NewsDAO newsDAO = new NewsDAO();
        News news = newsDAO.getById(deleteid);
        newsDAO.delete(news);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "News deleted!", null));

        FacesContext facesContext = FacesContext.getCurrentInstance();
        String outcome = "/secured/index"; // Do your thing?
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
    }

    public String delete(News news) {
        new NewsDAO().delete(news);
        current = null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "News deleted!", null));

        return "/secured/index?faces-redirect=true";
    }

}