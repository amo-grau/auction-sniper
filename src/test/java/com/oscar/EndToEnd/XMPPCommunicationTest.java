package com.oscar.EndToEnd;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMPPCommunicationTest {

    private XMPPConnection senderConnection;
    private XMPPConnection receiverConnection;

    @Before
    public void setUp() throws Exception {
        // Connect and login the sender
        senderConnection = new XMPPConnection("localhost"); // or your server's IP/domain
        senderConnection.connect();
        senderConnection.login("sniper", "sniper"); // replace with actual credentials

        // Connect and login the receiver
        receiverConnection = new XMPPConnection("localhost"); // or your server's IP/domain
        receiverConnection.connect();
        receiverConnection.login("auction-item-54321", "auction"); // replace with actual credentials
    }

    @Test
    public void testMessageCommunication() throws Exception {
        // Chat manager for the receiver to listen for incoming messages
        ChatManager chatManager = receiverConnection.getChatManager();

        // Variable to store received message content
        final String[] receivedMessage = new String[1];

        // Set up the receiver to listen for incoming messages
        chatManager.addChatListener((chat, createdLocally) -> chat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                // Store the received message
                receivedMessage[0] = message.getBody();
            }
        }));

        // Sender sends a message to the receiver
        String userJid = "auction-item-54321@localhost/Auction";
        Chat chat = chatManager.createChat(userJid, new MessageListener() {
            @Override
            public void processMessage(Chat arg0, Message arg1) {
                // nothing yet
            }
        }); // Ensure correct JID
        String messageBody = "Hello from sender!";
        chat.sendMessage(messageBody);

        // Wait a short time to ensure the message is delivered
        Thread.sleep(2000);

        // Verify that the receiver got the message
        assertEquals("Receiver should get the message", messageBody, receivedMessage[0]);
    }

    @After
    public void tearDown() throws Exception {
        // Disconnect both users after the test
        if (senderConnection != null && senderConnection.isConnected()) {
            senderConnection.disconnect();
        }
        if (receiverConnection != null && receiverConnection.isConnected()) {
            receiverConnection.disconnect();
        }
    }
}
