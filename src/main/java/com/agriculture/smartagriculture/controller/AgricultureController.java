package com.agriculture.smartagriculture.controller;

import com.agriculture.smartagriculture.model.PredictionRecord;
import com.agriculture.smartagriculture.repository.PredictionRepository;
import com.agriculture.smartagriculture.service.MlClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AgricultureController {
    private final MlClient ml;
    private final PredictionRepository history;
    public AgricultureController(MlClient ml, PredictionRepository history) { this.ml=ml; this.history=history; }

    @PostMapping("/crop-recommendation")
    public Map<?,?> crop(@RequestBody Map<String,Object> input) {
        Map<?,?> result=ml.post("/ml/crop-recommendation",input);
        save(input,result,"crop");
        return result;
    }
    @PostMapping("/yield-prediction")
    public Map<?,?> yield(@RequestBody Map<String,Object> input) {
        Map<?,?> result=ml.post("/ml/yield-prediction",input);
        save(input,result,"yield");
        return result;
    }
    @PostMapping("/fertilizer")
    public Map<?,?> fertilizer(@RequestBody Map<String,Object> input) {
        Map<?,?> result=ml.post("/ml/fertilizer",input);
        save(input,result,"fertilizer");
        return result;
    }
    @PostMapping(value="/disease-detection", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<?,?> disease(@RequestParam("file") MultipartFile file) throws Exception {
        Map<?,?> result=ml.disease(file.getBytes(),file.getOriginalFilename()==null?"leaf.jpg":file.getOriginalFilename());
        save(Map.of("filename",file.getOriginalFilename()==null?"leaf.jpg":file.getOriginalFilename()),result,"disease");
        return result;
    }
    @GetMapping("/history")
    public List<PredictionRecord> getHistory(@RequestParam(defaultValue="demo@agri.com") String email) {
        return history.findTop30ByUserEmailOrderByCreatedAtDesc(email);
    }
    private void save(Map<String,Object> input, Map<?,?> result, String type) {
        Map<String,Object> safe = new HashMap<>();
        result.forEach((k,v)->safe.put(String.valueOf(k),v));
        history.save(new PredictionRecord("demo@agri.com",type,input,safe));
    }
}
