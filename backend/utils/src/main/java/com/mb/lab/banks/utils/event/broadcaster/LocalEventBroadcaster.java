package com.mb.lab.banks.utils.event.broadcaster;

import java.util.Set;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Thanh
 */
public class LocalEventBroadcaster extends EventBroadcaster implements DisposableBean {
    
    private ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    
    public LocalEventBroadcaster(Environment environment, EventProperties eventProperties, EventSerializer eventSerializer) {
        super(environment, eventProperties, eventSerializer);
        this.taskExecutor.setBeanName("localEventExecutor");
        this.taskExecutor.setThreadGroupName("LocalEventExecutor");
        this.taskExecutor.afterPropertiesSet();
    }

    @Override
    protected void sendEventMessage(String message) {
    	taskExecutor.execute(new EventHandleTask(message, EventChannel.TOPIC));
    	taskExecutor.execute(new EventHandleTask(message, EventChannel.QUEUE));
    }

    @Override
    protected void registerTopicAndQueue(String applicationId) {
    }
    
    @Override
    protected void prepareForSubscribeApplications(Set<String> subscribleApplications) {
    }

    @Override
    public void destroy() throws Exception {
        this.taskExecutor.shutdown();
    }
    
    private class EventHandleTask implements Runnable {
        
        private String message;
        private EventChannel channel;
        
        private EventHandleTask(String message, EventChannel channel) {
            this.message = message;
            this.channel = channel;
        }

        @Override
        public void run() {
            handleEventMessage(this.message, this.channel);
        }
    }

}
