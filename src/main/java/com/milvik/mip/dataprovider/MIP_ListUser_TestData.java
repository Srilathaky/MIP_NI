package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_ListUser_TestData {
	/**
	 * Mobile Number
	 * 
	 * @return
	 */
	@DataProvider(name = "ListUserData")
	public static String[][] listUserData() {
		String[][] data = { { "77148509" }, { "77657719" } };
		return data;
	}
}
