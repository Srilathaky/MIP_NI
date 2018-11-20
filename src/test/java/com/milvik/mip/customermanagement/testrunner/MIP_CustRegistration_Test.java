package com.milvik.mip.customermanagement.testrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import com.milvik.mip.dataprovider.MIP_RegCust_TestData;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_RegisterCustomerPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_RegularExpression;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_CustRegistration_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;
	String testcaseName;
	MIP_RegisterCustomerPage regpage = null;

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser, @Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_CustRegistration_Test");
		report = new ExtentReports(".\\Test_Reports\\RegisterCustomer_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);

			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		}
	}

	@Test(testName = "TC88-TC102", dataProvider = "Customer_Registartion_validateobject", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustOne(String msisdn) throws Throwable {
		MIP_RegisterCustomerPage regpage = null;
		testcaseName = "MIP_webPortal_TC88-TC102_Register_Customer";
		try {
			logger = report.startTest("Register Customer-TC88-TC102");
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(msisdn).clickOnSearchButton();
			logger.log(LogStatus.INFO, "Validating Register Customer Page Objects");
			Assert.assertTrue(regpage.validateRegCustObjects());
			Assert.assertTrue(regpage.msisdnReadOnlyCheck().equalsIgnoreCase("true"));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("TC88-TC102 Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC89", dataProvider = "RegPrepaidActive", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustTwo(String mobilenum, String offer, String benefitlevel, String cust_name, String id, String dob,
			String gender, String relation, String ben_name, String msg, String Type, String is_active,
			String is_prepaid, String plan_code, String offer_cover_id, String registration_channel_id, String offer_id,
			String sms) throws Throwable {
		MIP_RegisterCustomerPage regpage = null;
		testcaseName = "MIP_webPortal_TC89_Register_Customer--" + mobilenum;
		try {
			logger = report.startTest("Register Customer-TC89--" + mobilenum);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(mobilenum).clickOnSearchButton();
			logger.log(LogStatus.INFO, "Entering Customer Information");
			logger.log(LogStatus.INFO, "Entering Beneficiary  Information");
			regpage.selectOfferInfo(offer, benefitlevel).enterCustInfo(cust_name, id, dob, gender)
					.enterBenInfo(relation, ben_name).clickOnSave().confirmCustReg("yes");
			logger.log(LogStatus.INFO, "Validating success Message after Registration");
			Assert.assertTrue(regpage.getSuccessMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(msg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.INFO, "Validating Customer Registration aganist DataBase");
			Map<String, String> custinfo = MIP_RegisterCustomer_Queries.getCustomerDetails(mobilenum);
			Assert.assertTrue(custinfo.get("Type").equalsIgnoreCase(Type));
			Assert.assertTrue(custinfo.get("is_active").equalsIgnoreCase(is_active));
			Assert.assertTrue(custinfo.get("is_prepaid").equalsIgnoreCase(is_prepaid));
			Assert.assertTrue(custinfo.get("plan_code").equalsIgnoreCase(plan_code));
			Map<String, String> offerinfo = MIP_RegisterCustomer_Queries.getOfferSubscription(mobilenum);
			Assert.assertTrue(offerinfo.get("offer_cover_id").equalsIgnoreCase(offer_cover_id));
			Assert.assertTrue(offerinfo.get("registration_channel_id").equalsIgnoreCase(registration_channel_id));
			Assert.assertTrue(offerinfo.get("offer_id").equalsIgnoreCase(offer_id));
			Map<String, String> beninfo = MIP_RegisterCustomer_Queries.getBeninfo(mobilenum);
			Assert.assertTrue(beninfo.get("name").equalsIgnoreCase(ben_name));
			Assert.assertTrue(beninfo.get("cust_relationship").equalsIgnoreCase(relation));
			/*
			 * Assert.assertTrue(MIP_RegisterCustomer_Queries
			 * .getSMSText(mobilenum).trim().replaceAll("\\s", "")
			 * .equalsIgnoreCase(sms.trim().replaceAll("\\s", "")));
			 */

			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {

			log.info("TC89 Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC90", dataProvider = "RegPrepaidSuspend", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustThree(String mobilenum, String offer, String benefitlevel, String cust_name, String id,
			String dob, String gender, String relation, String ben_name, String msg, String Type, String is_active,
			String is_prepaid, String plan_code, String offer_cover_id, String registration_channel_id, String offer_id,
			String sms) throws Throwable {
		testcaseName = "MIP_webPortal_TC90_Register_Customer--" + mobilenum;
		try {
			logger = report.startTest("Register Customer-TC90");

			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(mobilenum).clickOnSearchButton();
			logger.log(LogStatus.INFO, "Entering Customer Information");
			logger.log(LogStatus.INFO, "Entering Beneficiary  Information");
			regpage.selectOfferInfo(offer, benefitlevel).enterCustInfo(cust_name, id, dob, gender)
					.enterBenInfo(relation, ben_name).clickOnSave().confirmCustReg("yes");
			Assert.assertTrue(regpage.getSuccessMsg().trim().equalsIgnoreCase(msg));
			logger.log(LogStatus.INFO, "Validating Customer Registration aganist DataBase");
			Map<String, String> custinfo = MIP_RegisterCustomer_Queries.getCustomerDetails(mobilenum);
			Assert.assertTrue(custinfo.get("Type").equalsIgnoreCase(Type));
			Assert.assertTrue(custinfo.get("is_active").equalsIgnoreCase(is_active));
			Assert.assertTrue(custinfo.get("is_prepaid").equalsIgnoreCase(is_prepaid));
			Assert.assertTrue(custinfo.get("plan_code").equalsIgnoreCase(plan_code));
			Map<String, String> offerinfo = MIP_RegisterCustomer_Queries.getOfferSubscription(mobilenum);
			Assert.assertTrue(offerinfo.get("offer_cover_id").equalsIgnoreCase(offer_cover_id));
			Assert.assertTrue(offerinfo.get("registration_channel_id").equalsIgnoreCase(registration_channel_id));
			Assert.assertTrue(offerinfo.get("offer_id").equalsIgnoreCase(offer_id));

			Map<String, String> beninfo = MIP_RegisterCustomer_Queries.getBeninfo(mobilenum);
			Assert.assertTrue(beninfo.get("name").equalsIgnoreCase(ben_name));
			Assert.assertTrue(beninfo.get("cust_relationship").equalsIgnoreCase(relation));

			/*
			 * Assert.assertTrue(MIP_RegisterCustomer_Queries.getSMSText(
			 * mobilenum ) .trim().replaceAll("\\s", "")
			 * .equalsIgnoreCase(sms.trim().replaceAll("\\s", "")));
			 */
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("TC90 Test case Failed");

			log.info("Error occured in the test case", t);
			throw t;

		}
	}

	@Test(testName = "TC91-TC92-TC96-TC97-TC98", dataProvider = "NotAllowedState", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustFour(String msisdn, String errmsg) throws Throwable {
		testcaseName = "MIP_webPortal_TC91-TC92-TC96-TC97-TC98_Register_Customer--" + msisdn;
		try {
			logger = report.startTest("Register Customer-TC91");

			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(msisdn).clickOnSearchButton();

			logger.log(LogStatus.INFO, "Customer Registration test for NotAllowedState");

			Assert.assertTrue(regpage.getErrorMsg().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));

			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("TC91-TC92-TC96-TC97-TC98 test case Failed");

			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC93-TC94-TC95", dataProvider = "Customer_Registartion", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustFive(String mobilenum, String offer, String benefitlevel, String cust_name, String id,
			String dob, String gender, String relation, String ben_name, String msg, String Type, String is_active,
			String is_prepaid, String plan_code, String offer_cover_id, String registration_channel_id, String offer_id,
			String sms) throws Throwable {
		testcaseName = "MIP_webPortal_TC93-TC94-TC95_Register_Customer--" + mobilenum;
		try {
			logger = report.startTest("Register Customer-TC93-TC94-TC95");

			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(mobilenum).clickOnSearchButton();
			logger.log(LogStatus.INFO, "Entering Customer Information");
			logger.log(LogStatus.INFO, "Entering Beneficiary  Information");
			regpage.selectOfferInfo(offer, benefitlevel).enterCustInfo(cust_name, id, dob, gender)
					.enterBenInfo(relation, ben_name).clickOnSave().confirmCustReg("yes");
			Assert.assertTrue(regpage.getSuccessMsg().trim().equalsIgnoreCase(msg));
			logger.log(LogStatus.INFO, "Validating Customer Registration aganist DataBase");
			Map<String, String> custinfo = MIP_RegisterCustomer_Queries.getCustomerDetails(mobilenum);
			Assert.assertTrue(custinfo.get("Type").equalsIgnoreCase(Type));
			Assert.assertTrue(custinfo.get("is_active").equalsIgnoreCase(is_active));
			Assert.assertTrue(custinfo.get("is_prepaid").equalsIgnoreCase(is_prepaid));
			Assert.assertTrue(custinfo.get("plan_code").equalsIgnoreCase(plan_code));
			Map<String, String> offerinfo = MIP_RegisterCustomer_Queries.getOfferSubscription(mobilenum);
			Assert.assertTrue(offerinfo.get("offer_cover_id").equalsIgnoreCase(offer_cover_id));
			Assert.assertTrue(offerinfo.get("registration_channel_id").equalsIgnoreCase(registration_channel_id));
			Assert.assertTrue(offerinfo.get("offer_id").equalsIgnoreCase(offer_id));

			Map<String, String> beninfo = MIP_RegisterCustomer_Queries.getBeninfo(mobilenum);
			Assert.assertTrue(beninfo.get("name").equalsIgnoreCase(ben_name));
			Assert.assertTrue(beninfo.get("cust_relationship").equalsIgnoreCase(relation));

			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("TC93-TC94-TC95 test case failed");

			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC99", dataProvider = "NegativeTest", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustSix(String mobilenum, String errormsg) throws Throwable {
		testcaseName = "MIP_webPortal_TC99_Register_Customer--" + mobilenum;
		try {
			logger = report.startTest("Register Customer-TC99");
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(mobilenum).clickOnSearchButton();
			logger.log(LogStatus.INFO, "Validating Customer Registration with Negative Test Data");
			Assert.assertTrue(regpage.getValidationMsg().equalsIgnoreCase(errormsg));
		} catch (Throwable t) {
			log.info("TC99 test case failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC100", dataProvider = "BlankMobileNum", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustSeven(String errormsg) throws Throwable {
		testcaseName = "MIP_webPortal_TC100_Register_Customer";
		try {
			logger = report.startTest("Register Customer-TC100");
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN("").clickOnSearchButton();
			logger.log(LogStatus.INFO, "Register Customer Functionality with Blank Mobile Number");
			Assert.assertTrue(regpage.getValidationMsg().equalsIgnoreCase(errormsg));
		} catch (Throwable t) {
			log.info("TC100 test case failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC-118-TC-122-TC-124-TC125-TC-127-TC128-TC130 to TC138,TC140", dataProvider = "MIP_Customer_Registration_Positive_Scenario", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustEight(String testcase, String mobilenum, String offer, String benefitlevel, String cust_name,
			String id, String dob, String gender, String relation, String ben_name, String msg) throws Throwable {
		testcaseName = "MIP_webPortal_TC-118-TC-122-TC-124-TC125-TC-127-TC128-TC130 to TC138,TC140_Register_Customer--"
				+ mobilenum;
		try {
			logger = report.startTest("Register Customer-TC-122-TC-124-TC125-TC-127-TC128-TC130 to TC138");
			log.info("Running test case " + testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			logger.log(LogStatus.INFO, "Entering Customer Information");
			logger.log(LogStatus.INFO, "Entering Beneficiary  Information");
			regpage.enterMSISDN(mobilenum).clickOnSearchButton();
			regpage.selectOfferInfo(offer, benefitlevel).enterCustInfo(cust_name, id, dob, gender)
					.enterBenInfo(relation.trim(), ben_name).clickOnSave().confirmCustReg("yes");
			String actual = regpage.getSuccessMsg().trim();

			Assert.assertTrue(actual.equalsIgnoreCase(msg));
			Map<String, String> beninfo = MIP_RegisterCustomer_Queries.getBeninfo(mobilenum);
			Assert.assertTrue(beninfo.get("cust_relationship").equalsIgnoreCase(relation));
			Map<String, String> custinfo = MIP_RegisterCustomer_Queries.getCustomerDetails(mobilenum);
			Assert.assertTrue(custinfo.get("gender").equalsIgnoreCase(gender));
			log.info("test case " + testcase + " passed ");

		} catch (Throwable t) {

			log.info("TC-118-TC-122-TC-124-TC125-TC-127-TC128-TC130 to TC138 test case failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC115 to TC117-TC119-TC120-TC121-TC123-TC126-TC129,T141 to TC143", dataProvider = "Customer_Registartion_NegativeScenarios", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustNine(String testNo, String msisdn, String offer, String level, String Cus_name, String ID,
			String dob, String gender, String relation, String ben_name, String msg) throws Throwable {
		testcaseName = "MIP_webPortal_TC115 to TC117-TC119-TC120-TC121-TC123-TC126-TC129,T141 to TC143_Register_Customer--"
				+ msisdn;

		try {
			logger = report.startTest("Register Customer--" + testNo + "--" + msisdn);
			log.info("Execting Test Case " + testNo);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(msisdn).clickOnSearchButton();
			if (MIP_RegisterCustomer_Queries.getCustomerID(ID) || ID.length() < MIP_RegisterCustomerPage.NIDLENGTH) {
				regpage.selectOfferInfo(offer, level).enterCustInfo(Cus_name, ID, dob, gender)
						.enterBenInfo(relation.trim(), ben_name);
				String actual = regpage.getValidationMsg();
				/*
				 * String[] act_str = actual.split(":"); String[] output =
				 * msg.split(":");
				 * Assert.assertTrue(act_str[0].trim().equalsIgnoreCase(
				 * output[0].trim()));
				 * Assert.assertTrue(act_str[1].trim().equalsIgnoreCase(
				 * output[1].trim()));
				 */

				Assert.assertTrue(actual.trim().equalsIgnoreCase(msg.trim()));
				log.info(" Test Case- " + testNo + "pass");
			} else {
				regpage.selectOfferInfo(offer, level).enterCustInfo(Cus_name, ID, dob, gender)
						.enterBenInfo(relation.trim(), ben_name).clickOnSave();
				if (Cus_name == "" || Cus_name.length() < 3 || MIP_RegularExpression.custNameMatcher(Cus_name)
						|| MIP_RegularExpression.idMatcher(ID) || gender.equals("")
						|| MIP_RegularExpression.custNameMatcher(ben_name) || relation.equals("")) {
					String actual = regpage.getValidationMsg();
					String[] act_str = actual.split(":");
					String[] output = msg.split(":");

					Assert.assertTrue(act_str[0].trim().equalsIgnoreCase(output[0].trim()));
					Assert.assertTrue(act_str[1].trim().equalsIgnoreCase(output[1].trim()));
					log.info(" Test Case- " + testNo + "pass");
				} else if (((MIP_DateFunctionality.getCurrentYear() - MIP_DateFunctionality.exractYear(dob)) > 69
						|| (MIP_DateFunctionality.getCurrentYear() - MIP_DateFunctionality.exractYear(dob)) < 18)) {
					String actual = regpage.getValidationMsg();
					String[] act_str = actual.split(":");
					String[] output = msg.split(":");

					Assert.assertTrue(act_str[0].trim().equalsIgnoreCase(output[0].trim()));
					Assert.assertTrue(act_str[1].trim().equalsIgnoreCase(output[1].trim()));
					log.info(" Test Case- " + testNo + "pass");
				}
			}
		} catch (Throwable t) {

			log.info("TC115 to TC117-TC119-TC120-TC121-TC123-TC126-TC129 test case failed");
			log.info("Error occured in the test case", t);
			throw t;
		}

	}

	@Test(testName = "TC103-TC104", dataProvider = "MSISDN_Validatin", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustTen(String msisdn, String msg) throws Throwable {
		testcaseName = "MIP_webPortal_TC103-TC104_Register_Customer--" + msisdn;
		try {
			logger = report.startTest("Register Customer-TC88");
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(msisdn).clickOnSearchButton();

			logger.log(LogStatus.INFO, "Validating MSISDN in Customer Registration Page");
			String actual = regpage.getValidationMsg();
			String[] act_str = actual.split(":");
			String[] output = msg.split(":");

			Assert.assertTrue(act_str[0].trim().equalsIgnoreCase(output[0].trim()));
			Assert.assertTrue(act_str[1].trim().equalsIgnoreCase(output[1].trim()));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {

			log.info("TC103-TC104 test case failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC101", dataProvider = "AlreadyExistingCust", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustEleven(String msisdn) {
		testcaseName = "MIP_webPortal_TC101_Register_Customer--" + msisdn;
		try {
			logger = report.startTest("Register Customer-TC88");
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(msisdn).clickOnSearchButton();
			logger.log(LogStatus.INFO, "Customer Registration for Existing Customer");
			Assert.assertTrue(regpage.msisdnReadOnlyCheck().equalsIgnoreCase("true"));
			Assert.assertTrue(regpage.offerInfoReadOnlyCheck());
			Assert.assertTrue(regpage.custInfoReadOnlyCheck());
			Assert.assertTrue(regpage.benInfoReadOnlyCheck());
			Assert.assertTrue(regpage.verifyBackButton());

			logger.log(LogStatus.PASS, "Test passed");
		} catch (Exception e) {

			log.info("TC101 test case failed");
		}
	}

	@Test(testName = "TC106", dataProvider = "BenefitLevels", dataProviderClass = MIP_RegCust_TestData.class)
	public void regCustTwelve1(String msisdn, String type, String pre_level, String pos_level) throws Throwable {
		testcaseName = "MIP_webPortal_TC106_Register_Customer--" + msisdn;
		try {
			logger = report.startTest("Register Customer-TC88");
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN(msisdn).clickOnSearchButton();
			logger.log(LogStatus.INFO, "Validating Benefit Level in Register Customer Page");
			List<WebElement> ele = regpage.getBenefitleveloption();
			List<String> ben_option = new ArrayList<String>();
			for (int i = 1; i < ele.size(); i++) {
				ben_option.add(ele.get(i).getText());
			}
			if (type.equalsIgnoreCase("PRE")) {
				Assert.assertEquals(pre_level, ben_option.toString());
			} else {
				Assert.assertEquals(pos_level, ben_option.toString());
			}

			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {

			log.info("TC106 test case failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {
		if (res.getStatus() == ITestResult.FAILURE) {
			String path = MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName() + "_" + testcaseName);
			logger.log(LogStatus.FAIL, "----Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} else {
			logger.log(LogStatus.PASS, testcaseName + "----Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		else
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
		report.endTest(logger);
		report.flush();
	}

}
