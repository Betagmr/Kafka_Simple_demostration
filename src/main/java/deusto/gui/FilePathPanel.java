package deusto.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FilePathPanel extends JPanel {
    File fileDir;
    JTextField pathTextField;
    JButton downloadButton;

    public FilePathPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //Components
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathTextField = new JTextField();
        pathPanel.add(pathTextField);
        pathPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton fileChooserButton = new JButton();
        try {
            Image img = ImageIO.read(new File("assets/folderIconGray.png"));
            fileChooserButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            fileChooserButton.setText("...");
        }
        downloadButton = new JButton("SetFolder");

        buttonPanel.add(fileChooserButton);
        buttonPanel.add(downloadButton);

        //Listeners
        fileChooserButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setDialogTitle("Choose path...");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileDir = fileChooser.getSelectedFile();
                pathTextField.setText(fileDir.getAbsolutePath().replace('\\', '/'));
            }
            else {
                fileDir = null;
            }
        });

        add(pathPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.LINE_END);
    }
}
