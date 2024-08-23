package com.higa.schedulers;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@EnableScheduling
public class AppScheduler {
    @Scheduled(fixedDelayString = "${read.queue.delay.in.milli}")
    public void getMsgFromQueueInsertIntoDb(){
        System.out.println("-> " + Instant.now());
    }
}
