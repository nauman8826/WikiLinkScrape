package base;

import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import utils.ReportManager;
import utils.TestUtils;

import java.io.File;


public class BasePage {

    static WebDriver driver;
    String dateTime;
    String test;
    public static TestUtils utils = new TestUtils();
    public static Logger log = LogManager.getLogger(BasePage.class.getName());
    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime2) {
        this.dateTime = dateTime2;
    }

    public BasePage(WebDriver driver2) {
        driver = driver2;
        PageFactory.initElements(driver, this);
    }

    public BasePage() {}

    @BeforeSuite
    public void beforeSuite() {
        ReportManager.initReports("extentReport");
    }

    @BeforeTest
    @Parameters({"browserType", "TestName"})
    public void beforeTest(@Optional("chrome") String browserType, @Optional("ScrapLinkTest") String testName) {
        setDateTime(utils.dateTime());
        String strFile = System.getProperty("user.dir") + "/test-output/logs";
        File logFile = new File(strFile);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        ThreadContext.put("ROUTINGKEY", strFile);
        log.debug("Selecting the browser type");
        Reporter.log("Selecting the browser type");
        try {
            switch (browserType) {
                case "chrome":
                    driver = new ChromeDriver();
                    log.debug("Launching Chrome");
                    Reporter.log("Launching Chrome");
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    log.debug("Launching Firefox");
                    Reporter.log("Launching Firefox");
                    break;
                default:
                    driver = new SafariDriver();
                    log.debug("Launching Safari");
                    Reporter.log("Launching Safari");
                    break;
            }
        } catch (Exception e) {
            log.error("Error during browser launch", e);
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ReportManager.getTest().log(Status.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ReportManager.getTest().log(Status.PASS, result.getTestName());
        } else {
            ReportManager.getTest().log(Status.SKIP, result.getTestName());
        }
    }

    public void navigateToPage(String url) {
        log.debug("Navigating to: %s".formatted(url));
        driver.get(url);
        driver.manage().window().maximize();
    }

    @AfterSuite
    public void afterSuite() {
        log.debug("Closing the browser");
        ReportManager.flushReports();
        Reporter.log("Closing the browser");
        if (driver != null) driver.quit();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public String getTestName() {
        return this.test;
    }

    public void setTestName(String testName) {
        this.test = testName;
    }
}
