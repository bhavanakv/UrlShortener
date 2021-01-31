package com.example.UrlShortener.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.UrlShortener.common.IdConverter;
import com.example.UrlShortener.repository.ShortUrl;
import com.example.UrlShortener.repository.UrlRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlRepository repo;

    IdConverter INSTANCE = new IdConverter();

    ObjectMapper mapper = new ObjectMapper();

    Long reqId = -1L;

    public String shortenUrl(String localUrl, String longUrl) {
        reqId++;
        String uniqueId = INSTANCE.createUniqueId(reqId);
        repo.save(new ShortUrl(reqId, longUrl));
        String baseString = formatLocalUrl(localUrl);
        return baseString + uniqueId;
    }

    private String formatLocalUrl(String localUrl) {
        localUrl = localUrl.replace("shorten", "");
        return localUrl;
    }

    public String getLongUrl(String id) throws Exception {
        Long key = INSTANCE.getKeyFromUniqueId(id);
        System.out.println(key);
        Optional<ShortUrl> longUrl = repo.findById(key);
        String url = "";
        if(longUrl.isPresent()) {
            url = longUrl.get().getLongUrl();
        }else {
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
