package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_HomePage extends MIP_BasePage {
	WebDriver driver;
	public WebElement Homelink;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_HomePage");
	}

	public MIP_HomePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean validateHomePageNavigation() {
		try {
			logger.info("Validating Home Page link");
			Homelink = this.waitForElementToPresent(By.linkText("Home"));
			return Homelink.isDisplayed();
		} catch (Exception e) {
			logger.error("Exception while validating the Home Page link", e);
			return false;
		}

	}

	public boolean validateHomePageObjects(String home, String branch, String user, String leave, String cust,
			String SMS, String role, String repot, String admin, String pass, String loguot, String announ,
			String cust_stat, String cust_statistics) {
		try {
			logger.info("Validating Home Page Objects");
			Homelink = this.waitForElementToPresent(By.linkText(home));
			this.waitForElementToPresent(By.linkText(branch));
			this.waitForElementToPresent(By.linkText(user));
			this.waitForElementToPresent(By.linkText(leave));
			this.waitForElementToPresent(By.linkText(cust));
			this.waitForElementToPresent(By.linkText(SMS));
			this.waitForElementToPresent(By.linkText(role));
			this.waitForElementToPresent(By.linkText(repot));
			this.waitForElementToPresent(By.linkText(admin));
			this.waitForElementToPresent(By.linkText(pass));
			this.waitForElementToPresent(By.linkText(loguot));
			this.waitForElementToPresent(By.xpath("//h3[contains(text(),'" + cust_stat + "')]"));

			return true;
		} catch (Exception e) {
			logger.error("Exception while validating the Home Page link", e);
			return false;
		}
	}

	public String getCustStaticsCount(String cust_Statics) {
		try {
			this.waitForElementToPresent(By.xpath("//div[contains(text(),'" + cust_Statics + "')]"));
		} catch (Exception e) {
			logger.error("Exception while getting Customer Statistics", e);
		}
		return driver.findElement(By.xpath("//div[contains(text(),'" + cust_Statics + "')]//following-sibling::div"))
				.getText().replace(",", "");
	}

	public MIP_HomePage changeLanguage(String lang) {
		try {
			logger.info("changing the language to English");
			this.waitForElementToVisible(By.linkText(lang));
			this.waitForElementToVisible(By.linkText(lang)).click();
			logger.info("changed the language to English");
		} catch (NoSuchElementException e) {
			driver.findElement(By.linkText("Home"));
			driver.findElement(By.linkText("Spanish")).click();
		} catch (Exception e) {
			logger.error("Exception while changing the language", e);
		}
		return this;
	}

	public String getUserDetails() {
		logger.info("Getting User Details");
		return this.waitForElementToPresent(By.xpath("//div[@class='user-stat']")).getText();

	}

	public void clickOnMenu(String menu) {
		logger.info("Clicking on Menu " + menu);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",
				this.waitForElementToVisible(By.linkText(menu)));
		/*
		 * try { // this.waitForElementToVisible(By.linkText(menu)).click();
		 * ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
		 * this.waitForElementToVisible(By.linkText(menu))); logger.info(
		 * "Clicked on Menu " + menu); } catch (Exception e) { logger.error(
		 * "Eror while Clicking  on Menu " + menu, e); }
		 */
	}

}
