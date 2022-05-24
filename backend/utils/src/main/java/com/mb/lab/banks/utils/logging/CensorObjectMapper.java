package com.mb.lab.banks.utils.logging;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CensorObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;
    
    public CensorObjectMapper() {
        setAnnotationIntrospector(new CensorValueIntrospector());
    }

}
