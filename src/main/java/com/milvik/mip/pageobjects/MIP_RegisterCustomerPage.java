package com.milvik.mip.pageobjects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_RegisterCustomerPage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	public static final int NIDLENGTH = 16;
	static {
		logger = MIP_Logging.logDetails("MIP_RegisterCustomerPage");
	}

	public MIP_RegisterCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "msisdn")
	public WebElement msisdn;

	public MIP_RegisterCustomerPage enterMSISDN(String msisdn) {
		try {
			logger.info("Entering the customer MSISDN");
			this.enterText(this.waitForElementToPresent(By.id("msisdn")), msisdn);
		} catch (Exception e) {
			logger.error("Error while entering the MSISDN", e);
		}
		return this;
	}

	public boolean validateRegCustObjects() {
		boolean value1 = this.waitForElementToPresent(By.id("label_msisdn")).getText().trim()
				.equalsIgnoreCase("Mobile Number  :  *");
		boolean value2 = this.waitForElementToPresent(By.id("msisdn")).getAttribute("type").equalsIgnoreCase("text");

		boolean value3 = driver.findElement(By.id("label_offerId")).getText().trim()
				.equalsIgnoreCase("Available Offers  :  *");
		boolean value4 = driver.findElement(By.id("offerId")).getAttribute("type").equalsIgnoreCase("checkbox");

		boolean value6 = driver.findElement(By.id("label_fName")).getText().trim()
				.equalsIgnoreCase("Customer Name  :  *");
		boolean value7 = this.waitForElementToPresent(By.id("fName")).getAttribute("type").equalsIgnoreCase("text");
		boolean value8 = this.waitForElementToPresent(By.id("label_nid")).getText().trim().equalsIgnoreCase("ID  :");
		boolean value9 = this.waitForElementToPresent(By.id("nid")).getAttribute("type").equalsIgnoreCase("text");
		boolean value10 = this.waitForElementToPresent(By.id("label_dob")).getText().trim()
				.equalsIgnoreCase("Date of Birth  : *");
		boolean value11 = this.waitForElementToPresent(By.id("dob")).getAttribute("type").equalsIgnoreCase("text");
		boolean value12 = this.waitForElementToPresent(By.id("label_age")).getText().trim().equalsIgnoreCase("Age  :");
		boolean value13 = this.waitForElementToPresent(By.id("age")).getAttribute("type").equalsIgnoreCase("text");
		boolean value14 = this.waitForElementToPresent(By.id("label_gender")).getText().trim()
				.equalsIgnoreCase("Gender  :  *");
		boolean value15 = this.waitForElementToPresent(By.id("gender")).getAttribute("type").equalsIgnoreCase("radio");

		boolean value16 = this.waitForElementToPresent(By.id("label_insRelation")).getText().trim()
				.equalsIgnoreCase("Relationship with Customer  : *");
		boolean value17 = this.waitForElementToPresent(By.id("insRelation")).getTagName().equalsIgnoreCase("select");

		boolean value18 = this.waitForElementToPresent(By.id("label_irName")).getText().trim()
				.equalsIgnoreCase("Beneficiary's Name  :  *");
		boolean value19 = this.waitForElementToPresent(By.id("irName")).getAttribute("type").equalsIgnoreCase("text");

		boolean value20 = this.waitForElementToPresent(By.id("saveBtn")).getText().trim().equalsIgnoreCase("Save");
		boolean value21 = this.waitForElementToPresent(By.id("clearBtn")).getText().trim().equalsIgnoreCase("Clear");
		if (value1 && value2 && value3 && value4 && value6 && value7 && value8 && value9 && value10 && value11
				&& value12 && value13 && value14 && value15 && value16 && value17 && value18 && value19 && value20
				&& value21) {
			return true;
		}
		return false;
	}

	public MIP_RegisterCustomerPage selectOfferInfo(String offer, String benefit) {
		logger.info("Entering the offer information");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.waitForElementToVisible(By.id("offerId")).click();
		logger.info("Selected Available offers");

		try {
			this.selectDropDownbyText(this.waitForElementToVisible(By.id("offerCoverIdInsurance")), benefit.trim());

		} catch (Exception e) {
			this.selectDropDownbyText(this.waitForElementToVisible(By.id("offerCoverIdInsurance")), benefit.trim());
		}

		return this;
	}

	public MIP_RegisterCustomerPage enterCustInfo(String name, String id, String dob, String gender) {
		this.enterText(this.waitForElementToPresent(By.id("fName")), name);
		if (!id.equals("")) {
			this.enterText(this.waitForElementToPresent(By.id("nid")), id);
		}
		this.enterText(this.waitForElementToPresent(By.id("dob")), dob);
		if (!gender.equals("")) {
			if (gender.equalsIgnoreCase("Male")) {
				driver.findElement(By.xpath("//input[@id='gender'][@value='M']")).click();
			} else {
				driver.findElement(By.xpath("//input[@id='gender'][@value='F']")).click();
			}
		}
		return this;
	}

	public MIP_RegisterCustomerPage enterBenInfo(String relation, String name) {
		if (!relation.equals("")) {
			this.selectDropDownbyText(this.waitForElementToPresent(By.id("insRelation")), relation);
		}
		this.enterText(this.waitForElementToPresent(By.id("irName")), name);
		return this;
	}

	public MIP_RegisterCustomerPage clickOnSave() {
		logger.info("Clicking on Save button in Customer REgistration page");
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	public void clickOnClear() {
		this.clickOnElement(By.id("clearBtn"));
	}

	public MIP_RegisterCustomerPage confirmCustReg(String option) {
		this.confirmPopUp(option);
		return this;
	}

	public String getSuccessMsg() {
		return this.waitForElementToPresent(By.id("message_div")).getText();
	}

	public String getErrorMsg() {
		return this.waitForElementToPresent(By.id("errormessage_div")).getText();
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Customer registration page");
		return this.waitForElementToPresent(By.id("validationMessages")).getText();
	}

	public String msisdnReadOnlyCheck() {

		return this.waitForElementToPresent(By.id("msisdn")).getAttribute("readonly");
	}

	public boolean offerInfoReadOnlyCheck() {

		String str1 = this.waitForElementToPresent(By.id("offerId")).getAttribute("disabled");
		String str2 = this.waitForElementToPresent(By.id("offerCoverIdInsurance")).getAttribute("disabled");
		if (str1.equalsIgnoreCase("true") && str2.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public boolean custInfoReadOnlyCheck() {
		String cusName = this.waitForElementToPresent(By.id("fName")).getAttribute("readonly");
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

		String str1 = this.waitForElementToPresent(By.id("insRelation")).getAttribute("disabled");
		String str2 = this.waitForElementToPresent(By.id("irName")).getAttribute("readonly");
		if (str1.equalsIgnoreCase("true") && str2.equalsIgnoreCase("true")) {
			return true;
		}
		return false;

	}

	public boolean verifyBackButton() {
		return this.waitForElementToPresent(By.id("backBtn")).isDisplayed();
	}

	public List<WebElement> getBenefitleveloption() {
		this.waitForElementToVisible(By.id("offerId")).click();
		return this.selectDropDownOptions(this.waitForElementToVisible(By.id("offerCoverIdInsurance")));
	}
}
