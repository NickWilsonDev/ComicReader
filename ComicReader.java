/**
 * @author Nick Wilson
 * @version 1.18.16
 *
 * This application will allow an user to read "digital" forms of comic books
 * .cbz and .cbr files.
 */

public class ComicReader {

    /**
     * Constructor for this class. Provides an entry point into the application.
     */
    public ComicReader() {
        MainWindow mainWindow = new MainWindow();
        mainWindow.display();
    }

    /**
     * Main method for application. Creates a ComicReader object and that sets
     * everything into motion.
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ComicReader reader = new ComicReader();
            }
        });
    }
}
