package com.agriculture.smartagriculture.repository;

import com.agriculture.smartagriculture.model.PredictionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PredictionRepository extends MongoRepository<PredictionRecord, String> {
    List<PredictionRecord> findTop30ByUserEmailOrderByCreatedAtDesc(String userEmail);
}
