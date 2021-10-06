package deusto.gui;

import deusto.kafka.KafkaManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DownloadPanel extends JPanel {

    KafkaManager kManager;
    private JComboBox<String> jComboBox;
    private ArrayList<String> topicList;
    private String path;

    public DownloadPanel() {

        setLayout(new GridLayout(2,1));

        JPanel topicPanle = new JPanel(new GridLayout(1,2));
        JButton reloadButton = new JButton("Reload Topics");
        JButton sendFiles = new JButton("Donwload Files");

        kManager = new KafkaManager();
        jComboBox = new JComboBox<>();


        loadTopics();

        topicPanle.add(jComboBox);
        topicPanle.add(reloadButton);
        add(topicPanle);
        add(sendFiles);

        sendFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kManager.downloadData(path, (String) jComboBox.getSelectedItem());
            }
        });

        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTopics();
            }
        });
    }

    public void loadTopics(){

        jComboBox.removeAllItems();
        topicList = kManager.listTopics();

        for (String t : topicList) {
            jComboBox.addItem(t);
        }
    }

    public void setkManagerIp(String ip){
        kManager.setIp(ip);
    }

    public void setPath(String path1){
        path = path1;
    }
}
