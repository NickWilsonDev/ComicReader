/**
 * @author Nick Wilson
 * @version 1.17.16
 *
 * This class models the main frame or window of the application.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;


import java.nio.file.NoSuchFileException;
import java.nio.file.DirectoryNotEmptyException;

import java.io.IOException;
import java.lang.String;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.KeyStroke;

public class MainWindow {

    private JFrame mainFrame;
    private JMenuBar mainMenuBar;
    private JMenu menuFile;

    private JButton buttonRight;
    private JButton buttonLeft;
    private JButton zoomInButton;
    private JButton zoomOutButton;

    private JMenuItem menuOpen;
    private JMenuItem menuExit;
    private int currentPage;

    protected JScrollPane scrollPane;
    protected JPanel comicPanel = null;
    protected UnRar comic;
    private int imgHeight;
    private int imgWidth;

    /**
     * Constructor for this class.
     * This method calls appropriate helper methods to setup the window, menu,
     * ect.
     */
    public MainWindow() {
        JPanel comicPanel = new JPanel();
        mainFrame = new JFrame("ComicReader");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent winEvent) {
                cleanup();
                System.exit(0);
            }
        });

        // setup menu
        mainMenuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        currentPage = 1;
        comic = null;

        imgHeight = 0;
        imgWidth  = 0;

        setupOpen();
        setupExit();
        setupArrows();
    }

    private void setupArrows() {
        ImageIcon leftArrow = new ImageIcon("icons/leftArrow.png");
        buttonLeft = new JButton(leftArrow);
        buttonLeft.addActionListener(new ActionListener() {
            @Override                                                           
            public void actionPerformed(ActionEvent leftEvent) {   
                System.out.println("menu left pressed...");
                pressLeft();
            }
        });
        ImageIcon rightArrow = new ImageIcon("icons/rightArrow.png");
        buttonRight = new JButton(rightArrow);
        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent rightEvent) {
                System.out.println("menu right pressed...");
                pressRight();
            }
        });
        ImageIcon plusIcon = new ImageIcon("icons/plusIcon.png");
        zoomInButton = new JButton(plusIcon);
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent zoomInEvent) {
                System.out.println("zoom in button pressed...");
                zoomIn();
            }
        });
        ImageIcon minusIcon = new ImageIcon("icons/minusIcon.png");
        zoomOutButton = new JButton(minusIcon);
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent zoomOutEvent) {
                System.out.println("zoom Out button pressed...");
                zoomOut();
            }
        });
    }

    private void zoomIn() {
        System.out.println("Zoom in");
        if (comic != null) {
            JLabel temp = (JLabel)comicPanel.getComponent(0);
            ImageIcon imageIcon = (ImageIcon)temp.getIcon();
            Image image = imageIcon.getImage();
            imgHeight = (int) Math.round(imgHeight * 1.5);
            imgWidth = (int)  Math.round(imgWidth * 1.5);
            image = image.getScaledInstance(imgWidth, imgHeight, 0);
            imageIcon = new ImageIcon(image);
            JLabel imgLabel = new JLabel(imageIcon);
            JPanel comicPanel = new JPanel(new BorderLayout());
            comicPanel.add(imgLabel, BorderLayout.CENTER);
            updatePanel(comicPanel);
        }
    }

    private void addScrollControlls(JScrollPane sPane) {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        InputMap im = vertical.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke("DOWN"), "positiveUnitIncrement");
        im.put(KeyStroke.getKeyStroke("UP"), "negativeUnitIncrement");
        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
        InputMap imh = horizontal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imh.put(KeyStroke.getKeyStroke("RIGHT"), "positiveUnitIncrement");
        imh.put(KeyStroke.getKeyStroke("LEFT"), "negativeUnitIncrement");
    }

    private void setUpHeightWidth(Image image) {
        imgHeight = (int) image.getHeight(null);
        imgWidth  = (int) image.getWidth(null);
    }

    private void updatePanel(JPanel comicPanel) {
            scrollPane = new JScrollPane(comicPanel);
            addScrollControlls(scrollPane);
            mainFrame.getContentPane().removeAll();
            mainFrame.getContentPane().add(scrollPane); //comicPanel);
            mainFrame.validate();
            scrollPane.requestFocusInWindow();
    }

    private void zoomOut() {
        System.out.println("Zoom out");
        if (comic != null) {
            JLabel temp = (JLabel)comicPanel.getComponent(0);
            ImageIcon imageIcon = (ImageIcon)temp.getIcon();
            Image image = imageIcon.getImage();
            imgHeight = (int) Math.round(imgHeight / 1.5);
            imgWidth = (int)  Math.round(imgWidth / 1.5);
            image = image.getScaledInstance(imgWidth, imgHeight, 0);
            imageIcon = new ImageIcon(image);
            JLabel imgLabel = new JLabel(imageIcon);
            JPanel comicPanel = new JPanel(new BorderLayout());
            comicPanel.add(imgLabel, BorderLayout.CENTER);
            updatePanel(comicPanel);
        }
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
                    comicPanel = comic.getImagePanel(0);
                    JLabel temp = (JLabel)comicPanel.getComponent(0);
                    ImageIcon imageIcon = (ImageIcon)temp.getIcon();
                    Image image = imageIcon.getImage();
                    setUpHeightWidth(image);
                    currentPage = 0;
                    updatePanel(comicPanel);
                } else {
                    System.out.println("No comic selected this time, or Improper file format");
                }
            }                                                                   
        });                                                                     
    }
    
    private void pressRight() {
        if (comic != null) {
            if (currentPage < comic.getImageList().size() - 1) {
                currentPage++;
            } else {
                currentPage = currentPage;
            }
            comicPanel = comic.getImagePanel(currentPage);
            updatePanel(comicPanel);
            JLabel temp = (JLabel)comicPanel.getComponent(0);
            ImageIcon imageIcon = (ImageIcon)temp.getIcon();
            Image image = imageIcon.getImage();
            setUpHeightWidth(image);
        } else {
            System.out.println("right key pressed but no comic selected");
        }
    }

    private void pressLeft() {
        if (comic != null) {
            if (currentPage == 0) {
                currentPage = 0;
            } else {
                currentPage--;
                comicPanel = comic.getImagePanel(currentPage);
                updatePanel(comicPanel);
                JLabel temp = (JLabel)comicPanel.getComponent(0);
                ImageIcon imageIcon = (ImageIcon)temp.getIcon();
                Image image = imageIcon.getImage();
                setUpHeightWidth(image);
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
                String extension = filename.substring(filename.lastIndexOf(".") + 1,
                                                     filename.length());
                if (!extension.equalsIgnoreCase("cbr") 
                                && !extension.equalsIgnoreCase("cbz")) {
                    JOptionPane.showMessageDialog(mainFrame,
                        "Comic file must be .CBR or .CBZ.", 
                        "File type error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                //System.out.println("isFile():: " + selectedFile.isFile());
                //System.out.println("File Absolute path:: " + selectedFile.getAbsolutePath());
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
        return selectedFile;                                                      
    }                                                                           
                                                                                
    public void display() {                                                     
        menuFile.add(menuOpen);                                                 
        menuFile.add(menuExit);
                                                                                
        mainMenuBar.add(menuFile);                                              
        mainMenuBar.add(buttonLeft);
        mainMenuBar.add(buttonRight);
        mainMenuBar.add(zoomInButton);
        mainMenuBar.add(zoomOutButton);
        
        mainFrame.setJMenuBar(mainMenuBar);
        mainFrame.pack();                                                       
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // max window size   
        mainFrame.setVisible(true);                                             
    }        
}      
