package com.milvik.mip.rolemanagement.testrunner;

import java.util.List;
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
import com.milvik.mip.dataprovider.MIP_RoleManagement_TestData;
import com.milvik.mip.dbqueries.MIP_UserManagement_Queries;
import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;
import com.milvik.mip.dbqueries.MIP_RoleManagement_Queries;
import com.milvik.mip.pageobjects.MIP_AddRolePage;
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

public class MIP_AddRole_Test {
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
		log = MIP_Logging.logDetails("MIP_AddRole_Test");
		report = new ExtentReports(".\\Test_Reports\\AddRole_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
		}
	}

	@Test(priority = 0, testName = "TC192")
	public void roleManagement() throws Throwable {
		testcasename = "MIP_webPortal_TC192_Role_Management";
		try {
			logger = report.startTest("Role Management-TC192");
			log.info("Running test case - TC192");
			logger.log(LogStatus.INFO, "Validating Role Management Menu");
			homepage.waitForElementToVisible(By.linkText(MIP_Menu_Constants.ADD_ROLE));
			Assert.assertTrue(
					MIP_Test_Configuration.driver.findElement(By.linkText(MIP_Menu_Constants.ADD_ROLE)).isDisplayed());
			homepage.waitForElementToVisible(By.linkText(MIP_Menu_Constants.VIEW_ROLE));
			Assert.assertTrue(
					MIP_Test_Configuration.driver.findElement(By.linkText(MIP_Menu_Constants.VIEW_ROLE)).isDisplayed());
		} catch (Throwable t) {
			log.info("Testcase TC192  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC193")
	public void addRoleOne() throws Throwable {
		testcasename = "MIP_webPortal_TC193_Add_Role";
		try {
			logger = report.startTest("Add Role -TC193");
			log.info("Running test case - TC193");
			MIP_AddRolePage addrole = PageFactory.initElements(MIP_Test_Configuration.driver, MIP_AddRolePage.class);
			logger.log(LogStatus.INFO, "Validating Add Role Page UI elements");
			Assert.assertTrue(addrole.roleName.isDisplayed());
			Assert.assertEquals(addrole.homeMenu.getText().trim(), MIP_Menu_Constants.HOME.trim());
			Assert.assertTrue(addrole.homeMenu_checkbox.getAttribute("disabled").equalsIgnoreCase("true"));

			Assert.assertEquals(addrole.branchmanageMenu.getText().trim(), MIP_Menu_Constants.BRANCH.trim());
			Assert.assertTrue(addrole.branchmanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addrole.addBranchMenu.getText().trim(), MIP_Menu_Constants.ADD_BRANCH.trim());
			Assert.assertTrue(addrole.addBranch_checkbox.isDisplayed());

			Assert.assertEquals(addrole.listBranchMenu.getText().trim(), MIP_Menu_Constants.LIST_BRANCH.trim());
			Assert.assertTrue(addrole.listBranch_checkbox.isDisplayed());

			Assert.assertEquals(addrole.usermanageMenu.getText().trim(), MIP_Menu_Constants.USER.trim());
			Assert.assertTrue(addrole.usermanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addrole.adduserMenu.getText().trim(), MIP_Menu_Constants.ADD_USER.trim());
			Assert.assertTrue(addrole.addUser_checkbox.isDisplayed());

			Assert.assertEquals(addrole.listuserMenu.getText().trim(), MIP_Menu_Constants.LIST_USER.trim());
			Assert.assertTrue(addrole.listUser_checkbox.isDisplayed());

			Assert.assertEquals(addrole.searchuserMenu.getText().trim(), MIP_Menu_Constants.SEARCH_USER.trim());
			Assert.assertTrue(addrole.searchUser_checkbox.isDisplayed());

			Assert.assertEquals(addrole.resetPasswordMenu.getText().trim(), MIP_Menu_Constants.RESET_PASSWORD.trim());
			Assert.assertTrue(addrole.resetPassword_checkbox.isDisplayed());

			Assert.assertEquals(addrole.leavemanageMenu.getText().trim(), MIP_Menu_Constants.LEAVE.trim());
			Assert.assertTrue(addrole.leavemanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addrole.applyLeaveMenu.getText().trim(), MIP_Menu_Constants.APPLY_LEAVE.trim());
			Assert.assertTrue(addrole.applyLeave_checkbox.isDisplayed());

			Assert.assertEquals(addrole.viewLeaveMenu.getText().trim(), MIP_Menu_Constants.VIEW_LEAVE.trim());
			Assert.assertTrue(addrole.viewLeave_checkbox.isDisplayed());

			Assert.assertEquals(addrole.custmanageMenu.getText().trim(), MIP_Menu_Constants.CUSTOMER.trim());
			Assert.assertTrue(addrole.custmanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addrole.regCustMenu.getText().trim(), MIP_Menu_Constants.REGISTER_CUSTOMER.trim());
			Assert.assertTrue(addrole.regCust_checkbox.isDisplayed());

			Assert.assertEquals(addrole.searchCustMenu.getText().trim(), MIP_Menu_Constants.SEARCH_CUSTOMER.trim());
			Assert.assertTrue(addrole.searchCust_checkbox.isDisplayed());

			Assert.assertEquals(addrole.deRegCustMenu.getText().trim(), MIP_Menu_Constants.DE_REGISTER_CUSTOMER.trim());
			Assert.assertTrue(addrole.deRegCust_checkbox.isDisplayed());

			Assert.assertEquals(addrole.switchprdlevelMenu.getText().trim(),
					MIP_Menu_Constants.SWITCH_PRODUCT_LEVEL.trim());
			Assert.assertTrue(addrole.switchprdlevel_checkbox.isDisplayed());

			Assert.assertEquals(addrole.coverHistMenu.getText().trim(), MIP_Menu_Constants.COVER_HISTORY.trim());
			Assert.assertTrue(addrole.coverHist_checkbox.isDisplayed());

			Assert.assertEquals(addrole.smsmanageMenu.getText().trim(), MIP_Menu_Constants.SMS.trim());
			Assert.assertTrue(addrole.smsmanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addrole.editSmsMenu.getText().trim(), MIP_Menu_Constants.EDIT_SMS_TEMPLATE.trim());
			Assert.assertTrue(addrole.editSmsMenu.isDisplayed());

			Assert.assertEquals(addrole.refundSmsMenu.getText().trim(), MIP_Menu_Constants.REFUND_SMS.trim());
			Assert.assertTrue(addrole.refundSms_checkbox.isDisplayed());

			Assert.assertEquals(addrole.returnRequestMenu.getText().trim(), MIP_Menu_Constants.RETURN_REQUESTS.trim());
			Assert.assertTrue(addrole.returnRequest_checkbox.isDisplayed());

			Assert.assertEquals(addrole.rolemanageMenu.getText().trim(), MIP_Menu_Constants.ROLE.trim());
			Assert.assertTrue(addrole.rolemanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addrole.addRoleMenu.getText().trim(), MIP_Menu_Constants.ADD_ROLE.trim());
			Assert.assertTrue(addrole.addRole_checkbox.isDisplayed());

			Assert.assertEquals(addrole.viewRoleMenu.getText().trim(), MIP_Menu_Constants.VIEW_ROLE.trim());
			Assert.assertTrue(addrole.viewRole_checkbox.isDisplayed());

			Assert.assertEquals(addrole.reportmanageMenu.getText().trim(), MIP_Menu_Constants.REPORT.trim());
			Assert.assertTrue(addrole.reportmanageMenu_checkbox.isDisplayed());

			Assert.assertEquals(addrole.custReportMenu.getText().trim(), MIP_Menu_Constants.CUST_REPORT.trim());
			Assert.assertTrue(addrole.custReport_checkbox.isDisplayed());

			Assert.assertEquals(addrole.agentReportMenu.getText().trim(), MIP_Menu_Constants.AGENT_REPORT.trim());
			Assert.assertTrue(addrole.agentReport_checkbox.isDisplayed());

			Assert.assertEquals(addrole.adminConfigMenu.getText().trim(), MIP_Menu_Constants.ADMIN_CONFIG.trim());
			Assert.assertTrue(addrole.adminConfig_checkbox.isDisplayed());

			Assert.assertEquals(addrole.changePassMenu.getText().trim(), MIP_Menu_Constants.CHANGE_PASSWORD.trim());
			Assert.assertTrue(addrole.changePass_checkbox.getAttribute("disabled").equalsIgnoreCase("true"));

			Assert.assertEquals(addrole.logoutMenu.getText().trim(), MIP_Menu_Constants.LOGOUT.trim());
			Assert.assertTrue(addrole.logout_checkbox.getAttribute("disabled").equalsIgnoreCase("true"));
			Assert.assertTrue(addrole.save.isDisplayed());
			Assert.assertTrue(addrole.clear.isDisplayed());
		} catch (Throwable t) {
			log.info("Testcase TC193  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "TC194", dataProvider = "addroleTestData", dataProviderClass = MIP_RoleManagement_TestData.class)
	public void addRoleTwo(String testcase, String roleName, String addbranch, String listbranch, String adduser,
			String listuser, String searchuser, String resetpassword, String applyleave, String viewleave,
			String regCust, String searchCust, String deregCust, String switchprod, String coverHist, String editsms,
			String refndsms, String returnrequest, String addRole, String viewRole, String custReport,
			String agentReport, String adminConfig, String msg, String fname, String sname, String mobilenum,
			String email, String dob, String gender, String role, String branch, String supervisor, String succmsg)
			throws Throwable {
		testcasename = "MIP_webPortal_TC194_Add_Branch";
		try {
			logger = report.startTest("Add Role-TC194");
			log.info("Running test case -" + testcase);
			MIP_AddRolePage addroleepage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_AddRolePage.class);
			logger.log(LogStatus.INFO, "Adding Role " + roleName);
			addroleepage.enterRoleName(roleName);
			logger.log(LogStatus.INFO, "Seletcing Menu for the Role");
			if (addbranch.equalsIgnoreCase("True"))
				addroleepage.selectAddBranch();
			if (listbranch.equalsIgnoreCase("True"))
				addroleepage.selectListBranch();
			if (adduser.equalsIgnoreCase("True"))
				addroleepage.selectAddUser();
			if (listuser.equalsIgnoreCase("True"))
				addroleepage.selectListUser();
			if (searchuser.equalsIgnoreCase("True"))
				addroleepage.selectSearchUser();
			if (resetpassword.equalsIgnoreCase("True"))
				addroleepage.selectresetPassword();
			if (applyleave.equalsIgnoreCase("True"))
				addroleepage.selectapplyLeave();
			if (viewleave.equalsIgnoreCase("True"))
				addroleepage.selectviewLeave();
			if (regCust.equalsIgnoreCase("True"))
				addroleepage.selectregsCust();
			if (searchCust.equalsIgnoreCase("True"))
				addroleepage.selectsearchCust();
			if (deregCust.equalsIgnoreCase("True"))
				addroleepage.selectdeRegsCust();
			if (switchprod.equalsIgnoreCase("True"))
				addroleepage.selectswitchProdLevel();
			if (coverHist.equalsIgnoreCase("True"))
				addroleepage.selectcoverHistry();
			if (editsms.equalsIgnoreCase("True"))
				addroleepage.selecteditSms();
			if (refndsms.equalsIgnoreCase("True"))
				addroleepage.selectrefundSms();
			if (returnrequest.equalsIgnoreCase("True"))
				addroleepage.selectreturnRequest();
			if (addRole.equalsIgnoreCase("True"))
				addroleepage.selectaddRole();
			if (viewRole.equalsIgnoreCase("True"))
				addroleepage.selectviewRole();
			if (custReport.equalsIgnoreCase("True"))
				addroleepage.selectCustReport();
			if (agentReport.equalsIgnoreCase("True"))
				addroleepage.selectAgentReport();
			if (adminConfig.equalsIgnoreCase("True"))
				addroleepage.selectAdminConfig();
			logger.log(LogStatus.INFO, "Clicking on Save Button");
			addroleepage.clickOnSave();
			addroleepage.confirmAddRole("yes");
			logger.log(LogStatus.INFO, "Validating Success Message");
			Assert.assertTrue(addroleepage.getSuccessMessage().trim().replaceAll("\\s", "")
					.contains(msg.trim().replaceAll("\\s", "")));
			logger.log(LogStatus.INFO, "Validating Added Role Aganist DataBase");
			Assert.assertTrue(MIP_RoleManagement_Queries.getRoleName(roleName));

			homepage.waitForElementToVisible(By.linkText(MIP_Menu_Constants.USER));
			homepage.clickOnMenu(MIP_Menu_Constants.USER);
			homepage.waitForElementToVisible(By.linkText(MIP_Menu_Constants.ADD_USER));
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_USER);
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
			List<String> rolemap = MIP_RoleManagement_Queries.getMappedRoles(roleName);

			if (addbranch.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.ADD_BRANCH));

			if (listbranch.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.LIST_BRANCH));
			if (adduser.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.ADD_USER));
			if (listuser.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.LIST_USER));
			if (searchuser.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.SEARCH_USER));
			if (resetpassword.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.RESET_PASSWORD));
			if (applyleave.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.APPLY_LEAVE));
			if (viewleave.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.VIEW_LEAVE));
			if (regCust.equalsIgnoreCase("True")) {
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.REGISTER_CUSTOMER));
			}
			if (searchCust.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.SEARCH_CUSTOMER));
			if (deregCust.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.DE_REGISTER_CUSTOMER));
			if (switchprod.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains("Change Cover Level/Deduction Scheme"));
			if (coverHist.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.COVER_HISTORY));
			if (editsms.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.EDIT_SMS_TEMPLATE));
			if (addRole.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.ADD_ROLE));
			if (viewRole.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.VIEW_ROLE));

			if (custReport.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.CUST_REPORT));
			if (agentReport.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.AGENT_REPORT));
			if (refndsms.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.REFUND_SMS));
			if (adminConfig.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains(MIP_Menu_Constants.ADMIN_CONFIG));

			if (returnrequest.equalsIgnoreCase("True"))
				Assert.assertTrue(rolemap.toString().contains("Refund Return Sms"));

		} catch (Throwable t) {
			log.info("Testcase TC194  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "TC195", dataProvider = "addroleNegativeTestData", dataProviderClass = MIP_RoleManagement_TestData.class)
	public void addRoleThree(String testcase, String roleName, String addbranch, String listbranch, String adduser,
			String listuser, String searchuser, String resetpassword, String applyleave, String viewleave,
			String regCust, String searchCust, String deregCust, String switchprod, String coverHist, String editsms,
			String refndsms, String returnrequest, String addRole, String viewRole, String custReport,
			String agentReport, String adminConfig, String msg) throws Throwable {
		testcasename = "MIP_webPortal_TC195_Add_Role";
		try {
			logger = report.startTest("Add Role-TC195");
			log.info("Running test case TC195");
			MIP_AddRolePage addroleepage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_AddRolePage.class);
			logger.log(LogStatus.INFO, "Validating Clear Button Functionality in Add Role Page");
			addroleepage.enterRoleName(roleName);
			if (addbranch.equalsIgnoreCase("True"))
				addroleepage.selectAddBranch();
			if (listbranch.equalsIgnoreCase("True"))
				addroleepage.selectListBranch();
			if (adduser.equalsIgnoreCase("True"))
				addroleepage.selectAddUser();
			if (listuser.equalsIgnoreCase("True"))
				addroleepage.selectListUser();
			if (searchuser.equalsIgnoreCase("True"))
				addroleepage.selectSearchUser();
			if (resetpassword.equalsIgnoreCase("True"))
				addroleepage.selectresetPassword();
			if (applyleave.equalsIgnoreCase("True"))
				addroleepage.selectapplyLeave();
			if (viewleave.equalsIgnoreCase("True"))
				addroleepage.selectviewLeave();
			if (regCust.equalsIgnoreCase("True"))
				addroleepage.selectregsCust();
			if (searchCust.equalsIgnoreCase("True"))
				addroleepage.selectsearchCust();
			if (deregCust.equalsIgnoreCase("True"))
				addroleepage.selectdeRegsCust();
			if (switchprod.equalsIgnoreCase("True"))
				addroleepage.selectswitchProdLevel();
			if (coverHist.equalsIgnoreCase("True"))
				addroleepage.selectcoverHistry();
			if (editsms.equalsIgnoreCase("True"))
				addroleepage.selecteditSms();
			if (refndsms.equalsIgnoreCase("True"))
				addroleepage.selectrefundSms();
			if (returnrequest.equalsIgnoreCase("True"))
				addroleepage.selectreturnRequest();
			if (addRole.equalsIgnoreCase("True"))
				addroleepage.selectaddRole();
			if (viewRole.equalsIgnoreCase("True"))
				addroleepage.selectviewRole();
			if (custReport.equalsIgnoreCase("True"))
				addroleepage.selectCustReport();
			if (agentReport.equalsIgnoreCase("True"))
				addroleepage.selectAgentReport();
			if (adminConfig.equalsIgnoreCase("True"))
				addroleepage.selectAdminConfig();
			addroleepage.clickOnClear();
			if (addbranch.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.addBranch_checkbox.isSelected());
			if (listbranch.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.listBranch_checkbox.isSelected());
			if (adduser.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.addUser_checkbox.isSelected());
			if (listuser.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.listUser_checkbox.isSelected());
			if (searchuser.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.searchCust_checkbox.isSelected());
			if (resetpassword.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.resetPassword_checkbox.isSelected());
			if (applyleave.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.applyLeave_checkbox.isSelected());
			if (viewleave.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.viewLeave_checkbox.isSelected());
			if (regCust.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.regCust_checkbox.isSelected());
			if (searchCust.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.searchCust_checkbox.isSelected());
			if (deregCust.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.deRegCust_checkbox.isSelected());
			if (switchprod.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.switchprdlevel_checkbox.isSelected());
			if (coverHist.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.coverHist_checkbox.isSelected());
			if (editsms.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.editSms_checkbox.isSelected());
			if (refndsms.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.refundSms_checkbox.isSelected());
			if (returnrequest.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.returnRequest_checkbox.isSelected());
			if (addRole.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.addRole_checkbox.isSelected());
			if (viewRole.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.viewRole_checkbox.isSelected());
			if (custReport.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.custReport_checkbox.isSelected());
			if (agentReport.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.agentReport_checkbox.isSelected());
			if (adminConfig.equalsIgnoreCase("True"))
				Assert.assertFalse(addroleepage.adminConfig_checkbox.isSelected());
			Assert.assertTrue(addroleepage.roleName.getText().equals(""));
			Assert.assertTrue(addroleepage.homeMenu_checkbox.isSelected());
			Assert.assertTrue(addroleepage.changePass_checkbox.isSelected());
			Assert.assertTrue(addroleepage.logout_checkbox.isSelected());
		} catch (Throwable t) {
			log.info("Testcase TC195  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 4, testName = "TC196-TC197", dataProvider = "addroleNegativeTestData", dataProviderClass = MIP_RoleManagement_TestData.class)
	public void addRoleFour(String testcase, String roleName, String addbranch, String listbranch, String adduser,
			String listuser, String searchuser, String resetpassword, String applyleave, String viewleave,
			String regCust, String searchCust, String deregCust, String switchprod, String coverHist, String editsms,
			String refndsms, String returnrequest, String addRole, String viewRole, String custReport,
			String agentReport, String adminConfig, String msg) throws Throwable {
		testcasename = "MIP_webPortal_TC196_TC197_Add_Role";
		try {
			logger = report.startTest("Add Role-" + testcase);
			log.info("Running test case " + testcase);
			MIP_AddRolePage addroleepage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_AddRolePage.class);
			Thread.sleep(1000);
			addroleepage.enterRoleName(roleName);

			if (addbranch.equalsIgnoreCase("True"))
				addroleepage.selectAddBranch();
			if (listbranch.equalsIgnoreCase("True"))
				addroleepage.selectListBranch();
			if (adduser.equalsIgnoreCase("True"))
				addroleepage.selectAddUser();
			if (listuser.equalsIgnoreCase("True"))
				addroleepage.selectListUser();
			if (searchuser.equalsIgnoreCase("True"))
				addroleepage.selectSearchUser();
			if (resetpassword.equalsIgnoreCase("True"))
				addroleepage.selectresetPassword();
			if (applyleave.equalsIgnoreCase("True"))
				addroleepage.selectapplyLeave();
			if (viewleave.equalsIgnoreCase("True"))
				addroleepage.selectviewLeave();
			if (regCust.equalsIgnoreCase("True"))
				addroleepage.selectregsCust();
			if (searchCust.equalsIgnoreCase("True"))
				addroleepage.selectsearchCust();
			if (deregCust.equalsIgnoreCase("True"))
				addroleepage.selectdeRegsCust();
			if (switchprod.equalsIgnoreCase("True"))
				addroleepage.selectswitchProdLevel();
			if (coverHist.equalsIgnoreCase("True"))
				addroleepage.selectcoverHistry();
			if (editsms.equalsIgnoreCase("True"))
				addroleepage.selecteditSms();
			if (refndsms.equalsIgnoreCase("True"))
				addroleepage.selectrefundSms();
			if (returnrequest.equalsIgnoreCase("True"))
				addroleepage.selectreturnRequest();
			if (addRole.equalsIgnoreCase("True"))
				addroleepage.selectaddRole();
			if (viewRole.equalsIgnoreCase("True"))
				addroleepage.selectviewRole();
			if (custReport.equalsIgnoreCase("True"))
				addroleepage.selectCustReport();
			if (agentReport.equalsIgnoreCase("True"))
				addroleepage.selectAgentReport();
			if (adminConfig.equalsIgnoreCase("True"))
				addroleepage.selectAdminConfig();
			addroleepage.clickOnSave();
			logger.log(LogStatus.INFO, "Validating Error message in Add Role Page");
			Assert.assertTrue(addroleepage.getValidationMessage().trim().replaceAll("\\s", "")
					.equalsIgnoreCase(msg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
		} catch (Throwable t) {
			log.info("Testcase TC196-TC197  Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.ADD_ROLE);
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
			homepage.clickOnMenu(MIP_Menu_Constants.ROLE);
		report.endTest(logger);
		report.flush();
	}
}
