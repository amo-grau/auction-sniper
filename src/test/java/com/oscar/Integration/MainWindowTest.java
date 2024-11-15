package com.oscar.Integration;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import com.oscar.AuctionSniperDriver;
import com.oscar.ui.MainWindow;
import com.oscar.ui.SnipersTableModel;
import com.oscar.ui.UserRequestListener;

public class MainWindowTest {
    private final SnipersTableModel tabelModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tabelModel);
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

        driver.startBiddingFor("item-id");
        driver.check(buttonProbe);
    }
}
