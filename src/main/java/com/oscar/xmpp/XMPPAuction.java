package com.oscar.xmpp;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import com.oscar.Auction;

public class XMPPAuction implements Auction{
    public final static String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public final static String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

    private final Chat chat;

    public XMPPAuction(Chat chat){
        this.chat = chat;
    }

    @Override
    public void bid(int amount) {
        sendMessage(String.format(BID_COMMAND_FORMAT, amount));
    }
    
    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(final String message){
        try{
            chat.sendMessage(message);
        } catch (XMPPException e){
            e.printStackTrace();
        }
    }
}
