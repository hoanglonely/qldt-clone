package com.mb.lab.banks.utils.event;

import com.mb.lab.banks.utils.event.broadcaster.Event;

@Event("UserPasswordChangedEvent")
public class UserPasswordChangedEvent {

    private Long userId;

    public UserPasswordChangedEvent() {
    }

    public UserPasswordChangedEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

}
