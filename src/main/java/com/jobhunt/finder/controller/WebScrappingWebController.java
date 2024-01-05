package com.jobhunt.finder.controller;


import com.jobhunt.finder.service.WebScrapingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class WebScrappingWebController {

    private final WebScrapingService webScrapingService;


    public WebScrappingWebController(WebScrapingService webScrapingService) {
        this.webScrapingService = webScrapingService;
    }

    @GetMapping("/")
    public String index(Model model) throws IOException, InterruptedException {
        List<Map<String, String>> jobPostings = webScrapingService.urlMethod();
        model.addAttribute("jobPostings", jobPostings);

        // Log the content of jobPostings
        jobPostings.forEach(System.out::println);

        return "index";
    }
}
