package ratelimiter.tokenBucket;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class TokenBucketRateLimiter {
    private final Cache<String, TokenBucket> tokenBuckets;
    private final double capacity;
    private final double refillRate;

    public TokenBucketRateLimiter(double capacity, double refillRate, long expirySeconds) {
        this.tokenBuckets = Caffeine.newBuilder()
                .expireAfterAccess(expirySeconds, TimeUnit.SECONDS)
                .build();
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    public boolean allowUserRequest(String userId) {
        TokenBucket bucket = tokenBuckets.get(userId, id -> new TokenBucket(capacity, refillRate));
        return bucket.allowRequest();
    }

}
