package com.tracker.store.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.store.entity.Store;
import com.tracker.store.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

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
                    System.out.println("Store saved: " + store.getName());
                } else {
                    System.out.println("Store already exists: " + store.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to save stores: " + e.getMessage());
        }
    }
}
