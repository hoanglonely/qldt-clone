package com.mb.lab.banks.utils.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface SendSmsStreams {

    interface InBound {

        String NAME = "send-sms-in";

        @Input(NAME)
        SubscribableChannel channel();
    }

    interface OutBound {

        String NAME = "send-sms-out";

        @Output(NAME)
        MessageChannel channel();
    }

}
