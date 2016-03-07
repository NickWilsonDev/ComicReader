/**
 * @author Nick Wilson
 * @version 2.22.16
 *
 * UnZip.java
 *
 * This class should enable unzipping files.
 * Copied and modified from this site.
 * http://www.avajava.com/tutorials/lessons/how-do-i-unzip-the-contents-of-a-zip-file.html
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;

/**
 * @author Nick Wilson
 * @version 2.25.16
 *
 * UnZip.java
 * This allows the application to work with 'cbz' files.
 */
public class UnZip implements ComicArchive {
    /* A File object that represents the temporary directory where the jpeg
     * comic images will be stored while the application is running.
     */
    File tempDir;

    /* A list of File objects, each one represents a jpeg image and each of 
     * those usually represent a page in the comic
     */
    ArrayList<File> imageFileList = null;
    
    /**
     * The constructor for the class, takes a File parameter that represents
     * the 'cbz' file that will be unzipped.
     *
     * @param
     *  targetFile - a File object that represents the 'cbz' file that will
     *               be unzipped
     */
    public UnZip(File targetFile) {
        tempDir = new File("tempComic");

        if (tempDir.exists()) {
            System.out.println("temp directory already exists...");
        } else {
            System.out.println("Directory does not exist, creating it now");
            boolean success = tempDir.mkdir();
            if (success) {
                System.out.printf("Successfully created new directory : temp");
            } else {
                System.out.printf("Failed to create new directory: temp");
            }
        }

        UnzipUtility unzipper = new UnzipUtility();
        try {
            unzipper.unzip(targetFile.getAbsolutePath(), tempDir.getAbsolutePath());
        } catch (Exception ex) {
            System.out.println("Errors occurred while unzipping archive");
            ex.printStackTrace();
        }
        
        imageFileList = new ArrayList<File>(Arrays.asList(tempDir.listFiles()));

        Collections.sort(imageFileList, new Comparator<File>() {
            @Override
            public int compare(File file2, File file1) {
                return file2.toString().compareTo(file1.toString());
            }
        });
        imageFileList.trimToSize();
    }

    /**
     * This method returns an ArrayList of File objects that represent all jpeg
     * images that were unzipped.
     *
     * @return
     *  ArrayList<File> - list of jpeg images
     */
    public ArrayList<File> getImageList() {
        return imageFileList;
    }

    public File getTempDirectory() {
        return tempDir;
    }

    public JPanel getImagePanel(int index) {
        JPanel panel = null;
        System.out.println("image we are trying for:: " + imageFileList.get(index).toString());
        JLabel imgLabel = new JLabel(new ImageIcon(imageFileList.get(index).toString()));
        panel = new JPanel(new BorderLayout());
        panel.add(imgLabel, BorderLayout.CENTER);
        return panel;
    }
}
            

