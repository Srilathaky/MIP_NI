package login_multiuser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_RegisterCustomerPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MultiUser_Test extends TestConfig {
	// static WebDriver driver;
	static MIP_LoginPage loginpage = null;
	static MIP_HomePage homepage = null;

	static String testcasename;

	@Test
	@Parameters({ "username", "password" })
	public void login_test1(String username, String password) throws InterruptedException {
		testcasename = "TC_" + username;
		WebDriver driver1 = null;
		MIP_RegisterCustomerPage regpage = null;
		try {
			TestConfig.report = new ExtentReports(".\\Test_Reports\\MultiUser_" + username + "_Test.html");
			driver1 = MIP_BrowserFactory.openBrowser(driver1, "chrome");
			MIP_ReadPropertyFile.loadProperty("config");
			MIP_LaunchApplication.openApplication(driver1);

			TestConfig.logger = TestConfig.report.startTest("MultiUser_" + username);
			loginpage = PageFactory.initElements(driver1, MIP_LoginPage.class);
			homepage = loginpage.login(username, password);
			Thread.sleep(1000);
			homepage.changeLanguage("Inglés");
			Thread.sleep(3000);
			homepage.waitForElementTobeClickable(By.linkText(MIP_Menu_Constants.CUSTOMER)).click();
			homepage.waitForElementTobeClickable(By.linkText(MIP_Menu_Constants.REGISTER_CUSTOMER)).click();
			regpage = PageFactory.initElements(driver1, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN("25185000").clickOnSearchButton();
			regpage.selectOfferInfo("Life Insurance", "80,000")
					.enterCustInfo("gint kin", "12458679889654i0", "12/05/1989", "FEMALE")
					.enterBenInfo("Father", "ben name").clickOnSave().confirmCustReg("yes");
			Assert.assertTrue(
					regpage.getSuccessMsg().trim().equalsIgnoreCase("The customer has been registered successfully."));

		} catch (Exception e) {
			throw e;
		} finally {
			driver1.quit();
			// TestConfig.driver.quit();
		}
	}

	@Test
	@Parameters({ "username", "password" })
	public void login_test2(String username, String password) throws InterruptedException {
		// WebDriver driver = null;
		testcasename = "TC_" + username;
		WebDriver driver1 = null;
		MIP_RegisterCustomerPage regpage = null;
		try {
			TestConfig.report = new ExtentReports(".\\Test_Reports\\MultiUser_" + username + "_Test.html");

			driver1 = MIP_BrowserFactory.openBrowser(driver1, "chrome");
			MIP_ReadPropertyFile.loadProperty("config");
			MIP_LaunchApplication.openApplication(driver1);

			TestConfig.logger = TestConfig.report.startTest("MultiUser_" + username);
			loginpage = PageFactory.initElements(driver1, MIP_LoginPage.class);
			homepage = loginpage.login(username, password);
			Thread.sleep(1000);
			homepage.changeLanguage("Inglés");
			Thread.sleep(5000);
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			regpage = PageFactory.initElements(driver1, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN("25185001").clickOnSearchButton();
			Thread.sleep(5000);
			regpage.selectOfferInfo("Life Insurance", "80,000")
					.enterCustInfo("gint kin", "12458679889654i1", "12/05/1989", "FEMALE")
					.enterBenInfo("Father", "ben name").clickOnSave().confirmCustReg("yes");
			Assert.assertTrue(
					regpage.getSuccessMsg().trim().equalsIgnoreCase("The customer has been registered successfully."));
		} catch (Exception e) {
			throw e;
		} finally {
			driver1.quit();
		}
	}

	@Test
	@Parameters({ "username", "password" })
	public void login_test3(String username, String password) throws InterruptedException {
		// WebDriver driver = null;
		testcasename = "TC_" + username;
		MIP_RegisterCustomerPage regpage = null;
		WebDriver driver1 = null;
		try {
			TestConfig.report = new ExtentReports(".\\Test_Reports\\MultiUser_" + username + "_Test.html");

			driver1 = MIP_BrowserFactory.openBrowser(driver1, "firefox");
			MIP_ReadPropertyFile.loadProperty("config");
			MIP_LaunchApplication.openApplication(driver1);

			TestConfig.logger = TestConfig.report.startTest("MultiUser_" + username);
			loginpage = PageFactory.initElements(driver1, MIP_LoginPage.class);
			homepage = loginpage.login(username, password);
			Thread.sleep(1000);
			homepage.changeLanguage("Inglés");
			Thread.sleep(5000);
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			regpage = PageFactory.initElements(driver1, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN("25185002").clickOnSearchButton();
			Thread.sleep(5000);
			regpage.selectOfferInfo("Life Insurance", "80,000")
					.enterCustInfo("gint kin", "12458679889654i2", "12/05/1989", "FEMALE")
					.enterBenInfo("Father", "ben name").clickOnSave().confirmCustReg("yes");
			Assert.assertTrue(
					regpage.getSuccessMsg().trim().equalsIgnoreCase("The customer has been registered successfully."));
		} catch (Exception e) {
			throw e;
		} finally {
			driver1.quit();
		}
	}

	@Test
	@Parameters({ "username", "password" })
	public void login_test4(String username, String password) throws InterruptedException {
		// WebDriver driver = null;
		testcasename = "TC_" + username;
		MIP_RegisterCustomerPage regpage = null;
		WebDriver driver1 = null;
		try {
			TestConfig.report = new ExtentReports(".\\Test_Reports\\MultiUser_" + username + "_Test.html");

			driver1 = MIP_BrowserFactory.openBrowser(driver1, "firefox");
			MIP_ReadPropertyFile.loadProperty("config");
			MIP_LaunchApplication.openApplication(driver1);

			TestConfig.logger = TestConfig.report.startTest("MultiUser_" + username);
			loginpage = PageFactory.initElements(driver1, MIP_LoginPage.class);
			homepage = loginpage.login(username, password);
			Thread.sleep(1000);
			homepage.changeLanguage("Inglés");
			Thread.sleep(5000);
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			regpage = PageFactory.initElements(driver1, MIP_RegisterCustomerPage.class);
			regpage.enterMSISDN("25185003").clickOnSearchButton();
			Thread.sleep(5000);
			regpage.selectOfferInfo("Life Insurance", "80,000")
					.enterCustInfo("gint kin", "12458679889654i3", "12/05/1989", "FEMALE")
					.enterBenInfo("Father", "ben name").clickOnSave().confirmCustReg("yes");
			Assert.assertTrue(
					regpage.getSuccessMsg().trim().equalsIgnoreCase("The customer has been registered successfully."));
		} catch (Exception e) {
			throw e;
		} finally {
			driver1.quit();
		}
	}

	@Test
	@Parameters({ "username", "password" })
	public void login_test5(String username, String password) throws InterruptedException {
		// WebDriver driver = null;
		testcasename = "TC_" + username;
		WebDriver driver1 = null;
		try {
			TestConfig.report = new ExtentReports(".\\Test_Reports\\MultiUser_" + username + "_Test.html");
			/*
			 * driver = MIP_BrowserFactory.openBrowser(driver, "firefox");
			 * MIP_ReadPropertyFile.loadProperty("config");
			 * MIP_LaunchApplication.openApplication(driver);
			 */
			TestConfig.logger = TestConfig.report.startTest("MultiUser_" + username);
			loginpage = PageFactory.initElements(driver1, MIP_LoginPage.class);
			homepage = loginpage.login(username, password);
			Thread.sleep(1000);
			homepage.changeLanguage("Inglés");
			// homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			// homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			Thread.sleep(2000);
		} catch (Exception e) {
			throw e;
		} finally {
			// driver.quit();
		}
	}

	@Test
	@Parameters({ "username", "password" })
	public void login_test6(String username, String password) throws InterruptedException {
		// WebDriver driver = null;
		WebDriver driver1 = null;
		testcasename = "TC_" + username;
		try {
			TestConfig.report = new ExtentReports(".\\Test_Reports\\MultiUser_" + username + "_Test.html");
			/*
			 * driver = MIP_BrowserFactory.openBrowser(driver, "firefox");
			 * MIP_ReadPropertyFile.loadProperty("config");
			 * MIP_LaunchApplication.openApplication(driver);
			 */
			TestConfig.logger = TestConfig.report.startTest("MultiUser_" + username);
			loginpage = PageFactory.initElements(driver1, MIP_LoginPage.class);
			homepage = loginpage.login(username, password);
			Thread.sleep(1000);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			Thread.sleep(2000);
		} catch (Exception e) {
			throw e;
		} finally {
			// driver.quit();
		}
	}

	/*
	 * @AfterMethod(alwaysRun = true) public void after_test(ITestResult res) {
	 * if (res.getStatus() == ITestResult.FAILURE) {
	 * TestConfig.logger.log(LogStatus.FAIL, "----Test Failed");
	 * TestConfig.logger.log(LogStatus.ERROR, res.getThrowable()); } else {
	 * TestConfig.logger.log(LogStatus.PASS, testcasename + "----Test passed");
	 * } }
	 */

}
