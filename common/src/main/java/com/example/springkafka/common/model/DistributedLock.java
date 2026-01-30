package com.example.springkafka.common.model;

import java.time.Duration;
import java.util.Optional;

public interface DistributedLock {
    boolean tryLock(String key, String ownerId, Duration ttl);

    boolean unlock(String key, String ownerId);

    Optional<Duration> getRemainingTtl(String key);
}
