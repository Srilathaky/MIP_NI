package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_SMSManagement_TestData {

	/**
	 * Data in the order Template Name,Content,SMS validity
	 * 
	 * @return
	 */
	@DataProvider(name = "EditSMSTestData")
	public static String[][] editSMS() {
		String[][] data = { { "confirmation_sucess_life",
				"Bienvenido al servicio Seguro de Vida. Se cargara C$ <AC> al mes. Respaldado por Seguros  LAFISE.",
				"10", "The SMS Template has been saved successfully. Click here to go back" } };
		return data;
	}

	/**
	 * template name, content field validation message,
	 * 
	 * @return
	 */
	@DataProvider(name = "EditSMSContentErrorMessage")
	public static String[][] EditSMSContentErrorMessage() {
		String[][] data = { { "confirmation_sucess_life", "Content :  Field should not be empty.",
				"Content :  SMS Content should contain all the place holders in the list provided." } };
		return data;
	}

	/**
	 * template Name,validity,error message
	 * 
	 * @return
	 */
	@DataProvider(name = "EditSMSValidtyErrorMessage")
	public static String[][] EditSMSValidtyErrorMessage() {
		String[][] data = { { "confirmation_sucess_life", "", "SMS Validity :  Field should not be empty." },
				{ "registration_success_life", "as", "SMS Validity :  Enter a valid number." } };
		return data;
	}

	@DataProvider(name = "refundSMS")
	public static String[][] refundSMS() {
		String[][] data = { { "82865838" }, { "82922434" } };
		return data;
	}

	@DataProvider(name = "RefundSMSNegative")
	public static String[][] refundSmsNegative() {
		return storeData("MIP_RefundSMS_Negative_Scenarios");
	}

	public static String[][] storeData(String filename) {
		Sheet s = MIP_XLOperation.loadXL(filename);
		int num_row = MIP_XLOperation.getNumRows();
		int num_cell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
		String[][] data = new String[num_row - 1][num_cell];
		for (int i = 1; i < num_row; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception e) {
						data[rowcount][j] = new Double(r.getCell(j).getNumericCellValue()).longValue() + "";
					}
				}
			}
			rowcount++;
		}
		return data;
	}

	/**
	 * Msisdn,errormessage
	 * 
	 * @return
	 */
	@DataProvider(name = "sendSMSTest")
	public static String[][] sendSMSTestData() {
		String[][] data = { { "85905867", "Send SMS  :  Please choose a value from the Drop down." } };
		return data;
	}

	@DataProvider(name = "generateRefundSMS")
	public static String[][] generateRefundSMS() {

		return storeData("MIP_RefundSMS_Positive_Scenarios");
	}

	/**
	 * postpaid msisdn,succes msg,sms prepaid msisdn,success msg,sms
	 * 
	 * @return
	 */
	@DataProvider(name = "returnRefund")
	public static String[][] returnRefundTestData() {
		String[][] data = {
				{ "85905867", "SMS Sent successfully.",
						"Su solicitud ha sido recibida y será analizada dentro de 1 día hábil" },
				{ "76887156", "SMS Sent successfully.",
						"Su solicitud ha sido recibida y será analizada dentro de 1 día hábil" } };
		return data;
	}
}
