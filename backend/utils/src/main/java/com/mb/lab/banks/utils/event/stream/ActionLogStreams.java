package com.mb.lab.banks.utils.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ActionLogStreams {

    interface InBound {

        String NAME = "action-log-in";

        @Input(NAME)
        SubscribableChannel channel();
    }

    interface OutBound {

        String NAME = "action-log-out";

        @Output(NAME)
        MessageChannel channel();
    }

}
