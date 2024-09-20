package com.higa.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AzureSvc {

    @Value("${azure.endpoint.protocol}")
    private String endpointProtocol;

    @Value("${azure.endpoint.suffix}")
    private String endpointSuffix;

    @Value("${azure.storage.account.name}")
    private String storageAccountName;

    @Value("${azure.storage.account.key}")
    private String storageAccountKey;

    @Getter
    @Value("${azure.queue.name}")
    private String queueName;

    @Getter
    @Value("${azure.container.name}")
    private String containerName;

//    public QueueClient getAzQueueClient(){
//        return new QueueClientBuilder()
//                .connectionString(getAzConnStr())
//                .queueName(getQueueName())
//                .buildClient();
//    }

    public String getAzConnStr(){
        return new StringBuilder()
                .append("DefaultEndpointsProtocol=").append(endpointProtocol).append(";")
                .append("AccountName=").append(storageAccountName).append(";")
                .append("AccountKey=").append(storageAccountKey).append(";")
                .append("EndpointSuffix=").append(endpointSuffix)
                .toString();
    }

}
