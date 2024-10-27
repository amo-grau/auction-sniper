package com.oscar;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import com.oscar.ui.MainWindow;
import com.oscar.xmpp.AuctionMessageTranslator;

public class App implements AuctionEventListener
{
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE =  "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = 
        ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    public final static String MAIN_WINDOW_NAME = "Auction Sniper Main";    

    public final static String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public final static String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

    private MainWindow ui;
    @SuppressWarnings("unused") private Chat notToBeGCd;

    public App() throws Exception{
        startUserInterface();
    }

    public static void main( String[] args ) throws Exception
    {
        App main = new App();
        main.joinAuction(
            connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]), 
            args[ARG_ITEM_ID]);
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException{
        final Chat chat = connection.getChatManager().createChat(
            auctionId(itemId, connection), 
            new AuctionMessageTranslator(this));

        this.notToBeGCd = chat;
        chat.sendMessage(JOIN_COMMAND_FORMAT);
    }
    
    private static String auctionId(String itemId, XMPPConnection connection) {
        //return "auction-item-54321@localhost/Auction";
        return String.format(AUCTION_ID_FORMAT, "item-54321", "localhost");
    }

    public static XMPPConnection connection(String hostname, String username, String password) throws XMPPException{
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        return connection;
    }

    public void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run(){
                ui = new MainWindow();
            }
        });
    }

    @Override
    public void auctionClosed() {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                ui.showStatus(MainWindow.STATUS_LOST);
            }
        });
    }

    @Override
    public void currentPrice(int price, int increment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'currentPrice'");
    }
}
