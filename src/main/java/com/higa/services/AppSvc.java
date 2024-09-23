package com.higa.services;

import com.google.gson.JsonObject;
import com.higa.models.ReqBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
        JsonObject certNascImportantContent = getImportantInfoFromCertNasc(certNascContent);
        azureSvc.uploadFileToAzContainer(
                certNascImportantContent.get("cpf").getAsString() + "_" + DateTimeFormatter.ofPattern("dd-MM-yy_hh-mm-ss").withZone(ZoneId.systemDefault()).format(Instant.now()) + ".json",
                certNascImportantContent);
        return ResponseEntity.status(201).body(certNascImportantContent.toString());
    }

    private JsonObject getImportantInfoFromCertNasc(String certNascContent){
        JsonObject object = new JsonObject();
        object.addProperty("cpf", getValueIfExists(certNascContent, "CPF"));
        object.addProperty("nome", getValueIfExists(certNascContent, "NOME"));
        object.addProperty("matricula", getValueIfExists(certNascContent, "MATRICULA"));
        object.addProperty("dataNasc", getValueIfExists(certNascContent, "DATA DE NASCIMENTO"));
        object.addProperty("raw_data", certNascContent);
        return object;
    }

    private String getValueIfExists(String certNascContent, String subStr){
        int begin = certNascContent.toLowerCase().indexOf(subStr.toLowerCase());
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
