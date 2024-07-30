package utils;
import base.BasePage;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.io.File;
import java.io.IOException;

public class TestUtils extends BasePage {

	private static final Logger logger = LogManager.getLogger(TestUtils.class);
	private static String screenshotName;
	public static String relativePath;

	public static void captureScreenshot() throws IOException {
		File screenshotFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

		// Generate a unique screenshot name with a timestamp
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		screenshotName = "screenshot_" + timestamp + ".jpg";

		String strFile = System.getProperty("user.dir") + "/test-output/screenshots/";
		File screenShotFile = new File(strFile);
		if (!screenShotFile.exists()) {
			screenShotFile.mkdirs();
		}
		// Use relative path for the report
		relativePath = strFile + screenshotName;
		File destinationFile = new File(relativePath);

		try {
			FileUtils.copyFile(screenshotFile, destinationFile);
			logger.debug("Screenshot saved: " + destinationFile.getAbsolutePath());
		} catch (IOException e) {
			logger.error("Error saving screenshot: " + e.getMessage());
			throw e;
		}
	}

	public String dateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}