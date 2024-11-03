package com.oscar;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.*;

import com.oscar.ui.MainWindow;
import com.oscar.ui.SnipersTableModel;
import com.oscar.ui.SwingThreadSniperListener;
import com.oscar.xmpp.AuctionMessageTranslator;
import com.oscar.xmpp.XMPPAuction;

public class App {
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE =  "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = 
        ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    public final static String MAIN_WINDOW_NAME = "Auction Sniper Main";    

    private MainWindow ui;
    private SnipersTableModel snipers = new SnipersTableModel();
    @SuppressWarnings("unused") private Collection<Chat> notToBeGCd = new ArrayList<>();

    public App() throws Exception{
        startUserInterface();
    }

    public static void main( String[] args ) throws Exception
    {
        App main = new App();
        XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);

        for (int i = 3; i < args.length; i++){
            main.joinAuction(connection, args[i]);
        }
    }

    private void disconnectWhenUICloses(XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws Exception{
        final Chat chat = connection.getChatManager().createChat(
            auctionId(itemId, connection), null);
        notToBeGCd.add(chat);

        Auction auction = new XMPPAuction(chat); 
        
        safelyAddItemToModel(itemId);

        chat.addMessageListener(
            new AuctionMessageTranslator(
                connection.getUser(),
                new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers))));
        
        auction.join();
    }
    
    private void safelyAddItemToModel(String itemId) throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                snipers.addSniper(SniperSnapshot.joining(itemId));
            }
        });
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        //return "auction-item-54321@localhost/Auction";
        return String.format(AUCTION_ID_FORMAT, itemId, "localhost");
    }

    public static XMPPConnection connection(String hostname, String username, String password) throws XMPPException{
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        return connection;
    }

    public void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run(){ ui = new MainWindow(snipers);
            }
        });
    }
}
