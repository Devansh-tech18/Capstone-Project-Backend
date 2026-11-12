package com.agriculture.smartagriculture.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class MlClient {
    private final RestClient client;
    public MlClient(@Value("${ml.service.url}") String url) {
        this.client = RestClient.builder().baseUrl(url).build();
    }
    public Map<?,?> post(String path, Map<String,Object> body) {
        return client.post().uri(path).contentType(MediaType.APPLICATION_JSON).body(body)
                .retrieve().body(Map.class);
    }
    public Map<?,?> disease(byte[] bytes, String filename) {
        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override public String getFilename() { return filename; }
        };
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        return client.post().uri("/ml/disease").contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body).retrieve().body(Map.class);
    }
}
