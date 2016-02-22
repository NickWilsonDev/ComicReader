/**
 * @author Nick Wilson
 * @version 1.30.16
 *
 * UnRar.java
 *
 * This class should enable the program to unrar Rar files.
 */

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;

public class UnRar {

        File filename;
        Archive archive = null;
        FileHeader fh = null;
        ArrayList<File> imageFileList = null;
        File tempDir = null;

    public UnRar(File targetFile) {

        filename = targetFile;
        tempDir = new File("temp");                                        
        Archive a = null;                                                       
                                                                                
        // directory stuff                                                      
        if (tempDir.exists()) {                                                 
            System.out.println("temp directory already exists...");             
        } else {                                                                
            System.out.println("Directory not exists, creating now");           
            boolean success = tempDir.mkdir();                                  
            if (success) {                                                      
                System.out.printf("Successfully created new directory : temp"); 
            } else {                                                            
                System.out.printf("Failed to create new directory: temp");      
            }                                                                   
        }                                                                       
                                                                                
        try {                                                                   
            a = new Archive(new FileVolumeManager(filename));                          
        } catch (RarException e) {                                              
            e.printStackTrace();                                                
        } catch (IOException e) {                                               
            e.printStackTrace();                                                
        }                                                                       
        if (a != null) {                                                        
            FileHeader fh = a.nextFileHeader();                                 
            while (fh != null) {                                                
                try {                                                           
                    File out = new File(tempDir.getPath() + "/"                 
                            + fh.getFileNameString().trim());                   
                    System.out.println(out.getAbsolutePath());                  
                    FileOutputStream os = new FileOutputStream(out); 
                    a.extractFile(fh, os);                                      
                    os.close();                                                 
                } catch (FileNotFoundException e) {                             
                    e.printStackTrace();                                        
                } catch (RarException e) {                                      
                    e.printStackTrace();                                        
                } catch (IOException e) {                                       
                    e.printStackTrace();                                        
                }                                                               
                fh = a.nextFileHeader();                                        
            }                                                                   
        }                                                                       
        imageFileList = new ArrayList<File>(Arrays.asList(tempDir.listFiles()));
        
        Collections.sort(imageFileList, new Comparator<File>() {
            @Override
            public int compare(File file2, File file1) {
                return  file2.toString().compareTo(file1.toString());
            }
        });
        imageFileList.trimToSize();
        printImageFilenameList();
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

    public ImageIcon getImage(int index) {
        ImageIcon image = new ImageIcon(imageFileList.get(index).toString());
        return image;
    }

    public void printImageFilenameList() {
        System.out.println("------------ list of comic images -------------");
        for(File temp : imageFileList) {
            System.out.println(temp.toString());
        }
    }
}
