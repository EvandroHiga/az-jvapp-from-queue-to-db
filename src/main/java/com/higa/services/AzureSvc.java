package com.higa.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

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
    @Value("${azure.container.name}")
    private String containerName;

    public void uploadFileToAzContainer(String nomeArquivo, JsonObject jsonFileToStore){
        BlobContainerClient containerClient =
                new BlobContainerClientBuilder()
                        .connectionString(getAzConnStr())
                        .containerName(getContainerName())
                        .buildClient();
        BlobClient blobClient = containerClient.getBlobClient(nomeArquivo);

        blobClient.upload(
                new ByteArrayInputStream(jsonFileToStore.toString().getBytes(StandardCharsets.UTF_8)),
                jsonFileToStore.toString().length(),
                true);
    }

    private String getAzConnStr(){
        return "DefaultEndpointsProtocol=" + endpointProtocol + ";" +
                "AccountName=" + storageAccountName + ";" +
                "AccountKey=" + storageAccountKey + ";" +
                "EndpointSuffix=" + endpointSuffix;
    }

}
