package login_multiuser;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestConfig {
	// public WebDriver driver;
	static ExtentReports report;
	static ExtentTest logger;

	/*
	 * @BeforeMethod public void beforetest() { this.driver =
	 * MIP_BrowserFactory.openBrowser(this.driver, "chrome");
	 * MIP_ReadPropertyFile.loadProperty("config");
	 * MIP_LaunchApplication.openApplication(this.driver); }
	 * 
	 * @AfterMethod(alwaysRun = true) public void tear_down() {
	 * this.driver.quit(); report.endTest(logger); report.flush();
	 * 
	 * }
	 */

}
