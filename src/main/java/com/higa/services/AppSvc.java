package com.higa.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.google.gson.JsonObject;
import com.higa.models.ReqBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
public class AppSvc {
    private final AzureSvc azureSvc;
    private final GoogleSvc googleSvc;

    @Autowired
    public AppSvc(AzureSvc azureSvc, GoogleSvc googleSvc){
        this.azureSvc = azureSvc;
        this.googleSvc = googleSvc;
    }

    public void readAndStoreCertNascBase64(ReqBody reqBody){
//        JsonObject responseBodyJson = googleSvc.readCertNasc(reqBody.getCert_nasc_base64());
//        String certNascContent = googleSvc.getCertNascContentFromResponse(responseBodyJson);
//        JsonObject responseObj = getOnlyImportantInfo(certNascContent); //TODO

        JsonObject responseObj = new JsonObject();
        responseObj.addProperty("status", "sucesso");

        BlobContainerClient containerClient = new BlobContainerClientBuilder().connectionString(azureSvc.getAzConnStr()).containerName(azureSvc.getContainerName()).buildClient();
        BlobClient blobClient = containerClient.getBlobClient("arquivo.json");
        String responseObjStr = responseObj.toString();

        ByteArrayInputStream dataStream = new ByteArrayInputStream(responseObjStr.getBytes(StandardCharsets.UTF_8));

        blobClient.upload(dataStream, responseObjStr.length(), true);
    }

    // TODO guardar: cpf, nome, matricula, dataNasc, nomeMae
    private JsonObject getOnlyImportantInfo(String certNascContent){
        JsonObject object = new JsonObject();
        object.addProperty("status", "sucesso! :)");
        return object;
    }
}
