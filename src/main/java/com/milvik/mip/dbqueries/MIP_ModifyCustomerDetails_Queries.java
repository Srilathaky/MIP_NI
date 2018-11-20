package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_ModifyCustomerDetails_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_ModifyCustomerDetails_Queries");
	}
	static Map<String, String> modifcustDetails = new HashMap<String, String>();

	public static Map<String, String> getModifiedCustomerDetails(String msisdn) {
		logger.info("Executing getModifiedCustomerDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT name,dob,gender,nid from customer_details where msisdn="
							+ msisdn + ";");
			result.next();

			modifcustDetails.put("name", result.getString("name"));
			modifcustDetails.put("dob", result.getString("dob"));
			modifcustDetails.put("nid", result.getString("nid"));
			if (result.getString("gender").equalsIgnoreCase("M")) {
				modifcustDetails.put("gender", "Male");
			} else {
				modifcustDetails.put("gender", "Female");
			}

			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT rd.name,cr.cust_relationship FROM insured_relative_details rd join customer_relationship_table cr on cr.cr_id=rd.cust_relationship "
							+ "where cust_id=(SELECT cust_id FROM customer_details where msisdn="
							+ msisdn + ");");
			result.next();
			modifcustDetails.put("ben_name", result.getString("name"));
			modifcustDetails.put("insured_relative_details",
					result.getString("cust_relationship"));

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getModifiedCustomerDetails queries",
					e);
		}
		return modifcustDetails;
	}


}
