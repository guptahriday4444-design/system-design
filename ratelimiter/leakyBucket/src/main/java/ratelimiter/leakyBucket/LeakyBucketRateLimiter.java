package ratelimiter.leakyBucket;

import com.github.benmanes.caffeine.cache.Cache;

class LeakyBucketRateLimiter {
    private final Cache<String, LeakyBucket> leakyBuckets;
    private final double capacity;
    private final double processRate;

    public LeakyBucketRateLimiter(double capacity, double processRate, long expirySeconds) {
        this.leakyBuckets = com.github.benmanes.caffeine.cache.Caffeine.newBuilder()
                .expireAfterAccess(expirySeconds, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.capacity = capacity;
        this.processRate = processRate;
    }

    public boolean allowUserRequest(String userId) {
        LeakyBucket bucket = leakyBuckets.get(userId, id -> new LeakyBucket(capacity, processRate));
        return bucket.allowRequest();
    }
}
