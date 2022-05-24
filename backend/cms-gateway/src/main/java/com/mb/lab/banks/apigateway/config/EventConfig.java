package com.mb.lab.banks.apigateway.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mb.lab.banks.utils.event.stream.EventStreamsHelper;
import com.mb.lab.banks.utils.event.stream.OpenAPIRequestLogStreams;

@Configuration
@EnableBinding({
    OpenAPIRequestLogStreams.OutBound.class,
})
public class EventConfig {
    
    @Bean
    public EventStreamsHelper eventStreamsHelper() {
        return new EventStreamsHelper();
    }
}
