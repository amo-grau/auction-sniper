package com.oscar.ui;

import com.oscar.SniperSnapshot;

public enum Column {
    ITEM_IDENTIFIER("Item"){
        @Override public Object valueIn(SniperSnapshot snapshot){
            return snapshot.itemId();
        }
    },
    LAST_PRICE("Last Price"){
        @Override public Object valueIn(SniperSnapshot snapshot){
            return snapshot.lastPrice();
        }
    },
    LAST_BID("Last Bid"){
        @Override public Object valueIn(SniperSnapshot snapshot){
            return snapshot.lastBid();
        }
    },
    SNIPER_STATE("State"){
        @Override public Object valueIn(SniperSnapshot snapshot){
            return SnipersTableModel.textFor(snapshot.state());
        }
    };

    public final String name;

    private Column(String name){
        this.name = name;
    }
    
    public static Column at(int offset) { return values() [offset]; }
    abstract public Object valueIn(SniperSnapshot snapshot);
}
