package com.milvik.mip.loginlogout.testrunner;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.dataprovider.MIP_Login_TestData;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_Logout_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;

	@BeforeTest
	@Parameters({ "browser" })
	public void test_setup(@Optional("firefox") String browser) {
		log = MIP_Logging.logDetails("MIP_Logout_Test");
		report = new ExtentReports(".\\Test_Reports\\Logout_Test.html");
		try {
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
		} catch (Exception e) {
			log.info("Exception while killing exe");
		}
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase();
		driver = MIP_BrowserFactory.openBrowser(driver, browser);

	}

	@BeforeMethod
	public void startTest() {
		try {
			MIP_LaunchApplication.openApplication(driver);
		} catch (Exception e) {

		}

	}

	@Test(testName = "TC217")
	public void logoutTestOne() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC217");
			log.info("Running test case - TC217");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickonLogout();
			Assert.assertTrue(loginpage.userid.isDisplayed());
			Assert.assertTrue(loginpage.password.isDisplayed());
			Assert.assertTrue(loginpage.login_button.isDisplayed());
			Assert.assertTrue(loginpage.clear_button.isDisplayed());
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.userid.getAttribute("type"), "text");
			Assert.assertEquals(loginpage.password.getTagName(), "input");
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.forgotPassword_link.getTagName(), "a");
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC217  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(testName = "TC218", dataProvider = "logoutdata", dataProviderClass = MIP_Login_TestData.class)
	public void logoutTestTwo(String menu) throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC218");
			log.info("Running test case - TC218");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(menu);
			homepage.clickonLogout();
			Assert.assertTrue(loginpage.userid.isDisplayed());
			Assert.assertTrue(loginpage.password.isDisplayed());
			Assert.assertTrue(loginpage.login_button.isDisplayed());
			Assert.assertTrue(loginpage.clear_button.isDisplayed());
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.userid.getAttribute("type"), "text");
			Assert.assertEquals(loginpage.password.getTagName(), "input");
			Assert.assertTrue(loginpage.forgotPassword_link.isDisplayed());
			Assert.assertEquals(loginpage.forgotPassword_link.getTagName(), "a");
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC218  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@AfterMethod
	public void after_test(ITestResult res) {
		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(driver, res.getName());
			logger.log(LogStatus.FAIL, "Test Failed");
		}
	}

	@AfterTest(alwaysRun = true)
	public void tear_down() {
		MIP_BrowserFactory.closeDriver(driver);
		report.endTest(logger);
		report.flush();
	}
}
