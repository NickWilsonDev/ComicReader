/**
 * @author Nick Wilson
 * @version 1.18.16
 *
 * This application will allow an user to read "digital" forms of comic books
 * .cbz and .cbr files.
 */

public class ComicReader {
    public ComicReader() {
        MainWindow mainWindow = new MainWindow();
        mainWindow.display();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ComicReader reader = new ComicReader();
            }
        });
    }
}
