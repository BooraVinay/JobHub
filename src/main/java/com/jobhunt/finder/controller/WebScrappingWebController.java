package com.jobhunt.finder.controller;


import com.jobhunt.finder.service.LinkedInScraper;
import com.jobhunt.finder.service.NaukariScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
public class WebScrappingWebController {

    private final LinkedInScraper linkedInScraper;
    private final NaukariScraper naukariScraper;


    @GetMapping("/")
    public CompletableFuture<String> home(Model model) {
        CompletableFuture<List<Map<String, String>>> linkedJobPostingFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return linkedInScraper.scrapeData(linkedInScraper.buildURL("SoftwareEngineer", "India", 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<List<Map<String, String>>> naukarijobPostingsFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return naukariScraper.scrapeData(naukariScraper.buildURL("SoftwareEngineer", "India", 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Combine the results of linkedJobPostingFuture and naukarijobPostingsFuture
        CompletableFuture<List<Map<String, String>>> combinedResults = linkedJobPostingFuture.thenCombine(naukarijobPostingsFuture, (linked, naukari) -> {
            List<Map<String, String>> combinedList = new ArrayList<>(linked);
            combinedList.addAll(naukari);
            return combinedList;
        });

        // Return a CompletableFuture<String> that completes when combinedResults is done
        return combinedResults.thenApply(combinedList -> {
            // Add the results to the model
            model.addAttribute("jobPostings", combinedList);

            // Return the view name
            return "home";
        }).exceptionally(ex -> {
            // Handle exceptions here
            model.addAttribute("error", ex.getMessage());
            return "error"; // Return an error view
        });
    }



    @GetMapping("/{keyword}")
    public String search(Model model, @PathVariable String keyword, @RequestParam(defaultValue = "India") String location,@RequestParam(defaultValue = "0") int num) throws IOException, InterruptedException {

        model.addAttribute("jobPostings",naukariScraper.scrapeData(naukariScraper.buildURL(keyword,location,num)));
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
