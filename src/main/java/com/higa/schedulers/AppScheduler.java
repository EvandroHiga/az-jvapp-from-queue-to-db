package com.higa.schedulers;

import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.models.QueueMessageItem;
import com.higa.services.AzureSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
//@EnableScheduling
public class AppScheduler {

    private final AzureSvc azureSvc;

    @Autowired
    public AppScheduler(AzureSvc azureSvc){
        this.azureSvc = azureSvc;
    }

//    @Scheduled(fixedDelayString = "${read.queue.delay.in.milli}")
//    public void getMsgFromQueueInsertIntoDb(){
//    }

    private String getMessageFromQueue(){
        QueueClient queueClient = new QueueClientBuilder()
                .connectionString(azureSvc.getAzConnStr())
                .queueName(azureSvc.getQueueName())
                .buildClient();

        QueueMessageItem queueMessage = queueClient.receiveMessage();
        final String messageStr = new String(queueMessage.getBody().toBytes(), StandardCharsets.UTF_8);
        queueClient.deleteMessage(queueMessage.getMessageId(), queueMessage.getPopReceipt());
        return messageStr;
    }
}
