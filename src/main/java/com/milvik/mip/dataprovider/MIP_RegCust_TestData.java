package com.milvik.mip.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

/**
 * @author srilatha_yajnanaraya
 *
 */
public class MIP_RegCust_TestData {

	/**
	 * mobilenum,offer,benefitlevel,cust_name,id,String dob, gender, relation,
	 * ben_name,"success masg", Type, is_active, is_prepaid, plan_code,
	 * offer_cover_id, registration_channel_id, offer_id, sms
	 */
	@DataProvider(name = "RegPrepaidActive")
	public String[][] RegisterPrepaidActive() {
		String data[][] = { {
				"25458110",
				"Life Insurance",
				"40,000",
				"Test MIP",
				"1234556547851289",
				"30/11/1990",
				"Male",
				"Father",
				"Test Ben",
				"The customer has been registered successfully.",
				"Pre",
				"1",
				"1",
				"3E",
				"1",
				"1",
				"1",
				"Bienvenido al servicio Seguro de Vida. Responda a este mensaje con la palabra SI para confirmar su registro. Respaldado por Seguros LAFISE." } };
		return data;
	}
	/**
	 * mobilenum,offer,benefitlevel,cust_name,id,String dob, gender, relation,
	 * ben_name,"success masg", Type, is_active, is_prepaid, plan_code,
	 * offer_cover_id, registration_channel_id, offer_id, sms
	 */
	@DataProvider(name = "RegPrepaidSuspend")
	public String[][] RegisterPrepaidSuspended() {
		String data[][] = { {
				"25458569",
				"Life Insurance",
				"80,000",
				"Test MIP",
				"1234556547851285",
				"30/11/1990",
				"Male",
				"Father",
				"Test Ben",
				"The customer has been registered successfully.",
				"Pre",
				"0",
				"1",
				"3E",
				"1",
				"1",
				"1",
				"Bienvenido al servicio Seguro de Vida. Responda a este mensaje con la palabra SI para confirmar su registro. Respaldado por Seguros LAFISE." } };
		return data;
	}

	/**Mobile Number,Error Message
	 * @return
	 */
	@DataProvider(name = "NotAllowedState")
	public String[][] RegisterPrepaidNotAllowed() {
		String data[][] = { { "25458569", "Please Enter valid MSISDN" } };
		return data;
	}
 
	/**Mobile Number,Error Message
	 * @return
	 */
	@DataProvider(name = "NegativeTest")
	public String[][] regCustNegative() {
		String data[][] = { { "25458",
				"Mobile Number  :  Entered text length must be of 8 characters" } };
		return data;
	}

	/**Error Message
	 * @return
	 */
	@DataProvider(name = "BlankMobileNum")
	public String[][] blankMobileNum() {
		String data[][] = { { "Mobile Number  :  Field should not be empty." } };
		return data;
	}

	@DataProvider(name = "Customer_Registartion")
	public String[][] customer_Registration() {
		return storeCellData("MIP_Customer_Registration");
	}

	/**Mobile Number
	 * @return
	 */
	@DataProvider(name = "Customer_Registartion_validateobject")
	public String[][] Customer_Registartion_validateobject() {
		String data[][] = { { "25185886" }, { "02145858" } };
		return data;
	}

	/**Mobile Number,Error Message
	 * @return
	 */
	@DataProvider(name = "MSISDN_Validatin")
	public String[][] msisdn_Validatin() {
		String data[][] = {
				{ "001458965", "Mobile Number:Enter a valid number." },
				{ "^$^$fhf", "Mobile Number:Enter a valid number." } };
		return data;
	}

	@DataProvider(name = "MIP_Customer_Registration_Positive_Scenario")
	public String[][] beneficiary_Relation() {
		return storeCellData("MIP_Customer_Registration_Positive_Scenario");
	}

	@DataProvider(name = "Customer_Registartion_NegativeScenarios")
	public String[][] Negative_scenario() {
		return storeCellData("MIP_Customer_Registration_Negative_Scenarios");
	}

	/**Mobile Number
	 * @return
	 */
	@DataProvider(name = "AlreadyExistingCust")
	public String[][] checkExistingCust() {
		String data[][] = { { "25185886" } };
		return data;
	}

	/**Benefit levels
	 * @return
	 */
	@DataProvider(name = "BenefitLevels")
	public String[][] getbenefitlevel() {
		List<String> pre_level = new ArrayList<String>();
		pre_level.add("40,000");
		pre_level.add("80,000");
		pre_level.add("160,000");

		List<String> pos_level = new ArrayList<String>();
		pos_level.add("88,000");
		pos_level.add("176,000");
		pos_level.add("384,000");
		String data[][] = { { "02547896", "PRE", pre_level.toString(),
				pos_level.toString() } };
		return data;
	}

	public static String[][] storeCellData(String filename) {
		Sheet s = MIP_XLOperation.loadXL(filename);
		int numRows = MIP_XLOperation.getNumRows();
		int numcell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
		int DOB_col = 0;
		for (int i = 0; i < numcell; i++) {

			if (s.getRow(0).getCell(i).getStringCellValue()
					.equalsIgnoreCase("DOB")) {
				DOB_col = i;
				break;
			}
		}
		String[][] data = new String[numRows - 1][numcell];
		for (int i = 1; i < numRows; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception ex) {
						if (j == DOB_col) {
							DataFormatter df = new DataFormatter();
							data[rowcount][j] = df
									.formatCellValue(r.getCell(j));
						} else {
							data[rowcount][j] = new Double(r.getCell(j)
									.getNumericCellValue()).longValue() + "";
						}

					}
				}

			}
			rowcount++;
		}
		return data;
	}

}
