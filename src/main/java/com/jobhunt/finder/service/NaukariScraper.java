package com.jobhunt.finder.service;

import com.jobhunt.finder.utilities.Utilities;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NaukariScraper implements JobScraper {

    @Override
    @Cacheable("tauriDataCache")
    public List<Map<String, String>> scrapeData(String URL) throws IOException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--user-agent=" + Utilities.getRandomUserAgent());
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(chromeOptions);
        try {
            driver.get(URL);

            // Add implicit wait (wait for up to 10 seconds for an element to be present)
            driver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS);

            // Add explicit wait (FluentWait) for the page to be ready

            // Use FluentWait for waiting
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofSeconds(5))
                    .ignoring(org.openqa.selenium.NoSuchElementException.class);

            // Wait for an element with the specified selector to be present on the page
            WebElement firstElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class='srp-jobtuple-wrapper']")));
            // Wait for an element to be present on the page


            // Wait for the page to be in a complete state
            waitForPageLoad(driver);

            List<Map<String, String>> data = new ArrayList<>();

            List<WebElement> elements = driver.findElements(By.cssSelector("[class='srp-jobtuple-wrapper']"));
            log.info("Elements {}", elements);

            for (WebElement element : elements) {
                Map<String, String> details = new HashMap<>();
                details.put("Job Title:", element.findElement(By.cssSelector("div>[class=' row1']")).getText());
                details.put("Company Name:", element.findElement(By.cssSelector("div>[class=' row2']>span>a")).getText());
                details.put("Job Location:", element.findElement(By.cssSelector("div>[class=' row3']>div>span:nth-child(3)>span")).getText());
                details.put("Posting Date:", element.findElement(By.cssSelector("div>[class=' row6']>span")).getText());

                String jobUrl = element.findElement(By.cssSelector("[class=' row1']>a")).getAttribute("href");
                details.put("jobUrl", jobUrl);
                data.add(details);
            }

            log.info("{}", data);
            return data;
        } finally {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, new File("screenshot.png"));

            // Quit the driver
            driver.quit();
        }
    }

    @Override
    public String buildURL(String keyword, String location, int num) {
        return "https://www.naukri.com/" + keyword + "-jobs-in-" + location+"-"+num;
    }

    private void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
}
