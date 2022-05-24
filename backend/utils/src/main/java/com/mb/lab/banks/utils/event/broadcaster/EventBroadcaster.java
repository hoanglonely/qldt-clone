package com.mb.lab.banks.utils.event.broadcaster;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.eventbus.EventBus;

/**
 * @author Thanh
 */
public abstract class EventBroadcaster implements InitializingBean {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Environment environment;
    private EventProperties eventProperties;
    private String broadcasterId;
    
    private EventSerializer eventSerializer;
    
    private EventBus topicEventBus;
    private EventBus queueEventBus;

    public EventBroadcaster(Environment environment, EventProperties eventProperties, EventSerializer eventSerializer) {
        this.environment = environment;
        this.eventProperties = eventProperties;
        this.eventSerializer = eventSerializer;
        
        this.broadcasterId = UUID.randomUUID().toString();
        logger.info("Event broadcaster id for this server instance is " + this.broadcasterId);
        
        this.topicEventBus = new EventBus("TopicEventBus");
        this.queueEventBus = new EventBus("QueueEventBus");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String applicationId = environment.getProperty("spring.application.name");
        Assert.notNull(applicationId, "'spring.application.name' must be defined");
        this.registerTopicAndQueue(applicationId);
        
        if (!CollectionUtils.isEmpty(eventProperties.getApplications())) {
            Set<String> subscribleApplications = new HashSet<>(eventProperties.getApplications());
            this.prepareForSubscribeApplications(subscribleApplications);
        }
    }
    
    public void post(Object event) {
        String message = this.eventSerializer.serializeEvent(new EventData(this.broadcasterId, event));
        if (StringUtils.isEmpty(message)) {
            return;
        }

        this.sendEventMessage(message);
    }
    
    public void register(Object listener, EventChannel channel) {
        if (channel == EventChannel.QUEUE) {
            this.queueEventBus.register(listener);
        } else {
            this.topicEventBus.register(listener);
        }
    }
    
    public void unregister(Object listener, EventChannel channel) {
        if (channel == EventChannel.QUEUE) {
            this.queueEventBus.unregister(listener);
        } else {
            this.topicEventBus.unregister(listener);
        }
    }
    
    protected abstract void registerTopicAndQueue(String applicationId);
    
    protected abstract void prepareForSubscribeApplications(Set<String> subscribleApplications);
    
    protected abstract void sendEventMessage(String message);

    protected void handleEventMessage(String message, EventChannel channel) {
        EventData eventData = this.eventSerializer.deserializeEvent(message);
        if (eventData == null) {
            return;
        }
        
        if (logger.isTraceEnabled()) {
            logger.trace("Received " + channel + " message " + eventData.getEvent() + " from " + eventData.getSource());
        }

        if (channel == EventChannel.QUEUE) {
            queueEventBus.post(eventData.getEvent());
        } else {
            topicEventBus.post(eventData.getEvent());
        }
    }

}
