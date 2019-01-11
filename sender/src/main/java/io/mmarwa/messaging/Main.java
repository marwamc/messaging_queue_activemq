package io.mmarwa.messaging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {

    static Logger log = LoggerFactory.getLogger(Main.class);
    static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {


        int numSenders = Integer.parseInt( System.getProperty("n", "10") );
		ExecutorService sex = Executors.newFixedThreadPool(numSenders);

		try {

			log.info("Staring sender....");
			int num2Send = Integer.parseInt(System.getProperty("s", "2000"));
			log.info("Sending messages: " + num2Send);

			for (int s = 1; s < numSenders+1; s++) {
				sex.submit(new JMS2Sender(s, num2Send/numSenders, count));
			}


		}catch (Exception ex){

			log.error("Exception in sender main!", ex);

		} finally {

			sex.shutdown();

		}


	}

}
