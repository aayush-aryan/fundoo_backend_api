package com.fundoobackendapi.utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisTemplateUtility<T> {
    @SuppressWarnings("unused")
    private org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Object, String> hashOperation;

    @Autowired
    public RedisTemplateUtility(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperation = redisTemplate.opsForHash();
    }
    public void putMap(String redisKey, Object key, String data) {
        hashOperation.put(redisKey, key, data);
    }
    public String getMap(String redisKey, Object key) {
        return hashOperation.get(redisKey, key);
    }
    public void deleteMap(String redisKey, Object key) {
        hashOperation.delete(redisKey,key);}

}
