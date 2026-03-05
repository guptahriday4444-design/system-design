package ratelimiter.tokenBucket;

public class Main {
    public static void main(String[] args) {

        double capacity = 100;
        double refillRate = 100.0 / 60.0; // 100 per minute

        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(capacity, refillRate, 180);

        String user = "user123";

        for (int i = 0; i < 120; i++) {
            System.out.print("Request " + i + ": ");
            if (limiter.allowUserRequest(user)) {
                System.out.println("Allowed");
            } else {
                System.out.println("Rejected");
            }
        }
    }
}
