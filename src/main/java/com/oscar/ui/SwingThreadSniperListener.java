package com.oscar.ui;

import javax.swing.SwingUtilities;

import com.oscar.SniperListener;
import com.oscar.SniperSnapshot;

public class SwingThreadSniperListener implements SniperListener{
    private final SnipersTableModel delegate;

    public SwingThreadSniperListener(SnipersTableModel delegate){
        this.delegate = delegate;
    }
    
    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                delegate.sniperStateChanged(sniperSnapshot);
            }
        });
    } 
}
