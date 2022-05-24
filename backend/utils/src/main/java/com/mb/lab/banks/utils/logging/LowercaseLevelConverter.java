package com.mb.lab.banks.utils.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LowercaseLevelConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return event.getLevel().toString().toLowerCase();
    }

}
