package com.milvik.mip.passwordmanagement;

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

import com.milvik.mip.dataprovider.MIP_ForgotPass_TestData;
import com.milvik.mip.pageobjects.MIP_ForgotPasswordPage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_ForgotPassword_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;

	@BeforeTest
	@Parameters({ "browser" })
	public void test_setup(@Optional("firefox") String browser) {
		report = new ExtentReports(".\\Test_Reports\\Forgotpassword_Test.html");
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

	@Test(testName = "TC11")
	public void forgotPassOne() throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Forgotpassword-TC11");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			Assert.assertTrue(loginpage.clickOnForgotPassword().getForgotPassObjects());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC11  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(testName = "TC12", dataProvider = "withoutEmail", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassTwo(String userid, String errormsg) throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Forgotpassword-TC12");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			MIP_ForgotPasswordPage forgotpasspage = loginpage.clickOnForgotPassword().enterUserId(userid);
			forgotpasspage.clickOnSubmit();
			Assert.assertTrue(forgotpasspage.getErrorMsg(errormsg).equals(errormsg));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC12  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC14", dataProvider = "invaliduser", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassThree(String userid, String errormsg) throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Forgotpassword-TC14");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			MIP_ForgotPasswordPage forgotpasspage = loginpage.clickOnForgotPassword().enterUserId(userid);
			forgotpasspage.clickOnSubmit();
			Assert.assertTrue(forgotpasspage.getErrorMsg(errormsg).equals(errormsg));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC14  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC15", dataProvider = "withoutEmail", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassFour(String invaliduserid, String validuserid) throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Forgotpassword-TC15");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			MIP_ForgotPasswordPage forgotpasspage = null;
			forgotpasspage = loginpage.clickOnForgotPassword().enterUserId(invaliduserid);
			forgotpasspage.clickOnCancel();
			Assert.assertTrue(driver.getCurrentUrl().contains("login.jsp"));
			loginpage.clickOnForgotPassword().enterUserId(validuserid);
			forgotpasspage.clickOnCancel();
			Assert.assertTrue(driver.getCurrentUrl().contains("login.jsp"));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC15  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC16", dataProvider = "withoutuserid", dataProviderClass = MIP_ForgotPass_TestData.class)
	public void forgotPassFive(String errormsg) throws Throwable {
		MIP_LoginPage loginpage = null;
		try {
			logger = report.startTest("Forgotpassword-TC16");
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			MIP_ForgotPasswordPage forgotpasspage = loginpage.clickOnForgotPassword();
			forgotpasspage.clickOnSubmit();
			Assert.assertTrue(forgotpasspage.getAlertText().equals(errormsg));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC16  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod
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
