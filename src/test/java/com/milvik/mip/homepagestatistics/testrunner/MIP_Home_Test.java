package com.milvik.mip.homepagestatistics.testrunner;

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

import com.milvik.mip.dataprovider.MIP_Home_TestData;
import com.milvik.mip.dbqueries.MIP_Homepage_CustomerStatistics_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
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

public class MIP_Home_Test {
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
		log = MIP_Logging.logDetails("MIP_Home_Page_Statistics_Test");
		report = new ExtentReports(".\\Test_Reports\\Home_Page_Statistics_Test.html");
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

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
		}
	}

	@Test(priority = 0, testName = "TC17", dataProvider = "homeobjects", dataProviderClass = MIP_Home_TestData.class)
	public void homeTestOne(String home, String branch, String user, String leave, String cust, String SMS, String role,
			String repot, String admin, String pass, String loguot, String announ, String cust_stat,
			String cust_statistics) throws Throwable {
		testcasename = "MIP_webPortal_TC17_HomePage_Statistics";
		try {
			logger = report.startTest("Home Page Test-TC17");
			log.info("Running testcase--TC17");
			logger.log(LogStatus.INFO, "Validating the Home Page Statistis Objects");
			Assert.assertTrue(homepage.validateHomePageObjects(home, branch, user, leave, cust, SMS, role, repot, admin,
					pass, loguot, announ, cust_stat, cust_statistics));
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC17  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC18", dataProvider = "CustStatics", dataProviderClass = MIP_Home_TestData.class)
	public void homeTestTwo(String conf_sale, String unconf_sale, String self_reg, String cust_reg, String conf_agent)
			throws Throwable {
		testcasename = "MIP_webPortal_TC18_HomePage_Statistics";
		try {
			logger = report.startTest("Home page Statistics-TC18");
			log.info("Running testcase-TC18");
			homepage.validateHomePageNavigation();
			logger.log(LogStatus.INFO, "Validating Home Page Navigation");
			Assert.assertEquals(MIP_Homepage_CustomerStatistics_Queries.getConfirmedCust(),
					homepage.getCustStaticsCount(conf_sale));
			Assert.assertEquals(MIP_Homepage_CustomerStatistics_Queries.getUnconfirmedCust(),
					homepage.getCustStaticsCount(unconf_sale));
			Assert.assertEquals(MIP_Homepage_CustomerStatistics_Queries.getSelfRegCount(),
					homepage.getCustStaticsCount(self_reg));
			Assert.assertEquals(MIP_Homepage_CustomerStatistics_Queries.getCustRegCount(),
					homepage.getCustStaticsCount(cust_reg));
			Assert.assertEquals(MIP_Homepage_CustomerStatistics_Queries.getCustConfCount(),
					homepage.getCustStaticsCount(conf_agent));
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC18  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC19")
	public void homeTestThree() throws Throwable {
		testcasename = "MIP_webPortal_TC19_HomePage_Statistics";
		try {
			logger = report.startTest("Home page Statistics-TC19");
			log.info("Running testcase--TC19");
			logger.log(LogStatus.INFO, "Validating User Information in Home Page Statistics");
			String userdata = homepage.getUserDetails();
			Assert.assertTrue(userdata.contains(MIP_Homepage_CustomerStatistics_Queries
					.userDetails(MIP_ReadPropertyFile.getPropertyValue("username"))));
			Assert.assertTrue(userdata.contains(MIP_DateFunctionality.getCurrentDate("dd/MM/yyyy")));
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC19  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, enabled = true, testName = "TC20", dataProvider = "CustStaticsDiffRole", dataProviderClass = MIP_Home_TestData.class)
	public void homeTestFour(String username, String password, String conf_sale, String unconf_sale) throws Throwable {
		MIP_LoginPage loginpage = null;
		testcasename = "MIP_webPortal_TC20_HomePage_Statistics";
		try {
			logger = report.startTest("Home page Statistics-TC20");
			log.info("Running testcase--TC20");
			homepage.clickonLogout();
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(username, password);
			homepage.changeLanguage("Inglés");
			logger.log(LogStatus.INFO, "Validating Home Page Statistics for Different Role");
			Assert.assertEquals(MIP_Homepage_CustomerStatistics_Queries.getConfCust_diffRole(username),
					homepage.getCustStaticsCount(conf_sale));
			Assert.assertEquals(MIP_Homepage_CustomerStatistics_Queries.getUnconfCust_diffRole(username),
					homepage.getCustStaticsCount(unconf_sale));
			homepage.clickonLogout();
			// logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			loginpage.clickonLogout();
			log.info("Testcase TC20  Failed");
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
		} else {
			logger.log(LogStatus.PASS, testcasename + "----Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		report.endTest(logger);
		report.flush();
	}
}
