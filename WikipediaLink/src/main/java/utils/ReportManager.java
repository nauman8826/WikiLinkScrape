package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportManager {
    private static ExtentReports extentReports;
    private static ExtentSparkReporter extentSparkReporter;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static void initReports(String reportName) {
        if (extentReports == null) {
            extentSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/htmlReport/" + reportName + ".html")
                    .viewConfigurer()
                    .viewOrder()
                    .as(new ViewName[] {
                            ViewName.DASHBOARD,
                            ViewName.TEST,
                            ViewName.AUTHOR,
                            ViewName.DEVICE,
                            ViewName.EXCEPTION,
                            ViewName.LOG
                    })
                    .apply();

            extentReports = new ExtentReports();
            extentReports.attachReporter(extentSparkReporter);

            extentSparkReporter.config().setDocumentTitle("ScrapeLink Automation Report");
            extentSparkReporter.config().setReportName("Test Report");
            extentSparkReporter.config().setTheme(Theme.DARK);
            extentSparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

            Logger log = LogManager.getLogger(ReportManager.class.getName());
            log.info("Report initialized");
        }
    }

    public static ExtentTest createTest(String testName) {
        ExtentTest test = extentReports.createTest(testName);
        extentTest.set(test);
        return getTest();
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
