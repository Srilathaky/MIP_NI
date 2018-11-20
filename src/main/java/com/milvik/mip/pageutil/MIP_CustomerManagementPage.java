package com.milvik.mip.pageutil;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.utility.MIP_Logging;

public class MIP_CustomerManagementPage extends MIP_BasePage {

	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_CustomerManagementPage");
	}

	public MIP_CustomerManagementPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(id = "msisdn")
	public WebElement mobileNum;
	@FindBy(id = "search-icon")
	public WebElement searchIcon;

	public boolean validateBackBtn() {
		return this.waitForElementToVisible(By.id("backBtn")).isDisplayed();
	}

	public void clickOnBackBtn() {
		this.waitForElementToVisible(By.id("backBtn"));
	}

	public void enterMsisdn(String msisdn) {
		this.mobileNum.sendKeys(msisdn);
	}

	public String getValidationMessage() {
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}

	public void confirmCustManagementPopup(String option) {
		this.confirmPopUp(option);
	}

	public String getSuccessMessage() {
		return this.waitForElementToVisible(By.id("message_div")).getText();
	}

}
