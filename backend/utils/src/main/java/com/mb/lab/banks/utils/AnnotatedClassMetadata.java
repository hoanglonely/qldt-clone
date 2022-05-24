package com.mb.lab.banks.utils;

import org.springframework.core.type.classreading.MetadataReader;

public class AnnotatedClassMetadata {

    private Class<?> clazz;

    private MetadataReader metadata;

    public AnnotatedClassMetadata(Class<?> clazz, MetadataReader metadata) {
        this.clazz = clazz;
        this.metadata = metadata;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public MetadataReader getMetadata() {
        return metadata;
    }

}
