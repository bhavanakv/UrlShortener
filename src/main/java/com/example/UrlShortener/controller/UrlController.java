package com.example.UrlShortener.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.UrlShortener.common.URLValidator;
import com.example.UrlShortener.repository.ShortUrl;
import com.example.UrlShortener.service.UrlShortenerService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
    
    @PostMapping(value = "/shorten",consumes = "application/json") 
    public ResponseEntity<String> shortenUrl(@RequestBody ShortenRequest shortenRequest, HttpServletRequest request) {
        String longUrl = shortenRequest.getUrl();
        if(validator.isValidUrl(longUrl)) {
            String localUrl = request.getRequestURL().toString();
            String shortUrl = urlShortner.shortenUrl(localUrl, longUrl);
            logger.debug("Short url: {} from long url: {}", shortUrl, longUrl);
            if(shortUrl.equals("Error"))
                return new ResponseEntity<String>("Url already exists", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<String>(shortUrl, HttpStatus.CREATED);
        }
        logger.error("Url validation failed");
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
    public ResponseEntity<String> error() {
        return new ResponseEntity<String>("Could not fetch url with that id",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/history")
    public ResponseEntity<Object> getHistory() {
        List<ShortUrl> list = urlShortner.getHistory();
        if (list == null || list.isEmpty()) {
            return new ResponseEntity<>("No requests stored in the database", HttpStatus.OK);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    public static class ShortenRequest {

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
