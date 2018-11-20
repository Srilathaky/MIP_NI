package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_RegisterCustomer_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_RegisterCustomer_Queries");
	}
	static Map<String, String> custDetails = new HashMap<String, String>();
	static Map<String, String> offer_subs = new HashMap<String, String>();
	static Map<String, String> ben_info = new HashMap<String, String>();
	static String sms;

	public static Map<String, String> getCustomerDetails(String msisdn) {
		logger.info("Executing getCustomerDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select is_prepaid,plan_code,Type,is_active,gender FROM customer_details where msisdn="
							+ msisdn + ";");
			result.next();

			custDetails.put("is_prepaid", result.getString("is_prepaid"));
			custDetails.put("plan_code", result.getString("plan_code"));
			custDetails.put("Type", result.getString("Type"));
			custDetails.put("is_active", result.getString("is_active"));
			if (result.getString("gender").equalsIgnoreCase("M")) {
				custDetails.put("gender", "Male");
			} else {
				custDetails.put("gender", "Female");
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDetails queries", e);
		}
		return custDetails;
	}

	public static Map<String, String> getOfferSubscription(String msisdn) {
		logger.info("Executing getOfferSubscription query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("Select offer_id,offer_cover_id,	registration_channel_id from  offer_subscription "
							+ "where cust_id=(SELECT cust_id FROM customer_details where msisdn="
							+ msisdn + ");");
			result.next();

			offer_subs.put("offer_id", result.getString("offer_id"));
			offer_subs
					.put("offer_cover_id", result.getString("offer_cover_id"));
			offer_subs.put("registration_channel_id",
					result.getString("registration_channel_id"));

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDetails queries", e);
		}
		return offer_subs;
	}

	public static Map<String, String> getBeninfo(String msisdn) {
		logger.info("Executing getBeninfo query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT rd.name,cr.cust_relationship FROM insured_relative_details rd join customer_relationship_table cr on cr.cr_id=rd.cust_relationship "
							+ "where cust_id=(SELECT cust_id FROM customer_details where msisdn="
							+ msisdn + ");");
			result.next();

			ben_info.put("name", result.getString("name"));
			ben_info.put("cust_relationship",
					result.getString("cust_relationship"));

		} catch (SQLException e) {
			logger.error("Error while executing the getBeninfo queries", e);
		}
		return ben_info;
	}

	public static String getSMSText(String msisdn) {
		logger.info("Executing getSMSText query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT sms_text FROM sms_in_queue where sms_msisdn="
							+ msisdn + ";");
			result.next();
			sms = result.getString("sms_text");

		} catch (SQLException e) {
			logger.error("Error while executing the getSMSText queries", e);
		}
		return sms;
	}

	public static boolean getCustomerID(String ID) {
		logger.info("Executing getCustomerID query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT nid FROM customer_details where nid='"
							+ ID + "';");
			boolean rs = result.next();
			if (rs) {
				if (result.getString("nid").equals(ID)) {
					return true;
				}
			} else {
				return false;
			}

		} catch (SQLException e) {
			logger.error("Error while executing the getSMSText queries", e);
		}
		return false;
	}

}
