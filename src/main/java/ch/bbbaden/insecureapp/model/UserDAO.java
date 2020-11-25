package ch.bbbaden.insecureapp.model;

import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.w3c.dom.*;
import javax.faces.context.FacesContext;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 *
 * @author Alexander Flick / adapted by Michael Schneider
 */
public class UserDAO {

    private final File USER_FILE;

    public UserDAO() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        path = path + "WEB-INF/users.xml";
        USER_FILE = new File(path);
    }

    public User check(final String username, final String password) {
        User returnUser = null;

        try {
            final DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document doc = dBuilder.parse(USER_FILE);
            doc.getDocumentElement().normalize();
            final NodeList userList = doc.getElementsByTagName("user");
            for (int userIndex = 0; userIndex < userList.getLength(); userIndex++) {
                final Node user = userList.item(userIndex);
                if (user.getNodeType() == Node.ELEMENT_NODE) {
                    final Element userElement = (Element) user;
                    final int entryId = Integer.parseInt(userElement.getAttribute("id"));
                    final String entryUsername = userElement.getElementsByTagName("username").item(0).getTextContent();
                    final String entryPassword = userElement.getElementsByTagName("password").item(0).getTextContent();
                    final boolean isAdmin = "1".equals(userElement.getElementsByTagName("isadmin").item(0).getTextContent());
                    if (username.toLowerCase().equals(entryUsername.toLowerCase()) && password.equals(entryPassword)) {
                        returnUser = new User(entryId, entryUsername, entryPassword, isAdmin);
                    }
                }
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return returnUser;
    }

    public void changePassword(User user, String newPassword) {
        try {
            final DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document doc = dBuilder.parse(USER_FILE);
            doc.getDocumentElement().normalize();
            final NodeList userList = doc.getElementsByTagName("user");
            for (int userIndex = 0; userIndex < userList.getLength(); userIndex++) {
                final Node xmlUser = userList.item(userIndex);
                if (xmlUser.getNodeType() == Node.ELEMENT_NODE) {
                    final Element userElement = (Element) xmlUser;
                    final int entryId = Integer.parseInt(userElement.getAttribute("id"));
                    if (entryId == user.getId()) {
                        // Because passwords can contain special chars
                        Node disableEscapingLt = doc.createProcessingInstruction(StreamResult.PI_DISABLE_OUTPUT_ESCAPING, "<");
                        Node disableEscapingGt = doc.createProcessingInstruction(StreamResult.PI_DISABLE_OUTPUT_ESCAPING, ">");
                        
                        final Node password = userElement.getElementsByTagName("password").item(0);
                        password.setTextContent(newPassword);
                        password.getParentNode().insertBefore(disableEscapingGt, password);
                        password.getParentNode().insertBefore(disableEscapingLt, password);
                        
                        break;
                    }
                }
            }
            writeDocumentToFile(doc);
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
            return;
        }

    }

    private void writeDocumentToFile(Document document) {

        try {
            // Make a transformer factory to create the Transformer
            TransformerFactory tFactory = TransformerFactory.newInstance();

            // Make the Transformer
            Transformer transformer = tFactory.newTransformer();

            // Mark the document as a DOM (XML) source
            DOMSource source = new DOMSource(document);

            // Say where we want the XML to go
            StreamResult result = new StreamResult(USER_FILE);

            // Write the XML to file
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }

}
