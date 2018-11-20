package com.milvik.mip.adminconfig.testrunner;

import java.util.Map;

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
import com.milvik.mip.dataprovider.MIP_AdminConfiguration_TestData;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
import com.milvik.mip.pageobjects.MIP_AdminConfigurationPage;
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

public class MIP_AdminConfiguration_Test {
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
		log = MIP_Logging.logDetails("MIP_AdminConfig_Test");
		report = new ExtentReports(".\\Test_Reports\\AdminConfig_Test.html");
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
			homepage.changeLanguage("Ingl�s");
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Ingl�s");
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
		}
	}

	@Test(testName = "TC202")
	public void adminConfigOne() throws Throwable {
		MIP_AdminConfigurationPage adminconfig = null;
		testcasename = "MIP_webPortal_TC202 Admin Configuration";
		try {
			logger = report.startTest("Admin Configuration-TC202");
			log.info("Running test case -TC202");
			adminconfig = new MIP_AdminConfigurationPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Validating Admin Configuration Objects");
			Assert.assertTrue(adminconfig.validateAdminConfigObjects());
			//logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
			log.info("TC202 Test Failed");
			log.info("Error while executing test case-TC202 " + t);
			throw t;
		}
	}

	@Test(testName = "TC203", dataProvider = "adminconfigdata", dataProviderClass = MIP_AdminConfiguration_TestData.class)
	public void adminConfigTwo(String password, String loginid, String passhist, String loginattempt, String idlestate,
			String announce, String echangerate, String msg) throws Throwable {
		MIP_AdminConfigurationPage adminconfig = null;
		testcasename = "MIP_webPortal_TC203 Admin Configuration";
		try {
			logger = report.startTest("Admin Configuration-TC203");
			log.info("Running test case -TC203");
			adminconfig = new MIP_AdminConfigurationPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Editing  Admin Configuration Values");
			adminconfig.editAdminConfigInfo(password, loginid, passhist, loginattempt, idlestate, announce, echangerate)
					.clickOnSave();
			Assert.assertTrue(adminconfig.getSuccessMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(msg.trim().replaceAll("\\s", "")));
			adminconfig.gotoAdminConfigPage();
			logger.log(LogStatus.INFO, "Validating the result aganist Database");
			Map<String, String> admindetils = MIP_AdminConfig_Queries.getAdminConfigInfo();
			Assert.assertTrue(admindetils.get("default_password").trim()
					.equalsIgnoreCase(adminconfig.getAdminConfigInfo("default_password").trim()));
			Assert.assertTrue(admindetils.get("user_login_prefix").trim()
					.equalsIgnoreCase(adminconfig.getAdminConfigInfo("user_login_prefix").trim()));
			Assert.assertTrue(admindetils.get("password_history_limit").trim()
					.equalsIgnoreCase(adminconfig.getAdminConfigInfo("password_history_limit").trim()));
			Assert.assertTrue(admindetils.get("max_login_attempts").trim()
					.equalsIgnoreCase(adminconfig.getAdminConfigInfo("max_login_attempts").trim()));
			Assert.assertTrue(admindetils.get("max_idle_count").trim()
					.equalsIgnoreCase(adminconfig.getAdminConfigInfo("max_idle_count").trim()));
			Assert.assertTrue(admindetils.get("announcement_message").trim()
					.equalsIgnoreCase(adminconfig.getAdminConfigInfo("announcement_message").trim()));
			Assert.assertTrue(admindetils.get("doller_value").trim()
					.equalsIgnoreCase(adminconfig.getAdminConfigInfo("doller_value").trim()));
			//logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
			log.info("TC203 Test Failed");
			log.info("Error while executing test case-TC203 " + t);
			throw t;
		}
	}

	@Test(testName = "TC204 to 210", dataProvider = "adminconfignegativecases", dataProviderClass = MIP_AdminConfiguration_TestData.class)
	public void adminConfigThree(String testcase, String fields, String value, String errmsg) throws Throwable {
		MIP_AdminConfigurationPage adminconfig = null;
		testcasename = "MIP_webPortal_TC204 to TC210_Admin Configuration";
		try {
			logger = report.startTest("Admin Configuration-TC204 to 210");
			log.info("Running test case -" + testcase);
			logger.log(LogStatus.INFO, "Mandatory field:" + fields + " validation in Admin Configuration page");
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
			Thread.sleep(1000);
			adminconfig = new MIP_AdminConfigurationPage(MIP_Test_Configuration.driver);
			if (fields.equalsIgnoreCase("all")) {
				adminconfig.clearAdminConfigInfo("default_password", value);
				adminconfig.clearAdminConfigInfo("user_login_prefix", value);
				adminconfig.clearAdminConfigInfo("password_history_limit", "");
				adminconfig.clearAdminConfigInfo("max_login_attempts", value);
				adminconfig.clearAdminConfigInfo("max_idle_count", value);
				adminconfig.clearAdminConfigInfo("announcement_message", value);
				adminconfig.clearAdminConfigInfo("doller_value", value);
				adminconfig.clickOnSave();
				System.out.println(adminconfig.getValidationMsg().trim().replaceAll("\\s", ""));
				System.out.println("----------------------------------------------------------");
				System.out.println(errmsg.trim().replaceAll("\\s", ""));
				// Thread.sleep(1000);
				Assert.assertTrue(adminconfig.getValidationMsg().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));

			}
			if (fields.trim().equalsIgnoreCase("default_password")) {
				adminconfig.clearAdminConfigInfo("default_password", value);
				adminconfig.clickOnSave();
				Assert.assertTrue(adminconfig.getValidationMsg().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}
			if (fields.trim().equalsIgnoreCase("user_login_prefix")) {
				adminconfig.clearAdminConfigInfo("user_login_prefix", value);
				adminconfig.clickOnSave();
				Assert.assertTrue(adminconfig.getValidationMsg().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}
			if (fields.trim().equalsIgnoreCase("password_history_limit")) {
				adminconfig.clearAdminConfigInfo("password_history_limit", value);
				adminconfig.clickOnSave();
				Assert.assertTrue(adminconfig.getValidationMsg().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}
			if (fields.trim().equalsIgnoreCase("max_login_attempts")) {
				adminconfig.clearAdminConfigInfo("max_login_attempts", value);
				adminconfig.clickOnSave();
				Assert.assertTrue(adminconfig.getValidationMsg().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}
			if (fields.trim().equalsIgnoreCase("max_idle_count")) {
				adminconfig.clearAdminConfigInfo("max_idle_count", value);
				adminconfig.clickOnSave();
				Assert.assertTrue(adminconfig.getValidationMsg().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}
			if (fields.trim().equalsIgnoreCase("announcement_message")) {
				adminconfig.clearAdminConfigInfo("announcement_message", value);
				adminconfig.clickOnSave();
				Assert.assertTrue(adminconfig.getValidationMsg().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}
			//logger.log(LogStatus.PASS, "Test passed");

		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
			log.info(testcase + " Test Failed");
			log.info("Error while executing test case- " + testcase + t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADMIN_CONFIG);
		report.endTest(logger);
		report.flush();
	}
}
