/**
 * @author Nick Wilson
 * @version 2.24.16
 *
 * ComicArchive.java
 *
 * An Archive that will either be a zip file or a rar file.
 */

import java.util.ArrayList;
import java.io.File;
import javax.swing.JPanel;

public interface ComicArchive {

    /* Method returns an ArrayList of file objects, that are the images or
     * pages of the comic book
     */
    public ArrayList<File> getImageList();

    public JPanel getImagePanel(int index);

    public File getTempDirectory();
    // may add other methods later

}
