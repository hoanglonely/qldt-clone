package com.mb.lab.banks.utils.event.broadcaster;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author Thanh
 */
public class EventNameMapper {

    private BiMap<String, Class<?>> nameToClassMap;

    public EventNameMapper(Map<String, Class<?>> mapping) {
        this.nameToClassMap = HashBiMap.create(mapping);
    }

    public Class<?> getEventClass(String eventName) {
        return nameToClassMap.get(eventName);
    }

    public String getEventName(Class<?> eventClass) {
        return nameToClassMap.inverse().get(eventClass);
    }

}
