package com.mb.lab.banks.utils.event.broadcaster;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Thanh
 */
public class EventSerializer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    private EventNameMapper eventNameMapper;

    public EventSerializer(EventNameMapper eventNameMapper) {
        this.eventNameMapper = eventNameMapper;
    }

    public String serializeEvent(EventData eventData) {
        Object event = eventData.getEvent();
        String eventName = this.eventNameMapper.getEventName(event.getClass());
        if (StringUtils.isEmpty(eventName)) {
            logger.info("No event name for '" + event.getClass().getName() + "'. Consider annotate with @Event.");
            return null;
        }

        try {
            String eventJson = this.objectMapper.writeValueAsString(event);
            return eventName + "|" + eventData.getSource() + "|" + eventJson;
        } catch (JsonProcessingException e) {
            logger.error("Can not serialize event to JSON string", e);
        }

        return null;
    }

    public EventData deserializeEvent(String message) {
        if (StringUtils.isEmpty(message)) {
            return null;
        }

        int firstDelim = message.indexOf('|');
        int secondDelim = message.lastIndexOf('|');

        if (firstDelim == -1 || firstDelim == secondDelim) {
            throw new IllegalArgumentException("Invalid event message format");
        }

        String eventName = message.substring(0, firstDelim);
        String source = message.substring(firstDelim + 1, secondDelim);
        String eventJson = message.substring(secondDelim + 1);

        Class<?> eventClass = this.eventNameMapper.getEventClass(eventName);
        if (eventClass == null) {
            logger.info("No event class for '" + eventName + "'. Consider annotate with @Event.");
            return null;
        }

        try {
            Object event = this.objectMapper.readValue(eventJson, eventClass);
            return new EventData(source, event);
        } catch (IOException e) {
            logger.error("Cannot deserialize event from JSON string", e);
        }

        return null;
    }

}
