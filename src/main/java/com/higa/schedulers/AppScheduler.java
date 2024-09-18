package com.higa.schedulers;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;
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
import java.util.HashMap;
import java.util.Map;

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
        createTable();

        String msgFromQueue = getMessageFromQueue();
        JsonObject object1 = googleSvc.readCertNasc(msgFromQueue);
        String certNascContent = googleSvc.getCertNascContentFromResponse(object1);
        JsonObject responseObj = getOnlyImportantInfo(certNascContent);

        Map<String, Object> tableValues = new HashMap<>();

        tableValues.put("cpf", responseObj.get("cpf"));
        tableValues.put("nome", responseObj.get("nome"));
        tableValues.put("matricula", responseObj.get("matricula"));
        tableValues.put("dataNasc", responseObj.get("dataNasc"));
        tableValues.put("nomeMae", responseObj.get("nomeMae"));

        TableClient tableClient = new TableClientBuilder()
                .connectionString(azureSvc.getAzConnStr())
                .tableName("db_cert_nasc_info")
                .buildClient();

        TableEntity tableEntity = new TableEntity("",responseObj.get("cpf").getAsString()).setProperties(tableValues);

        tableClient.createEntity(tableEntity);
    }

    public String getMessageFromQueue(){
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

    // TODO remover no futuro
    private void createTable(){
        TableServiceClient tableServiceClient = new TableServiceClientBuilder()
                .connectionString(azureSvc.getAzConnStr())
                .buildClient();
        tableServiceClient.createTableIfNotExists("db_cert_nasc_info");
    }

}
