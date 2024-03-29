package com.jobhunt.finder.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JobScraper {
    List<Map<String, String>> scrapeData(String url) throws IOException;
    String buildURL(String keyword, String location, int num);
}
