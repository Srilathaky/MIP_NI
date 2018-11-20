package com.milvik.mip.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ModifyCustomerPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ModifyCustomerPage");
	}

	public MIP_ModifyCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean validateModifyCustObjects() {
		boolean value1 = this.waitForElementToVisible(By.id("label_msisdn")).getText().trim()
				.equalsIgnoreCase("Mobile Number  :  *".trim());

		boolean value2 = this.waitForElementToPresent(By.id("msisdn")).getAttribute("type").equalsIgnoreCase("text");
		boolean value3 = driver.findElement(By.id("label_offerId")).getText().trim()
				.equalsIgnoreCase("Subscribed Offers  : *".trim());
		boolean value4 = driver.findElement(By.id("offerId")).getAttribute("type").equalsIgnoreCase("checkbox");

		boolean value6 = driver.findElement(By.id("label_custName")).getText().trim()
				.equalsIgnoreCase("Customer Name  : *");
		boolean value7 = this.waitForElementToPresent(By.id("custName")).getAttribute("type").equalsIgnoreCase("text");
		boolean value10 = this.waitForElementToPresent(By.id("label_dob")).getText().trim()
				.equalsIgnoreCase("Date of Birth  :");
		boolean value11 = this.waitForElementToPresent(By.id("dob")).getAttribute("type").equalsIgnoreCase("text");
		boolean value12 = this.waitForElementToPresent(By.id("label_age")).getText().trim()
				.equalsIgnoreCase("Age  : *");
		boolean value13 = this.waitForElementToPresent(By.id("age")).getAttribute("type").equalsIgnoreCase("text");
		boolean value14 = this.waitForElementToPresent(By.id("label_gender")).getText().trim()
				.equalsIgnoreCase("Gender  : *");
		boolean value15 = this.waitForElementToPresent(By.id("gender")).getAttribute("type").equalsIgnoreCase("radio");
		boolean value16 = this.waitForElementToPresent(By.id("label_insRelation")).getText().trim()
				.equalsIgnoreCase("Relationship with Customer  : *");
		boolean value17 = this.waitForElementToPresent(By.xpath("//*[@id='insRelationNonAdmin' or @id='insRelation']"))
				.isDisplayed();
		System.out.println(driver.findElement(By.id("label_irName")).getText());
		boolean value18 = this.waitForElementToPresent(By.id("label_irName")).getText().trim()
				.equalsIgnoreCase("Beneficiary's Name  : *");
		boolean value19 = this.waitForElementToPresent(By.id("irName")).getAttribute("type").equalsIgnoreCase("text");

		boolean value20 = this.waitForElementToPresent(By.id("backBtn")).getText().trim().equalsIgnoreCase("Back");

		if (value1 && value2 && value3 && value4 && value6 && value7 && value10 && value11 && value12 && value13
				&& value14 && value15 && value16 && value17 && value18 && value19 && value20) {
			return true;
		}
		return false;
	}

	public void clickOnSaveChanges() {
		logger.info("Clicking on Save Changed button in Modify Customer page");
		this.clickOnElement(By.id("saveBtn"));
	}

	public MIP_ModifyCustomerPage modifyCustInfo(String name, String id, String dob, String gender) {
		if (!name.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("custName"));
			ele.clear();
			if (!name.equalsIgnoreCase("Empty"))
				ele.sendKeys(name);
		}
		if (!id.equals("")) {
			WebElement ele = this.waitForElementToPresent(By.id("nid"));
			ele.clear();
			ele.sendKeys(id);
		}
		if (!dob.equals("")) {
			String[] date = MIP_DateFunctionality.getDate(dob, MIP_Constants.DOB_FORMAT);
			this.waitForElementToPresent(By.xpath("//div[@class='calendar-icon']"));
			this.waitForElementToVisible(By.xpath("//div[@class='calendar-icon']")).click();

			Actions a = new Actions(driver);
			a.moveToElement(this.waitForElementToVisible(
					By.xpath("/html/body/table/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div"))).build().perform();
			this.waitForElementToVisible(By.xpath("/html/body/table/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div"))
					.click();
			WebElement ele = this.waitForElementToVisible(By.className("DynarchCalendar-menu-year"));
			ele.clear();
			ele.sendKeys(date[2]);
			this.waitForElementToVisible(
					By.xpath("//table[@class='DynarchCalendar-menu-mtable']/tbody//tr//td/div[contains(text(),'"
							+ date[1] + "')]"))
					.click();
			if ((date[0].charAt(0) + "").equals("0")) {
				date[0] = date[0].charAt(1) + "";
			}
			try {
				this.waitForElementToVisible(By
						.xpath("//table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day DynarchCalendar-weekend' or @class='DynarchCalendar-day'][contains(text(),'"
								+ date[0] + "')]"))
						.click();
			} catch (Exception e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(

						By.xpath(
								"//table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day DynarchCalendar-weekend' or @class='DynarchCalendar-day'][contains(text(),'"
										+ date[0] + "')]")));
			}
		}
		if (!gender.equals("")) {
			if (gender.equalsIgnoreCase("Male")) {
				driver.findElement(By.xpath("//input[@id='gender'][@value='M']")).click();
			} else {
				driver.findElement(By.xpath("//input[@id='gender'][@value='F']")).click();
			}
		}
		return this;
	}

	public MIP_ModifyCustomerPage modifyBenInfo(String relation, String name) {
		if (!relation.equals("")) {

			this.selectDropDownbyText(this.waitForElementToPresent(By.id("insRelation")), relation);
		}

		if (!name.equals("")) {
			WebElement ele = this.waitForElementToPresent(By.id("irName"));
			ele.clear();
			if (!name.equalsIgnoreCase("Empty"))
				ele.sendKeys(name);
		}
		return this;
	}

	public MIP_SearchCustomerPage confirmModifyCust(String option) {
		this.confirmPopUp(option);
		return new MIP_SearchCustomerPage(driver);
	}

	public String getSuccessMsg() {
		return this.waitForElementToPresent(By.xpath("//div[@class='error-div-body']/div[2]")).getText();
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Modify Customer  page");
		return this.waitForElementToPresent(By.id("validationMessages")).getText();
	}

	public boolean offerInfoReadOnlyCheck() {

		String str1 = this.waitForElementToPresent(By.id("offerId")).getAttribute("disabled");
		if (str1.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public boolean custInfoReadOnlyCheck() {
		String cusName = this.waitForElementToPresent(By.id("custName")).getAttribute("readonly");
		String id = this.waitForElementToPresent(By.id("nid")).getAttribute("readonly");

		String dob = this.waitForElementToPresent(By.id("dob")).getAttribute("readonly");
		String gen_male = driver.findElement(By.xpath("//input[@id='gender'][@value='M']")).getAttribute("disabled");

		String gen_female = driver.findElement(By.xpath("//input[@id='gender'][@value='F']")).getAttribute("disabled");
		if (cusName.equalsIgnoreCase("true") && id.equalsIgnoreCase("true") && dob.equalsIgnoreCase("true")
				&& gen_male.equalsIgnoreCase("true") && gen_female.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public boolean benInfoReadOnlyCheck() {

		String str1 = this.waitForElementToPresent(By.xpath("//*[@id='insRelation' or @id='insRelationNonAdmin']"))
				.getAttribute("readonly");
		String str2 = this.waitForElementToPresent(By.id("irName")).getAttribute("readonly");
		if (str1.equalsIgnoreCase("true") && str2.equalsIgnoreCase("true")) {
			return true;
		}
		return false;

	}

	public MIP_ModifyCustomerPage clickOnClear() {
		this.clickOnElement(By.id("clearBtn"));
		return this;
	}

	public String getCustInfo(String value) {
		if (value.equalsIgnoreCase("name")) {
			return this.waitForElementToPresent(By.id("custName")).getAttribute("value");
		} else if (value.equalsIgnoreCase("id")) {
			return this.waitForElementToPresent(By.id("nid")).getAttribute("value");
		} else if (value.equalsIgnoreCase("dob")) {
			return this.waitForElementToPresent(By.id("dob")).getAttribute("value");
		}
		return "false";
	}

	public String getBenInfo(String value) {
		if (value.equalsIgnoreCase("name")) {
			return this.waitForElementToPresent(By.id("irName")).getAttribute("value");
		}
		return "false";
	}

	public MIP_SearchCustomerPage clickOnBackButton() {
		this.clickOnElement(By.id("backBtn"));
		return new MIP_SearchCustomerPage(driver);

	}
}
