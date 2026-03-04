#include <algorithm>
#include <chrono>
#include <iostream>
#include <string>
#include <unordered_map>

using namespace std;
using namespace std::chrono;

class TokenBucket {
private:
  double capacity;
  double tokens;
  double refill_rate;
  steady_clock::time_point last_refill_time;

public:
  TokenBucket(double capacity, double refill_rate)
      : capacity(capacity), tokens(capacity), refill_rate(refill_rate),
        last_refill_time(steady_clock::now()) {}

  bool allowRequest() {
    auto current_time = steady_clock::now();
    double elapsed_time =
        duration<double>(current_time - last_refill_time).count();
    double tokens_to_add = elapsed_time * refill_rate;

    tokens = min(capacity, tokens + tokens_to_add);

    if (tokens < 1) {
      return false;
    }
    tokens--;
    last_refill_time = current_time;
    return true;
  }
};

class RateLimitManager {
private:
  unordered_map<string, TokenBucket *> token_buckets;
  double capacity;
  double refill_rate;

public:
  RateLimitManager(double capacity, double refill_rate)
      : capacity(capacity), refill_rate(refill_rate) {}

  bool allowUserRequest(string userId) {
    TokenBucket *bucket;
    if (token_buckets.find(userId) == token_buckets.end()) {
      token_buckets[userId] = new TokenBucket(capacity, refill_rate);
    }
    bucket = token_buckets[userId];
    return bucket->allowRequest();
  }
};

int main() {
  long capacity = 100;
  double refillRate = 100.0 / 60.0;

  RateLimitManager limiter(capacity, refillRate);

  string user = "user123";

  for (int i = 0; i < 120; i++) {
    cout << "Request " << i << ": ";
    if (limiter.allowUserRequest(user))
      cout << "Allowed\n";
    else
      cout << "Rejected\n";
  }

  return 0;
}
