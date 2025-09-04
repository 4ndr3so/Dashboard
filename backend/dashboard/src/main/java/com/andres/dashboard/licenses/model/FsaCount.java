package com.andres.dashboard.licenses.model;

public class FsaCount {
  private String fsa;
  private long count;

  public FsaCount() {}
  public FsaCount(String fsa, long count) { this.fsa = fsa; this.count = count; }

  public String getFsa() { return fsa; }
  public void setFsa(String fsa) { this.fsa = fsa; }
  public long getCount() { return count; }
  public void setCount(long count) { this.count = count; }
}