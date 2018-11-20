package com.milvik.mip.customermanagement.testrunner;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import com.milvik.mip.dbqueries.MIP_SwitchProductLevel_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_SwitchProductLevelPage;
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

public class MIP_SwitchProductLevel_Test {
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
		log = MIP_Logging.logDetails("MIP_Switch_Product_Level_Test");
		report = new ExtentReports(".\\Test_Reports\\Switch_Product_Level_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.SWITCH_PRODUCT_LEVEL);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.SWITCH_PRODUCT_LEVEL);
		}
	}

	@Test(testName = "TC316")
	public void switchProductLevelOne() throws Throwable {
		testcasename = "MIP_webPortal_TC316_Switch_Product_Level";
		MIP_SwitchProductLevelPage switchprodictlevel = null;
		try {
			logger = report.startTest("CoverHistory-TC316");
			switchprodictlevel = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SwitchProductLevelPage.class);
			Assert.assertTrue(switchprodictlevel.mobileNum.isDisplayed());
			Assert.assertTrue(switchprodictlevel.searchIcon.isDisplayed());
		} catch (Throwable t) {
			log.info("TC316 Test Failed");
			log.info("Error occured in the test case TC316", t);
			throw t;
		}
	}

	@Test(testName = "TC317", dataProvider = "msisdnSwitchProductLevel", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void switchProductLevelTwo(String msisdn) throws Throwable {
		MIP_SwitchProductLevelPage switchprodictlevel = null;
		try {
			logger = report.startTest("CoverHistory-TC317");
			log.info("Running the test case TC317");
			switchprodictlevel = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SwitchProductLevelPage.class);
			Thread.sleep(300);
			switchprodictlevel.enterMsisdn(msisdn);
			switchprodictlevel.clickOnSearchButton();
			Assert.assertTrue(switchprodictlevel.ValidateSwitchProductLevel());
			Assert.assertTrue(MIP_SwitchProductLevel_Queries.getCurrentCoverLevel(msisdn)
					.contains(switchprodictlevel.getCurrentCoverLevel()));
			List<String> level = MIP_SwitchProductLevel_Queries.getLifeBenefitLevels(msisdn);
			List<WebElement> list = switchprodictlevel.getLifeBenefitLevelDropDownValue();
			for (int i = 0; i < list.size(); i++) {
				String text = list.get(i).getText().replaceAll("\\,", "");
				if (!text.equalsIgnoreCase("")) {
					Assert.assertTrue(level.toString().contains(text));
				}
			}
			homepage.clickOnMenu(MIP_Menu_Constants.SWITCH_PRODUCT_LEVEL);
		} catch (Throwable t) {
			log.info("TC317 Test Failed");
			log.info("Error occured in the test case TC317", t);
			throw t;
		}
	}

	@Test(testName = "TC317-TC326", dataProvider = "SwitchProductLevelPositive", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void switchProductLevelThree(String testcase, String msisdn, String benefitLevel, String successMsg,
			String sms) throws Throwable {
		MIP_SwitchProductLevelPage switchprodictlevel = null;
		try {
			logger = report.startTest("CoverHistory- " + testcase);
			switchprodictlevel = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SwitchProductLevelPage.class);
			switchprodictlevel.enterMsisdn(msisdn);
			switchprodictlevel.clickOnSearchButton();
			switchprodictlevel.switchProductLevel(benefitLevel).clickOnSaveChanges().confirmPopUp("yes");
			Assert.assertTrue(switchprodictlevel.getSuccessMessage().equalsIgnoreCase(successMsg.trim()));
			switchprodictlevel.waitForElementToVisible(By.linkText("here")).click();
			int confirmation_status = MIP_SwitchProductLevel_Queries.getConfirmationStatus(msisdn);
			if (confirmation_status == 0) {
				Assert.assertTrue(MIP_SwitchProductLevel_Queries.getCurrentCoverLevel(msisdn)
						.contains(benefitLevel.replaceAll("\\,", "")));
			} else if (confirmation_status == 1) {
				Assert.assertTrue(MIP_SwitchProductLevel_Queries.getProductLevelforConfirmedCustomer(msisdn)
						.contains(benefitLevel.replaceAll("\\,", "")));
			}
			Assert.assertTrue(
					MIP_SwitchProductLevel_Queries.getSwitchCoverLevelSMS(msisdn).equalsIgnoreCase(sms.trim()));

		} catch (Throwable t) {
			log.info("TC317-TC326 Test Failed");
			log.info("Error occured in the test case " + testcase, t);
			throw t;
		}
	}

	@Test(testName = "TC336", dataProvider = "msisdnSwitchProductLevel", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void switchProductLevelFour(String msisdn) throws Throwable {
		MIP_SwitchProductLevelPage switchprodictlevel = null;
		try {
			logger = report.startTest("CoverHistory-TC336");
			log.info("Running the test case TC336");
			switchprodictlevel = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SwitchProductLevelPage.class);
			switchprodictlevel.enterMsisdn(msisdn);
			switchprodictlevel.clickOnSearchButton();
			switchprodictlevel.waitForElementToVisible(By.id("backBtn")).click();
			switchprodictlevel.waitForElementToVisible(By.id("msisdn"));
			Assert.assertTrue(switchprodictlevel.mobileNum.isDisplayed());
			Assert.assertTrue(switchprodictlevel.searchIcon.isDisplayed());

		} catch (Throwable t) {
			log.info("TC336 Test Failed");
			log.info("Error occured in the test case TC336", t);
			throw t;
		}
	}

	@Test(testName = "TC327-335", dataProvider = "SwitchProductLevelmsisdnValidation", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void switchProductLevelFive(String msisdn, String errormsg) throws Throwable {
		MIP_SwitchProductLevelPage switchprodictlevel = null;
		try {
			logger = report.startTest("CoverHistory-TC327-335");
			log.info("Running the test case TC327-335");
			switchprodictlevel = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SwitchProductLevelPage.class);
			Thread.sleep(300);
			switchprodictlevel.enterMsisdn(msisdn);
			switchprodictlevel.clickOnSearchButton();
			String errmsg = switchprodictlevel.getValidationMessage();
			Assert.assertTrue(errmsg.trim().equalsIgnoreCase(errormsg.trim()));
			homepage.clickOnMenu(MIP_Menu_Constants.SWITCH_PRODUCT_LEVEL);
		} catch (Throwable t) {
			log.info("TC327-335 Test Failed");
			log.info("Error occured in the test case TC327-335", t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.SWITCH_PRODUCT_LEVEL);
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
			homepage.clickOnMenu(MIP_Menu_Constants.SWITCH_PRODUCT_LEVEL);
		report.endTest(logger);
		report.flush();
	}
}
