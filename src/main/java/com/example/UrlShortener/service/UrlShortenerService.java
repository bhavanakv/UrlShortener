package com.example.UrlShortener.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.UrlShortener.common.IdConverter;
import com.example.UrlShortener.repository.ShortUrl;
import com.example.UrlShortener.repository.UrlRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlRepository repo;

    IdConverter INSTANCE = new IdConverter();

    ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    Long reqId = -1L;

    public String shortenUrl(String localUrl, String longUrl) {
        if(repo.findByLongUrl(longUrl) == null) {
            reqId++;
            String uniqueId = INSTANCE.createUniqueId(reqId);
            String baseString = formatLocalUrl(localUrl);
            String shortUrl = baseString + uniqueId;
            ShortUrl url = new  ShortUrl(reqId, longUrl, shortUrl);
            logger.info("Saving {} into database", url);
            repo.save(url);
            return shortUrl;
        }
        else {
            logger.error("Long url {} already exists in the database", longUrl);
            return "Error";
        }
    }

    private String formatLocalUrl(String localUrl) {
        localUrl = localUrl.replace("shorten", "");
        return localUrl;
    }

    public String getLongUrl(String id) throws Exception {
        Long key = INSTANCE.getKeyFromUniqueId(id);
        Optional<ShortUrl> longUrl = repo.findById(key);
        String url = "";
        if(longUrl.isPresent()) {
            url = longUrl.get().getLongUrl();
            logger.info("Fetched {} from database");
        }else {
            logger.error("Could not find url in the database");
            return "Error";
        }
        return url;
    }

    public List<ShortUrl> getHistory() {
        List<ShortUrl> list = new ArrayList<>();
        repo.findAll().forEach(list::add);
        return list;
    }
}
