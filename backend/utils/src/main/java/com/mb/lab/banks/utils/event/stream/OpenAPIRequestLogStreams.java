package com.mb.lab.banks.utils.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OpenAPIRequestLogStreams {

    interface InBound {

        String NAME = "open-api-request-log-in";

        @Input(NAME)
        SubscribableChannel channel();
    }

    interface OutBound {

        String NAME = "open-api-request-log-out";

        @Output(NAME)
        MessageChannel channel();
    }
    
}
