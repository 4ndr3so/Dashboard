package com.andres.dashboard.ckan.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // <-- ignore _id and anything else not mapped
public class LicenseRecord {

  // Use exact field names from CKAN and normalised getters for service code
  @JsonProperty("Year")
  private Integer year;

  @JsonProperty("ANIMAL_TYPE")
  private String animalType;

  @JsonProperty("FSA")
  private String fsa;

  @JsonProperty("PRIMARY_BREED")
  private String primaryBreed;

  // getters/setters
  public Integer getYear() { return year; }
  public void setYear(Integer year) { this.year = year; }

  public String getAnimalType() { return animalType; }
  public void setAnimalType(String animalType) { this.animalType = animalType; }

  public String getFsa() { return fsa; }
  public void setFsa(String fsa) { this.fsa = fsa; }

  public String getPrimaryBreed() { return primaryBreed; }
  public void setPrimaryBreed(String primaryBreed) { this.primaryBreed = primaryBreed; }
}

