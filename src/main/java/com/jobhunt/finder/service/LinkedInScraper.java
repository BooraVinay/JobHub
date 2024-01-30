// LinkedInScraper.java
package com.jobhunt.finder.service;

import com.jobhunt.finder.utilities.Utilities;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class LinkedInScraper implements JobScraper {

    @Override
    @Cacheable("linkedInDataCache")
    public List<Map<String, String>> scrapeData(String URL) throws IOException {
        Document document = null;

        try {
            Connection connection = Jsoup.connect(URL);
            connection.header("User-Agent", Utilities.getRandomUserAgent());
            connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.header("Accept-Language", "en-US,en;q=0.9");
            connection.header("Connection", "keep-alive");
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select("[data-entity-urn^='urn:li:jobPosting']");
        List<Map<String, String>> data = new ArrayList<>();

        for (Element element : elements) {
            Map<String, String> details = new HashMap<>();
            details.put("Job Title:", element.select("[class='base-search-card__title']").text());
            details.put("Company Name:", element.select("[class='base-search-card__subtitle']").text());
            details.put("Job Location:", element.select("[class='job-search-card__location']").text());
            details.put("Posting DateTime:", element.select("[class='job-search-card__listdate']").attr("datetime"));
            details.put("Posting Date:", element.select("[class='job-search-card__listdate']").text());

            String jobUrl = element.select("a.base-card__full-link").attr("href");
            details.put("jobUrl", jobUrl);
            data.add(details);
        }

        return data;
    }

    @Override
    public String buildURL(String keyword, String location, int num) {
        return "https://www.linkedin.com/jobs/search?keywords=" + keyword +
                "&location=" + location + "&pageNum=" + num;
    }


}
