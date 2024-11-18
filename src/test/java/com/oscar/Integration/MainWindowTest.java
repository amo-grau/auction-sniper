package com.oscar.Integration;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import com.oscar.AuctionSniperDriver;
import com.oscar.SniperPortfolio;
import com.oscar.ui.MainWindow;
import com.oscar.UserRequestListener;

public class MainWindowTest {
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test public void 
    makesUserRequestWhenJoinButtonClicked(){
        final ValueMatcherProbe<String> buttonProbe = 
            new ValueMatcherProbe<String>(equalTo("item-id"), "join request");

        mainWindow.addUserRequestListener(
            new UserRequestListener() {
                public void joinAuction(String itemId){
                    buttonProbe.setReceivedValue(itemId);
                }
            });

        driver.startBiddingFor("item-id", Integer.MAX_VALUE);
        driver.check(buttonProbe);
    }
}
