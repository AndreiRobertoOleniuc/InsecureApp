package ch.bbbaden.insecureapp.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
public class NewsDAO {

    public NewsDAO() throws SQLException {
        if (!DbAccess.tableExists("news")) {
            createInitialNews();
        }
    }

    private void createInitialNews() throws SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS news ("
                + " id INTEGER PRIMARY KEY,"
                + " posted DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + " header TEXT NOT NULL,"
                + " detail TEXT,"
                + " author TEXT NOT NULL,"
                + " is_admin_news INTEGER DEFAULT 0 NOT NULL"
                + ");";
        try (Statement stmt = DbAccess.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, e);
        }

        Calendar posted = Calendar.getInstance();
        posted.set(2017, 2, 15, 18, 30);
        insert(new News(0, posted.getTime(), "Initial Release", "The BBB InsecureApp has been released for Module 183.", "administrator", true));
        posted.set(2017, 2, 10, 11, 15);
        insert(new News(0, posted.getTime(), "Hackbraten!", "Die Semmeln in Scheiben schneiden und mit Wasser übergiessen, quellen lassen. Gut ausdrücken. Die Gewürzgurken in sehr feine Würfel schneiden. Zwiebeln ebenfalls in feine Würfel schneiden.", "user", false));
        posted.set(2017, 2, 9, 9, 40);
        insert(new News(0, posted.getTime(), "Fröhliches Hacken", "Hack and have fun!", "user", false));
        posted.set(2017, 2, 8, 11, 45);
        insert(new News(0, posted.getTime(), "Search available by HTTP GET", "You can now use search with GET: <a href=\"searchResult.xhtml?searchString=Search-By-GET\">searchResult.xhtml?searchString=Search-By-GET</a>", "administrator", true));
    }

    public List<News> getAll() {
        final String sql = "SELECT id, posted, header, detail, author, is_admin_news FROM news ORDER BY posted DESC";
        ArrayList<News> allNews = new ArrayList<>();

        try (Statement stmt = DbAccess.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                allNews.add(new News(rs.getInt("id"), rs.getTimestamp("posted"), rs.getString("header"), rs.getString("detail"), rs.getString("author"), rs.getBoolean("is_admin_news")));
            }
        } catch (SQLException e) {
            Logger.getLogger(NewsDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return allNews;
    }

    public int insert(News news) throws SQLException {
        int id = 0;
        String query = "INSERT INTO news (posted, header, detail, author, is_admin_news) VALUES (?,?,?,?,?)";
        PreparedStatement ps = DbAccess.getConnection().prepareStatement(query);
        ps.setTimestamp(1, new java.sql.Timestamp(news.getPosted().getTime()));
        ps.setString(2, news.getHeader());
        ps.setString(3, news.getDetail());
        ps.setString(4, news.getAuthor());
        ps.setInt(5, Integer.parseInt(news.getIsAdminNews() ? "1" : "0"));
        ps.execute();
        ps.close();

        return id;

    }

    public void edit(News news) {
        String sql2 = "UPDATE news SET posted=?, "
                + "header=?,"
                + "detail=?,"
                + "author=?,"
                + "is_admin_news=? "
                + "WHERE id = ?";

        try (PreparedStatement ps = DbAccess.getConnection().prepareStatement(sql2)) {
            ps.setTimestamp(1, new java.sql.Timestamp(news.getPosted().getTime()));
            ps.setString(2, news.getHeader());
            ps.setString(3, news.getDetail());
            ps.setString(4, news.getAuthor());
            ps.setInt(5, Integer.parseInt(news.getIsAdminNews() ? "1" : "0"));
            ps.setInt(6, news.getId());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(NewsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void delete(News news) {
        final String sql = "DELETE FROM news WHERE id = " + news.getId();

        try (Statement stmt = DbAccess.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            Logger.getLogger(NewsDAO.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public News getById(long id) {
        final String sql = "SELECT id, posted, header, detail, author, is_admin_news FROM news WHERE id = " + id;

        News news = null;

        try (Statement stmt = DbAccess.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                news = new News(rs.getInt("id"), rs.getTimestamp("posted"), rs.getString("header"), rs.getString("detail"), rs.getString("author"), rs.getBoolean("is_admin_news"));
            }
        } catch (SQLException e) {
            Logger.getLogger(NewsDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return news;
    }
}
