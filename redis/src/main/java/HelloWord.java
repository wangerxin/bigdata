import org.redisson.Redisson;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;

public class HelloWord {

    public static void main(String[] args) {

        RedisClient redisClient = RedisClient.create(new RedisClientConfig());
    }
}
