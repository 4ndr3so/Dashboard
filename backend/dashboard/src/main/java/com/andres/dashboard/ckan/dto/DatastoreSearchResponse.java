package com.andres.dashboard.ckan.dto;

import java.util.List;
import java.util.Map;

public class DatastoreSearchResponse {
  private boolean success;
  private Result result;

  public boolean isSuccess() { return success; }
  public void setSuccess(boolean success) { this.success = success; }
  public Result getResult() { return result; }
  public void setResult(Result result) { this.result = result; }

  public static class Result {
    private int total;
    private List<Map<String, Object>> records;
    private String next;
    private String prev;

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public List<Map<String, Object>> getRecords() { return records; }
    public void setRecords(List<Map<String, Object>> records) { this.records = records; }

    public String getNext() { return next; }
    public void setNext(String next) { this.next = next; }

    public String getPrev() { return prev; }
    public void setPrev(String prev) { this.prev = prev; }
  }
}