package com.oscar;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;

public class AuctionSniperDriver extends JFrameDriver{
    @SuppressWarnings("unchecked")
    public AuctionSniperDriver(int timeoutMillis){
        super(new GesturePerformer(),
            JFrameDriver.topLevelFrame(
                named(App.MAIN_WINDOW_NAME),
                showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));
    }

    @SuppressWarnings("unchecked")
    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText){
        JTableDriver table = new JTableDriver(this);

        table.hasRow(matching(
            withLabelText(equalTo(itemId)),
            withLabelText(equalTo(String.valueOf(lastPrice))),
            withLabelText(equalTo(String.valueOf(lastBid))),
            withLabelText(equalTo(statusText))
        ));
        new JTableDriver(this).hasCell(withLabelText(equalTo(statusText)));  
    }

    @SuppressWarnings("unchecked")
    public void hasColumnTitles(){
        JTableHeaderDriver headedrs = new JTableHeaderDriver(this, JTableHeader.class);
        headedrs.hasHeaders(matching(withLabelText("Item"), withLabelText("Last Price"),
                                    withLabelText("Last Bid"), withLabelText("State")));
    }
}
