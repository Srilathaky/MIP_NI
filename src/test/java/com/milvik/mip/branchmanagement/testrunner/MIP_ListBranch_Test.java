package com.milvik.mip.branchmanagement.testrunner;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
import com.milvik.mip.dataprovider.MIP_BranchManagement_TestData;
import com.milvik.mip.dbqueries.MIP_BranchManagement_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_ListBranchPage;
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

public class MIP_ListBranch_Test {
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
		log = MIP_Logging.logDetails("MIP_ListBranch_Test");
		report = new ExtentReports(".\\Test_Reports\\ListBranch_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.BRANCH);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		}
	}

	@Test(priority = 0, testName = "TC30")
	public void listBranchOne() throws Throwable {
		MIP_ListBranchPage listbranch = null;
		testcasename = "MIP_webPortal_TC30_List_Branch";
		try {
			logger = report.startTest(testcasename);
			log.info("Running test case -TC30");
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			Assert.assertTrue(MIP_BranchManagement_Queries.getNumberOfBranch() == (listbranch.getNumberofBranch()));
			logger.log(LogStatus.INFO, "Validating List Branch Page Objects");
			List<WebElement> tablecontent = listbranch.validatelistBranchObjects();
			Assert.assertTrue(tablecontent.get(0).getText().equalsIgnoreCase("Branch Code"));
			Assert.assertTrue(tablecontent.get(1).getText().equalsIgnoreCase("Branch Name"));
			Assert.assertTrue(tablecontent.get(2).getText().equalsIgnoreCase("Street"));
			Assert.assertTrue(tablecontent.get(3).getText().equalsIgnoreCase("Region"));
			Assert.assertTrue(tablecontent.get(4).getText().equalsIgnoreCase("City"));
			Assert.assertTrue(tablecontent.get(5).getText().equalsIgnoreCase("Registered Date"));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC30 Test Failed");
			log.info("Error while executing test case-TC30 " + t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC31", dataProvider = "branchName", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchTwo(String branchname) throws Throwable {
		MIP_ListBranchPage listbranch = null;
		testcasename = "MIP_webPortal_TC31_List_Branch";
		try {
			logger = report.startTest("List Branch-TC31");
			log.info("Running test case -TC31");
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			Map<String, String> branchdetails = MIP_BranchManagement_Queries.getBranchDetails(branchname);
			Assert.assertTrue(branchdetails.get("branchcode").equalsIgnoreCase(listbranch.getBranchCode(branchname)));
			logger.log(LogStatus.INFO, "Clicking on the branch " + branchname);
			listbranch.clickOnBranch(branchname);
			logger.log(LogStatus.INFO, "Validating  the branch information");
			Assert.assertTrue(branchname.equalsIgnoreCase(listbranch.getBranchInfo("name")));
			Assert.assertTrue(branchdetails.get("street").equalsIgnoreCase(listbranch.getBranchInfo("street")));
			Assert.assertTrue(branchdetails.get("region").equalsIgnoreCase(listbranch.getBranchInfo("region")));
			Assert.assertTrue(branchdetails.get("city").equalsIgnoreCase(listbranch.getBranchInfo("city")));
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC31 Test Failed");
			log.info("Error while executing test case-TC31 " + t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC32", dataProvider = "editBranchData", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchThree(String branchname, String newbranchname, String street, String region, String city)
			throws Throwable {
		MIP_ListBranchPage listbranch = null;
		testcasename = "MIP_webPortal_TC32_List_Branch";
		try {
			logger = report.startTest("List Branch-TC32");
			log.info("Running test case -TC32");
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			Map<String, String> branchdetails = MIP_BranchManagement_Queries.getBranchDetails(branchname);
			String branchCode = branchdetails.get("branchcode");
			Assert.assertTrue(branchCode.equalsIgnoreCase(listbranch.getBranchCode(branchname)));
			logger.log(LogStatus.INFO, "Editing  the branch information");
			listbranch.clickOnBranch(branchname).editBranchInfo(newbranchname, street, region, city).clickOnSave()
					.confirmOption("yes").gotoListPage();
			if (!newbranchname.equalsIgnoreCase(branchname) && !(newbranchname.equals(""))) {
				branchname = newbranchname;
			}
			logger.log(LogStatus.INFO, "Validating  the edited branch information aganist DataBase");
			listbranch.clickOnBranch(branchname);
			branchdetails = MIP_BranchManagement_Queries.getBranchDetails(branchname);
			Assert.assertTrue(branchname.equalsIgnoreCase(listbranch.getBranchInfo("name")));
			Assert.assertTrue(branchdetails.get("street").equalsIgnoreCase(listbranch.getBranchInfo("street")));
			Assert.assertTrue(branchdetails.get("region").equalsIgnoreCase(listbranch.getBranchInfo("region")));
			Assert.assertTrue(branchdetails.get("city").equalsIgnoreCase(listbranch.getBranchInfo("city")));
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC32 Test Failed");
			log.info("Error while executing test case-TC32 " + t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC33", dataProvider = "branchName", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchFour(String branchname) throws Throwable {
		MIP_ListBranchPage listbranch = null;
		testcasename = "MIP_webPortal_TC33_List_Branch";
		try {
			logger = report.startTest("List Branch-TC33");
			log.info("Running test case -TC33");
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnClear();
			logger.log(LogStatus.INFO, "Validating  Clear Button functionality in List Branch Page");
			Assert.assertTrue("".equalsIgnoreCase(listbranch.getBranchInfo("name")));
			Assert.assertTrue("".equalsIgnoreCase(listbranch.getBranchInfo("street")));
			Assert.assertTrue("".equalsIgnoreCase(listbranch.getBranchInfo("region")));
			Assert.assertTrue("".equalsIgnoreCase(listbranch.getBranchInfo("city")));
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC33 Test Failed");
			log.info("Error while executing test case-TC33 " + t);
			throw t;
		}
	}

	@Test(priority = 4, testName = "TC34", dataProvider = "branchName", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchFive(String branchname) throws Throwable {
		MIP_ListBranchPage listbranch = null;
		testcasename = "MIP_webPortal_TC34_List_Branch";
		try {
			logger = report.startTest("List Branch-TC34");
			log.info("Running test case -TC34");
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnBack();
			logger.log(LogStatus.INFO, "Validating  Back Button functionality in List Branch Page");
			Assert.assertTrue("LIST BRANCHES".equalsIgnoreCase(listbranch.validatelistbranch()));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC34 Test Failed");
			log.info("Error while executing test case-TC34 " + t);
			throw t;
		}
	}

	@Test(enabled = false, testName = "TC35", dataProvider = "branchName", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchSix(String branchname) throws Throwable {
		MIP_ListBranchPage listbranch = null;
		testcasename = "MIP_webPortal_TC35_List_Branch";
		try {
			logger = report.startTest("List Branch-TC35");
			log.info("Running test case -TC35");
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			logger.log(LogStatus.INFO, "Deactivating the Branch " + branchname);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnDeactivate().confirmOption("yes");
			logger.log(LogStatus.INFO, "Validating the result of deactivated Branch " + branchname);
			Map<String, String> branchdetails = MIP_BranchManagement_Queries.getBranchDetails(branchname);
			Assert.assertTrue(branchdetails.get("is_active").equalsIgnoreCase("0"));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC35 Test Failed");
			log.info("Error while executing test case-TC35 " + t);
			throw t;
		}
	}

	@Test(enabled = false, testName = "TC36", dataProvider = "deactivateassociatedbranch", dataProviderClass = MIP_BranchManagement_TestData.class)
	public void listBranchSeven(String branchname, String errormsg) throws Throwable {
		MIP_ListBranchPage listbranch = null;
		testcasename = "MIP_webPortal_TC36_List_Branch";
		try {
			logger = report.startTest("List Branch-TC36");
			log.info("Running test case -TC36");
			listbranch = new MIP_ListBranchPage(MIP_Test_Configuration.driver);
			listbranch.clickOnBranch(branchname);
			listbranch.clickOnDeactivate();
			logger.log(LogStatus.INFO, "Deactivating the Associated Branch " + branchname);
			Assert.assertTrue(listbranch.getValidationMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errormsg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
			log.info("TC36 Test Failed");
			log.info("Error while executing test case-TC36 " + t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
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
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_BRANCH);
		report.endTest(logger);
		report.flush();
	}
}
