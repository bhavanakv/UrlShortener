package com.example.UrlShortener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.UrlShortener.controller.UrlController;
import com.example.UrlShortener.controller.UrlController.ShortenRequest;
import com.example.UrlShortener.repository.ShortUrl;
import com.example.UrlShortener.service.UrlShortenerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@AutoConfigureDataJpa
public class UrlControllerTest{

    @Mock
    UrlShortenerService urlShortener;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    UrlController urlController;

    @Test
    public void getHistory() throws Exception{
        List<ShortUrl> list = new ArrayList<>();
        ShortUrl obj1 = new ShortUrl(1L, "twitter.com", "a");
        ShortUrl obj2 = new ShortUrl(2L, "facebook.com", "b");
        ShortUrl obj3 = new ShortUrl(3L, "linkedin.com", "c");
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        Mockito.when(urlShortener.getHistory()).thenReturn(list);
        ResponseEntity<Object> resp = urlController.getHistory();
        assertEquals(list, resp.getBody());  
        assertEquals(HttpStatus.OK, resp.getStatusCode());       
    }

    @Test
    public void getHistoryNullList() throws Exception {
        Mockito.when(urlShortener.getHistory()).thenReturn(null);
        ResponseEntity<Object> resp = urlController.getHistory();
        assertEquals("No requests stored in the database",resp.getBody());
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void getHistoryEmptyList() throws Exception {
        List<ShortUrl> list = new ArrayList<>();
        Mockito.when(urlShortener.getHistory()).thenReturn(list);
        ResponseEntity<Object> resp = urlController.getHistory();
        assertEquals("No requests stored in the database",resp.getBody());
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void shortenUrlSuccess() {
        String actualUrl = "http://localhost:8080/shorten";
        String longUrl = "http://www.twitter.com";
        String shortUrl = "http://localhost:8080/a";
        ShortenRequest req = new ShortenRequest("http://www.twitter.com");
        StringBuffer localUrl = new StringBuffer(actualUrl);
        Mockito.when(request.getRequestURL()).thenReturn(localUrl);
        Mockito.when(urlShortener.shortenUrl(actualUrl, longUrl)).thenReturn(shortUrl);
        ResponseEntity<String> resp = urlController.shortenUrl(req, request);
        assertEquals(shortUrl, resp.getBody());
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    }

    @Test
    public void shortenUrlFailure() {
        String actualUrl = "http://localhost:8080/shorten";
        String longUrl = "http://www.twitter.com";
        ShortenRequest req = new ShortenRequest("http://www.twitter.com");
        StringBuffer localUrl = new StringBuffer(actualUrl);
        Mockito.when(request.getRequestURL()).thenReturn(localUrl);
        Mockito.when(urlShortener.shortenUrl(actualUrl, longUrl)).thenReturn("Error");
        ResponseEntity<String> resp = urlController.shortenUrl(req, request);
        assertEquals("Url already exists", resp.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    public void shortenUrlValidationFail() {
        ShortenRequest req = new ShortenRequest("://www.twitter.com");
        ResponseEntity<String> resp = urlController.shortenUrl(req, request);
        assertEquals("Please enter valid URL", resp.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());

        req = new ShortenRequest("http://.twitter.com");
        resp = urlController.shortenUrl(req, request);
        assertEquals("Please enter valid URL", resp.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());

        req = new ShortenRequest("http:www.twitter.com");
        resp = urlController.shortenUrl(req, request);
        assertEquals("Please enter valid URL", resp.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    public void getLongUrlSuccess() throws Exception {
        String longUrl = "http://www.twitter.com";
        Mockito.when(urlShortener.getLongUrl("1")).thenReturn(longUrl);
        RedirectView view = urlController.longUrl("1", request, response);
        assertEquals(longUrl, view.getUrl());
    }

    @Test
    public void getLongUrlFailure() throws Exception {
        String longUrl = "http://localhost:8080/error";
        Mockito.when(urlShortener.getLongUrl("1")).thenReturn("Error");
        RedirectView view = urlController.longUrl("1", request, response);
        assertEquals(longUrl, view.getUrl());
    }
}
