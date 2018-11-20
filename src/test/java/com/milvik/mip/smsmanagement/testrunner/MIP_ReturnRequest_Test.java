package com.milvik.mip.smsmanagement.testrunner;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
import com.milvik.mip.dataprovider.MIP_SMSManagement_TestData;
import com.milvik.mip.dbqueries.MIP_SmsManagement_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_ReturnRequestPage;
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

public class MIP_ReturnRequest_Test {
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
		log = MIP_Logging.logDetails("MIP_ReturnRequest_Test");
		report = new ExtentReports(".\\Test_Reports\\ReturnRequest_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.RETURN_REQUESTS);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.RETURN_REQUESTS);
		}
	}

	@Test(testName = "TC580")
	public void returnRequestOne() throws Throwable {
		testcasename = "MIP_webPortal_TC580_Return_Request";
		try {
			logger = report.startTest("Return Request-TC580");
			log.info("Running test case - TC580");
			MIP_ReturnRequestPage returnreqpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ReturnRequestPage.class);
			returnreqpage.waitForElementToVisible(By.id("msisdn"));
			logger.log(LogStatus.INFO, "Validating Return Request Page Objects");
			Assert.assertTrue(returnreqpage.mobileNumber.isDisplayed());
			Assert.assertTrue(returnreqpage.sendSms.isDisplayed());
			Assert.assertTrue(returnreqpage.backBtn.isDisplayed());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.RETURN_REQUESTS);
		} catch (Throwable t) {
			log.info("Testcase TC580 Failed");
			log.info("Error occured in the test case TC580", t);
			throw t;
		}
	}

	@Test(testName = "TC589")
	public void returnRequestTwo() throws Throwable {
		testcasename = "MIP_webPortal_TC589_Return_Request";
		try {
			logger = report.startTest("Return Request-TC589");
			log.info("Running test case - TC589");
			MIP_ReturnRequestPage returnreqpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ReturnRequestPage.class);
			logger.log(LogStatus.INFO, "Validating Back Button Functionality in Return Request Page");
			returnreqpage.clickOnBackBtn();
			Assert.assertTrue(returnreqpage.waitForElementToVisible(By.xpath("//*[text()='Welcome !']")).isDisplayed());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.RETURN_REQUESTS);
		} catch (Throwable t) {
			log.info("Testcase TC589 Failed");
			log.info("Error occured in the test case TC589", t);
			throw t;
		}
	}

	@Test(testName = "TC583 to TC585-TC587-TC588", dataProvider = "RefundSMSNegative", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void returnRequestThree(String testcase, String msisdn, String errormsg) throws Throwable {
		testcasename = "MIP_webPortal_TC583 to TC585-TC587-TC588_Return_Request";
		try {
			logger = report.startTest("Return Request-TC583 to TC585-TC587-TC588");
			log.info("Running test case - TC583 to TC585-TC587-TC588");
			MIP_ReturnRequestPage returnreqpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ReturnRequestPage.class);
			Thread.sleep(900);
			returnreqpage.enterMsisdn(msisdn).clickOnSendSMS();
			logger.log(LogStatus.INFO, "Validating Return Request with Negative Test data");
			Assert.assertTrue(returnreqpage.getvalidationMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errormsg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.RETURN_REQUESTS);
		} catch (Throwable t) {
			log.info("Testcase FailedTC583 to TC585-TC587-TC588");
			log.info("Error occured in the test case TC583 to TC585-TC587-TC588", t);
			throw t;
		}
	}

	@Test(testName = "TC581-TC582", dataProvider = "returnRefund", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void returnRequestFour(String msisdn, String successMsg, String sms) throws Throwable {
		testcasename = "MIP_webPortal_TC581-TC582_Return_Request";
		try {
			logger = report.startTest("Return Request-TC581-TC582");
			log.info("Running test case - TC581-TC582");
			MIP_ReturnRequestPage returnreqpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ReturnRequestPage.class);
			Thread.sleep(300);
			returnreqpage.enterMsisdn(msisdn).clickOnSendSMS();
			logger.log(LogStatus.INFO, "Validating SEND SMS Button functionality in Return Request Page");
			Assert.assertTrue(returnreqpage.getSuccessMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(successMsg.trim().replaceAll("\\s", "")));
			String sms_text = MIP_SmsManagement_Queries.getRefundSms(msisdn);
			Assert.assertEquals(sms_text.trim(), sms.trim());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.RETURN_REQUESTS);
		} catch (Throwable t) {
			log.info("Testcase FailedTC583 to TC581-TC582");
			log.info("Error occured in the test case TC581-TC582", t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.RETURN_REQUESTS);
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
