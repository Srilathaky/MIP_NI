package com.milvik.mip.passwordmanagement;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_ChangePassword_TestData;
import com.milvik.mip.dbqueries.MIP_ChangePassword_Queries;
import com.milvik.mip.pageobjects.MIP_ChangePasswordPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_ChangePassword_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	String testcasename = "";

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser, @Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_ChangePassword_Test");
		report = new ExtentReports(".\\Test_Reports\\ChangePassword_Test.html");
		if (flag.equals("0")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			} catch (Exception e) {
				log.info("No exe found");
			}
			MIP_Test_Configuration.driver = MIP_BrowserFactory.openBrowser(MIP_Test_Configuration.driver, browser);

			MIP_ReadPropertyFile.loadProperty("config");
			MIP_DataBaseConnection.connectToDatabase();
			MIP_LaunchApplication.openApplication(MIP_Test_Configuration.driver);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_LoginPage.class);
			homepage = loginpage.login(MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		}
	}

	@Test(priority = 0, testName = "TC211")
	public void changePassOne() throws Throwable {
		MIP_ChangePasswordPage changepasspage = null;
		testcasename = "MIP_webPortal_TC211_ChangePassword";
		try {
			logger = report.startTest("ChangePassword-TC211");
			log.info("Running test case - TC211");
			changepasspage = new MIP_ChangePasswordPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Validating Change Password Page Objects");
			Assert.assertTrue(changepasspage.validateChangePassObjects());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		} catch (Throwable t) {
			log.info("Testcase TC211  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC213", dataProvider = "ChangePAsswordData", dataProviderClass = MIP_ChangePassword_TestData.class)
	public void changePassTwo(String username, String password, String oldpass, String newpass, String confpass,
			String errmsg) throws Throwable {
		MIP_ChangePasswordPage changepasspage = null;
		testcasename = "MIP_webPortal_TC213_ChangePassword";
		try {
			logger = report.startTest("Change Password-TC213");
			log.info("Running test case - TC213");
			changepasspage = new MIP_ChangePasswordPage(MIP_Test_Configuration.driver);
			changepasspage.enterPassworddata(oldpass, newpass, confpass).clickOnClear();
			logger.log(LogStatus.INFO, "Validating Clear Button Functionality in Change Password Page");
			Assert.assertTrue(changepasspage.getOldPassword().equals(""));
			Assert.assertTrue(changepasspage.getNewPassword().equals(""));
			Assert.assertTrue(changepasspage.getConfPassword().equals(""));
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		} catch (Throwable t) {
			log.info("Testcase TC213  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC214-TC215,TC216", dataProvider = "negativeTestData", dataProviderClass = MIP_ChangePassword_TestData.class)
	public void changePassThree(String testcase, String oldpass, String newpass, String confpass, String errmsg)
			throws Throwable {
		MIP_ChangePasswordPage changepasspage = null;
		testcasename = "MIP_webPortal_" + testcase + "_ChangePassword";
		try {
			logger = report.startTest("Change Password-TC214-TC215,TC216");
			log.info("Running test case - " + testcase);
			changepasspage = new MIP_ChangePasswordPage(MIP_Test_Configuration.driver);
			Thread.sleep(1000);
			changepasspage.enterPassworddata(oldpass.trim(), newpass.trim(), confpass.trim()).clickOnSave();
			logger.log(LogStatus.INFO, "Validating Error Message for Field validation in Change Password Page");
			Assert.assertTrue(changepasspage.getValidationMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		} catch (Throwable t) {
			log.info("Testcase " + testcase + "  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC212", dataProvider = "ChangePAsswordData", dataProviderClass = MIP_ChangePassword_TestData.class)
	public void changePassFour(String username, String password, String oldpass, String newpass, String confpass,
			String errmsg) throws Throwable {
		MIP_ChangePasswordPage changepasspage = null;
		testcasename = "MIP_webPortal_TC212_ChangePassword";
		try {
			logger = report.startTest("Change Password-TC212");
			log.info("Running test case - TC212");
			homepage.clickonLogout();
			homepage = loginpage.login(username, password);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
			String beforepass = MIP_ChangePassword_Queries.getPasswordDetails(username);
			changepasspage = new MIP_ChangePasswordPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Validating Change Password Functionality in Change Password Page");
			changepasspage.enterPassworddata(oldpass, newpass, confpass).clickOnSave().confirmChangePass("yes");
			Assert.assertTrue(changepasspage.getSuccessMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.INFO, "Validating the changed password details aganist Database");
			String afterpass = MIP_ChangePassword_Queries.getPasswordDetails(username);
			Assert.assertNotEquals(beforepass.trim(), afterpass.trim());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		} catch (Throwable t) {
			log.info("Testcase TC212  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {
		if (res.getStatus() == ITestResult.FAILURE) {
			String path = MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName() + "_" + testcasename);
			logger.log(LogStatus.FAIL, "----Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		} else {
			logger.log(LogStatus.PASS, testcasename + "----Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		else
			homepage.clickOnMenu(MIP_Menu_Constants.CHANGE_PASSWORD);
		report.endTest(logger);
		report.flush();
	}
}
