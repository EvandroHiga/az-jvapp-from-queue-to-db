package com.higa.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.google.gson.JsonObject;
import com.higa.models.ReqBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class AppSvc {
    private final AzureSvc azureSvc;
    private final GoogleSvc googleSvc;

    @Autowired
    public AppSvc(AzureSvc azureSvc, GoogleSvc googleSvc){
        this.azureSvc = azureSvc;
        this.googleSvc = googleSvc;
    }

    public ResponseEntity<String> readAndStoreCertNascBase64(ReqBody reqBody){
        JsonObject responseBodyJson = googleSvc.readImageCertNasc(reqBody.getCertNascBase64());
        String certNascContent = googleSvc.getCertNascContentFromResponse(responseBodyJson);

        JsonObject certNascInfosJsonObj = getImportantInfoFromCertNasc(certNascContent);

        String nomeArquivo = certNascInfosJsonObj.get("cpf").getAsString() + " " + Instant.now();

        BlobContainerClient containerClient =
                new BlobContainerClientBuilder()
                        .connectionString(azureSvc.getAzConnStr())
                        .containerName(azureSvc.getContainerName())
                        .buildClient();
        BlobClient blobClient = containerClient.getBlobClient(nomeArquivo);

        blobClient.upload(
                new ByteArrayInputStream(certNascInfosJsonObj.toString().getBytes(StandardCharsets.UTF_8)),
                certNascInfosJsonObj.toString().length(),
                true);

        return ResponseEntity.status(201).body(certNascInfosJsonObj.toString());
    }

    public JsonObject getImportantInfoFromCertNasc(String certNascContent){
        JsonObject object = new JsonObject();
        object.addProperty("cpf", getValueIfExists(certNascContent, "CPF"));
        object.addProperty("nome", getValueIfExists(certNascContent, "NOME"));
        object.addProperty("matricula", getValueIfExists(certNascContent, "MATRICULA"));
        object.addProperty("dataNasc", getValueIfExists(certNascContent, "DATA DE NASCIMENTO"));
        object.addProperty("raw_data", certNascContent);
        return object;
    }

    private String getValueIfExists(String certNascContent, String subStr){
        int begin = certNascContent.indexOf(subStr);
        if(begin >= 0){
            int finish = certNascContent.indexOf("\n", begin);
            int beginValue = begin + (finish - begin) + 1;
            int finishValue = certNascContent.indexOf("\n", beginValue);
            return certNascContent.substring(beginValue, finishValue);
        } else {
            return "";
        }
    }
}
