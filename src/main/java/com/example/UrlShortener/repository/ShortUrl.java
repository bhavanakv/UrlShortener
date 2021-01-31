package com.example.UrlShortener.repository;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ShortUrl {

    @Id
    private Long id;

    private String longUrl;

    private String shortUrl;

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

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public ShortUrl() {
    }

    public ShortUrl(Long i, String longUrl, String shortUrl) {
        this.id = i;
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    @Override
    public String toString() {
        return "ShortUrl [id=" + id + ", longUrl=" + longUrl + ", shortUrl=" + shortUrl + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((longUrl == null) ? 0 : longUrl.hashCode());
        result = prime * result + ((shortUrl == null) ? 0 : shortUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShortUrl other = (ShortUrl) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (longUrl == null) {
            if (other.longUrl != null)
                return false;
        } else if (!longUrl.equals(other.longUrl))
            return false;
        if (shortUrl == null) {
            if (other.shortUrl != null)
                return false;
        } else if (!shortUrl.equals(other.shortUrl))
            return false;
        return true;
    }
}
