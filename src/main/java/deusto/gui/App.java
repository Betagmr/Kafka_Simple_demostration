package deusto.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class App extends JFrame {

    private static boolean dark = true;
    DownloadPanel downloadPanel;
    SendPanel sendPanel;
    FilePathPanel filePathPanel;

    //FilePathPanel filePathPanel;
    JTextField urlTextField;
    JTabbedPane tabbedPane;
    JButton searchButton;


    public App() {
        //1.- Initial config
        setTitle("Kafka Panel");
        setSize(700, 350);
        //setMinimumSize(new Dimension());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);

        //2.- Components
        JPanel urlPanel = new JPanel(); //Panel containing URL label and JTF
        urlPanel.setLayout(new BorderLayout());
        urlPanel.setBorder(BorderFactory.createEmptyBorder(7,5,5,5));

        JLabel urlLabel = new JLabel("Ip and Port");
        urlLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        urlTextField = new JTextField();

        JPanel searchButtonPanel = new JPanel(new BorderLayout()); //Used because there's no gap between the JTF and the button
        searchButtonPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        searchButton = new JButton("Set");
        searchButtonPanel.add(searchButton);

        urlPanel.add(urlLabel, BorderLayout.LINE_START);
        urlPanel.add(urlTextField, BorderLayout.CENTER);
        urlPanel.add(searchButtonPanel, BorderLayout.LINE_END);

        addJMenu();
        initPanels();

        JPanel videoPanel = new JPanel(); //Panel containing video download and info options
        videoPanel.setLayout(new GridLayout(1,2));

        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        tabbedPane.addTab("Data Sender", sendPanel);
        tabbedPane.addTab("Data Downloader", downloadPanel);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        videoPanel.add(tabbedPane);

        add(urlPanel, BorderLayout.NORTH);
        add(videoPanel, BorderLayout.CENTER);
        add(filePathPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addJMenu() {
        JMenuBar menuBar = new JMenuBar();

        //File menu
        JMenu fileMenu = new JMenu("File");
        JCheckBoxMenuItem darkThemeMenuItem = new JCheckBoxMenuItem("Dark theme");
        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(darkThemeMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(settingsMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        darkThemeMenuItem.addActionListener(e -> {
            dark = !dark;
            refreshUI();
        });

        exitMenuItem.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        //About menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void refreshUI() {
        try {
            if (isDark()) UIManager.setLookAndFeel(new FlatDarkLaf());
            else UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);

            //if (dark) setIconImage(ImageIO.read(new File("assets/lightCloudDownloadIcon.png")));
            //else setIconImage(ImageIO.read(new File("assets/darkCloudDownloadIcon.png")));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void initPanels() { //TODO: actionListeners FROM HERE
        //Initialization
        downloadPanel = new DownloadPanel();
        sendPanel = new SendPanel();
        filePathPanel = new FilePathPanel();


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendPanel.setkManagerIp(urlTextField.getText());
                downloadPanel.setkManagerIp(urlTextField.getText());
            }
        });

        filePathPanel.downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendPanel.setPath(filePathPanel.fileDir.getPath());
                downloadPanel.setPath(filePathPanel.fileDir.getPath());
            }
        });

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("MenuItem.selectionType", "underline");
            UIManager.put("TextComponent.arc", 5);
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(App::new);
    }

    public static boolean isDark() {
        return dark;
    }

}
