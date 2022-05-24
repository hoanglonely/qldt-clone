package com.mb.lab.banks.utils.event;

import java.util.List;

import com.mb.lab.banks.utils.event.broadcaster.Event;

@Event("SendEmailEvent")
public class SendEmailEvent {

    private List<String> recipients;
    private String subject;
    private String content;

    public SendEmailEvent() {
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
