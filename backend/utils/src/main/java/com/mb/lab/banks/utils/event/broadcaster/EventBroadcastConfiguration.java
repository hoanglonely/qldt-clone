package com.mb.lab.banks.utils.event.broadcaster;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;

import com.hazelcast.core.HazelcastInstance;
import com.mb.lab.banks.utils.AnnotatedClassMetadata;
import com.mb.lab.banks.utils.AnnotatedClassScanner;

/**
 * @author Thanh
 */
@Configuration
@EnableConfigurationProperties(EventProperties.class)
@ConditionalOnProperty(name = "event.enabled", matchIfMissing = false)
public class EventBroadcastConfiguration implements BeanClassLoaderAware, ImportAware {
	
	@Configuration
    @ConditionalOnProperty(prefix = "event", name = "method", havingValue = "local", matchIfMissing = true)
    public static class LocalBroadcasterConfig {

        @Bean
        public EventBroadcaster eventBroadcaster(Environment environment, EventProperties eventProperties, EventSerializer eventSerializer) {
            return new LocalEventBroadcaster(environment, eventProperties, eventSerializer);
        }

    }
    
    @Configuration
    @ConditionalOnProperty(prefix = "event", name = "method", havingValue = "hazelcast", matchIfMissing = false)
    public static class HazelcastBroadcasterConfig {

        @Bean
        public EventBroadcaster eventBroadcaster(Environment environment, EventProperties eventProperties, EventSerializer eventSerializer, HazelcastInstance hazelcastInstance) {
            return new HazelcastEventBroadcaster(environment, eventProperties, eventSerializer, hazelcastInstance);
        }

    }
    
    @Configuration
    @ConditionalOnProperty(prefix = "event", name = "method", havingValue = "redisson", matchIfMissing = false)
    public static class RedissonBroadcasterConfig {

        @Bean
        public EventBroadcaster eventBroadcaster(Environment environment, EventProperties eventProperties, EventSerializer eventSerializer, RedissonClient redissonClient) {
            return new RedissonEventBroadcaster(environment, eventProperties, eventSerializer, redissonClient);
        }

    }

    private ClassLoader classLoader;

    private String[] basePackages;

    private Class<?>[] basePackageClasses;

    private Class<Event> eventAnnotationType = Event.class;

    @SuppressWarnings("unchecked")
    @Bean
    public EventNameMapper eventNameMapper() {
        AnnotatedClassScanner scanner;
        if (basePackageClasses != null) {
            scanner = new AnnotatedClassScanner(this.classLoader, basePackageClasses, new Class[] { eventAnnotationType });
        } else {
            scanner = new AnnotatedClassScanner(this.classLoader, basePackages, new Class[] { eventAnnotationType });
        }

        Set<AnnotatedClassMetadata> classMetadatas = scanner.scanPackages();

        Map<String, Class<?>> mapping = new HashMap<>(classMetadatas.size());
        for (AnnotatedClassMetadata classMetadata : classMetadatas) {
            mapping.put(determineEventNameFromAnnotation(classMetadata.getMetadata()), classMetadata.getClazz());
        }

        return new EventNameMapper(mapping);
    }

    @Bean
    public EventSerializer eventSerializer(EventNameMapper eventNameMapper) {
        return new EventSerializer(eventNameMapper);
    }
    
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> enableAttrMap = importMetadata.getAnnotationAttributes(EnableEventBroadcasting.class.getName());
        AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);

        this.basePackages = enableAttrs.getStringArray("basePackages");
        this.basePackageClasses = enableAttrs.getClassArray("basePackageClasses");

        if (isEmpty(basePackages) && isEmpty(basePackageClasses)) {
            throw new IllegalArgumentException("Either 'basePackages' or 'basePackageClasses' must be declare");
        }
        if (!isEmpty(basePackages) && !isEmpty(basePackageClasses)) {
            throw new IllegalArgumentException("Only 'basePackages' or 'basePackageClasses' can be declare at same time");
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private String determineEventNameFromAnnotation(MetadataReader classMetadata) {
        Map<String, Object> attributes = classMetadata.getAnnotationMetadata().getAnnotationAttributes(this.eventAnnotationType.getName());
        if (attributes != null) {
            return (String) attributes.get("value");
        }
        return null;
    }

    private boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }
}
