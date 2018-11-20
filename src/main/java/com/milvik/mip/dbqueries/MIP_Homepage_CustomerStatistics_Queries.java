package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_Homepage_CustomerStatistics_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ReadPropertyFile");
	}

	public static String getConfirmedCust() {
		String count = "";
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM offer_subscription where is_confirmed=1;");
			result.next();
			count = result.getString("count");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return count;
	}

	public static String getUnconfirmedCust() {
		String count = "";
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM offer_subscription where is_confirmed=0;");
			result.next();
			count = result.getString("count");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return count;
	}

	public static String getSelfRegCount() {
		String count = "";
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM offer_subscription where created_by=2 ;");
			result.next();
			count = result.getString("count");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return count;
	}

	public static String getCustRegCount() {
		String count = "";
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM offer_subscription where created_by!=2 ;");
			result.next();
			count = result.getString("count");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return count;
	}

	public static String getCustConfCount() {
		String count = "";
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st.executeQuery(
					"SELECT count(*) as count FROM offer_subscription where created_by!=2 and is_confirmed=1;");
			result.next();
			count = result.getString("count");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return count;
	}

	public static String getConfCust_diffRole(String username) {
		String count = "";
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM offer_subscription where is_confirmed=1 and "
							+ "created_by=(SELECT user_id FROM user_details where user_uid='" + username
							+ "')and Month(confirmed_date)=Month(CURRENT_DATE());");
			result.next();
			count = result.getString("count");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return count;
	}

	public static String getUnconfCust_diffRole(String username) {
		String count = "";
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM offer_subscription where is_confirmed!=1 and "
							+ "created_by=(SELECT user_id FROM user_details where user_uid='" + username + "');");
			result.next();
			count = result.getString("count");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return count;
	}

	public static String userDetails(String username) {
		String details = null;
		logger.info("Executing query");
		try {
			result = MIP_DataBaseConnection.st.executeQuery(
					"select concat(fname,' ',sname) as name from  user_details where user_uid='" + username + "';");
			result.next();
			details = result.getString("name");
		} catch (SQLException e) {
			logger.error("Error while executing the sql queries", e);
		}
		return details;
	}
}
