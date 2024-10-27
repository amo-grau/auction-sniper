package com.oscar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.util.function.Consumer;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String AUCTION_PASSWORD = "auction";
    // public static final String AUCTION_ID_FORMAT = "auction-%s@%s/" + AUCTION_RESOURCE;

    private final String itemId;
    private final XMPPConnection connection;
    private final SingleMessageListener messageListener = new SingleMessageListener();
    private Chat currentChat;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), 
                        AUCTION_PASSWORD, 
                        AUCTION_RESOURCE);  
        
        connection.getChatManager().addChatListener(
            new ChatManagerListener() {
                public void chatCreated(Chat chat, boolean createdLocally){
                    currentChat = chat;
                    chat.addMessageListener(messageListener);
                }
            });
    }

    public void hasReceivedJoinRequestFromSniper(String sniperId) throws InterruptedException{
        receivesAMessageMatching(sniperId, body -> 
            assertThat(body).isEqualTo(App.JOIN_COMMAND_FORMAT));
    }

    public void announceClosed() throws XMPPException{
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void reportPrice(int price, int increment, String bidder) throws XMPPException{
        currentChat.sendMessage(
            String.format("SOLVersion: 1.1; Event: PRICE; "
                + "CurrentPrice: %d; Increment: %d; Bidder: %s;",
                price, increment, bidder));
    }

    public void hasReceivedBid(int bid, String sniperId) throws InterruptedException{
        assertThat(currentChat.getParticipant()).isEqualTo(sniperId);

        receivesAMessageMatching(sniperId, body -> 
            assertThat(body).isEqualTo(String.format(App.BID_COMMAND_FORMAT, bid)));
    }

    public void receivesAMessageMatching(String sniperId, Consumer<String> assertion) throws InterruptedException{
        messageListener.receivesAMessage(assertion);
        assertThat(currentChat.getParticipant()).isEqualTo(sniperId);
    }

    public void stop(){
        connection.disconnect();
    }

    public String getItemId(){
        return itemId;
    }
}
