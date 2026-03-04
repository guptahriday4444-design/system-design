package ratelimiter.tokenBucket;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class RateLimitManager {
    private final Cache<String, TokenBucket> tokenBuckets;
    private final double capacity;
    private final double refillRate;

    public RateLimitManager(double capacity, double refillRate, long expirySeconds) {
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

    // next improvement: use a distributed cache like Redis to store the token buckets
    // and use a lock to synchronize the access to the cache
    // this will allow us to scale the system horizontally
    // and to handle requests from multiple clients gracefully
    // Can also add batching to the requests to reduce the number of requests to the cache

    // for solving hotkey problem, we can do key striping to distribute the requests to the different token buckets
}
