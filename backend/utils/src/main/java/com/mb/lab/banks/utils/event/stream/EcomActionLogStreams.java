package com.mb.lab.banks.utils.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface EcomActionLogStreams {

    interface InBound {

        String NAME = "ecom-action-log-in";

        @Input(NAME)
        SubscribableChannel channel();
    }

    interface OutBound {

        String NAME = "ecom-action-log-out";

        @Output(NAME)
        MessageChannel channel();
    }
}
