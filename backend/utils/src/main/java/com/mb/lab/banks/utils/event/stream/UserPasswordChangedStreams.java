package com.mb.lab.banks.utils.event.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface UserPasswordChangedStreams {

    interface InBound {

        String NAME = "user-password-changed-in";

        @Input(NAME)
        SubscribableChannel channel();
    }

    interface OutBound {

        String NAME = "user-password-changed-out";

        @Output(NAME)
        MessageChannel channel();
    }

}
