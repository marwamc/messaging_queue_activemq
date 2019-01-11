package io.mmarwa.messaging;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;
import static java.lang.System.exit;


public class JMS2Sender implements Runnable {

    static Logger log = LoggerFactory.getLogger(JMS2Sender.class);
    int numMessages = 0;
    String SID = "";
    AtomicInteger count;

    public JMS2Sender(int sid, int numMessages, AtomicInteger count){
        this.SID = " S_"+sid+" ";
        this.numMessages = numMessages;
        this.count = count;
    }

    @Override
    public void run() {

        try{

            log.info( SID+"Starting sender...");
            ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://desk1:61616");
            ActiveMQConnection connection = (ActiveMQConnection) cf.createConnection();
            connection.start();

            ActiveMQSession session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("GEMS.NEW");

            QueueSender sender = session.createSender(queue);


            for (int i=0; i<numMessages; i++){

                count.getAndIncrement();

                Message message = session.createTextMessage( String.format("GET BHF_ACCT GAAP F DAILY 20190103 %1$4s %2$4d %3$4d ", SID, i, count.get()) );
                message.setJMSCorrelationID( String.format("BHF_ACCT_DAILY_GAAP_F %1$2s %2$4d", SID, count.get()) );
                sender.send( message );

                log.info( SID+"Message sent " + ((TextMessage)message).getText());

            }


            log.info( SID+"All Messages sent");
            exit(0);

        } catch (Exception sendEx){
            log.error( SID+"Exception while send messages!", sendEx);
            exit(111);
        }


    }



}
