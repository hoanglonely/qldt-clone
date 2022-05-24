package com.mb.lab.banks.utils.logging;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class CensorValueIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 1L;
    
    @Override
    public Object findSerializer(Annotated a) {
        CensorValue ann = _findAnnotation(a, CensorValue.class);
        if (ann != null) {
            return CensorJsonSerializer.class;
        }
        return super.findSerializer(a);
    }
    
}