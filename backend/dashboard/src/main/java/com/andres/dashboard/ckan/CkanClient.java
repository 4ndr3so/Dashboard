package com.andres.dashboard.ckan;
// src/main/java/com/andres/dashboard/ckan/CkanClient.java


import com.andres.dashboard.ckan.dto.DatastoreSearchResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class CkanClient {
  private final RestClient ckan;
  private static final String RESOURCE_ID = "4a773296-7225-442d-9929-074549b2ccc0";

  public CkanClient(RestClient ckanRestClient) { this.ckan = ckanRestClient; }

  public DatastoreSearchResponse fetchPage(int year, int limit, int offset) {
    Map<String, Object> body = new HashMap<>();
    body.put("resource_id", RESOURCE_ID);
    body.put("filters", Map.of("Year", year));   // <- JSON body, no URL encoding
    body.put("limit", limit);
    body.put("offset", offset);

    return ckan.post()
        .uri("/api/3/action/datastore_search")
        .contentType(MediaType.APPLICATION_JSON)
        .body(body)
        .retrieve()
        .body(DatastoreSearchResponse.class);
  }
}
