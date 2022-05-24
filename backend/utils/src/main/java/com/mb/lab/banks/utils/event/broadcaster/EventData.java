package com.mb.lab.banks.utils.event.broadcaster;

/**
 * @author Thanh
 */
public class EventData {

    private String source;
    private Object event;
    
    public EventData(String source, Object event) {
        this.source = source;
        this.event = event;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

}
