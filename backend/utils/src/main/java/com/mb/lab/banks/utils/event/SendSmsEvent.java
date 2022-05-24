package com.mb.lab.banks.utils.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mb.lab.banks.utils.event.broadcaster.Event;

@Event("SendSmsEvent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendSmsEvent {
    
    public static SendSmsEvent create(String recipient, String content) {
        SendSmsEvent event = new SendSmsEvent();
        event.setRecipient(recipient);
        event.setContent(content);
        event.setMessageId(UUID.randomUUID().toString());
        return event;
    }

    private String recipient;
    private String content;
    private String messageId;

    public SendSmsEvent() {
        
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "SendSmsEvent [recipient=" + recipient + ", content=" + content + ", messageId=" + messageId + "]";
    }

}
