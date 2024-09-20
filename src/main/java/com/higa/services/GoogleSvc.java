package com.higa.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class GoogleSvc {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public GoogleSvc(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JsonObject readImageCertNasc(String certNascImgBase64){
        final String url = "https://vision.googleapis.com/v1/images:annotate?key=" + apiKey;
        final String reqBodyStr = parseToGoogleVisionRequest(certNascImgBase64).toString();
        final HttpEntity<String> request = new HttpEntity<>(reqBodyStr);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();
    }

    private JsonObject parseToGoogleVisionRequest(String certNascImg){
        JsonObject jsonObject = new JsonObject();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        JsonArray arrayFeatures = new JsonArray();
        JsonObject objectType = new JsonObject();
        objectType.addProperty("type", "DOCUMENT_TEXT_DETECTION");
        arrayFeatures.add(objectType);
        object.add("features", arrayFeatures);
        JsonObject jsonObjectImage = new JsonObject();
        jsonObjectImage.addProperty("content", certNascImg);
        object.add("image", jsonObjectImage);
        array.add(object);
        jsonObject.add("requests", array);
        return jsonObject;
    }

    public String getCertNascContentFromResponse(JsonObject certNasc){
        JsonArray arrayResponses = certNasc.get("responses").getAsJsonArray();
        JsonObject object1 = arrayResponses.get(0).getAsJsonObject();
        JsonArray arrayTextAnnotations = object1.get("textAnnotations").getAsJsonArray();
        JsonObject objectContent = arrayTextAnnotations.get(0).getAsJsonObject();
        return objectContent.get("description").getAsString();
    }
}
