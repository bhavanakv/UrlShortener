package com.example.UrlShortener.repository;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ShortUrl {

    @Id
    private Long id;

    private String longUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public ShortUrl() {
    }

    public ShortUrl(Long reqId, String longUrl) {
        this.id = reqId;
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        return "ShortUrl [id=" + id + ", longUrl=" + longUrl + "]";
    }
}
