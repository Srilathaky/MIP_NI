package login_multiuser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class Run_Methods_Parallel {
	static MIP_LoginPage loginpage = null;
	static MIP_HomePage homepage = null;

	static String testcasename;

	@Test
	@Parameters({ "username", "password" })
	public void test_app(String username, String password) throws InterruptedException {
		WebDriver driver = null;
		try {

			driver = MIP_BrowserFactory.openBrowser(driver, "firefox");
			MIP_ReadPropertyFile.loadProperty("config");
			MIP_LaunchApplication.openApplication(driver);
			loginpage = PageFactory.initElements(driver, MIP_LoginPage.class);
			homepage = loginpage.login(username, password);
			Thread.sleep(1000);
			homepage.changeLanguage("Inglés");
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			driver.quit();
		} catch (Throwable e) {
			driver.quit();
			throw e;
		}
	}
}
