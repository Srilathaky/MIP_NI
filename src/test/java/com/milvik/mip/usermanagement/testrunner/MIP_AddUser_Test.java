package com.milvik.mip.usermanagement.testrunner;

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
import com.milvik.mip.dataprovider.MIP_AddUser_TestData;
import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
import com.milvik.mip.pageobjects.MIP_AddUserPage;
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

public class MIP_AddUser_Test {
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
		log = MIP_Logging.logDetails("MIP_Add_User_Test");
		report = new ExtentReports(".\\Test_Reports\\Add_User_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
		}
	}

	@Test(testName = "TC37")
	public void addUserOne() throws Throwable {
		testcasename = "MIP_webPortal_TC37_Add_User";
		try {
			logger = report.startTest("Add User-TC37");
			log.info("Running test case - TC37");
			MIP_AddUserPage adduserpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_AddUserPage.class);
			logger.log(LogStatus.INFO, "Validating Add User Page UI elements");
			Assert.assertTrue(adduserpage.fname.isDisplayed());
			Assert.assertTrue(adduserpage.sname.isDisplayed());
			Assert.assertTrue(adduserpage.mobilenumber.isDisplayed());
			Assert.assertTrue(adduserpage.calenderIcon.isDisplayed());
			Assert.assertTrue(adduserpage.emailID.isDisplayed());
			Assert.assertTrue(adduserpage.gender_male.isDisplayed());
			Assert.assertTrue(adduserpage.gender_female.isDisplayed());
			Assert.assertTrue(adduserpage.role.isDisplayed());
			Assert.assertTrue(adduserpage.branch.isDisplayed());
			Assert.assertTrue(adduserpage.supervisoryes.isDisplayed());
			Assert.assertTrue(adduserpage.supervisorno.isDisplayed());
			Assert.assertTrue(adduserpage.save.isDisplayed());
			Assert.assertTrue(adduserpage.clear.isDisplayed());
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC37  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC38", dataProvider = "addUserData", dataProviderClass = MIP_AddUser_TestData.class)
	public void addUserPassTwo(String fname, String sname, String mobilenum, String email, String dob, String gender,
			String role, String branch, String supervisor, String succmsg) throws Throwable {
		testcasename = "MIP_webPortal_TC38_Add_User";
		try {
			logger = report.startTest("Add User-TC38");
			log.info("Running test case - TC38");
			MIP_AddUserPage adduserpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_AddUserPage.class);
			adduserpage.enterUserInfo(fname, sname, mobilenum, email, dob, gender, role, branch, supervisor)
					.clickOnSave();
			adduserpage.confirmUser("yes");
			Assert.assertTrue(adduserpage.getmessage().contains(succmsg));
			Map<String, String> userDetails = MIP_UserManagement_Queries.getUserInfo(mobilenum);
			Assert.assertTrue(userDetails.get("fname").equalsIgnoreCase(fname));
			Assert.assertTrue(userDetails.get("sname").equalsIgnoreCase(sname));
			Assert.assertTrue(userDetails.get("gender").equalsIgnoreCase(gender));
			Assert.assertTrue(userDetails.get("email_id").equalsIgnoreCase(email));
			Assert.assertTrue(userDetails.get("is_supervisor").equalsIgnoreCase(supervisor));
			Assert.assertTrue(userDetails.get("role_name").equalsIgnoreCase(role));
			Assert.assertTrue(userDetails.get("branch_name").equalsIgnoreCase(branch));

			Assert.assertTrue(userDetails.get("user_uid").contains(MIP_AdminConfig_Queries.getLoginPrefix().trim()));
			if (!dob.equals("")) {
				String[] date_data = MIP_DateFunctionality.getDate(dob, MIP_Constants.DOB_FORMAT);
				String date = userDetails.get("dob".trim());
				String date_format = date.replaceAll("-", "/");
				String[] date_bd = MIP_DateFunctionality.getDate(date_format, "yyyy/MM/dd");
				Assert.assertEquals(date_data[0], date_bd[2]);
				Assert.assertEquals(date_data[1], date_bd[1]);
				Assert.assertEquals(date_data[2], date_bd[0]);
			}
			adduserpage.waitForElementToVisible(By.linkText("here")).click();
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC38  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC40", dataProvider = "addUserData", dataProviderClass = MIP_AddUser_TestData.class)
	public void addUserPassThree(String fname, String sname, String mobilenum, String email, String dob, String gender,
			String role, String branch, String supervisor, String succmsg) throws Throwable {
		testcasename = "MIP_webPortal_TC40_Add_User";
		try {
			logger = report.startTest("Add User-TC40");
			log.info("Running test case - TC40");
			MIP_AddUserPage adduserpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_AddUserPage.class);
			adduserpage.enterUserInfo(fname, sname, mobilenum, email, dob, gender, role, branch, supervisor)
					.clickOnClear();
			logger.log(LogStatus.INFO, "Validating Clear Button Functionality in Add User Page");
			Assert.assertTrue(adduserpage.fname.getText().equals(""));
			Assert.assertTrue(adduserpage.sname.getText().equals(""));
			Assert.assertTrue(adduserpage.mobilenumber.getText().equals(""));
			Assert.assertTrue(adduserpage.dob.getText().equals(""));
			Assert.assertTrue(adduserpage.emailID.getText().equals(""));
			Assert.assertTrue(!adduserpage.gender_male.isSelected());
			Assert.assertTrue(!adduserpage.gender_female.isSelected());
			Assert.assertTrue(!adduserpage.supervisoryes.isSelected());
			Assert.assertTrue(!adduserpage.supervisorno.isSelected());
			Assert.assertTrue(adduserpage.getSelectedOptions(adduserpage.role).equals(""));
			Assert.assertTrue(adduserpage.getSelectedOptions(adduserpage.branch).equals(""));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase TC40  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC39-TC41 to TC47", dataProvider = "addUserNegativeData", dataProviderClass = MIP_AddUser_TestData.class)
	public void addUserPassFour(String testcase, String fname, String sname, String mobilenum, String email, String dob,
			String gender, String role, String branch, String supervisor, String errmsg) throws Throwable {
		testcasename = "MIP_webPortal_TC39-TC41 to TC47_Add_User";
		try {
			logger = report.startTest("Add User-TC39-TC41 to TC47");
			log.info("Running test case - " + testcase);
			MIP_AddUserPage adduserpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_AddUserPage.class);
			adduserpage.enterUserInfo(fname, sname, mobilenum, email, dob, gender, role.trim(), branch, supervisor)
					.clickOnSave();
			String errorMsg = adduserpage.getValidationMsg();
			log.info("Expected Error Message " + errmsg);
			log.info("Found Error Message " + errorMsg);
			logger.log(LogStatus.INFO, "Validating Add User Test with Negative Test data");
			Assert.assertTrue(
					errorMsg.trim().replaceAll("\\s", "").equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.PASS, "Test passed");
		} catch (Throwable t) {
			log.info("Testcase" + testcase + " Failed");
			log.info("Error occured in the test case " + testcase, t);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
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
