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

//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;

public class UnZip implements ComicArchive {
    File tempDir;
    ArrayList<File> imageFileList = null;
    
    public UnZip(File targetFile) {
            /// not done
        //Archive a = null;
        tempDir = new File("temp");

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
            unzipper.unzip(targetFile.getAbsolutePath(), "temp");
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
            

