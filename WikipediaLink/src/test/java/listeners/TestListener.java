package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import utils.ReportManager;
import utils.TestUtils;
import java.io.IOException;

import static utils.TestUtils.relativePath;

public class TestListener implements ITestListener {
	TestUtils utils = new TestUtils();

	@Override
	public void onTestStart(ITestResult result) {
		ITestListener.super.onTestStart(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ITestListener.super.onTestSuccess(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.setProperty("org.uncommons.reportng.escape-output", "false");

		try {
			TestUtils.captureScreenshot();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExtentTest test = ReportManager.getTest();
		if (test != null) {
			test.fail("Test failed", MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
		} else {
			System.out.println("ExtentTest is null. Please ensure that createTest is called correctly.");
		}
		ITestListener.super.onTestFailure(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ITestListener.super.onTestSkipped(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		ReportManager.initReports("TestReport");
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		ReportManager.flushReports();
		ITestListener.super.onFinish(context);
	}
}
