package com.oscar;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

    @Override
    public void processMessage(Chat chat, Message message) {
        messages.add(message);
    }

    public void receivesAMessage(Consumer<String> assertion) throws InterruptedException{
        final Message message = messages.poll(5, TimeUnit.SECONDS);
        assertThat(message).as("Message").isNotNull();
        assertion.accept(message.getBody()); // TODO: see how the error message is
    }
}
