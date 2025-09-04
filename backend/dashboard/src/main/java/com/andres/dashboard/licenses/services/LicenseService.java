package com.andres.dashboard.licenses.services;

import com.andres.dashboard.ckan.CkanClient;
import com.andres.dashboard.ckan.dto.DatastoreSearchResponse;
import com.andres.dashboard.ckan.dto.LicenseRecord;
import com.andres.dashboard.licenses.model.FsaCount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Supplier; // <-- must be this one
import com.andres.dashboard.licenses.services.LocalCache; // match your package

@Service
public class LicenseService {
  private final CkanClient ckan;
  private final ObjectMapper mapper = new ObjectMapper();
  private final LocalCache cache = new LocalCache(Duration.ofHours(12));

  public LicenseService(CkanClient ckan) { this.ckan = ckan; }

   // 👇 add this method
    public List<LicenseRecord> fetchAll(int year) {
        List<LicenseRecord> out = new ArrayList<>();
        int limit = 10_000;   // CKAN page size
        int offset = 0;

        while (true) {
            var res = ckan.fetchPage(year, limit, offset);
            if (res == null || res.getResult() == null || res.getResult().getRecords() == null) {
                break;
            }

            var records = res.getResult().getRecords();
            if (records.isEmpty()) break;

            for (var r : records) {
                // CKAN returns LinkedHashMap<String,Object>, convert to DTO
                LicenseRecord rec = mapper.convertValue(r, LicenseRecord.class);
                out.add(rec);
            }

            offset += limit;
            if (offset >= res.getResult().getTotal()) {
                break;
            }
        }

        return out;
    }
    
  public Map<String, Long> countByAnimalType(int year) {
    Supplier<Map<String, Long>> sup =
        () -> fetchAll(year).stream()
              .filter(r -> r.getAnimalType() != null)
              .collect(Collectors.groupingBy(LicenseRecord::getAnimalType, Collectors.counting()));
    return cache.getOrCompute("animal:" + year, sup);
  }

  public List<FsaCount> countByFsa(int year) {
    Supplier<Map<String, Long>> sup =
        () -> fetchAll(year).stream()
              .filter(r -> r.getFsa() != null && !r.getFsa().isBlank())
              .collect(Collectors.groupingBy(LicenseRecord::getFsa, Collectors.counting()));
    Map<String, Long> map = cache.getOrCompute("fsa:" + year, sup);
    return map.entrySet().stream()
        .map(e -> new FsaCount(e.getKey(), e.getValue()))
        .sorted(Comparator.comparingLong(FsaCount::getCount).reversed())
        .toList();
  }

  public List<Map.Entry<String, Long>> topBreeds(int year, int limit) {
    Supplier<Map<String, Long>> sup =
        () -> fetchAll(year).stream()
              .filter(r -> r.getPrimaryBreed() != null && !r.getPrimaryBreed().isBlank())
              .collect(Collectors.groupingBy(LicenseRecord::getPrimaryBreed, Collectors.counting()));
    Map<String, Long> byBreed = cache.getOrCompute("breed:" + year, sup);
    return byBreed.entrySet().stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .limit(limit)
        .toList();
  }
}
