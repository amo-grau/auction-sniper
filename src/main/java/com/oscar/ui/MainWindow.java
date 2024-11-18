package com.oscar.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import com.oscar.App;
import com.oscar.Item;
import com.oscar.SniperPortfolio;
import com.oscar.UserRequestListener;
import com.oscar.util.Announcer;

public class MainWindow extends JFrame{
    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String SNIPERS_TABLE_NAME = "Auction Sniper Table";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String JOIN_BUTTON_NAME = "join button";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    
    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);
    private final SniperPortfolio portfolio;

    final JTextField itemIdField = new JTextField();
    final JFormattedTextField stopPriceField;

    public MainWindow(SniperPortfolio portfolio){
        super(APPLICATION_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        NumberFormatter formatter = new NumberFormatter();
        formatter.setValueClass(Integer.class);
        
        stopPriceField = new JFormattedTextField(formatter);
        stopPriceField.setColumns(15);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);

        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);

        setName(App.MAIN_WINDOW_NAME);
        this.portfolio = portfolio;
        fillContentPane(makeSnipersTable(), makeControls());
        pack();

    }

    private JPanel makeControls(){
        JPanel controls = new JPanel(new FlowLayout());
        controls.add(itemIdField);
        controls.add(stopPriceField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(new Item(itemId(), stopPrice()));
            }
        });
        controls.add(joinAuctionButton);

        return controls;
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortFolioListener(model);
        final JTable snipersTable = new JTable(model);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void addUserRequestListener(UserRequestListener sniperLauncher) {
        userRequests.addListener(sniperLauncher);
    }

    public String itemId(){
        return itemIdField.getText();
    }

    public Integer stopPrice(){
        return ((Number)stopPriceField.getValue()).intValue();
    }
}
