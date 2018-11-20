package com.milvik.mip.customermanagement.testrunner;

import java.util.List;

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

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_CustomerManagement_TestData;
import com.milvik.mip.dbqueries.MIP_CoverHistory_SwitchProductLevel_Queries;
import com.milvik.mip.pageobjects.MIP_CoverHistoryPage;
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

public class MIP_CoverHistory_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;

	@BeforeTest
	@Parameters({ "browser" })
	public void test_setup(@Optional("firefox") String browser) {
		log = MIP_Logging.logDetails("MIP_CoverHistory_Test");
		report = new ExtentReports(
				".\\Test_Reports\\MIP_CoverHistory_Test.html");
		try {
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
		} catch (Exception e) {
			System.out.println("No exe found");
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

	@Test(testName = "TC337")
	public void coverHistoryOne() throws Throwable {
		MIP_CoverHistoryPage coverhistory = null;
		try {
			logger = report.startTest("CoverHistory-TC297");
			log.info("Running the test case TC337");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
			coverhistory = PageFactory.initElements(driver,
					MIP_CoverHistoryPage.class);
			Assert.assertTrue(coverhistory.mobileNum.isDisplayed());
			Assert.assertTrue(coverhistory.searchIcon.isDisplayed());
			coverhistory.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			coverhistory.clickonLogout();
			log.info("TC337 Test Failed");
			log.info("Error occured in the test case TC337", t);
			throw t;
		}
	}

	@Test(testName = "TC341-TC342", dataProvider = "coverHistoryNegativeTest", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void coverHistoryTwo(String msisdn, String errorMsg)
			throws Throwable {
		MIP_CoverHistoryPage coverhistory = null;
		try {
			logger = report.startTest("CoverHistory-TC341-TC342");
			log.info("Running the test case TC341-TC342");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
			coverhistory = PageFactory.initElements(driver,
					MIP_CoverHistoryPage.class);
			coverhistory.enterMsisdn(msisdn);
			coverhistory.clickOnSearchButton();
			Assert.assertTrue(coverhistory.getResultText().trim()
					.equalsIgnoreCase(errorMsg.trim()));
			coverhistory.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			coverhistory.clickonLogout();
			log.info("TC341-TC342 Test Failed");
			log.info("Error occured in the test case TC341-TC342", t);
			throw t;
		}
	}

	@Test(testName = "TC338 to TC340", dataProvider = "coverHistoryPositiveTest", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void coverHistoryThree(String msisdn) throws Throwable {
		MIP_CoverHistoryPage coverhistory = null;
		try {
			logger = report.startTest("CoverHistory-TC338 to TC340");
			log.info("Running the test case TC338 to TC340");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
			coverhistory = PageFactory.initElements(driver,
					MIP_CoverHistoryPage.class);
			coverhistory.enterMsisdn(msisdn);
			coverhistory.clickOnSearchButton();
			Assert.assertTrue(coverhistory.validateTableHeading());
			List<String> details1 = coverhistory.getCoverHistoryDetails();
			List<String> details2 = MIP_CoverHistory_SwitchProductLevel_Queries
					.getCoverHistoryDetails(msisdn);
			Assert.assertEquals(details1.size(), details2.size());
			for (int i = 0; i < details1.size(); i++) {
				Assert.assertTrue(details2.get(i).contains(details1.get(i)));
			}
			coverhistory.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			coverhistory.clickonLogout();
			log.info("TC338 to TC340 Test Failed");
			log.info("Error occured in the test case TC338 to TC340", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
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
