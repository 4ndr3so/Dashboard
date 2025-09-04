package com.andres.dashboard.licenses.services;


import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

public class LocalCache {

  private static final class Entry {
    final Object value;
    final Instant expiresAt;
    Entry(Object value, Instant expiresAt) {
      this.value = value;
      this.expiresAt = expiresAt;
    }
    boolean valid() { return Instant.now().isBefore(expiresAt); }
  }

  private final Map<String, Entry> store = new ConcurrentHashMap<>();
  private final Map<String, FutureTask<Entry>> inflight = new ConcurrentHashMap<>();
  private final Duration ttl;

  public LocalCache(Duration ttl) {
    this.ttl = Objects.requireNonNull(ttl, "ttl");
  }

  @SuppressWarnings("unchecked")
  public <T> T getOrCompute(String key, Supplier<T> supplier) {
    // Fast path
    Entry e = store.get(key);
    if (e != null && e.valid()) {
      return (T) e.value;
    }

    // Build task
    FutureTask<Entry> newTask = new FutureTask<>(() -> {
      T v = supplier.get();
      if (v == null) throw new IllegalStateException("Supplier returned null for key: " + key);
      Entry fresh = new Entry(v, Instant.now().plus(ttl));
      store.put(key, fresh);
      return fresh;
    });

    // Put if absent → if we win, we must run; if not, another thread will run
    FutureTask<Entry> task = inflight.putIfAbsent(key, newTask);
    if (task == null) {
      task = newTask;
      task.run();
    }

    try {
      Entry updated = task.get();
      return (T) updated.value;
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      Entry prev = store.get(key);
      if (prev != null) return (T) prev.value;
      throw new RuntimeException("Cache refresh interrupted and no previous value for key: " + key, ie);
    } catch (ExecutionException ee) {
      Entry prev = store.get(key);
      if (prev != null) return (T) prev.value;
      throw new RuntimeException("Cache refresh failed and no previous value for key: " + key, ee.getCause());
    } finally {
      inflight.remove(key, task);
    }
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> getIfFresh(String key) {
    Entry e = store.get(key);
    if (e != null && e.valid()) return Optional.of((T) e.value);
    return Optional.empty();
  }

  public <T> void put(String key, T value) {
    if (value == null) throw new IllegalArgumentException("Null values not allowed");
    store.put(key, new Entry(value, Instant.now().plus(ttl)));
  }

  public void evict(String key) { store.remove(key); }
  public void clear() { store.clear(); inflight.clear(); }
}
