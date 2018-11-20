package com.milvik.mip.branchmanagement.testrunner;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
import com.milvik.mip.dataprovider.MIP_BranchManagement_TestData;
import com.milvik.mip.dbqueries.MIP_BranchManagement_Queries;
import com.milvik.mip.pageobjects.MIP_AddBranchPage;
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

public class MIP_AddBranch_Test {
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
		log = MIP_Logging.logDetails("MIP_AddBranch_Test");
		report = new ExtentReports(".\\Test_Reports\\AddBranch_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
		}
	}

	@Test(priority = 0, testName = "TC21")
	public void addBranchOne() throws Throwable {
		MIP_AddBranchPage addbranch = null;
		testcasename = "MIP_webPortal_TC21_Add_Branch";
		try {
			logger = report.startTest(testcasename);
			log.info("Running test case -TC21");
			addbranch = new MIP_AddBranchPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Validating Add Branch Page UI elements");
			Assert.assertTrue(addbranch.validateAddBranchObjecs());
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
			log.info("TC21 Test Failed");
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC22", dataProvider = "addBranchData", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void addBranchTwo(String name, String street, String region, String city, String msg) throws Throwable {
		MIP_AddBranchPage addbranch = null;
		testcasename = "MIP_webPortal_TC22_Add_Branch";
		try {
			logger = report.startTest(testcasename);
			log.info("Running test case -TC21");
			addbranch = new MIP_AddBranchPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Entering Branch Information");
			addbranch.enterBranchInfo(name, street, region, city).clickOnSave().confirmAddBranch("yes");
			logger.log(LogStatus.INFO, "Validating Added Branch Aganist DataBase");
			Assert.assertTrue(addbranch.getmessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(msg.trim().replaceAll("\\s", "")));
			Map<String, String> branchinfo = MIP_BranchManagement_Queries.getBranchDetails(name);
			Assert.assertTrue(branchinfo.get("name").equalsIgnoreCase(name));
			Assert.assertTrue(branchinfo.get("street").equalsIgnoreCase(street));
			Assert.assertTrue(branchinfo.get("region").equalsIgnoreCase(region));
			Assert.assertTrue(branchinfo.get("city").equalsIgnoreCase(city));
			addbranch.waitForElementToVisible(By.linkText("here")).click();
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
			log.info("TC22 Test Failed");
			log.error("error--", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC23", dataProvider = "addBranchData", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void addBranchThree(String name, String street, String region, String city, String msg) throws Throwable {
		MIP_AddBranchPage addbranch = null;
		testcasename = "MIP_webPortal_TC23_Add_Branch";
		try {
			logger = report.startTest(testcasename);
			log.info("Running test case -TC23");
			addbranch = new MIP_AddBranchPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Entering Branch Information");
			addbranch.enterBranchInfo(name, street, region, city).clickOnClear();
			logger.log(LogStatus.INFO, "Validating the results");
			Assert.assertTrue(addbranch.getBranchInfo("name").equals(""));
			Assert.assertTrue(addbranch.getBranchInfo("street").equals(""));
			Assert.assertTrue(addbranch.getBranchInfo("region").equals(""));
			Assert.assertTrue(addbranch.getBranchInfo("city").equals(""));
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
			log.info("TC23 Test Failed");
			log.error("error--", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC24 to TC29", dataProvider = "addBranchNegative", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void addBranchFour(String testcase, String name, String street, String region, String city, String msg)
			throws Throwable {
		MIP_AddBranchPage addbranch = null;
		testcasename = "MIP_webPortal_TC24 to TC29_Add_Branch";
		try {
			logger = report.startTest(testcasename);
			log.info("Running test case -" + testcase);
			addbranch = new MIP_AddBranchPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Entering Branch Information");
			String errmsg = addbranch.enterBranchInfo(name, street, region, city).clickOnSave().getValidationMsg();
			errmsg = errmsg.trim().replaceAll("\\s", "");
			msg = msg.trim().replaceAll("\\s", "").replace("  ", "");
			logger.log(LogStatus.INFO, "Validating Error Message");
			log.info("Expeted and Found Error Message:Expected=" + msg + " Found=" + errmsg);
			Assert.assertTrue(msg.equalsIgnoreCase(errmsg));
			addbranch.clickOnClear();
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
			log.info(testcase + " Test Failed");
			log.error("error while executing testcase" + testcase, t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_BRANCH);
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
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
		report.endTest(logger);
		report.flush();
	}
}
