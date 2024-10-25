package com.oscar;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.oscar.ui.MainWindow;

import static org.hamcrest.Matchers.equalTo; // Import the equalTo matcher

public class AuctionSniperDriver extends JFrameDriver{
    public AuctionSniperDriver(int timeoutMillis){
        super(new GesturePerformer(),
            JFrameDriver.topLevelFrame(
                named(App.MAIN_WINDOW_NAME),
                showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));
    }

    public void showsSniperStatus(String statusText){
        new JLabelDriver(
            this, named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(statusText));  
    }
}
