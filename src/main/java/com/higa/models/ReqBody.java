package com.higa.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class ReqBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("cert_nasc_base64")
    private String certNascBase64;
}
