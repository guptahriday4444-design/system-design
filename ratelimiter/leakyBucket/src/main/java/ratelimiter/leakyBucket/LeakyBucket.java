package ratelimiter.leakyBucket;

import java.util.concurrent.locks.ReentrantLock;

class LeakyBucket {
    private final double capacity;
    private final double processRate;
    private double queueSize;
    private long lastRequestTime;

    private final ReentrantLock lock;

    public LeakyBucket(double capacity, double processRate) {
        this.capacity = capacity;
        this.processRate = processRate;
        this.queueSize = 0;
        this.lastRequestTime = System.currentTimeMillis();
        this.lock = new ReentrantLock();
    }

    public boolean allowRequest() {
        lock.lock();
        try {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastRequestTime;
            double processed = (elapsedTime * processRate) / 1000.0;

            queueSize = Math.max(0, queueSize - processed);
            lastRequestTime = currentTime;
            if (queueSize < capacity) {
                queueSize += 1;
                return true;
            } else {
                return false; // Bucket is full, reject request
            }
        } finally {
            lock.unlock();
        }
    }
}
