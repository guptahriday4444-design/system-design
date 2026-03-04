package ratelimiter.tokenBucket;

import java.util.concurrent.locks.ReentrantLock;

class TokenBucket {
    private final double capacity;
    private double tokens;
    private final double refillRate;
    private long lastRefillTime;
    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucket(double capacity, double refillRate) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public boolean allowRequest() {
        lock.lock();
        try {
            refill();
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void refill() {
        long currentTime = System.currentTimeMillis();
        double elapsedTime = currentTime - lastRefillTime;
        double tokensToAdd = elapsedTime * refillRate / 1000.0;
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTime = currentTime;
    }
}