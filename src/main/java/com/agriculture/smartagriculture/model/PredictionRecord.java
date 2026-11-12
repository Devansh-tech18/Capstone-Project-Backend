package com.agriculture.smartagriculture.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Map;

@Document(collection = "predictions")
public class PredictionRecord {
    @Id private String id;
    private String userEmail;
    private String type;
    private Map<String, Object> input;
    private Map<String, Object> result;
    private Instant createdAt = Instant.now();

    public PredictionRecord() {}
    public PredictionRecord(String userEmail, String type, Map<String,Object> input, Map<String,Object> result) {
        this.userEmail=userEmail; this.type=type; this.input=input; this.result=result; this.createdAt=Instant.now();
    }
    public String getId(){return id;}
    public String getUserEmail(){return userEmail;}
    public String getType(){return type;}
    public Map<String,Object> getInput(){return input;}
    public Map<String,Object> getResult(){return result;}
    public Instant getCreatedAt(){return createdAt;}
}
