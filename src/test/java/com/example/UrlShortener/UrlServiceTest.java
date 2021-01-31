package com.example.UrlShortener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.UrlShortener.repository.ShortUrl;
import com.example.UrlShortener.repository.UrlRepository;
import com.example.UrlShortener.service.UrlShortenerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;

@ExtendWith(MockitoExtension.class)
@AutoConfigureDataJpa
public class UrlServiceTest {
    
    @Mock
    UrlRepository repo;

    @InjectMocks
    UrlShortenerService urlShortener;

    @Test
    public void shortenUrlSuccess() {
        String actualUrl = "http://localhost:8080/shorten";
        String longUrl = "http://www.twitter.com";
        String shortUrl = "http://localhost:8080/a";
        ShortUrl url = new ShortUrl(0L, longUrl, shortUrl);
        Mockito.doReturn(null).when(repo).findByLongUrl(longUrl);
        Mockito.doReturn(url).when(repo).save(url);
        String resp = urlShortener.shortenUrl(actualUrl, longUrl);
        assertEquals(shortUrl, resp);
    }

    @Test
    public void shortenUrlFailure() {
        String actualUrl = "http://localhost:8080/shorten";
        String longUrl = "http://www.twitter.com";
        String shortUrl = "http://localhost:8080/a";
        ShortUrl url = new ShortUrl(0L, longUrl, shortUrl);
        Mockito.doReturn(url).when(repo).findByLongUrl(longUrl);
        String resp = urlShortener.shortenUrl(actualUrl, longUrl);
        assertEquals("Error", resp);
    }

    @Test
    public void getUrlSuccess() throws Exception {
        String longUrl = "http://www.twitter.com";
        String shortUrl = "http://localhost:8080/a";
        ShortUrl url = new ShortUrl(0L, longUrl, shortUrl);
        Mockito.doReturn(Optional.of(url)).when(repo).findById(0L);
        String resp = urlShortener.getLongUrl("a");
        assertEquals(longUrl, resp);
    }

    @Test
    public void getUrlFailure() throws Exception {
        Mockito.doReturn(Optional.empty()).when(repo).findById(0L);
        String resp = urlShortener.getLongUrl("a");
        assertEquals("Error", resp);
    }

    @Test
    public void getHistory() throws Exception{
        List<ShortUrl> list = new ArrayList<>();
        ShortUrl obj1 = new ShortUrl(1L, "twitter.com", "a");
        ShortUrl obj2 = new ShortUrl(2L, "facebook.com", "b");
        ShortUrl obj3 = new ShortUrl(3L, "linkedin.com", "c");
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        Mockito.doReturn(list).when(repo).findAll();
        List<ShortUrl> listObtained = urlShortener.getHistory();
        assertEquals(list, listObtained);
    }
}
