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

@Service
public class AppSvc {
    private final AzureSvc azureSvc;
    private final GoogleSvc googleSvc;

    @Autowired
    public AppSvc(AzureSvc azureSvc, GoogleSvc googleSvc){
        this.azureSvc = azureSvc;
        this.googleSvc = googleSvc;
    }

    public ResponseEntity<Object> readAndStoreCertNascBase64(ReqBody reqBody){
        JsonObject responseBodyJson = googleSvc.readImageCertNasc(reqBody.getCert_nasc_base64());
        String certNascContent = googleSvc.getCertNascContentFromResponse(responseBodyJson);

        // TODO guardar: cpf, nome, matricula, dataNasc, nomeMae
        JsonObject certNascInfosJsonObj = getImportantInfoFromCertNasc(certNascContent);

        //TODO Gerar nome do arquivo
        String nomeArquivo = "arquivo.json";

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

        return ResponseEntity.status(201).build();
    }


    private JsonObject getImportantInfoFromCertNasc(String certNascContent){
        JsonObject object = new JsonObject();
        object.addProperty("status", "sucesso! :)");
        return object;
    }
}
