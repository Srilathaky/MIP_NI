package com.milvik.mip.leavemanagement.testrunner;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_ViewLeavesPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_ViewLeaves_Test {
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
		log = MIP_Logging.logDetails("MIP_View_Leave_Test");
		report = new ExtentReports(".\\Test_Reports\\View_Leave_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
		}
	}

	@Test(testName = "TC83")
	public void viewLeavesOne() throws Throwable {
		testcasename = "MIP_webPortal_TC83_ViewLeave";
		try {
			logger = report.startTest("ViewLeaves Test Test -TC83");
			log.info("Running test case - TC83");
			MIP_ViewLeavesPage viewleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ViewLeavesPage.class);
			List<WebElement> option = viewleave.getLeaveRangeOptions();
			logger.log(LogStatus.INFO, "Validating View Leave Page");
			Assert.assertEquals(MIP_ViewLeavesPage.leaveRangesize, option.size());
			Assert.assertEquals(option.get(0).getText().trim(), MIP_ViewLeavesPage.leaveOption_today);
			Assert.assertEquals(option.get(1).getText().trim(), MIP_ViewLeavesPage.leaveOption_week);
			Assert.assertEquals(option.get(2).getText().trim(), MIP_ViewLeavesPage.leaveOption_month);
			Assert.assertEquals(option.get(3).getText().trim(), MIP_ViewLeavesPage.leaveOption_custom);
			Assert.assertTrue(viewleave.viewBtn.isDisplayed());
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
			log.info("Testcase TC83  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC84 to TC86")
	public void viewLeavesTwo() throws Throwable {
		testcasename = "MIP_webPortal_TC84 to TC86_ViewLeave";
		try {
			logger = report.startTest("ViewLeaves Test Test -TC84 to TC86");
			log.info("Running test case - TC84 to TC86");
			MIP_ViewLeavesPage viewleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ViewLeavesPage.class);
			int count = MIP_LeaveManagemen_Queries.getTodayLeavedetails();
			logger.log(LogStatus.INFO, "Validating Applied Leave Data in View Leave Page");
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave.getSearchresultText().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(MIP_ViewLeavesPage.search_result_message.trim().replaceAll("\\s", "")));
			}
			viewleave.selectLeaveRangeDropDownText(MIP_ViewLeavesPage.leaveOption_week).clickOnView();
			count = MIP_LeaveManagemen_Queries.getWeekLeavedetails();
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				System.out.println(viewleave.getLeaveListDetails());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave.getSearchresultText().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(MIP_ViewLeavesPage.search_result_message.trim().replaceAll("\\s", "")));
			}
			viewleave.selectLeaveRangeDropDownText(MIP_ViewLeavesPage.leaveOption_month).clickOnView();
			count = MIP_LeaveManagemen_Queries.getMonthLeavedetails();
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave.getSearchresultText().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(MIP_ViewLeavesPage.search_result_message.trim().replaceAll("\\s", "")));
			}
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
			log.info("Testcase TC84 to TC86  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC87", dataProvider = "viewLeaveTestData", dataProviderClass = MIP_LeaveManagement_TestData.class)
	public void viewLeavesThree(String fromDate, String Todate) throws Throwable {
		testcasename = "MIP_webPortal_TC87_ViewLeave";
		try {
			logger = report.startTest("ViewLeaves Test Test -TC87");
			log.info("Running test case - TC87");
			MIP_ViewLeavesPage viewleave = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ViewLeavesPage.class);
			viewleave.selectLeaveRangeDropDownText(MIP_ViewLeavesPage.leaveOption_custom);
			logger.log(LogStatus.INFO, "Validating View Leaves based on From and To Dtae");
			viewleave.selectFromLeaveDate(fromDate);
			viewleave.selectToDate(Todate);
			viewleave.clickOnView();
			viewleave.clickOnView();
			fromDate = MIP_DateFunctionality.converDateToDBDateFormat(fromDate);
			Todate = MIP_DateFunctionality.converDateToDBDateFormat(Todate);
			int count = MIP_LeaveManagemen_Queries.getCustomLeavedetails(fromDate, Todate);
			if (count > 0) {
				Assert.assertTrue(viewleave.validateTableHeading());
				Assert.assertEquals(count, viewleave.getLeaveListDetails());
			} else {
				Assert.assertTrue(viewleave.getSearchresultText().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(MIP_ViewLeavesPage.search_result_message.trim().replaceAll("\\s", "")));
			}
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
			log.info("Testcase TC87  Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.VIEW_LEAVE);
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
			homepage.clickOnMenu(MIP_Menu_Constants.LEAVE);
		report.endTest(logger);
		report.flush();
	}
}
