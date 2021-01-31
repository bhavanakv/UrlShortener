package com.example.UrlShortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<ShortUrl, Long> {
    
    ShortUrl findByLongUrl(String longUrl);
}
