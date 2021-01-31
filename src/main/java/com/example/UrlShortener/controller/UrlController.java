package com.example.UrlShortener.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.UrlShortener.common.URLValidator;
import com.example.UrlShortener.repository.ShortUrl;
import com.example.UrlShortener.service.UrlShortenerService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UrlController {
    
    @Autowired
    private UrlShortenerService urlShortner;

    URLValidator validator = new URLValidator();
    
    @PostMapping(value = "/shorten",consumes = "application/json") 
    public ResponseEntity<String> shortenUrl(@RequestBody ShortenRequest shortenRequest, HttpServletRequest request) {
        String longUrl = shortenRequest.getUrl();
        if(validator.isValidUrl(longUrl)) {
            String localUrl = request.getRequestURL().toString();
            String shortUrl = urlShortner.shortenUrl(localUrl, longUrl);
            return new ResponseEntity<String>(shortUrl, HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("Please enter valid URL", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public RedirectView longUrl(@PathVariable String id, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String fullUrl = urlShortner.getLongUrl(id);
        RedirectView view = new RedirectView();
        if(!fullUrl.equals("Error")) {
            view.setUrl(fullUrl);
        }
        else {
            view.setUrl("http://localhost:8080/error");
        }
        return view;
    }

    @GetMapping("/error")
    public String error() {
        return "Could not fetch url with that id";
    }

    @GetMapping("/history")
    public List<ShortUrl> getHistory() {
        return urlShortner.getHistory();
    }

    private static class ShortenRequest {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @JsonCreator
        public ShortenRequest() {

        }

        @JsonCreator
        public ShortenRequest(@JsonProperty String url) {
            this.url = url;
        }
        
    }
}
