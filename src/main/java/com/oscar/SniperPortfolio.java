package com.oscar;

import java.util.ArrayList;
import com.oscar.util.Announcer;

public class SniperPortfolio implements SniperCollector {
    private final ArrayList<AuctionSniper> snipers = new ArrayList<AuctionSniper>();
    private final Announcer<PortfolioListener> listeners = Announcer.to(PortfolioListener.class);

    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        listeners.announce().sniperAdded(sniper);
    }

    public void addPortFolioListener(PortfolioListener listener){
        listeners.addListener(listener);
    }
}