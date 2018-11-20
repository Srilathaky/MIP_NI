package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_RefundsSMSPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_RefundsSMSPage");
	}

	public MIP_RefundsSMSPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "msisdn")
	public WebElement mobileNumber;
	@FindBy(id = "search-icon")
	public WebElement search_icon;

	public MIP_RefundsSMSPage enterMsisdn(String msisdn) {
		logger.info("Entering MSISDN");
		this.waitForElementToVisible(By.id("msisdn"));
		this.enterText(this.mobileNumber, msisdn);
		logger.info("Entered MSISDN");
		return this;
	}

	public MIP_RefundsSMSPage clickOnSearchIcon() {
		logger.info("Clicking on search Icon");
		this.waitForElementToVisible(By.id("search-icon"));
		search_icon.click();
		logger.info("Clicked on search Icon");
		return this;
	}

	public boolean validateRefundSMSObjects() {
		this.waitForElementToVisible(By.id("msisdn"));
		boolean value1 = this.mobileNumber.getAttribute("readonly").equalsIgnoreCase("true");
		boolean value2 = this.waitForElementToVisible(By.id("offerId")).isEnabled();
		boolean value3 = this.waitForElementToVisible(By.id("coverLevel")).isEnabled();
		boolean value4 = this.waitForElementToVisible(By.id("refundSMSCheck")).isDisplayed();
		boolean value5 = this.waitForElementToVisible(By.id("saveBtn")).isDisplayed();
		boolean value6 = this.waitForElementToVisible(By.id("backBtn")).isDisplayed();
		if (value1 && !value2 && !value3 && value4 && value5 && value6) {
			return true;
		}
		return false;
	}

	public String getOfferInfo() {
		return this.waitForElementToVisible(By.xpath("//div[@id='offer_id_div']/div[@class='data_text']")).getText();
	}

	public String getCoverLevel() {
		return this.waitForElementToVisible(By.id("coverLevel")).getAttribute("value");
	}

	public String getvalidationMessage() {
		return this.waitForElementToVisible(By.id("validationMessages")).getText();
	}

	public MIP_RefundsSMSPage selectSendSmsOption(String option) {
		WebElement ele = this.waitForElementToVisible(By.id("refundSMSCheck"));
		Select s = new Select(ele);
		if (option.equalsIgnoreCase("yes")) {
			s.selectByValue("Yes");
		} else if (option.equalsIgnoreCase("No")) {
			s.selectByValue("No");
		}
		return this;
	}

	public MIP_RefundsSMSPage clickOnSave() {
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	public MIP_RefundsSMSPage clickOnBack() {
		this.clickOnElement(By.id("backBtn"));
		return this;
	}

	public String getSuccessMessage() {
		return this.waitForElementToVisible(By.id("message_div")).getText();

	}

}
