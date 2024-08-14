package com.test.appservice;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.test.entity.Region;
import com.test.repo.RegionRepository;

/**
 * Service to fetch and persist region data from an external API.
 */
@Service
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final String API_URL = "https://dummy-json.mock.beeceptor.com/continents";

    private final Gson gson;
    private final RestTemplate restTemplate;
    private final RegionRepository regionRepository;

    @Autowired
    public Application(RestTemplate restTemplate, RegionRepository regionRepository) {
        this.gson = new Gson();
        this.restTemplate = restTemplate;
        this.regionRepository = regionRepository;
    }

    /**
     * Fetches data from the API and processes it after initialization.
     */
    @PostConstruct
    public void fetchData() {
        try {
            String jsonData = fetchJsonDataFromApi();
            if (jsonData != null && !jsonData.isEmpty()) {
                List<Region> regions = parseJsonData(jsonData);
                if (regions != null && !regions.isEmpty()) {
                    saveRegions(regions);
                } else {
                    LOGGER.warn("Parsed regions list is empty or null.");
                }
            } else {
                LOGGER.warn("Received empty or null JSON data from API.");
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while fetching or processing data", e);
        }
    }

    /**
     * Fetches JSON data from the API.
     * @return JSON data as a String
     */
    private String fetchJsonDataFromApi() {
        try {
            return restTemplate.getForObject(API_URL, String.class);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch data from API", e);
            return null;
        }
    }

    /**
     * Parses the JSON data into a list of Region objects.
     * @param jsonData JSON data as a String
     * @return List of Region objects
     */
    private List<Region> parseJsonData(String jsonData) {
        try {
            Type regionListType = new TypeToken<List<Region>>() {}.getType();
            return gson.fromJson(jsonData, regionListType);
        } catch (JsonSyntaxException e) {
            LOGGER.error("Failed to parse JSON data", e);
            return null;
        }
    }

    /**
     * Saves the list of regions to the database.
     * @param regions List of Region objects
     */
    private void saveRegions(List<Region> regions) {
        try {
            regionRepository.saveAll(regions);
            LOGGER.info("Successfully saved {} regions to the database.", regions.size());
        } catch (Exception e) {
            LOGGER.error("Failed to save regions to the database", e);
        }
    }
}