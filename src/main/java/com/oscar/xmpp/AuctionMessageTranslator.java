package com.oscar.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.oscar.AuctionEventListener;

public class AuctionMessageTranslator implements MessageListener{
    private final AuctionEventListener listener; 
    private final String sniperId;

    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener)
    {
        this.listener = listener;
        this.sniperId = sniperId;
    }

    @Override
    public void processMessage(Chat chat, Message messsage){
        AuctionEvent event = AuctionEvent.from(messsage.getBody());

        String type = event.type();
        if ("CLOSE".equals(type)){
            listener.auctionClosed();
        } else if ("PRICE".equals(type)){
            listener.currentPrice(
                event.currentPrice(), event.increment(), event.isFrom(sniperId));
        }
    }
}