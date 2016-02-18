/**
 * @author Nick Wilson
 * @version 1.30.16
 *
 * UnRar.java
 *
 * This class should enable the program to unrar Rar files.
 */
import com.github.junrar.*;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.io.InputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

public class UnRar {

        File filename;
        Archive archive = null;
        FileHeader fh = null;
        ArrayList<File> imageFileList = null;
        File tempDir = null;

    public UnRar(String targetFile) {

        filename = new File(targetFile);
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
            // TODO Auto-generated catch block                                  
            e.printStackTrace();                                                
        } catch (IOException e) {                                               
            // TODO Auto-generated catch block                                  
            e.printStackTrace();                                                
        }                                                                       
        if (a != null) {                                                        
            //a.getMainHeader().print();                                        
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
                    // TODO Auto-generated catch block                          
                    e.printStackTrace();                                        
                } catch (RarException e) {                                      
                    // TODO Auto-generated catch block                          
                    e.printStackTrace();                                        
                } catch (IOException e) {                                       
                    // TODO Auto-generated catch block                          
                    e.printStackTrace();                                        
                }                                                               
                fh = a.nextFileHeader();                                        
            }                                                                   
        }                                                                       
        //File f = new File();
        imageFileList = new ArrayList<File>(Arrays.asList(tempDir.listFiles()));
        
        Collections.sort(imageFileList, new Comparator<File>() {
            @Override
            public int compare(File file2, File file1) {
                return  file2.toString().compareTo(file1.toString());
            }
        });
        //imageFileList.remove(0);
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
        BufferedImage image;
        JPanel panel = null;
        System.out.println("image we are trying for:: " + imageFileList.get(index).toString());
        JLabel imgLabel = new JLabel(new ImageIcon(imageFileList.get(index).toString()));
        //try {
          //  image = ImageIO.read(out); //files[0]);

            //ImageIcon image = new ImageIcon(im);
            //JLabel label = new JLabel(" hey "); //new ImageIcon(image));
            panel = new JPanel(new BorderLayout());
            panel.add(imgLabel, BorderLayout.CENTER);


       // } catch(IOException e) {
       //     System.out.println("ioexception");
        //}
        return panel;
    }

    public void printImageFilenameList() {
        System.out.println("------------ list of comic images -------------");
        for(File temp : imageFileList) {
            System.out.println(temp.toString());
        }
    }
}
