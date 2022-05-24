package com.mb.lab.banks.utils.event.broadcaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;


/**
 * @author Thanh
 */
public class RedissonEventBroadcaster extends EventBroadcaster implements DisposableBean {
    
    private static final String DEFAULT_PREFIX = "event-broadcast-";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private RedissonClient redissonClient;
    
    private ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    private QueueEventProcessor queueEventProcessor = new QueueEventProcessor();
    
    private RTopic receiveTopic;
    private RBlockingQueue<String> receiveQueue;
    private Integer receiveTopicRegistrationId;
    
    private CopyOnWriteArrayList<RBlockingQueue<String>> subscribeQueues;
    
    private MessageListener<String> topicMessageListener = new MessageListener<String>() {
        
        @Override
        public void onMessage(CharSequence channel, String message) {
            taskExecutor.execute(new EventHandleTask(message, EventChannel.TOPIC));
        }
    };

    public RedissonEventBroadcaster(Environment environment, EventProperties eventProperties, EventSerializer eventSerializer, RedissonClient redissonClient) {
        super(environment, eventProperties, eventSerializer);
        this.redissonClient = redissonClient;
        this.taskExecutor.setBeanName("redissonEventExecutor");
        this.taskExecutor.setThreadGroupName("RedissonEventExecutor");
        this.taskExecutor.afterPropertiesSet();
    }

    @Override
    protected void sendEventMessage(String message) {
        // Send event to shared topic
        this.receiveTopic.publish(message);
        
        // Send event to all subscribe applications
        if (CollectionUtils.isEmpty(this.subscribeQueues)) {
            return;
        }
        
        for (RQueue<String> queue : this.subscribeQueues) {
            queue.offer(message);
        }
    }

    @Override
    protected void registerTopicAndQueue(String applicationId) {
        String topicName = DEFAULT_PREFIX + "topic";
        this.receiveTopic = this.redissonClient.getTopic(topicName);
        logger.info("Listening for events on topic '" + topicName + "'");
        
        String queueName = DEFAULT_PREFIX + "queue-" + applicationId;
        this.receiveQueue = this.redissonClient.getBlockingQueue(queueName);
        logger.info("Listening for events on queue '" + queueName + "'");
        
        // JRebel may call afterPropertiesSet multiple time
        if (this.receiveTopicRegistrationId == null) {
            this.receiveTopicRegistrationId = this.receiveTopic.addListener(String.class, topicMessageListener);
        }
        
        if (!this.queueEventProcessor.isAlive()) {
            this.queueEventProcessor.start();
        }
    }
    
    @Override
    protected void prepareForSubscribeApplications(Set<String> subscribleApplications) {
        logger.info("Pre-creating event queue for applications " + subscribleApplications);
        List<RBlockingQueue<String>> queues = new ArrayList<>(subscribleApplications.size());
        for (String applicationId : subscribleApplications) {
            String queueName = DEFAULT_PREFIX + "queue-" + applicationId;
            queues.add(this.redissonClient.getBlockingQueue(queueName));
        }
        if (this.subscribeQueues != null) {
            this.subscribeQueues.clear();
            this.subscribeQueues.addAll(queues);
        } else {
            this.subscribeQueues = new CopyOnWriteArrayList<>(queues);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.queueEventProcessor.interrupt();
        this.receiveTopic.removeListener(this.receiveTopicRegistrationId);
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
    
    private class QueueEventProcessor extends Thread {
        
        public QueueEventProcessor() {
            setName("RedissonQueueEventProcessorThread");
        }
        
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (redissonClient.isShutdown() || redissonClient.isShuttingDown()) {
                        return;
                    }
                    taskExecutor.execute(new EventHandleTask(receiveQueue.take(), EventChannel.QUEUE));
                }
            } catch (InterruptedException e) {
                logger.debug("Queue event processor thread interrupted");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Exception while waiting", e);
            }
        }
    }

}
