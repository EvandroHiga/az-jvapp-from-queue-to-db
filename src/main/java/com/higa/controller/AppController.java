package com.higa.controller;

import com.higa.models.ReqBody;
import com.higa.services.AppSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private final AppSvc appSvc;

    @Autowired
    public AppController(AppSvc appSvc){
        this.appSvc = appSvc;
    }

    @PostMapping
    public ResponseEntity<Void> readAndStoreCertNascBase64(@RequestBody ReqBody reqBody){
        return appSvc.readAndStoreCertNascBase64(reqBody);
    }
}
