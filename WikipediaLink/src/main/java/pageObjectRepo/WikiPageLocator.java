package pageObjectRepo;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class WikiPageLocator{

    @FindBy(xpath = "//a[starts-with(@href, '/wiki/') and not(contains(@href, ':'))]")
    public List<WebElement> wikiLinks;

}
