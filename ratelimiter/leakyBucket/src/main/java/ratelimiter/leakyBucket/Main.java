package ratelimiter.leakyBucket;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int capacity = 5;
        double leakRate = 1.0; // 5 requests per second

        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(capacity, leakRate, 120);

        String user = "user123";

        for (int i = 0; i < 30; i++) {

            boolean allowed = limiter.allowUserRequest(user);

            System.out.println(
                    "Request " + i + " : " +
                            (allowed ? "Allowed" : "Rejected"));

            Thread.sleep(100);
        }
    }
}
