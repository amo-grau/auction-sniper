package com.oscar;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.SwingUtilities;

import com.oscar.ui.MainWindow;
import com.oscar.xmpp.XMPPAuctionHouse;

public class App {
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID = 3;

    public final static String MAIN_WINDOW_NAME = "Auction Sniper Main";    

    private MainWindow ui;
    private final SniperPortfolio portfolio = new SniperPortfolio();

    @SuppressWarnings("unused") private Collection<Auction> notToBeGCd = new ArrayList<>();

    public App() throws Exception{
        startUserInterface();
    }

    public static void main( String[] args ) throws Exception
    {
        App main = new App();
        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(auctionHouse);
        main.addUserRequestListenerFor(auctionHouse);
    }

    private void addUserRequestListenerFor(final AuctionHouse auctionHouse) {
        SniperLauncher sniperLauncher = new SniperLauncher(auctionHouse, portfolio);
        ui.addUserRequestListener(sniperLauncher);
    }
        
    private void disconnectWhenUICloses(AuctionHouse auctionHouse) {
        ui.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                auctionHouse.disconnect();
            }
        });
    }

    public void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run(){ ui = new MainWindow(portfolio);
            }
        });
    }
}
