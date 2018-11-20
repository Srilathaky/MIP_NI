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

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_CustomerManagement_TestData;
import com.milvik.mip.dbqueries.MIP_DeRegisterCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_DeRegisterCustomerPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
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

public class MIP_DeRegisterCustomer_Test {
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	static Logger log;

	@BeforeTest
	@Parameters({ "browser" })
	public void test_setup(@Optional("firefox") String browser) {
		log = MIP_Logging.logDetails("MIP_DeRegisterCustomer_Test");
		report = new ExtentReports(
				".\\Test_Reports\\MIP_DeRegisterCustomer_Test.html");
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

	@Test(testName = "TC297")
	public void deRegCustOne() throws Throwable {
		MIP_DeRegisterCustomerPage deregcust = null;
		try {
			logger = report.startTest("DeRegister Customer-TC297");
			log.info("Running the test case TC297");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			deregcust = PageFactory.initElements(driver,
					MIP_DeRegisterCustomerPage.class);
			Assert.assertTrue(deregcust.mobileNum.isDisplayed());
			Assert.assertTrue(deregcust.searchIcon.isDisplayed());
			deregcust.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			deregcust.clickonLogout();
			log.info("TC297 Test Failed");
			log.info("Error occured in the test case TC297", t);
			throw t;
		}
	}

	@Test(testName = "TC298-TC299", dataProvider = "deRegisterCust", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void deRegCustTwo(String msisdn) throws Throwable {
		MIP_DeRegisterCustomerPage deregcust = null;
		try {
			logger = report.startTest("DeRegister Customer-TC298-TC299");
			log.info("Running the test case TC298-TC299");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			deregcust = PageFactory.initElements(driver,
					MIP_DeRegisterCustomerPage.class);
			deregcust.enterMsisdn(msisdn);
			deregcust.clickOnSearchButton();
			Assert.assertTrue(deregcust.validateOfferInfo());
			Assert.assertTrue(deregcust.validateBackBtn());
			Assert.assertTrue(deregcust.validateDeRegisterBtn());
			Assert.assertTrue(deregcust.validateDeRegTableHeading());
			Map<String, String> userdetails = MIP_DeRegisterCustomer_Queries
					.getCustomerInfo(msisdn);
			Assert.assertTrue(userdetails.get("cust_name").trim()
					.equalsIgnoreCase(deregcust.getCustomerName().trim()));
			Assert.assertTrue(userdetails.get("offer_name").equalsIgnoreCase(
					deregcust.getOfferName()));
			String[] str = deregcust.getOfferLevel().split("\\,");
			String offer_level = "";
			for (int i = 0; i < str.length; i++) {
				offer_level = offer_level + str[i];
			}

			Assert.assertTrue(offer_level.contains(userdetails.get(
					"offer_level").split("\\.")[0]));
			Assert.assertTrue(userdetails.get("offer_cover").contains(
					deregcust.getCoverEarned()));
			System.out.println(userdetails.get("deducted_amount"));
			System.out.println(deregcust.getDeductedAmount());
			Assert.assertTrue(deregcust.getDeductedAmount().contains(
					userdetails.get("deducted_amount")));
			Assert.assertTrue(userdetails.get("confirmation").equalsIgnoreCase(
					deregcust.getConfirmationStatus()));
			deregcust.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			deregcust.clickonLogout();
			log.info("TC298-TC299 Test Failed");
			log.info("Error occured in the test case TC298-TC299", t);
			throw t;
		}
	}

	@Test(testName = "TC315", dataProvider = "deRegisterCust", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void deRegCustThree(String msisdn) throws Throwable {
		MIP_DeRegisterCustomerPage deregcust = null;
		try {
			logger = report.startTest("DeRegister Customer-TC298-TC299");
			log.info("Running the test case TC298-TC299");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			deregcust = PageFactory.initElements(driver,
					MIP_DeRegisterCustomerPage.class);
			deregcust.enterMsisdn(msisdn);
			deregcust.clickOnSearchButton();
			deregcust.clickOnBackBtn();
			deregcust.waitForElementToVisible(By.id("msisdn"));
			Assert.assertTrue(deregcust.mobileNum.isDisplayed());
			Assert.assertTrue(deregcust.searchIcon.isDisplayed());
			deregcust.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			deregcust.clickonLogout();
			log.info("TC298-TC299 Test Failed");
			log.info("Error occured in the test case TC298-TC299", t);
			throw t;
		}
	}

	@Test(testName = "TC304-TC314", dataProvider = "msisdnValidation", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void deRegCustFour(String msisdn, String errorMessage)
			throws Throwable {
		MIP_DeRegisterCustomerPage deregcust = null;
		try {
			logger = report.startTest("DeRegister Customer-TC298-TC299");
			log.info("Running the test case TC298-TC299");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			deregcust = PageFactory.initElements(driver,
					MIP_DeRegisterCustomerPage.class);
			deregcust.enterMsisdn(msisdn);
			deregcust.clickOnSearchButton();
			Assert.assertTrue(deregcust.getValidationMessage().trim()
					.equalsIgnoreCase(errorMessage.trim()));
			deregcust.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			deregcust.clickonLogout();
			log.info("TC298-TC299 Test Failed");
			log.info("Error occured in the test case TC298-TC299", t);
			throw t;
		}
	}

	@Test(testName = "TC300-tc301", dataProvider = "deRegisterCustomerPositive", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void deRegCustFive(String testcase, String msisdn, String offer,
			String successMessage, String churntype, String refundAmt,
			String sms) throws Throwable {
		MIP_DeRegisterCustomerPage deregcust = null;
		try {
			logger = report.startTest("DeRegister Customer-TC300");
			log.info("Running the test case TC300");
			MIP_LoginPage loginpage = PageFactory.initElements(driver,
					MIP_LoginPage.class);
			MIP_HomePage homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.changeLanguage("Inglés").clickOnMenu(
					MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			deregcust = PageFactory.initElements(driver,
					MIP_DeRegisterCustomerPage.class);
			deregcust.enterMsisdn(msisdn);
			deregcust.clickOnSearchButton();
			String cust_id = MIP_DeRegisterCustomer_Queries.getCustID(msisdn);
			deregcust.selectOffer(offer.trim()).clickOnDeRegisterBtn()
					.confirmCustManagementPopup("yes");
			successMessage = successMessage.replaceAll("XXXXXXXX", msisdn);
			Assert.assertTrue(deregcust.getSuccessMessage().trim()
					.equalsIgnoreCase(successMessage.trim()));
			Assert.assertEquals(MIP_DeRegisterCustomer_Queries
					.getDeRegsterCustInCustDetails(msisdn), 0);
			Assert.assertEquals(MIP_DeRegisterCustomer_Queries
					.getDeRegsterCustInOferSubscription(cust_id), 0);
			Map<String, String> deRegCustDetails = MIP_DeRegisterCustomer_Queries
					.getDeregisterCustInfo(msisdn);
			Assert.assertTrue(deRegCustDetails.get("bc_churn_type")
					.equalsIgnoreCase(churntype));
			Assert.assertTrue(deRegCustDetails.get("sms_text")
					.equalsIgnoreCase(sms));
			Assert.assertTrue(deRegCustDetails.get("oc_churn_type")
					.equalsIgnoreCase(churntype));
			String currentDate = MIP_DateFunctionality
					.getCurrentDate("YYYY-MM-DD");
			Assert.assertTrue(deRegCustDetails.get("bc_date").equalsIgnoreCase(
					currentDate));
			Assert.assertTrue(deRegCustDetails.get("oc_date").equalsIgnoreCase(
					currentDate));
			Assert.assertTrue(deRegCustDetails.get("record_deletion_date")
					.equalsIgnoreCase(currentDate));
			Assert.assertTrue(deRegCustDetails.get("refund_amount")
					.equalsIgnoreCase(refundAmt));

			deregcust.clickonLogout();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			deregcust.clickonLogout();
			log.info("TC300 Test Failed");
			log.info("Error occured in the test case TC300", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
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
