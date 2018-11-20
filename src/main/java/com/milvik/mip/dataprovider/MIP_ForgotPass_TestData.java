package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_ForgotPass_TestData {
	/**
	 * Logi Id,Error Message
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] invaliduser() {
		String[][] data = { { "invalid",
				"El id de usuario ingresado es inválido." } };
		return data;
	}

	/**
	 * Logi Id
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] validinvaliduser() {
		String[][] data = { { "invalid", "DASHBOARD" } };
		return data;
	}

	/**
	 * Error message
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] withoutuserid() {
		String[][] data = { { "Please enter Login ID" } };
		return data;
	}

	/**
	 * Login ID,Error message
	 * 
	 * @return
	 */
	@DataProvider
	public String[][] withoutEmail() {
		String[][] data = { {
				"DASHBOARD",
				"Lo sentimos, no tenemos su ID de correo electrónico. Por favor pongase en contacto con el centro de ayuda para reinicar su contraseña." } };
		return data;
	}

}
