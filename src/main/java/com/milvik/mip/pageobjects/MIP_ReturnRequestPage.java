package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ReturnRequestPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ReturnRequestPage");
	}

	public MIP_ReturnRequestPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "msisdn")
	public WebElement mobileNumber;
	@FindBy(id = "saveBtn")
	public WebElement sendSms;
	@FindBy(id = "backBtn")
	public WebElement backBtn;

	public MIP_ReturnRequestPage clickOnBackBtn() {
		this.waitForElementToVisible(By.id("backBtn")).click();
		return this;
	}

	public MIP_ReturnRequestPage enterMsisdn(String msisdn) {
		logger.info("Entering MSISDN");
		this.enterText(this.mobileNumber, msisdn);
		logger.info("Entered MSISDN");
		return this;
	}

	public String getvalidationMessage() {
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}

	public String getSuccessMessage() {
		return this.waitForElementToVisible(By.id("message_div")).getText();

	}

	public MIP_ReturnRequestPage clickOnSendSMS() {
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}
}
