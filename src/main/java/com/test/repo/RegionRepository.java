package com.test.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.entity.Region;

public interface RegionRepository extends MongoRepository<Region, String> {
}
