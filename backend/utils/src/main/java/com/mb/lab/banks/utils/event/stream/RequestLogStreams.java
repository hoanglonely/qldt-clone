package com.mb.lab.banks.utils.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface RequestLogStreams {

    interface InBound {

        String NAME = "request-log-in";

        @Input(NAME)
        SubscribableChannel channel();
    }

    interface OutBound {

        String NAME = "request-log-out";

        @Output(NAME)
        MessageChannel channel();
    }

}
