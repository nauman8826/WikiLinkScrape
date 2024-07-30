package testcases;

import base.BasePage;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.WikiPage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.aventstack.extentreports.Status;
import org.testng.annotations.*;
import utils.ReadDataFromFile;
import utils.ReportManager;
import utils.WriteResultToJson;

public class ScrapLinkTest extends BasePage {

    private static final int MAX_LINKS = 10;
    private static final String WIKI_PREFIX = "https://en.wikipedia.org/wiki/";

    @AfterClass
    public void tearDown() {
        ReportManager.flushReports();
    }

    @Test(dataProvider = "readTestData", dataProviderClass = ReadDataFromFile.class)
    public void testWikiLinkScraping(String startUrl, int n) {
        String testParameters = " URL: " + startUrl + " n: " + n;
        ExtentTest extentTest = ReportManager.createTest("Test Case WikiLinkScraping " + testParameters);

        try {
            Assert.assertTrue(isValidWikipediaLink(startUrl), "Invalid Wikipedia link.");
            extentTest.log(Status.PASS, "Validated the Wikipedia link.");

            Set<String> visitedLinks = new HashSet<>();
            List<String> toVisitLinks = new ArrayList<>();
            toVisitLinks.add(startUrl);

            int cycles = 0;

            while (!toVisitLinks.isEmpty() && cycles < n) {
                int linksToVisit = toVisitLinks.size();
                for (int i = 0; i < linksToVisit; i++) {
                    String currentUrl = toVisitLinks.remove(0);
                    if (visitedLinks.contains(currentUrl)) {
                        continue;
                    }
                    if (visitedLinks.size() < MAX_LINKS) {
                        visitedLinks.add(currentUrl);
                        navigateToPage(currentUrl);
                        WikiPage wikiPage = new WikiPage(getDriver());
                        List<WebElement> wikiLinks = wikiPage.getWikiLinks();

                        int count = 0;
                        for (WebElement link : wikiLinks) {
                            String href = link.getAttribute("href");
                            if (!visitedLinks.contains(href) && toVisitLinks.size() < MAX_LINKS) {
                                toVisitLinks.add(href);
                                count++;
                            }
                        }
                    }
                }
                cycles++;
            }
            WriteResultToJson writer = new WriteResultToJson();
            writer.writeResultsToJsonFile(visitedLinks);
            extentTest.log(Status.PASS, "WikiLink scraping test completed successfully.");
        } catch (Exception e) {
            log.error(e.getMessage());
            extentTest.log(Status.FAIL, e.getMessage());
            throw e;
        }
    }

    private boolean isValidWikipediaLink(String url) {
        try {
            new URL(url).toURI();
            return url.startsWith(WIKI_PREFIX);
        } catch (Exception e) {
            return false;
        }
    }
}
