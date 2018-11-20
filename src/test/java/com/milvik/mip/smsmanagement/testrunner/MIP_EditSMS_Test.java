package com.milvik.mip.smsmanagement.testrunner;

import java.util.Map;

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
import com.milvik.mip.pageobjects.MIP_EditSMSPage;
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

public class MIP_EditSMS_Test {
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
		log = MIP_Logging.logDetails("MIP_Edit_SMS_Test");
		report = new ExtentReports(".\\Test_Reports\\Edit_SMS_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.SMS);
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		}
	}

	@Test(testName = "TC219")
	public void editSMSOne() throws Throwable {
		testcasename = "MIP_webPortal_TC219_EditSMS";
		try {
			logger = report.startTest("Edit SMS Template-TC219");
			log.info("Running test case - TC219");
			logger.log(LogStatus.INFO, "Validating Edit SMS Template Page Objects");
			MIP_EditSMSPage editsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_EditSMSPage.class);
			Assert.assertTrue(editsmspage.templateName.isDisplayed());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			log.info("Testcase TC219 Failed");
			log.info("Error occured in the test case TC219", t);
			throw t;
		}
	}

	@Test(testName = "TC220", dataProvider = "EditSMSTestData", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSTwo(String... templateName) throws Throwable {
		testcasename = "MIP_webPortal_TC220_EditSMS";
		try {
			logger = report.startTest("Edit SMS Template-TC220");
			log.info("Running test case - TC220");
			MIP_EditSMSPage editsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName[0]);
			logger.log(LogStatus.INFO, "Validating Edit SMS Template Page Objects for SMS Template" + templateName[0]);
			Assert.assertTrue(editsmspage.validateEditSMSTemplate());
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			log.info("Testcase TC220 Failed");
			log.info("Error occured in the test case TC220", t);
			throw t;
		}
	}

	@Test(testName = "TC221 to TC223", dataProvider = "EditSMSTestData", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSThree(String templateName, String content, String validity, String message) throws Throwable {
		testcasename = "MIP_webPortal_TC221 to TC223 EditSMS";
		try {
			logger = report.startTest("Edit SMS Template-TC221 to TC223");
			log.info("Running test case - TC221 to TC223");
			MIP_EditSMSPage editsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearContent().enterContent(content).clearSmsValidity().enterSmsValidity(validity).clikOnSave()
					.confirmEditSMS("yes");
			logger.log(LogStatus.INFO, "Validating edit information Field in Edit SMS Template Page");
			Assert.assertTrue(editsmspage.getSuccessMessage().trim().contains(message.trim()));
			Map<String, String> editsms = MIP_SmsManagement_Queries.geteditSmsInfo(templateName);
			Assert.assertTrue(editsms.get("content").equalsIgnoreCase(content));
			Assert.assertTrue(editsms.get("validity").equalsIgnoreCase(validity));
			editsmspage.waitForElementToVisible(By.linkText("here")).click();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC221 to TC223 Failed");
			log.info("Error occured in the test case TC221 to TC223", t);
			throw t;
		}
	}

	@Test(enabled = false, testName = "TC224", dataProvider = "EditSMSErrorMessage", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSFour(String templateName, String content, String validity, String message) throws Throwable {
		testcasename = "MIP_webPortal_TC224 EditSMS";
		try {
			logger = report.startTest("Edit SMS Template-TC224");
			log.info("Running test case -TC224");
			MIP_EditSMSPage editsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearContent().enterContent(content).clickOnPreview();
			logger.log(LogStatus.INFO, "Validating Preview Button Functionality in Edit SMS Template Page");
			Assert.assertTrue(editsmspage.getPreviewContent().contains(content));
			editsmspage.clickOnPreviewOk();
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {
			log.info("Testcase TC224 Failed");
			log.info("Error occured in the test case TC224", t);
			throw t;
		}
	}

	@Test(testName = "TC225", dataProvider = "EditSMSContentErrorMessage", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSFive(String templateName, String contenterror, String contentplaceholdererror) throws Throwable {
		testcasename = "MIP_webPortal_TC225 EditSMS";
		try {
			logger = report.startTest("Edit SMS Template-TC225");
			log.info("Running test case -TC225");
			MIP_EditSMSPage editsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearContent().clikOnSave();
			logger.log(LogStatus.INFO, "Validating Clear Button Functionality  in Edit SMS Template Page");
			String errormsg = editsmspage.getvalidationMessage();
			Assert.assertTrue(errormsg.contains(contenterror));
			if (MIP_SmsManagement_Queries.getplaceHolderCount(templateName) > 0) {
				Assert.assertTrue(errormsg.contains(contenterror));
			}
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {

			log.info("Testcase TC225 Failed");
			log.info("Error occured in the test case TC225", t);
			throw t;
		}
	}

	@Test(testName = "TC226-TC227", dataProvider = "EditSMSValidtyErrorMessage", dataProviderClass = MIP_SMSManagement_TestData.class)
	public void editSMSSix(String templateName, String validity, String validityrerror) throws Throwable {
		testcasename = "MIP_webPortal_TC226-TC227 EditSMS";
		try {
			logger = report.startTest("Edit SMS Template-TC226-TC227");
			log.info("Running test case -TC226-TC227");

			MIP_EditSMSPage editsmspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_EditSMSPage.class);
			editsmspage.selectTemplateName(templateName);
			editsmspage.clearSmsValidity().enterSmsValidity(validity).clikOnSave();
			String errormsg = editsmspage.getvalidationMessage();
			Assert.assertTrue(errormsg.trim().replaceAll("\\s", "")
					.equalsIgnoreCase(validityrerror.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
		} catch (Throwable t) {

			log.info("Testcase TC226-TC227 Failed");
			log.info("Error occured in the test case TC226-TC227", t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.EDIT_SMS_TEMPLATE);
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
