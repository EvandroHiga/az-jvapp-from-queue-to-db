package com.higa.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleSvc {

//    private final RestTemplate restTemplate;

//    @Autowired
//    public GoogleSvc(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public void readCertNasc(String certNascImg){
        String url = "https://vision.googleapis.com/v1/images:annotate?key=GOOGLE_VISION_API_HERE";

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject2 = new JsonObject();
        JsonArray jsonArrayFeatures = new JsonArray();
        JsonObject jsonObjectType = new JsonObject();
        jsonObjectType.addProperty("type", "DOCUMENT_TEXT_DETECTION");
        jsonArrayFeatures.add(jsonObjectType);
        jsonObject2.add("features", jsonArrayFeatures);
        JsonObject jsonObjectImage = new JsonObject();
        jsonObjectImage.addProperty("content", "BASE_64_IMG_TEST");
        jsonObject2.add("image", jsonObjectImage);
        jsonArray.add(jsonObject2);
        jsonObject.add("requests", jsonArray);

        HttpEntity<JsonObject> request = new HttpEntity<>(jsonObject);

//        ResponseEntity<JsonObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, JsonObject.class);
//        JsonObject responseBody = responseEntity.getBody();
    }
}
