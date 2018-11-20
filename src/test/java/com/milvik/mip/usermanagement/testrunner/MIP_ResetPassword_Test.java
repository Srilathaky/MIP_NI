package com.milvik.mip.usermanagement.testrunner;

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
import com.milvik.mip.dataprovider.MIP_ResetPassword_TestData;
import com.milvik.mip.pageobjects.MIP_ChangePasswordPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_ResetPasswordPage;
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

public class MIP_ResetPassword_Test {
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
		log = MIP_Logging.logDetails("MIP_Reset_Password_Test");
		report = new ExtentReports(".\\Test_Reports\\Reset_Password_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.RESET_PASSWORD);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.RESET_PASSWORD);
		}
	}

	@Test(priority = 0, testName = "TC62")
	public void resetPasswordOne() throws Throwable {
		testcasename = "MIP_webPortal_TC62_Reset_Password";
		try {
			logger = report.startTest("List User-TC62");
			log.info("Running test case - TC62");
			MIP_ResetPasswordPage resetpasspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ResetPasswordPage.class);
			logger.log(LogStatus.INFO, "Validating Reset Password objects");
			Assert.assertTrue(resetpasspage.userId.isDisplayed());
			Assert.assertTrue(resetpasspage.searchIcon.isDisplayed());
		} catch (Throwable t) {
			log.info("Testcase TC62  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC63-TC66-TC67", dataProvider = "ResetPassword", dataProviderClass = MIP_ResetPassword_TestData.class)
	public void resetPasswordTwo(String userID, String succMsg, String defaultPass, String invalidPass, String errmsg,
			String validPass, String successMsg) throws Throwable {
		testcasename = "MIP_webPortal_TC63-TC66-TC67_Reset_Password";
		try {
			logger = report.startTest("Reset_Password-TC63-TC66-TC67");
			log.info("Running test case - TC63-TC66-TC67");
			MIP_ResetPasswordPage resetpasspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ResetPasswordPage.class);
			logger.log(LogStatus.INFO, "Validating Reset Password button functionality");
			resetpasspage.enterUserID(userID).clickOnSearchIcon().clickOnResetPassword().confirmPopUp("yes");
			Assert.assertTrue(resetpasspage.getConfirmationMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(succMsg.trim().replaceAll("\\s", "")));
			homepage.clickonLogout();
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_LoginPage.class);
			loginpage.login(userID, defaultPass);
			MIP_ChangePasswordPage changepass = new MIP_ChangePasswordPage(MIP_Test_Configuration.driver);
			Assert.assertTrue(changepass.validateChangePassObjects());
			changepass.enterPassworddata(defaultPass, invalidPass, invalidPass).clickOnSave();
			Assert.assertTrue(changepass.getValidationMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			changepass.clickOnClear().enterPassworddata(defaultPass, validPass, validPass).clickOnSave()
					.confirmChangePass("yes");
			Assert.assertTrue(changepass.getSuccessMsg().trim().equalsIgnoreCase(successMsg.trim()));
		} catch (Throwable t) {
			log.info("Testcase TC63-TC66-TC67  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC64-TC65", dataProvider = "ResetPassNegative", dataProviderClass = MIP_ResetPassword_TestData.class)
	public void resetPasswordThree(String userID, String errmsg) throws Throwable {
		testcasename = "MIP_webPortal_TC64-TC65_Reset_Password";
		try {
			logger = report.startTest("Reset Password TC64-TC65");
			log.info("Running test case - TC63");
			logger.log(LogStatus.INFO, "Validating Reset Password Button Functionality with Negative Test Data");
			MIP_ResetPasswordPage resetpasspage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_ResetPasswordPage.class);
			if (!userID.equals("")) {
				resetpasspage.enterUserID(userID).clickOnSearchIcon();
				Assert.assertTrue(resetpasspage.getSearchResulMessage().trim().equalsIgnoreCase(errmsg.trim()));
			}
			if (userID.equals("")) {
				resetpasspage.enterUserID(userID).clickOnSearchIcon();
				Assert.assertTrue(resetpasspage.getValidationMessage().trim().replaceAll("\\s", "")
						.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			}

		} catch (Throwable t) {

			log.info("Testcase TC64-TC65  Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.RESET_PASSWORD);
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
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
		report.endTest(logger);
		report.flush();
	}
}
