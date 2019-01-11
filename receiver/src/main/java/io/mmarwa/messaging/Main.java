package io.mmarwa.messaging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.System.exit;

public class Main {

    static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        log.info("Starting Receivers");
        int numReceivers = Integer.parseInt(  System.getProperty("r", "20") );
        log.info( "Running with "+numReceivers+" receivers");

        ExecutorService rex = Executors.newFixedThreadPool(numReceivers);


        for( int r=1; r< numReceivers+2; r++){
            log.info("Starting receiver ", r);
            int finalR = r;
            rex.submit(() -> {
                new JMS2Receiver(finalR);
            });
        }



    }
}
