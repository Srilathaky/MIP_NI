package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_CoverHistoryPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_CoverHistoryPage");
	}

	public MIP_CoverHistoryPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getResultText() {
		return this.waitForElementToVisible(By.id("message_div")).getText();
	}

	public boolean validateTableHeading() {
		logger.info("Validating Cover History TableHeading ");
		this.waitForElementToVisible(By
				.xpath("//table[@id='coverHistoryList1']/thead/tr/th[contains(text(),'Rolled over offer amount')]"));
		String xpath = "//table[@id='coverHistoryList1']/thead/tr/th[";
		if ((driver.findElement(By.xpath(xpath + "contains(text(),'Month')]"))
				.isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "contains(text(),'Year')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "contains(text(),'Cover Level')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "contains(text(),'Deducted Charges')]"))
						.isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "contains(text(),'Cover Earned')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "contains(text(),'Rolled over offer amount')]"))
						.isDisplayed())) {
			return true;
		}
		return false;
	}

	public List<String> getCoverHistoryDetails() {
		logger.info("Getting Cover History details ");

		List<String> result = new ArrayList<String>();
		this.waitForElementToVisible(By.id("coverHistoryList1"));
		List<WebElement> coverele = driver.findElements(By
				.xpath("//table[@id='coverHistoryList1']/tbody//tr//td"));
		for (int i = 0; i < coverele.size(); i++) {
			result.add(coverele.get(i).getText());
		}
		return result;
	}

}
