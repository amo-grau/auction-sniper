package com.oscar.ui;

import javax.swing.SwingUtilities;

import com.oscar.SniperListener;

public class SniperStateDisplayer implements SniperListener{
    private final MainWindow ui;

    public SniperStateDisplayer(MainWindow ui){
        this.ui = ui;
    }
    
    @Override
    public void sniperBidding() {
        showStatus(MainWindow.STATUS_BIDDING);
    } 

    @Override
    public void sniperLost() {
        showStatus(MainWindow.STATUS_LOST);
    }

    @Override 
    public void sniperWinning(){
        showStatus(MainWindow.STATUS_WINNING);
    }

    private void showStatus(final String status){
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                ui.showStatus(status);
            }
        });
    }
}
