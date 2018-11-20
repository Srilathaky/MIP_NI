package com.milvik.mip.leavemanagement.testrunner;

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
import com.milvik.mip.dataprovider.MIP_LeaveManagement_TestData;
import com.milvik.mip.dbqueries.MIP_LeaveManagemen_Queries;
import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.pageobjects.MIP_ApplyLeavePage;
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

public class MIP_ApplyLeave_Test {
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
		log = MIP_Logging.logDetails("MIP_ApplyLeave_Test");
		report = new ExtentReports(".\\Test_Reports\\ApplyLeave_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		}
	}

	@Test(priority = 0, testName = "TC68")
	public void leaveManagement() throws Throwable {
		testcasename = "MIP_webPortal_TC68_Leave_Management";
		try {
			logger = report.startTest("Leave Management Test -TC68");
			log.info("Running test case - TC68");
			logger.log(LogStatus.INFO, "Validating Leave Management Menu Options");
			Assert.assertTrue(MIP_Test_Configuration.driver.findElement(By.linkText(MIP_Menu_Constants.APPLY_LEAVE))
					.isDisplayed());
			homepage.waitForElementToVisible(By.linkText(MIP_Menu_Constants.VIEW_LEAVE));
			Assert.assertTrue(MIP_Test_Configuration.driver.findElement(By.linkText("View Leaves")).isDisplayed());
		} catch (Throwable t) {
			log.info("Testcase TC68  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC69")
	public void applyLeaveOne() throws Throwable {
		testcasename = "MIP_webPortal_TC69_Apply Leave";
		try {
			logger = report.startTest("Aply Leave  Test -TC69");
			log.info("Running test case - TC69");
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ApplyLeavePage.class);
			logger.log(LogStatus.INFO, "Validating Apply Leave Page Objects");
			Assert.assertTrue(applyleave.userName.isDisplayed());
			Assert.assertTrue(applyleave.fromDate.isDisplayed());
			Assert.assertTrue(applyleave.toDate.isDisplayed());
			Assert.assertTrue(applyleave.reason.isDisplayed());
			Assert.assertTrue(applyleave.fromCalender.isDisplayed());
			Assert.assertTrue(applyleave.toDate.isDisplayed());
			Assert.assertTrue(applyleave.applyBtn.isDisplayed());
			Assert.assertTrue(applyleave.clearBtn.isDisplayed());
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC69  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC70 to TC75", dataProvider = "applyLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void applyLeaveTwo(String userName, String fromDate, String toDate, String reason) throws Throwable {
		testcasename = "MIP_webPortal_TC69_Apply Leave";
		try {
			logger = report.startTest("Aply Leave  Test -TC70 to TC75");
			log.info("Running test case - TC70 to TC75");
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ApplyLeavePage.class);
			logger.log(LogStatus.INFO, "Validating Functionality of each field in Apply Leave Page");
			Assert.assertEquals(applyleave.getUserDetails(), MIP_UserManagement_Queries.getActiveUser());
			applyleave.selectUserName(userName);
			Assert.assertEquals(applyleave.getSelectedOptionFromUserNameDropDown().trim(), userName.trim());
			applyleave.clickOnFromDate().selectLeaveDate(fromDate);
			Assert.assertEquals(applyleave.fromDate.getAttribute("value").trim(), fromDate.trim());
			applyleave.selectReason(reason);
			Assert.assertEquals(applyleave.getSelectedOptionFromReasonDropDown().trim(), reason.trim());
			applyleave.clickOnToDate();
			applyleave.selectToLeaveDate(toDate);
			Assert.assertEquals(applyleave.toDate.getAttribute("value").trim(), toDate.trim());
			applyleave.clearFromDate();
			Assert.assertEquals(applyleave.fromDate.getAttribute("value"), "");
			applyleave.clearToDate();
			Assert.assertEquals(applyleave.toDate.getAttribute("value"), "");
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC70 to TC75  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC77", dataProvider = "applyLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void applyLeaveThree(String userName, String fromDate, String toDate, String reason) throws Throwable {
		testcasename = "MIP_webPortal_TC77_Apply Leave";
		try {
			logger = report.startTest("Aply Leave  Test -TC77");
			log.info("Running test case - TC77");
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ApplyLeavePage.class);
			logger.log(LogStatus.INFO, "Validating Functionality of clear Button in Apply Leave Page");
			Assert.assertEquals(applyleave.getUserDetails(), MIP_UserManagement_Queries.getActiveUser());
			applyleave.selectUserName(userName);
			applyleave.clickOnFromDate().selectLeaveDate(fromDate);
			applyleave.selectReason(reason);
			applyleave.clickOnToDate().selectToLeaveDate(toDate);
			applyleave.clickOnClear();
			Assert.assertEquals(applyleave.getSelectedOptionFromUserNameDropDown().trim(), "");
			Assert.assertEquals(applyleave.getSelectedOptionFromReasonDropDown().trim(), "");
			Assert.assertEquals(applyleave.fromDate.getAttribute("value"), "");
			Assert.assertEquals(applyleave.toDate.getAttribute("value"), "");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC77  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 4, testName = "TC79 to TC82", dataProvider = "applyLeaveNegativeTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void applyLeaveFour(String testcase, String userName, String fromDate, String toDate, String reason,
			String errormsg) throws Throwable {
		testcasename = "MIP_webPortal_TC79 to TC82_Apply Leave";
		try {
			logger = report.startTest("Aply Leave  Test -TC79 to TC82");
			log.info("Running test case - " + testcase);
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ApplyLeavePage.class);
			applyleave.selectUserName(userName);
			logger.log(LogStatus.INFO, "Validating Apply leave success message in Apply Leave Page");
			if (!fromDate.equals(""))
				applyleave.clickOnFromDate().selectLeaveDate(fromDate);
			if (!toDate.equals(""))
				applyleave.clickOnToDate().selectToLeaveDate(toDate);

			applyleave.selectReason(reason);
			applyleave.clickOnApply();
			Assert.assertTrue(applyleave.getValidationMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errormsg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC79 to TC82  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 5, testName = "TC76", dataProvider = "applyLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void applyLeaveFive(String userName, String fromDate, String toDate, String reason) throws Throwable {
		testcasename = "MIP_webPortal_TC76_Apply Leave";
		try {
			logger = report.startTest("Aply Leave  Test -TC76");
			log.info("Running test case - TC76");
			logger.log(LogStatus.INFO, "Applying Leave");
			MIP_ApplyLeavePage applyleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ApplyLeavePage.class);
			int count = MIP_LeaveManagemen_Queries.getLeavedetails(userName);
			applyleave.selectUserName(userName);
			applyleave.clickOnFromDate().selectLeaveDate(fromDate);
			applyleave.selectReason(reason);
			applyleave.clickOnToDate().selectToLeaveDate(toDate);
			applyleave.clickOnApply();
			applyleave.confirmPopUp("yes");
			logger.log(LogStatus.INFO, "Validating apply leave details aganist DataBase Details");
			Assert.assertTrue(applyleave.getSuccessMessage().trim().replaceAll("\\s", "").equalsIgnoreCase(
					"The leave details have been updated successfully.".trim().replaceAll("\\s", "")));
			Assert.assertTrue(count < MIP_LeaveManagemen_Queries.getLeavedetails(userName));
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
			log.info("Testcase TC76  Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.APPLY_LEAVE);
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
