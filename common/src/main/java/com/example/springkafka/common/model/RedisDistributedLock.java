package com.example.springkafka.common.model;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

public class RedisDistributedLock implements DistributedLock {
    private static final String UNLOCK_LUA =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "  return redis.call('del', KEYS[1]) " +
            "else " +
            "  return 0 " +
            "end";

    private final StringRedisTemplate redis;
    private final DefaultRedisScript<Long> unlockScript;

    public RedisDistributedLock(StringRedisTemplate redis) {
        this.redis = redis;
        this.unlockScript = new DefaultRedisScript<>(UNLOCK_LUA, Long.class);
    }

    @Override
    public boolean tryLock(String key, String ownerId, Duration ttl) {
        Boolean acquired = redis.opsForValue().setIfAbsent(key, ownerId, ttl);
        return Boolean.TRUE.equals(acquired);
    }

    @Override
    public boolean unlock(String key, String ownerId) {
        Long result = redis.execute(unlockScript, List.of(key), ownerId);
        return result != null && result == 1L;
    }

    @Override
    public Optional<Duration> getRemainingTtl(String key) {
        Long ttlMillis = redis.getExpire(key, TimeUnit.MILLISECONDS);
        if (ttlMillis == null || ttlMillis < 0) {
            return Optional.empty();
        }
        return Optional.of(Duration.ofMillis(ttlMillis));
    }
}
