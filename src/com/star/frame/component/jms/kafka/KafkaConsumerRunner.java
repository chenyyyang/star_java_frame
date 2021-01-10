package com.star.frame.component.jms.kafka;

import com.dianping.cat.Cat;
import com.star.frame.core.util.ClassUtilsEx;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaConsumerRunner implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerRunner.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private KafkaConsumer consumer;

    public KafkaConsumerRunner(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    public void preConsume(){}

    public void afterConsume(){}

    public void consume(ConsumerRecords<String, String> records) throws Exception{
        for (ConsumerRecord<String, String> record : records) {
            String topicName = record.topic();
            Method method = KafkaConsumerStarter.consumerMethodMap.get(topicName);
            if (method != null) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("record", record);
                /*内层也需要try catch，如果消费异常，不提交消费位点，下次拉取依然无法重复消费，且会跳过这批的所有消息
               如需 捕获异常并且重复消费 ，
               int tryTime = 0;
                while (tryTime < MAX_TRY_TIMES){
                    try {
                        ClassUtilsEx.invokeMethod(method.getDeclaringClass().getSimpleName(), method.getName(), paramMap);
                        break;
                    } catch (Exception e) {
                        tryTime++;
                        logger.error("[Kafka]处理消息发生异常:" + e.getMessage(), e);
                    }
                }*/
                try {
                    ClassUtilsEx.invokeMethod(method.getDeclaringClass().getSimpleName(), method.getName(), paramMap);
                } catch (Exception e) {
                    logger.error("[Kafka]处理消息发生异常:" + e.getMessage(), e);
                }

            } else {
                logger.error("[Kafka]处理消息发生异常: topic未找到相应的处理方法" + ",topic=" + topicName);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (!closed.get()) {
                // 外层try catch保证循环不会异常退出
                try {
                    //必须在下次 poll 之前消费完这些数据, 且总耗时不得超过 SESSION_TIMEOUT_MS_CONFIG 的值
                    //With timeout set to MAX_VALUE, consumer will wait forever to get the data
                    ConsumerRecords<String, String> records = consumer.poll(1000);

                    preConsume();

                    // 内层也需要try catch，如果消费异常，不提交消费位点，下次拉取依然无法重复消费
                    //如需重复消费 可使用 guava-retry  spring-retry.
                    consume(records);

                    afterConsume();

                    consumer.commitAsync();
                } catch (InvocationTargetException e) {
                    Throwable targetException = e.getTargetException();
                    Cat.logError(targetException);
                    logger.error("[Kafka]处理消息发生异常:" + targetException.getMessage(), targetException);
                } catch (Exception e) {
                    logger.error("[Kafka]处理消息发生异常:" + e.getMessage(), e);
                }

            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) {
                throw e;
            }
        } finally {
            try {
                consumer.commitSync(); // 最后一次提交使用同步阻塞式提交
            } finally {
                consumer.close();
            }
        }
    }


    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }

}