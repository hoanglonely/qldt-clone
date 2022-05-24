package com.mb.lab.banks.utils.logging;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mb.lab.banks.utils.common.StringUtils;

public class CensorJsonSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(getCensoredValue(value));
    }
    
    private String getCensoredValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return StringUtils.repeat("*", ((CharSequence)value).length());
        }
        return "***";
    }

}
