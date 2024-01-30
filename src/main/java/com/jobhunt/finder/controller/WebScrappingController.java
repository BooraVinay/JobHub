package com.jobhunt.finder.controller;


/*import com.jobhunt.finder.service.LinkedInScraper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job-postings")
public class WebScrappingController {

    private final LinkedInScraper webScrapingService;


    public WebScrappingController(WebScrapingService webScrapingService) {
        this.webScrapingService = webScrapingService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String,String>>> getAllPostings() {
        try {
            List<Map<String,String>> jobPostings = webScrapingService.urlMethod();
            return new ResponseEntity<>(jobPostings, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();  // Log the exception or handle it based on your application's requirements
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}*/
