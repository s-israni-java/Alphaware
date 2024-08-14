package com.test.appservice;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.entity.Region;
import com.test.repo.RegionRepository;

@Service
public class Application {
	private final Gson gson = new Gson();
	private final RestTemplate restTemplate;

	@Autowired
	public Application(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Autowired
	RegionRepository regionRepo;

	@PostConstruct
	public String fetchData() {
		String url = "https://dummy-json.mock.beeceptor.com/continents";
		String jsonData = restTemplate.getForObject(url, String.class);
		System.out.println(jsonData);
		List<Region> region = bindData(jsonData);
		if (region != null) {
			return "Saved succssfully..";
		}
		return "Something went wrong";
	}

	public List<Region> bindData(String jsonData) {
		Type regionListType = new TypeToken<List<Region>>() {
		}.getType();
		List<Region> regions = gson.fromJson(jsonData, regionListType);
		return save(regions);
	}

	public List<Region> save(List<Region> regions) {
		List<Region> savedFlag = regionRepo.saveAll(regions);
		return savedFlag;

	}
}
