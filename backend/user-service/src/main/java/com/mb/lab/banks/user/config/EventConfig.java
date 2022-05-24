package com.mb.lab.banks.user.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mb.lab.banks.utils.event.stream.ActionLogStreams;
import com.mb.lab.banks.utils.event.stream.BusinessLogStreams;
import com.mb.lab.banks.utils.event.stream.ErrorLogStreams;
import com.mb.lab.banks.utils.event.stream.EventStreamsHelper;
import com.mb.lab.banks.utils.event.stream.SendEmailStreams;
import com.mb.lab.banks.utils.event.stream.SendSmsStreams;
import com.mb.lab.banks.utils.event.stream.UserDeactivationStreams;
import com.mb.lab.banks.utils.event.stream.UserPasswordChangedStreams;

@Configuration
@EnableBinding({
    ActionLogStreams.OutBound.class,
    BusinessLogStreams.OutBound.class,
    ErrorLogStreams.OutBound.class,
    UserDeactivationStreams.OutBound.class,
    UserPasswordChangedStreams.OutBound.class,
    SendEmailStreams.InBound.class,
    SendEmailStreams.OutBound.class,
    SendSmsStreams.InBound.class,
    SendSmsStreams.OutBound.class,
    ActionLogStreams.OutBound.class
})
public class EventConfig {

    @Bean
    public EventStreamsHelper eventStreamsHelper() {
        return new EventStreamsHelper();
    }

}
