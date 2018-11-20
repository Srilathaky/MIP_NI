package com.milvik.mip.pageobjects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.constants.MIP_Constants;
import com.milvik.mip.pageutil.MIP_BasePage;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ApplyLeavePage extends MIP_BasePage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ApplyLeavePage");
	}

	public MIP_ApplyLeavePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "userId")
	public WebElement userName;
	@FindBy(id = "fromDate")
	public WebElement fromDate;
	@FindBy(id = "toDate")
	public WebElement toDate;
	@FindBy(id = "reason")
	public WebElement reason;
	@FindBy(id = "applyBtn")
	public WebElement applyBtn;
	@FindBy(id = "clearBtn")
	public WebElement clearBtn;
	@FindBy(id = "calBut1")
	public WebElement fromCalender;
	@FindBy(xpath = "//input[@id='toDate']/following-sibling::div[@id='calBut2']")
	public WebElement toCalender;

	/**
	 * This method will gives the count of the user in the UserName Dropdown
	 * field in Apply Leave page
	 */
	public int getUserDetails() {
		logger.info("Getting UserDetails from ApplyLeavePage");
		int count = 0;
		List<WebElement> username = this.selectDropDownOptions(userName);
		for (int i = 0; i < username.size(); i++) {
			if (username.get(i).getText().equals("")) {
				count++;
			}
		}
		return username.size() - count;
	}

	/**
	 * This method will return the selected option of the userName dropdown
	 * 
	 * @return
	 */
	public String getSelectedOptionFromUserNameDropDown() {
		logger.info("Getting selected option from User Name DropDown in Apply Leave Page");
		return this.getSelectedOption(userName);
	}

	/**
	 * This method will return the selected option of the Reason dropdown
	 * 
	 * @return
	 */
	public String getSelectedOptionFromReasonDropDown() {
		logger.info("Getting selected option from Reason DropDown in Apply Leave Page");
		return this.getSelectedOption(reason);
	}

	/**
	 * This method will select the username from the username dropdown
	 * 
	 * @param userName
	 */
	public void selectUserName(String userName) {
		logger.info("Selecting USerName in Apply Leave Page");
		this.waitForElementToVisible(By.id("userId"));
		if (!userName.equals(""))
			this.selectDropDownbyText(this.userName, userName.trim());
	}

	/**
	 * This method will select the reason from the reason dropdown
	 * 
	 * @param reason
	 */
	public void selectReason(String reason) {
		logger.info("Selecting Reason in Apply Leave Page");
		if (!reason.equals(""))
			this.selectDropDownbyText(this.reason, reason);
	}

	/**
	 * This method will clicks on the From Date calender icon
	 * 
	 * @return
	 */
	public MIP_ApplyLeavePage clickOnFromDate() {
		logger.info("Clicking on From Date in Apply Leave Page");
		this.fromCalender.click();
		return this;
	}

	/**
	 * This method will clicks on the To Date calender icon
	 * 
	 * @return
	 */
	public MIP_ApplyLeavePage clickOnToDate() {
		logger.info("Clicking on To Date in Apply Leave Page");
		this.waitForElementTobeClickable(By.xpath("//div[@class='calendar-icon'][@id='calBut2']"));
		driver.findElement(By.xpath("//div[@class='calendar-icon'][@id='calBut2']")).click();
		logger.info("Clicked on To Date");
		return this;
	}

	/**
	 * This Method will select the date to apply the leave
	 * 
	 * @param dob
	 */
	public void selectLeaveDate(String dob) {
		logger.info("Selecting  From Date in Apply Leave Page");
		if (!dob.equals("")) {
			String[] date = MIP_DateFunctionality.getDate(dob, MIP_Constants.DOB_FORMAT);
			this.waitForElementToPresent(By.xpath("//div[@class='calendar-icon']"));
			Actions a = new Actions(driver);
			a.moveToElement(this.waitForElementToVisible(
					By.xpath("//table[@class='DynarchCalendar-titleCont']/tbody/tr/td/div/div"))).build().perform();
			this.waitForElementToVisible(By.xpath("//table[@class='DynarchCalendar-titleCont']/tbody/tr/td/div/div"))
					.click();
			WebElement ele = this.waitForElementToVisible(By.className("DynarchCalendar-menu-year"));
			ele.clear();
			ele.sendKeys(date[2]);
			this.waitForElementTobeClickable(
					By.xpath("//table[@class='DynarchCalendar-menu-mtable']/tbody//tr//td/div[contains(text(),'"
							+ date[1] + "')]"))
					.click();
			if ((date[0].charAt(0) + "").equals("0")) {
				date[0] = date[0].charAt(1) + "";
			}

			this.waitForElementToVisible(By
					.xpath("//div[@class='DynarchCalendar-body']/table[@class='DynarchCalendar-bodyTable']/tbody//tr//td/div[@class='DynarchCalendar-day' ][contains(text(),'"
							+ date[0] + "')]"))
					.click();
		}
	}

	public void selectToLeaveDate(String dob) {
		logger.info("Selecting To  Date in Apply Leave Page");
		if (!dob.equals("")) {
			String[] date = MIP_DateFunctionality.getDate(dob, MIP_Constants.DOB_FORMAT);
			driver.findElement(By.xpath("//div[@class='calendar-icon'][@id='calBut2']")).click();
			Actions a = new Actions(driver);
			WebElement ele;
			try {
				a.moveToElement(this.waitForElementToVisible(
						By.xpath("/html/body/table[2]/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div"))).build()
						.perform();
				this.waitForElementToVisible(
						By.xpath("/html/body/table[2]/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div")).click();
				ele = this.waitForElementToVisible(By.xpath(
						"/html/body/table[2]/tbody/tr/td/div/div[4]/table/tbody/tr/td/table[1]/tbody/tr[1]/td/input"));
				ele.clear();
				ele.sendKeys(date[2]);
				this.waitForElementToVisible(By
						.xpath("/html/body/table[2]/tbody/tr/td/div/div[4]/table/tbody/tr/td/table[2]/tbody//tr//td/div[contains(text(),'"
								+ date[1] + "')]"))
						.click();
				if ((date[0].charAt(0) + "").equals("0")) {
					date[0] = date[0].charAt(1) + "";
				}
			} catch (Exception e) {
				a.moveToElement(this.waitForElementToVisible(
						By.xpath("/html/body/table/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div"))).build()
						.perform();
				this.waitForElementToVisible(
						By.xpath("/html/body/table/tbody/tr/td/div/div[1]/table/tbody/tr/td/div/div")).click();
				ele = this.waitForElementToVisible(By.xpath(
						"/html/body/table/tbody/tr/td/div/div[4]/table/tbody/tr/td/table[1]/tbody/tr[1]/td/input"));
				ele.clear();
				ele.sendKeys(date[2]);
				this.waitForElementToVisible(By
						.xpath("/html/body/table/tbody/tr/td/div/div[4]/table/tbody/tr/td/table[2]/tbody/tr[1]/td[1]/div[contains(text(),'"
								+ date[1] + "')]"))
						.click();
				if ((date[0].charAt(0) + "").equals("0")) {
					date[0] = date[0].charAt(1) + "";
				}
			}

			try {
				this.waitForElementToVisible(By
						.xpath("/html/body/table[2]/tbody/tr/td/div/div[2]/table/tbody//tr//td/div[@class='DynarchCalendar-day' ][contains(text(),'"
								+ date[0] + "')]"))
						.click();
			} catch (Exception e) {
				this.waitForElementToVisible(By
						.xpath("/html/body/table/tbody/tr/td/div/div[2]/table/tbody//tr//td/div[@class='DynarchCalendar-day' ][contains(text(),'"
								+ date[0] + "')]"))
						.click();

			}
		}
	}

	public MIP_ApplyLeavePage clearFromDate() {
		logger.info("Clearing From Date field in Apply Leave Page");
		this.waitForElementToVisible(By.xpath("//input[@id='fromDate']/following-sibling::div[@class='clear-icon']"))
				.click();
		return this;
	}

	public MIP_ApplyLeavePage clearToDate() {
		logger.info("Clearing To Date field in Apply Leave Page");

		this.waitForElementToVisible(By.xpath("//input[@id='toDate']/following-sibling::div[@class='clear-icon']"))
				.click();
		return this;
	}

	public MIP_ApplyLeavePage clickOnClear() {
		logger.info("Clicking on Clear  in Apply Leave Page");

		this.clickOnElement(By.id("clearBtn"));
		return this;
	}

	public MIP_ApplyLeavePage clickOnApply() {
		logger.info("Clicking on Apply Button in Apply Leave Page");

		this.clickOnElement(By.id("applyBtn"));
		return this;
	}

	public String getValidationMessage() {
		logger.info("Getting validation message in Apply Leave Page");
		return this.waitForElementToVisible(By.id("validationMessages")).getText();

	}

	public String getSuccessMessage() {
		logger.info("Getting Success message in Apply Leave Page");
		return this.waitForElementToVisible(By.className("error-div-body")).getText();

	}

}
