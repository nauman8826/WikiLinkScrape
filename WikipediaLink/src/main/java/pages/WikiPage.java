package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import pageObjectRepo.WikiPageLocator;

import java.util.List;

public class WikiPage extends BasePage {

    private WikiPageLocator wikiPageLocator = new WikiPageLocator();
    public WikiPage(WebDriver driver) {
        super(driver);
        wikiPageLocator = new WikiPageLocator();
        PageFactory.initElements(driver, wikiPageLocator);
    }

    public List<WebElement> getWikiLinks() {
        log.debug("Total wiki links: " + wikiPageLocator.wikiLinks.size());
        return wikiPageLocator.wikiLinks;
    }
}
