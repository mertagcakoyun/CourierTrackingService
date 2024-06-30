package com.tracker.store.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.store.entity.Store;
import com.tracker.store.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
public class StoreDataLoader {
    private final Logger logger = LoggerFactory.getLogger(StoreDataLoader.class);

    @Autowired
    private StoreRepository storeRepository;

    @PostConstruct
    public void init() {
        loadStoresFromJson();
    }

    public void loadStoresFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Store>> typeReference = new TypeReference<List<Store>>() {};
            InputStream inputStream = TypeReference.class.getResourceAsStream("/stores.json");
            List<Store> stores = mapper.readValue(inputStream, typeReference);
            for (Store store : stores) {
                Optional<Store> existingStore = storeRepository.findByName(store.getName());
                if (!existingStore.isPresent()) {
                    storeRepository.save(store);
                    logger.info("Store saved: " + store.getName());
                } else {
                    logger.info("Store already exists: " + store.getName());
                }
            }
        } catch (Exception e) {
            logger.error("Unable to save stores: " + e.getMessage());
        }
    }
}
