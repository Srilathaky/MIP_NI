package com.milvik.mip.usermanagement.testrunner;

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
import com.milvik.mip.dataprovider.MIP_SearchUser_TestData;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_SearchUserPage;
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

public class MIP_SearchUser_Test {
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
		log = MIP_Logging.logDetails("MIP_Search_User_Test");
		report = new ExtentReports(".\\Test_Reports\\Search_User_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_USER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_USER);
		}
	}

	@Test(testName = "TC52")
	public void searchUserOne() throws Throwable {
		testcasename = "MIP_webPortal_TC52_Search_User";
		try {
			logger = report.startTest("Search User-TC52");
			log.info("Running test case - TC52");
			MIP_SearchUserPage searchuser = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SearchUserPage.class);
			logger.log(LogStatus.INFO, "Validating Search User page Objects");
			Assert.assertTrue(searchuser.fnmae.isDisplayed());
			Assert.assertTrue(searchuser.snmae.isDisplayed());
			Assert.assertTrue(searchuser.msisdn.isDisplayed());
			Assert.assertTrue(searchuser.role.isDisplayed());
			Assert.assertTrue(searchuser.searchbtn.isDisplayed());
			Assert.assertTrue(searchuser.clearBtn.isDisplayed());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC52  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC53 to TC57", dataProvider = "searchUserDataPositive", dataProviderClass = MIP_SearchUser_TestData.class)
	public void searchUserTwo(String testcase, String fname, String sname, String msisdn, String role)
			throws Throwable {
		testcasename = "MIP_webPortal_TC53 to TC57_Search_User";
		try {
			logger = report.startTest("Search User-TC53 to TC57");
			log.info("Running test case - " + testcase);
			MIP_SearchUserPage searchuser = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SearchUserPage.class);
			searchuser.enterSearchCriteria(fname, sname, msisdn, role).clickOnSearch();
			logger.log(LogStatus.INFO, "Validating Search Result in Search User Page");
			if ((!fname.equals("")) || (!sname.equals(""))) {
				List<WebElement> namelist = searchuser.getUserName();
				for (int i = 0; i < namelist.size(); i++) {
					if (!fname.equals(""))
						Assert.assertTrue(namelist.get(i).getText().toUpperCase().contains(fname.trim().toUpperCase()));
					if (!sname.equals(""))
						Assert.assertTrue(namelist.get(i).getText().toUpperCase().contains(sname.toUpperCase()));
				}
			}
			if (!msisdn.equals("")) {
				Assert.assertTrue(searchuser.getMsisdn().getText().equalsIgnoreCase(msisdn.trim()));
			}
			if (!role.equals("")) {
				List<WebElement> rolelist = searchuser.getRole();
				for (int i = 0; i < rolelist.size(); i++) {
					Assert.assertTrue(rolelist.get(i).getText().contains(role.trim()));
				}
			}
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase " + testcase + " Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC58", dataProvider = "SearcUserTestData", dataProviderClass = MIP_SearchUser_TestData.class)
	public void searchUserThree(String fname, String sname, String msisdn, String role) throws Throwable {
		testcasename = "MIP_webPortal_TC58_Search_User";
		try {
			logger = report.startTest("Search User-TC58");
			log.info("Running test case - TC58");
			MIP_SearchUserPage searchuser = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SearchUserPage.class);
			searchuser.enterSearchCriteria(fname, sname, msisdn, role).clickOnClear();
			logger.log(LogStatus.INFO, "Validating Clear Button Functionality in Search user Page");
			Assert.assertTrue(searchuser.fnmae.getText().equals(""));
			Assert.assertTrue(searchuser.snmae.getText().equals(""));
			Assert.assertTrue(searchuser.msisdn.getText().equals(""));
			Assert.assertTrue(searchuser.getDropDownValue().equals(""));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC58  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC59 to TC60", dataProvider = "searchUserDataNegative", dataProviderClass = MIP_SearchUser_TestData.class)
	public void searchUserFour(String testacse, String fname, String sname, String msisdn, String errmsg)
			throws Throwable {
		testcasename = "MIP_webPortal_TC59 to TC60_Search_User";
		try {
			logger = report.startTest("Search User-TC59 to TC60");
			log.info("Running test case - " + testacse);
			MIP_SearchUserPage searchuser = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SearchUserPage.class);
			searchuser.enterSearchCriteria(fname, sname, msisdn, "").clickOnSearch();
			logger.log(LogStatus.INFO, "Validating Search User with Negative Test Data");
			Assert.assertTrue(searchuser.getResultMsg().trim().equalsIgnoreCase(errmsg.trim()));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC59 -TC60  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC61")
	public void searchUserFive() throws Throwable {
		testcasename = "MIP_webPortal_TC61_Search_User";
		try {
			logger = report.startTest("Search User-TC61");
			log.info("Running test case - TC61");
			MIP_SearchUserPage searchuser = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_SearchUserPage.class);
			searchuser.clickOnSearch();
			logger.log(LogStatus.INFO, "Validating Search User without any search criteria");
			Assert.assertTrue(searchuser.getValidationMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(MIP_SearchUser_TestData.withoutsearchcriteria.trim().replaceAll("\\s", "")));

			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC61  Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_USER);
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
