package com.jobhunt.finder.controller;


import com.jobhunt.finder.service.LinkedInScraper;
import com.jobhunt.finder.service.NaukariScraper;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
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

        CompletableFuture<List<Map<String, String>>> naukariJobPostingsFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return naukariScraper.scrapeData(naukariScraper.buildURL("SoftwareEngineer", "India", 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Combine the results of linkedJobPostingFuture and naukariJobPostingsFuture
        CompletableFuture<List<Map<String, String>>> combinedResults = linkedJobPostingFuture.thenCombine(naukariJobPostingsFuture, (linked, naukari) -> {
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
            return "home"; // Return an error view
        });
    }



    @GetMapping("/{keyword}")
    public String search(Model model, @PathVariable String keyword, @RequestParam(defaultValue = "India") String location,@RequestParam(defaultValue = "0") int num) throws IOException {
        model.addAttribute("jobPostings",naukariScraper.scrapeData(naukariScraper.buildURL(keyword,location,num)));
        return "home";
    }

    @HxRequest
    @PostMapping("/filter/jobs/location")
    public CompletableFuture<HtmxResponse> getLocationBased(@RequestParam("location") String location) {

        CompletableFuture<List<Map<String, String>>> linkedJobPostingFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return linkedInScraper.scrapeData(linkedInScraper.buildURL("SoftwareEngineer", location, 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<List<Map<String, String>>> naukariJobPostingsFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return naukariScraper.scrapeData(naukariScraper.buildURL("SoftwareEngineer", location, 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Combine the results of linkedJobPostingFuture and naukariJobPostingsFuture
        CompletableFuture<List<Map<String, String>>> combinedResults = linkedJobPostingFuture.thenCombine(naukariJobPostingsFuture, (linked, naukari) -> {
            List<Map<String, String>> combinedList = new ArrayList<>(linked);
            combinedList.addAll(naukari);
            return combinedList;
        });

        // Return a CompletableFuture<String> that completes when combinedResults is done
        return combinedResults.thenApply(combinedList -> {
            // Add the results to the model
            //model.addAttribute("jobPostings", combinedList);

            // Return the view name
            return HtmxResponse
                    .builder()
                    .view(new ModelAndView("jobcards :: jobPostings", Map.of("jobPostings", combinedList)))
                    .build();
        }).exceptionally(ex -> {
            // Handle exceptions here
            log.error("" + ex);
            return null;
        });

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
