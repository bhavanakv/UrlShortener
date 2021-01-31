package com.example.UrlShortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<ShortUrl, Long> {
    
    // private final Jedis jedis;
    // private final String idKey;
    // private final String urlKey;

    // public UrlRepository() {
    //     this.jedis = new Jedis();
    //     this.idKey = "id";
    //     this.urlKey = "url:";
    // }

    // public UrlRepository(Jedis jedis, String idKey, String urlKey) {
    //     this.jedis = jedis;
    //     this.idKey = idKey;
    //     this.urlKey = urlKey;
    // }

    // public Long incrementId() {
    //     Long id = jedis.incr(idKey);
    //     return id - 1;
    // }

    // public void saveUrl(String longUrl, String key) {
    //     jedis.hset(urlKey, key, longUrl);
    // }

    // public String getUrl(Long key) {
    //     String longUrl = jedis.hget(urlKey, "url:"+key);
    //     return longUrl;
    // }
}
