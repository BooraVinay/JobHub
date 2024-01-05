package com.jobhunt.finder.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
public class WebScrapingService {

    public List<Map<String,String>> scrapeQuotes(String URL) throws IOException {
        Document document = null;

        try {
            Connection connection = Jsoup.connect(URL);
            connection.header("User-Agent", getRandomUserAgent());
            connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.header("Accept-Language", "en-US,en;q=0.9");
            connection.header("Connection", "keep-alive");
            document = connection.get();
        } catch (IOException e) {

            e.printStackTrace();
        }

        Elements jobElements = document.select("[data-entity-urn^='urn:li:jobPosting']");
        List<Map<String,String>> jobPostings=new ArrayList<>();
        for(Element jobElement:jobElements) {
            Map<String,String> jobDetails=new HashMap<>();
            jobDetails.put("Job Title:",jobElement.select("[class='base-search-card__title']").text());

            jobDetails.put("Company Name:",jobElement.select("[class='base-search-card__subtitle']").text());
            jobDetails.put("Job Location:",jobElement.select("[class='job-search-card__location']").text());
            jobDetails.put("Posting DateTime:",jobElement.select("[class='job-search-card__listdate']").attr("datetime"));
            jobDetails.put("Posting Date:",jobElement.select("[class='job-search-card__listdate']").text());

            String jobUrl = jobElement.select("a.base-card__full-link").attr("href");
            jobDetails.put("jobUrl", jobUrl);
           jobPostings.add(jobDetails);
        }
        return jobPostings ;
    }

    public List<Map<String,String>> urlMethod() throws IOException, InterruptedException {
        List<Map<String,String>> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            String URL = "https://www.linkedin.com/jobs/search?keywords=&location=india&geoId=&trk=public_jobs_jobs-search-bar_search-submit&original_referer=https%3A%2F%2Fwww.linkedin.com%2Fjobs%2Fsearch%3Ftrk%3Dguest_homepage-basic_guest_nav_menu_jobs%26position%3D1%26pageNum%3D0&position=1&pageNum=" + i;
            data.addAll(scrapeQuotes(URL));
        }
        return data;
    }
    private static String getRandomUserAgent() {
        String[] userAgents = {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36",
                "Mozilla/5.0 (X11; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0",
                "Mozilla/5.0 (X11; Linux x86_64; rv:95.0) Gecko/20100101 Firefox/95.0",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edg/91.0.864.48",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edg/93.0.961.52",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edg/94.0.992.31",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edg/95.0.1020.44",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15",

        };

        int randomIndex = new Random().nextInt(userAgents.length);
        return userAgents[randomIndex];
    }
}
