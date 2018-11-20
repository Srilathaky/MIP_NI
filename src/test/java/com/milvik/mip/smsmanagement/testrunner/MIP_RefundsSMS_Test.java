package com.milvik.mip.smsmanagement.testrunner;

import static org.testng.Assert.assertTrue;

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
import com.milvik.mip.dataprovider.MIP_SMSManagement_TestData;
import com.milvik.mip.dbqueries.MIP_SmsManagement_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_RefundsSMSPage;
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

public class MIP_RefundsSMS_Test {
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
		log = MIP_Logging.logDetails("MIP_RefundSMS_Test");
		report = new ExtentReports(".\\Test_Reports\\RefundSMS_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.REFUND_SMS);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.REFUND_SMS);
		}
	}

	@Test(testName = "TC565")
	public void refundMSOne() throws Throwable {
		testcasename = "MIP_webPortal_TC565_RefundSMS";
		try {
			logger = report.startTest("Refunds SMS-TC565");
			log.info("Running test case - TC565");
			MIP_RefundsSMSPage refndsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RefundsSMSPage.class);
			refndsmspage.waitForElementToVisible(By.id("msisdn"));
			logger.log(LogStatus.INFO, "Validating Refund SMS Page Objects");
			Assert.assertTrue(refndsmspage.mobileNumber.isDisplayed());
			Assert.assertTrue(refndsmspage.search_icon.isDisplayed());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {

			log.info("Testcase TC565 Failed");
			log.info("Error occured in the test case TC565", t);
			throw t;
		}
	}

	@Test(testName = "TC566-TC570", dataProvider = "refundSMS", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void refundSMSTwo(String msisdn) throws Throwable {
		testcasename = "MIP_webPortal_TC566-TC570_RefundSMS";
		try {
			logger = report.startTest("Refunds SMS-TC566-TC570");
			log.info("Running test case - TC566-TC570");
			Thread.sleep(1000);
			MIP_RefundsSMSPage refndsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RefundsSMSPage.class);
			refndsmspage.enterMsisdn(msisdn).clickOnSearchIcon();
			logger.log(LogStatus.INFO, "Validating Refund SMS Page Objects");
			Thread.sleep(1000);
			assertTrue(refndsmspage.validateRefundSMSObjects());
			Map<String, String> refundDetails = MIP_SmsManagement_Queries.getRefundSMSInfo(msisdn);
			Assert.assertEquals(refundDetails.get("offer"), refndsmspage.getOfferInfo().trim());
			Assert.assertEquals(refundDetails.get("cover_level"), refndsmspage.getCoverLevel().trim());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.REFUND_SMS);
		} catch (Throwable t) {
			log.info("Testcase TC566-TC570 Failed");
			log.info("Error occured in the test case TC566-TC570", t);
			throw t;
		}
	}

	@Test(testName = "TC567 to TC569-TC571-TC572", dataProvider = "RefundSMSNegative", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void refundSMSThree(String testcase, String msisdn, String errormsg) throws Throwable {
		testcasename = "MIP_webPortal_TC567-TC569-TC571-TC572_RefundSMS";
		try {
			logger = report.startTest("Refunds SMS-TC567 to TC596-TC571-TC572");
			log.info("Running test case - " + testcase);
			MIP_RefundsSMSPage refndsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RefundsSMSPage.class);
			Thread.sleep(900);
			refndsmspage.enterMsisdn(msisdn).clickOnSearchIcon();
			logger.log(LogStatus.INFO, "Validating Refund SMS Test for Negative Test Data");
			Assert.assertTrue(refndsmspage.getvalidationMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errormsg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.REFUND_SMS);
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {

			log.info("Testcase Failed" + testcase);
			log.info("Error occured in the test case TC567 to TC596-TC571-TC572", t);
			throw t;
		}
	}

	@Test(testName = "TC578", dataProvider = "sendSMSTest", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void refundSMSFour(String msisdn, String errorMessage) throws Throwable {
		logger = report.startTest("Refunds SMS-TC578");
		try {
			logger = report.startTest("Refunds SMS-TC578");
			log.info("Running test case - TC578");
			MIP_RefundsSMSPage refndsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RefundsSMSPage.class);
			refndsmspage.enterMsisdn(msisdn).clickOnSearchIcon().selectSendSmsOption("").clickOnSave();
			logger.log(LogStatus.INFO, "Validating Mandatory Field SEND SMS OPTION field");
			Assert.assertTrue(refndsmspage.getvalidationMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errorMessage.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.REFUND_SMS);
		} catch (Throwable t) {
			log.info("Testcase TC578 Failed");
			log.info("Error occured in the test case TC578", t);
			throw t;
		}
	}

	@Test(testName = "TC579", dataProvider = "refundSMS", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void refundSMSFive(String msisdn) throws Throwable {
		logger = report.startTest("Refunds SMS-TC579");
		try {
			logger = report.startTest("Refunds SMS-TC578");
			log.info("Running test case - TC578");
			MIP_RefundsSMSPage refndsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RefundsSMSPage.class);
			refndsmspage.enterMsisdn(msisdn).clickOnSearchIcon().clickOnBack();
			logger.log(LogStatus.INFO, "Validating Back Button Functionality in Refund SMS Page");
			Assert.assertTrue(refndsmspage.waitForElementToVisible(By.xpath("//*[text()='Welcome !']")).isDisplayed());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.REFUND_SMS);
		} catch (Throwable t) {
			log.info("Testcase TC578 Failed");
			log.info("Error occured in the test case TC578", t);
			throw t;
		}
	}

	@Test(testName = "TC574 to TC577", dataProvider = "generateRefundSMS", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void refundSMSSix(String testcase, String msisdn, String option, String succMsg, String sms)
			throws Throwable {
		logger = report.startTest("Refunds SMS-TC574 to TC577");
		try {
			logger = report.startTest("Refunds SMS-TC574 to TC577");
			log.info("Running test case - " + testcase);
			Thread.sleep(600);
			MIP_RefundsSMSPage refndsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RefundsSMSPage.class);
			refndsmspage.enterMsisdn(msisdn).clickOnSearchIcon().selectSendSmsOption(option).clickOnSave();
			logger.log(LogStatus.INFO, "Validating Save Button functionality in Refund SMS Page");
			Assert.assertTrue(refndsmspage.getSuccessMessage().trim().equalsIgnoreCase(succMsg.trim()));
			String sms_text = MIP_SmsManagement_Queries.getRefundSms(msisdn);
			Assert.assertTrue(sms.trim().replaceAll("\\s", "").equalsIgnoreCase(sms_text.trim().replaceAll("\\s", "")));

			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {

			log.info("Testcase" + testcase + " Failed");
			log.info("Error occured in the test case " + testcase, t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.REFUND_SMS);
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
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
		report.endTest(logger);
		report.flush();
	}
}
