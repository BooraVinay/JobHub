package com.jobhunt.finder.controller;


import com.jobhunt.finder.service.WebScrapingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        return "home";
    }
    @GetMapping("/{keyword}")
    public String search(Model model, @PathVariable String keyword, @RequestParam String location) throws IOException, InterruptedException {

        webScrapingService.parseString(keyword,location,1);




        return "home";
    }
    @GetMapping("/aboutus")
    public String aboutUsPage()  {
        return "aboutus";
    }
    @GetMapping("/contactus")
    public String contactUsPage()  {
        return "contactus";
    }
    @PostMapping ("/upload")
    public String uploadPage()  {

        return "upload";
    }
}
