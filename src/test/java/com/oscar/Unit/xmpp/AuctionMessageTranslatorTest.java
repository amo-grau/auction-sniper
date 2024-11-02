package com.oscar.Unit.xmpp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oscar.ApplicationRunner;
import com.oscar.AuctionEventListener;
import com.oscar.AuctionEventListener.PriceSource;
import com.oscar.xmpp.AuctionMessageTranslator;

@RunWith(MockitoJUnitRunner.class)
public class AuctionMessageTranslatorTest {

    @Mock
    private AuctionEventListener listener;

    private AuctionMessageTranslator translator;

    public static final Chat UNUSED_CHAT = null;

    @Before
    public void setUp() {
        translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, listener);
    }

    @Test public void 
    notifiesAuctionclosedwhenCloseMessageReceived(){
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);

        verify(listener, times(1)).auctionClosed();
    }

    @Test public void
    notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder(){
        Message message = new Message();
        message.setBody(
            "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
        
        translator.processMessage(UNUSED_CHAT, message);

        verify(listener, times(1)).currentPrice(192, 7, PriceSource.FromOtherBidder);
    }

    @Test public void
    notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper(){
        Message message = new Message();
        message.setBody(
            "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: " + ApplicationRunner.SNIPER_ID + ";");
        
        translator.processMessage(UNUSED_CHAT, message);

        verify(listener, times(1)).currentPrice(234, 5, PriceSource.FromSniper);
    }
}
