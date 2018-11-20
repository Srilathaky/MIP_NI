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

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.dataprovider.MIP_Login_TestData;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
import com.milvik.mip.listeners.MIP_RetryAnalyzer;
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

public class MIP_Login_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;

	@BeforeTest
	@Parameters({ "browser" })
	public void test_setup(@Optional("firefox") String browser) {
		log = MIP_Logging.logDetails("MIP_Login_Test");
		report = new ExtentReports(".\\Test_Reports\\Login_Test.html");
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

	@Test(testName = "TC01")
	public void loginTestOne() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC01");
			log.info("Running test case - TC01");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
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
			log.info("Testcase TC01  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(testName = "TC02")
	public void loginTestTwo() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC02");
			log.info("Running test case - TC02");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés");
			Assert.assertTrue(homepage.validateHomePageNavigation());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC02  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(testName = "TC03")
	public void loginTestThree() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC03");
			log.info("Running test case - TC03");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			loginpage.userid.sendKeys(MIP_ReadPropertyFile
					.getPropertyValue("username"));
			loginpage.password.sendKeys(MIP_ReadPropertyFile
					.getPropertyValue("password"));
			loginpage.clickOnClear();
			Assert.assertEquals(loginpage.userid.getText(), "");
			Assert.assertEquals(loginpage.password.getText(), "");
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC03  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC04 to TC08", dataProvider = "invalid_login", dataProviderClass = MIP_Login_TestData.class, retryAnalyzer = MIP_RetryAnalyzer.class)
	public void loginTestFour(String username, String password, String errormsg)
			throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC04 to TC08");
			log.info("Running test case - TC04 to TC08");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			loginpage.login(username, password);
			if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
				System.out.println(errormsg);

				Assert.assertTrue((loginpage.getAlertText()
						.equalsIgnoreCase(errormsg.trim())));
			} else {
				Assert.assertTrue(loginpage.getErrorMsg(errormsg).trim()
						.equalsIgnoreCase(errormsg.trim()));
			}
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC04 to TC08  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC09", dataProvider = "useaccount_block", dataProviderClass = MIP_Login_TestData.class)
	public void loginTestFive(String username, String password, String errormsg)
			throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC09");
			log.info("Running test case - TC09");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			for (int i = 0; i <= MIP_Constants.MAX_LOGIN_ATTEMPTS; i++) {
				loginpage.login(username, password);
			}
			String res_one = errormsg.replaceAll("\\s", "").trim();
			String res_two = loginpage
					.getErrorMsg(loginpage.getErrorMsg(errormsg))
					.replaceAll("\\s", "").trim();
			System.out.println(res_one);
			System.out.println(res_two);
			Assert.assertTrue(res_one.equalsIgnoreCase(res_two));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC09  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC10", dataProvider = "login_blockAccount", dataProviderClass = MIP_Login_TestData.class)
	public void loginTestSix(String username, String password, String errormsg)
			throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Login-TC10");
			log.info("Running test case - TC010");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			loginpage.login(username, password);
			String res_one = errormsg.replaceAll("\\s", "").trim();
			String res_two = loginpage
					.getErrorMsg(loginpage.getErrorMsg(errormsg))
					.replaceAll("\\s", "").trim();
			Assert.assertTrue(res_one.equalsIgnoreCase(res_two));
			Assert.assertEquals(MIP_Constants.BLOCKED_ACCOUNT_STATUS,
					MIP_AdminConfig_Queries.getUserStatus(username));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC10  Failed");
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
