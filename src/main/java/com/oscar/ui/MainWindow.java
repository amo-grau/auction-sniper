package com.oscar.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import com.oscar.App;

public class MainWindow extends JFrame{
    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
    
    private final JLabel sniperStatus = createLabel(STATUS_JOINING);

    public MainWindow(){
        super("Auction Sniper");
        setName(App.MAIN_WINDOW_NAME);
        add(sniperStatus);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static JLabel createLabel(String initialTest){
        JLabel result = new JLabel(initialTest);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void showStatus(String status){
        sniperStatus.setText(status);
    }
}
