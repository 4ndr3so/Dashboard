package com.andres.dashboard.licenses.jobs;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.andres.dashboard.licenses.services.LicenseService;

@Component
public class LicenseSyncJob {

  private final LicenseService svc;
  public LicenseSyncJob(LicenseService svc) { this.svc = svc; }

  // Every Monday at 3:00 AM (Toronto time)
  @Scheduled(cron = "0 0 3 * * MON", zone = "America/Toronto")
  public void refresh() {
    // Warm cache (adjust years as needed)
    svc.countByAnimalType(2025);
    svc.countByFsa(2025);
    svc.topBreeds(2025, 20);
  }
}
