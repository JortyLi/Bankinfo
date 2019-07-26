package com.example.bankinfo.common.jms;

import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;

public class ClsMakeSentMQData implements MessageListener{
    private static Logger logger = Logger.getLogger(ClsMakeSentMQData.class);

    private String m_ACMQUrl;
    private String m_queueName;
    private String m_user;
    private String m_psw;

    private ActiveMQConnectionFactory m_connectionFactory = null;
    private Connection m_connection = null;
    private Session m_session = null;
    private Destination m_destination = null;
    private MessageProducer m_producer = null;
    private boolean m_bConnect = false;

    public ClsMakeSentMQData(String acmqParam) {

        try {
            JSONObject acmpJson = JSONObject.parseObject(acmqParam);

            m_ACMQUrl = acmpJson.getString("BrokerUrl");
            m_queueName = acmpJson.getString("QueueName");
            m_user = acmpJson.getString("User");
            m_psw = acmpJson.getString("Psw");
            initProducer();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    private void initProducer() {
        // 建立acmq连接
        try {
            if (m_bConnect) {
                return;
            }

            logger.info("连接mq url = " + m_ACMQUrl);
            // 创建connectionFaction
            m_connectionFactory = new ActiveMQConnectionFactory(m_user, m_psw, m_ACMQUrl);
            // 创建connection
            m_connection = (Connection) m_connectionFactory.createConnection();
            m_connection.start();
            // 创建session，设置消息确认机制
            m_session = m_connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建destination
            m_destination = m_session.createQueue(m_queueName);
            // 创建producer
            m_producer = m_session.createProducer(m_destination);
            // 设置JMS的持久性
            m_producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            m_bConnect = true;
        } catch (JMSException e) {
            logger.error(e);
            e.printStackTrace(System.out);
        }
    }

    private void unInitProducer() {
        try {
            m_bConnect = false;
            System.out.println("Closing send");

            if (m_producer != null)
                m_producer.close();

            if (m_session != null)
                m_session.close();

            if (m_connection != null)
                m_connection.close();

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace(System.out);
        }
    }

    public void pushMsgToACMQ(String msg) {
        try {
            // JMS消息体
            TextMessage message = m_session.createTextMessage(msg);

            // 发送消息message
            m_producer.send(message);
            // 关闭资源
            message.clearProperties();
        } catch (JMSException e) {
            unInitProducer();
            initProducer();
            logger.error(e);
            e.printStackTrace(System.out);
        } catch (Exception e) {
            unInitProducer();
            initProducer();
            logger.error(e);
            e.printStackTrace(System.out);
        }
    }
    @Override
    public void onMessage(Message message) {

    }
    public void close(){
        unInitProducer();
    }
}
