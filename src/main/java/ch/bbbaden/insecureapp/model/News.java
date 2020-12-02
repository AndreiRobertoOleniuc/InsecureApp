package ch.bbbaden.insecureapp.model;

import java.util.Date;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
public class News {

    private int id;
    @Past
    private Date posted;
    @Size(min = 1, message = "To short shorty")
    private String header;
    @Size(min = 1, message = "To short shorty")
    private String detail;
    @Size(min = 1, message = "To short shorty")
    private String author;
    private boolean isAdminNews;

    public News() {
        posted = new Date();
    }

    public News(int id, Date posted, String header, String detail, String author, boolean isAdminNews) {
        this.id = id;
        this.posted = posted;
        this.header = header;
        this.detail = detail;
        this.author = author;
        this.isAdminNews = isAdminNews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean getIsAdminNews() {
        return isAdminNews;
    }

    public void setIsAdminNews(boolean isAdminNews) {
        this.isAdminNews = isAdminNews;
    }

}
