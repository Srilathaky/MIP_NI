package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

import com.milvik.mip.dbqueries.MIP_AdminConfig_Queries;

public class MIP_ResetPassword_TestData {
	/**
	 * Test Data in the order "USer ID","Success Message",default
	 * password,invalid password,error message,valid password,success message
	 */
	@DataProvider(name = "ResetPassword")
	public String[][] resetPasswordData() {
		String defaultPass = MIP_AdminConfig_Queries.getDefaultPassword();
		String[][] data = { { "TA16",
				"The password has been reset and/or the account has been unlocked successfully. Click here to go back",
				defaultPass, "milvik123@",
				"Nueva Contrase�a : Debe de cumplir con los siguientes criterios,"
						+ "Un longitud mínima de 8 caracteres,"
						+ "Por lo menos un carácter del alfabeto en mayúsculas,"
						+ "Por lo menos un carácter del alfabet en minúsculas." + "Por lo menos un número."
						+ "Por lo menos un carácter especial.",
				"MIilvik123@", "Su contrase�a ha sido cambiada con �xito" } };
		return data;
	}

	/**
	 * User Name,Error Message
	 * 
	 * @return
	 */
	@DataProvider(name = "ResetPassNegative")
	public String[][] resetPassNegativeTestData() {
		String[][] data = { { "BDERE", "No user record found for the entered User Id." },
				{ "", "User ID:Field should not be empty." } };
		return data;

	}
}
