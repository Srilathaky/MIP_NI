package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_SearchCustomerPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_SearchCustomerPage");
	}

	public MIP_SearchCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public MIP_SearchCustomerPage enterMSISDN(String msisdn) {
		try {
			logger.info("Entering the customer MSISDN");
			this.enterText(this.waitForElementToVisible(By.id("msisdn")), msisdn);
		} catch (Exception e) {
			logger.error("Error while entering the MSISDN", e);
		}
		return this;
	}

	public MIP_SearchCustomerPage enterCustomerName(String name) {
		try {
			logger.info("Entering the customer Name");
			this.enterText(this.waitForElementToVisible(By.id("custName")), name);
		} catch (Exception e) {
			logger.error("Error while entering the Customer Name", e);
		}
		return this;
	}

	public MIP_SearchCustomerPage clickOnSearch() {
		try {
			logger.info("Clicking on search button");
			this.waitForElementToPresent(By.id("searchBtn")).click();
		} catch (Exception e) {
			logger.error("Error while clicking on Search button", e);
		}
		return this;
	}

	public MIP_SearchCustomerPage clickOnClear() {
		this.clickOnElement(By.id("clearBtn"));
		return this;
	}

	public String getMsisdn() {
		return this.waitForElementToPresent(By.id("msisdn")).getAttribute("value");
	}

	public String getName() {
		return this.waitForElementToPresent(By.id("custName")).getAttribute("value");
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Customer registration page");
		return this.waitForElementToVisible(By.id("validationMessages")).getText();
	}

	public MIP_ModifyCustomerPage clikOnCustomerRecord(String msisdn) {
		logger.info("Clicking on Customer record in Search Customer page");
		this.waitForElementToVisible(By.xpath("//table[@id='customerDetailsList']/tbody//tr//td[contains(text(),"
				+ msisdn + ")]//preceding-sibling::td/a"));
		/*
		 * this.waitForElementToPresent( By.xpath(
		 * "//table[@id='customerDetailsList']/tbody//tr//td[contains(text()," +
		 * msisdn + ")]//preceding-sibling::td/a")).click();
		 */
		((JavascriptExecutor) driver).executeScript("arguments[0].click()",
				driver.findElement(By.xpath("//table[@id='customerDetailsList']/tbody//tr//td[contains(text()," + msisdn
						+ ")]//preceding-sibling::td/a")));
		/*
		 * try { this.waitForElementToPresent(By.id("custName")); } catch
		 * (Exception e) { this.waitForElementToVisible( By.xpath(
		 * "//table[@id='customerDetailsList']/tbody//tr//td[contains(text()," +
		 * msisdn + ")]//preceding-sibling::td/a")).click(); }
		 */
		return new MIP_ModifyCustomerPage(driver);
	}

	public String getSuccessMsg() {
		return this.waitForElementToPresent(By.xpath("//div[@id='validationMessages']//li")).getText();
	}

	public boolean validateSearchCustObjects() {
		if (this.waitForElementToPresent(By.xpath("//div[@class='pagetitle']")).getText()
				.equalsIgnoreCase("SEARCH CUSTOMER") && this.waitForElementToPresent(By.id("custName")).isDisplayed()
				&& this.waitForElementToPresent(By.id("msisdn")).isDisplayed()) {
			return true;
		}
		return false;

	}
}
