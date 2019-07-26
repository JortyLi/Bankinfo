package com.example.bankinfo.common.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveMQUtil {
    static ConnectionFactory connectionFactory;
    static Connection connection = null;
    static Session session;
    static Map<String, MessageProducer> sendQueues = new ConcurrentHashMap<String, MessageProducer>();

    static Map<String, MessageConsumer> getQueues = new ConcurrentHashMap<String, MessageConsumer>();


    static {
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://14.29.47.99:61616?wireFormat.maxInactivityDuration=0");
        try
        {
            connection = connectionFactory.createConnection();

            connection.start();

            session = connection.createSession(Boolean.FALSE.booleanValue(),
                    1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MessageProducer getMessageProducer(String name) {
        if (sendQueues.containsKey(name))
            return ((MessageProducer)sendQueues.get(name));
        try
        {
            Destination destination = session.createQueue(name);
            MessageProducer producer = session.createProducer(destination);
            sendQueues.put(name, producer);
            return producer;
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return ((MessageProducer)sendQueues.get(name));
    }

    static MessageConsumer getMessageConsumer(String name) {
        if (getQueues.containsKey(name))
            return ((MessageConsumer)getQueues.get(name));
        try
        {
            Destination destination = session.createQueue(name);
            MessageConsumer consumer = session.createConsumer(destination);
            getQueues.put(name, consumer);
            return consumer;
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return ((MessageConsumer)getQueues.get(name));
    }

    public static void sendMessage(String queue, String text) {
        try {
            TextMessage message = session.createTextMessage(text);
            getMessageProducer(queue).send(message);
            // log.info("sendMessage " + queue + "\t\t" + text);
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }



    public static String getMessage(String queue)
    {
        try {
            TextMessage message = (TextMessage)getMessageConsumer(queue).receive(10000L);
            if (message != null)
                return message.getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close() {
        try {
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        ActiveMQUtil.sendMessage("nimeimei","hahahahahhahahahahaha");
        String str = ActiveMQUtil.getMessage("nimeimei");
        System.out.println(str);
        ActiveMQUtil.close();
    }
}
