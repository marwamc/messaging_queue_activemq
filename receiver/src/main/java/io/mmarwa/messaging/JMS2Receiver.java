package io.mmarwa.messaging;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;



public class JMS2Receiver implements MessageListener {

    static Logger log = LoggerFactory.getLogger(JMS2Receiver.class);
    ActiveMQConnectionFactory activeMQConnectionFactory;
    ActiveMQConnection connection;
    ActiveMQSession session;
    QueueReceiver queueReceiver;
    QueueSender replySender;
    String RID;
    boolean ready = false;


    public JMS2Receiver(int ID){
        try {
            this.RID = " R_"+ID+"  ";
            this.init();
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error( RID+"Unable to initialize receiver!");
            System.exit(111);
        }
    }


    public void init() throws Exception {
        log.info( RID+"Initializing recever...");
        try{
            activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://desk1:61616");
            connection = (ActiveMQConnection) activeMQConnectionFactory.createConnection();
            connection.start();

            session = (ActiveMQSession) connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            queueReceiver = session.createReceiver( session.createQueue("GEMS.NEW") );
            queueReceiver.setMessageListener(this);

            replySender = session.createSender( session.createQueue("GEMS.COMPLETE") );

            log.info( RID+"Receiver initialized...");
            this.ready = true;


        } catch (Exception initEx){
            log.error( RID+"Exception while initializing receiver!", initEx);
            throw initEx;
        }


    }


    @Override
    public void onMessage(Message message) {

        if(this.ready){

            try{

                String messageBody = ((TextMessage)message).getText();
                log.info( RID+"Message Received " + messageBody );
                Thread.sleep(5000);

                Message replyMessage = session.createTextMessage( messageBody );
                replyMessage.setJMSCorrelationID( message.getJMSCorrelationID() );
                replySender.send( replyMessage );

                session.commit();
                log.info( RID+" Finished "+message.getJMSCorrelationID() );

            } catch (Exception rcx){
                log.error( RID+"Exception while receiving message ", rcx );
            }

        } else {
            log.error( RID+" NOT READY!");
        }



    }


}
