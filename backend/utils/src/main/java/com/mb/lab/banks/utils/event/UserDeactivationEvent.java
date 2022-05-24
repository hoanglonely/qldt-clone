package com.mb.lab.banks.utils.event;

import com.mb.lab.banks.utils.event.broadcaster.Event;

@Event("UserDeactivationEvent")
public class UserDeactivationEvent {

    private Long userId;

    public UserDeactivationEvent() {
    }

    public UserDeactivationEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

}
