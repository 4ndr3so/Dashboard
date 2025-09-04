package com.andres.dashboard.licenses.api;

import com.andres.dashboard.licenses.model.FsaCount;
import com.andres.dashboard.licenses.services.LicenseService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/licenses")
public class PublicLicenseController {

    private final LicenseService svc;
    private final RestClient ckan; // 👈 inject the RestClient bean

    public PublicLicenseController(LicenseService svc, RestClient ckan) {
        this.svc = svc;
        this.ckan = ckan;
    }

    // Dogs vs Cats (2025): GET /api/licenses/2025/by-animal
    @GetMapping("/{year}/by-animal")
    public Map<String, Long> byAnimal(@PathVariable int year) {
        return svc.countByAnimalType(year);
    }

    // Licenses per FSA (2025): GET /api/licenses/2025/by-fsa
    @GetMapping("/{year}/by-fsa")
    public List<FsaCount> byFsa(@PathVariable int year) {
        return svc.countByFsa(year);
    }

    // Top N breeds (2025): GET /api/licenses/2025/top-breeds?limit=10
    @GetMapping("/{year}/top-breeds")
    public List<Map.Entry<String, Long>> topBreeds(
            @PathVariable int year,
            @RequestParam(defaultValue = "10") int limit) {
        return svc.topBreeds(year, limit);
    }

    // 🔹 Test endpoint to check CKAN connectivity

    @GetMapping("/_ckan/test")
    public ResponseEntity<?> testPost() {
        var entity = ckan.post()
                .uri("/api/3/action/datastore_search")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "resource_id", "4a773296-7225-442d-9929-074549b2ccc0",
                        "filters", Map.of("Year", 2025),
                        "limit", 1,
                        "offset", 0))
                .retrieve()
                .toEntity(String.class);

        return ResponseEntity.status(entity.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(entity.getBody());
    }
}
