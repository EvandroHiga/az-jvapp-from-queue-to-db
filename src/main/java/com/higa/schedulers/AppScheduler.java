package com.higa.schedulers;

import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.models.QueueMessageItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.higa.services.AzureSvc;
import com.higa.services.GoogleSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@EnableScheduling
public class AppScheduler {

    private final AzureSvc azureSvc;
    private final GoogleSvc googleSvc;

    @Autowired
    public AppScheduler(AzureSvc azureSvc, GoogleSvc googleSvc){
        this.azureSvc = azureSvc;
        this.googleSvc = googleSvc;
    }

    @Scheduled(fixedDelayString = "${read.queue.delay.in.milli}")
    public void getMsgFromQueueInsertIntoDb(){
        String msgFromQueue = getMessageFromQueue();
        JsonObject object1 = googleSvc.readCertNasc(msgFromQueue);
        JsonArray array1 = object1.get("responses").getAsJsonArray();
        JsonObject object2 = array1.get(0).getAsJsonObject();
        JsonArray array2 = object2.get("textAnnotations").getAsJsonArray();
        JsonObject object3 = array2.get(0).getAsJsonObject();
        String certNascContent = object3.get("description").getAsString();

        JsonObject responseObj = getOnlyImportantInfo(certNascContent);



        //TODO Guardar no banco
    }

    private String getMessageFromQueue(){
        QueueClient queueClient = azureSvc.getAzQueueClient();
        QueueMessageItem queueMessage = queueClient.receiveMessage();
        final String msg = new String(queueMessage.getBody().toBytes(), StandardCharsets.UTF_8);
        queueClient.deleteMessage(queueMessage.getMessageId(), queueMessage.getPopReceipt());
        return msg;
    }

    private JsonObject getOnlyImportantInfo(String certNascContent){
        // TODO guardar: cpf, nome, matricula, dataNasc, nomeMae
        JsonObject object = new JsonObject();

        return object;
    }

}
