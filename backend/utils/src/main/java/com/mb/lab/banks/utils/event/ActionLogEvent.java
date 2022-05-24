package com.mb.lab.banks.utils.event;

import com.mb.lab.banks.utils.event.broadcaster.Event;

@Event("ActionLogEvent")
public class ActionLogEvent {

    private Long userId;

    public ActionLogEvent() {
    }

    public ActionLogEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

}
