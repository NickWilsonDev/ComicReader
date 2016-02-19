/**
 * @author Nick Wilson
 * @version 1.17.16
 *
 * This class models the main frame or window of the application.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;

import java.io.File;


import java.nio.file.NoSuchFileException;
import java.nio.file.DirectoryNotEmptyException;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class MainWindow {

    private JFrame mainFrame;
    private JMenuBar mainMenuBar;
    private JMenu menuFile;

    private JMenu menuRight;
    private JMenu menuLeft;

    private JMenuItem menuOpen;
    private JMenuItem menuExit;
    private int currentPage;

    protected JPanel comicPanel = null;
    protected UnRar comic;
/** keyEvent codes for future use left-right = forward back pages
 * up-down = zoom in or out
 * VK_UP
 * VK_DOWN
 * VK_LEFT
 * VK_RIGHT
 *
 *
 * java's built in java.util.LinkedList is doubly linked
 */


    /**
     * Constructor for this class.
     * This method calls appropriate helper methods to setup the window, menu,
     * ect.
     */
    public MainWindow() {
        JPanel comicPanel = new JPanel();
        mainFrame = new JFrame("ComicReader");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        
        //mainFrame.addKeyListener(new MyKeyListener());
        
        // setup menu
        mainMenuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        currentPage = 1;
        comic = null;

        setupOpen();
        setupExit();
        setupArrows();
       // setupLeft();
       // setupRight();
    }

    private void setupArrows() {
        menuLeft = new JMenu("Left");
        menuLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent leftEvent) {
                pressLeft();
            }
        });
        menuRight = new JMenu("Right");
        menuRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent rightEvent) {
                pressRight();
            }
        });
        mainFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    pressLeft();
                }
                if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    pressRight();
                }
            }
        });
    }
    private void setupOpen() {                                                  
        menuOpen = new JMenuItem("Open"); // may add icons later                
        menuOpen.setMnemonic(KeyEvent.VK_O);                                    
        menuOpen.addActionListener(new ActionListener() {                       
            @Override                                                           
            public void actionPerformed(ActionEvent saveEvent) {                
                File targetFile = null;
                targetFile = FileNavigator("Open");                       

                if (targetFile != null) {
                    System.out.println("Filename:: " + targetFile.getName());
                    // if zip if rar file
                    comic = new UnRar(targetFile);
                    mainFrame.invalidate();
                    comicPanel = comic.getImagePanel(1);
                    currentPage = 1;
                    mainFrame.getContentPane().removeAll();
                    mainFrame.getContentPane().add(comicPanel);
                    mainFrame.validate();
                } else {
                    System.out.println("No comic selected this time");
                }
            }                                                                   
        });                                                                     
    }
    
    private void pressRight() {
        if (comic != null) {
            if (currentPage < comic.getImageList().size()) {
                currentPage++;
                comicPanel = comic.getImagePanel(currentPage);
                mainFrame.getContentPane().removeAll();
                mainFrame.getContentPane().add(comicPanel);
                mainFrame.validate();
            }
        } else {
            System.out.println("right key pressed but no comic selected");
        }
    }

    private void pressLeft() {
        if (comic != null) {
            if (currentPage == 1) {
                currentPage = 1;
            } else {
                currentPage--;
                comicPanel = comic.getImagePanel(currentPage);
                mainFrame.getContentPane().removeAll();
                mainFrame.getContentPane().add(comicPanel);
                mainFrame.validate();
            }
        } else {
            System.out.println("left key pressed but no comic selected");
        }
    }
         

    private void setupExit() {                                                  
        menuExit = new JMenuItem("Exit");                                       
        menuExit.setMnemonic(KeyEvent.VK_X);                                    
        menuExit.addActionListener(new ActionListener() {                       
            @Override                                                           
            public void actionPerformed(ActionEvent event) {                    
                cleanup();
                System.exit(0);                                                 
                // may need a dialog box before closing or auto save            
            }                                                                   
        });                                                                     
    }

    private void cleanup() {
        if (comic != null) {
            System.out.println("now removing temporary files");
            
            for( File file : comic.getImageList()) {
                    System.out.println("removing :: " + file.toString());
                    file.delete();
            }
            // remove temp directory
            File dir = comic.getTempDirectory();
            if (dir != null)
                dir.delete();
        }
    }


private File FileNavigator(String option) {                               
        String filename = "";                                                   
        File selectedFile = null;
        JFileChooser fileChooser;                                               
        fileChooser = new JFileChooser();                                       
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);   
        if (option.equals("Open")) {                                            
            int returnCode = fileChooser.showOpenDialog(null);                  
            if (returnCode == JFileChooser.APPROVE_OPTION) {                    
                selectedFile = fileChooser.getSelectedFile();              
                filename = selectedFile.getName();  
                System.out.println("isFile():: " + selectedFile.isFile());
                System.out.println("File Absolute path:: " + selectedFile.getAbsolutePath());
                try {
                    System.out.println("File canonical path:: " + selectedFile.getCanonicalPath());
                } catch (IOException io) {
                    System.out.println("IOException in FileNavigator method");
                }
                System.out.println("File Path:: " + selectedFile.getPath());                            
                System.out.println("FileNavigator selected filename: " + filename);                                   
            }                                                                   
        } else {                                                                
            fileChooser.showDialog(null, "Save");                               
        }                                                                       
        //return filename;
        return selectedFile;                                                      
    }                                                                           
                                                                                
    public void display() {                                                     
        menuFile.add(menuOpen);                                                 
        menuFile.add(menuExit);                                                 
                                                                                
        mainMenuBar.add(menuFile);                                              
        mainMenuBar.add(menuLeft);
        mainMenuBar.add(menuRight);
        mainFrame.setJMenuBar(mainMenuBar);
        
        mainFrame.pack();                                                       
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // max window size   
        mainFrame.setVisible(true);                                             
    }        
}      
