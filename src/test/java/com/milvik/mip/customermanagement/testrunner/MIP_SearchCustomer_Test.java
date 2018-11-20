package com.milvik.mip.customermanagement.testrunner;

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

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_SearchCustomer_TestData;
import com.milvik.mip.dbqueries.MIP_ModifyCustomerDetails_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_ModifyCustomerPage;
import com.milvik.mip.pageobjects.MIP_SearchCustomerPage;
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

public class MIP_SearchCustomer_Test {
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
		log = MIP_Logging.logDetails("MIP_Search_Customer_Test");
		report = new ExtentReports(".\\Test_Reports\\Search_Customer_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
		}
	}

	@Test(testName = "TC150 to TC152", dataProvider = "SearchCustomerNegative", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustOne(String testcase, String name, String msisdn, String errmsg) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		testcasename = "MIP_webPortal_TC150 to TC152_Search_Customer";
		try {
			logger = report.startTest("Search Customer-TC150 to TC152");
			log.info("Running test case -" + testcase);
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			Thread.sleep(400);
			searchpage.enterMSISDN(msisdn).enterCustomerName(name).clickOnSearch();
			Assert.assertTrue(searchpage.getValidationMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.PASS, "Test passed");
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
		} catch (Throwable t) {
			log.info("Testcase " + testcase + " Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC154", dataProvider = "searchCriteria", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustTwo(String name, String msisdn) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC154");
			log.info("Running test case -TC154");
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			searchpage.enterMSISDN(msisdn).enterCustomerName(name).clickOnClear();
			Assert.assertTrue(searchpage.getName().equals(""));
			Assert.assertTrue(searchpage.getMsisdn().equals(""));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC154 is  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC155-TC153", dataProvider = "searchCriteria", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustThree(String name, String msisdn) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC155-TC153");
			log.info("Running test case -TC155-TC153");
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			MIP_ModifyCustomerPage modifypage = searchpage.enterMSISDN(msisdn).enterCustomerName(name).clickOnSearch()
					.clikOnCustomerRecord(msisdn);
			Assert.assertTrue(modifypage.validateModifyCustObjects());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC155-TC153 is  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC156-TC157-TC171-TC172-TC174-TC175-TC178-TC182", dataProvider = "SearchCustomerPositive", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustFour(String testcase, String name, String id, String msisdn, String dob, String gender,
			String relation, String benName, String errmsg) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC156-TC157-TC171-TC172-TC174-TC175-TC178-TC182");
			log.info("Running test case -" + testcase);
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			MIP_ModifyCustomerPage modifypage = searchpage.enterMSISDN(msisdn).clickOnSearch()
					.clikOnCustomerRecord(msisdn);
			modifypage.modifyCustInfo(name, id, dob, gender).modifyBenInfo(relation, benName).clickOnSaveChanges();
			modifypage.confirmModifyCust("yes");
			Assert.assertTrue(modifypage.getSuccessMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			modifypage.waitForElementToVisible(By.linkText("here")).click();
			Map<String, String> modifyDetails = MIP_ModifyCustomerDetails_Queries.getModifiedCustomerDetails(msisdn);
			if (!name.equals(""))
				Assert.assertTrue(modifyDetails.get("name").trim().equalsIgnoreCase(name.trim()));
			if (!gender.equals(""))
				Assert.assertTrue(modifyDetails.get("gender").trim().equalsIgnoreCase(gender));
			if (!id.equals(""))
				Assert.assertTrue(modifyDetails.get("nid").equalsIgnoreCase(id));
			if (!benName.equals(""))
				Assert.assertTrue(modifyDetails.get("ben_name".trim()).equalsIgnoreCase(benName.trim()));
			if (!relation.equals(""))
				Assert.assertTrue(modifyDetails.get("insured_relative_details".trim()).equalsIgnoreCase(relation));
			if (!dob.equals("")) {
				String[] date_data = MIP_DateFunctionality.getDate(dob, MIP_Constants.DOB_FORMAT);
				String date = modifyDetails.get("dob".trim());
				String date_format = date.replaceAll("-", "/");
				String[] date_bd = MIP_DateFunctionality.getDate(date_format, "yyyy/MM/dd");
				Assert.assertEquals(date_data[0], date_bd[2]);
				Assert.assertEquals(date_data[1], date_bd[1]);
				Assert.assertEquals(date_data[2], date_bd[0]);
			}
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC156-TC157-TC171-TC172-TC174-TC175-TC178-TC182 is  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC158-TC159-TC160-TC166-TC167-TC168-TC170-TC173-TC179-TC180-TC181", dataProvider = "ModifyCustomerNegative", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustFive(String testcase, String msisdn, String name, String id, String gender, String dob,
			String benName, String relation, String errmsg) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		testcasename = "MIP_webPortal_" + testcase + "_Search_Customer";
		try {
			logger = report
					.startTest("Search Customer-TC158-TC159-TC160-TC166-TC167-TC168-TC170-TC173-TC179-TC180-TC181");
			log.info("Running test case -" + testcase);
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			Thread.sleep(600);
			MIP_ModifyCustomerPage modifypage = searchpage.enterMSISDN(msisdn).clickOnSearch()
					.clikOnCustomerRecord(msisdn);
			modifypage.modifyCustInfo(name, id, dob, gender).modifyBenInfo(relation, benName).clickOnSaveChanges();
			if (errmsg.equalsIgnoreCase("ID:Entered ID already exists")) {
				Thread.sleep(1000);
			}
			Assert.assertTrue(modifypage.getValidationMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC158-TC159-TC160-TC166-TC167-TC168-TC170-TC173-TC179-TC180-TC181 is  Failed");
			log.info("Error occured in the test case" + testcase, t);
			throw t;
		}
	}

	@Test(testName = "TC188", dataProvider = "readyOnlyCheck", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustSix(String testcase, String msisdn) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC188");
			log.info("Running test case -" + testcase);
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			MIP_ModifyCustomerPage modifypage = searchpage.enterMSISDN(msisdn).clickOnSearch()
					.clikOnCustomerRecord(msisdn);
			Assert.assertTrue(modifypage.custInfoReadOnlyCheck());
			Assert.assertTrue(modifypage.benInfoReadOnlyCheck());
			Assert.assertTrue(modifypage.offerInfoReadOnlyCheck());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase" + testcase + "  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC189", dataProvider = "readyOnlyofferinfo", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustSeven(String msisdn) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC189");
			log.info("Running test case -TC189");
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			MIP_ModifyCustomerPage modifypage = searchpage.enterMSISDN(msisdn).clickOnSearch()
					.clikOnCustomerRecord(msisdn);
			Assert.assertTrue(modifypage.offerInfoReadOnlyCheck());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC189  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC190", dataProvider = "searchCriteria", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustEight(String name, String msisdn) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC190");
			log.info("Running test case -TC190");

			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			MIP_ModifyCustomerPage modifypage = searchpage.enterMSISDN(msisdn).clickOnSearch()
					.clikOnCustomerRecord(msisdn).clickOnClear();
			Assert.assertTrue(modifypage.getCustInfo("name").equals(""));
			Assert.assertTrue(modifypage.getCustInfo("dob").equals(""));
			Assert.assertTrue(modifypage.getCustInfo("id").equals(""));
			Assert.assertTrue(modifypage.getBenInfo("name").equals(""));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC190  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC191", dataProvider = "searchCriteria", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustNine(String name, String msisdn) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC190");
			log.info("Running test case -TC190");
			searchpage = new MIP_SearchCustomerPage(MIP_Test_Configuration.driver);
			searchpage.enterMSISDN(msisdn).clickOnSearch().clikOnCustomerRecord(msisdn).clickOnBackButton();
			Assert.assertTrue(searchpage.validateSearchCustObjects());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC191  Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
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
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
		report.endTest(logger);
		report.flush();

	}
}