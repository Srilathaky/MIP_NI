package com.milvik.mip.pageobjects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_DeRegisterCustomerPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeRegisterCustomerPage");
	}

	public MIP_DeRegisterCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean validateDeRegTableHeading() {
		logger.info("Validating De-Register TableHeading ");
		this.waitForElementToVisible(By.id("customerDetailsList"));
		boolean value1 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr//th[contains(text(),'Customer Name')]"))
				.isDisplayed();
		boolean value2 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr//th[contains(text(),'Mobile Number')]"))
				.isDisplayed();
		boolean value3 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr//th[contains(text(),'Offer Name')]"))
				.isDisplayed();
		boolean value4 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr//th[contains(text(),'Registered Offer Level')]"))
				.isDisplayed();
		boolean value5 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr//th[contains(text(),'Deducted Amount (as on date)')]"))
				.isDisplayed();
		boolean value6 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr//th[contains(text(),'Cover Earned')]"))
				.isDisplayed();
		boolean value7 = this
				.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr//th[contains(text(),'Confirmation Status')]"))
				.isDisplayed();
		if (value1 && value2 && value3 && value4 && value5 && value6 && value7) {
			return true;
		}
		return false;
	}

	public boolean validateOfferInfo() {
		logger.info("Validating offerInfo ");

		return this.waitForElementToVisible(By.id("purchasedProducts"))
				.isDisplayed();
	}

	public boolean validateDeRegisterBtn() {
		logger.info("Validating De-Register button");

		return this.waitForElementToVisible(By.id("deRegisterBtn"))
				.isDisplayed();
	}

	public int getIndex(String name) {
		logger.info("Getting webelement index");
		int count = 0;
		List<WebElement> index = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/thead/tr//th"));
		for (int i = 0; i < index.size(); i++) {
			if (index.get(i).getText().equalsIgnoreCase(name)) {
				count = count + i + 1;
				break;

			}
		}
		return count;
	}

	public String getCustomerName() {
		logger.info("Getting Customer Name ");
		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElement(
				By.xpath("//table[@id='customerDetailsList']/tbody/tr//td["
						+ getIndex("Customer Name") + "]")).getText();
	}

	public String getOfferName() {
		logger.info("Getting Offer Name ");
		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElement(
				By.xpath("//table[@id='customerDetailsList']/tbody/tr//td["
						+ getIndex("Offer Name") + "]")).getText();
	}

	public String getOfferLevel() {
		logger.info("Getting Offer Level ");
		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElement(
				By.xpath("//table[@id='customerDetailsList']/tbody/tr//td["
						+ getIndex("Registered Offer Level") + "]")).getText();
	}

	public String getDeductedAmount() {
		logger.info("Getting Deducted Amount");
		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElement(
				By.xpath("//table[@id='customerDetailsList']/tbody/tr//td["
						+ getIndex("Deducted Amount (as on date)") + "]"))
				.getText();
	}

	public String getCoverEarned() {
		logger.info("Getting Cover Earned");
		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElement(
				By.xpath("//table[@id='customerDetailsList']/tbody/tr//td["
						+ getIndex("Cover Earned") + "]")).getText();
	}

	public String getConfirmationStatus() {
		logger.info("Confirming the pop-up");

		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElement(
				By.xpath("//table[@id='customerDetailsList']/tbody/tr//td["
						+ getIndex("Confirmation Status") + "]")).getText();
	}

	public MIP_DeRegisterCustomerPage selectOffer(String offer) {
		logger.info("Selecting the offer");
		this.waitForElementToVisible(By.id("purchasedProducts")).click();
		return this;
	}

	public MIP_DeRegisterCustomerPage clickOnDeRegisterBtn() {
		logger.info("clicking on de-Register Button");
		this.waitForElementToVisible(By.id("deRegisterBtn")).click();
		return this;
	}
}
