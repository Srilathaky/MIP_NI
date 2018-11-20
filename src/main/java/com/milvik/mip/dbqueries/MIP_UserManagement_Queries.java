package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_UserManagement_Queries {
	static ResultSet result;
	static Logger logger;
	static Map<String, String> userdetails = new HashMap<String, String>();
	static {
		logger = MIP_Logging.logDetails("MIP_AddUser_Queries");
	}

	public static Map<String, String> getUserInfo(String mobilenum) {
		try {
			logger.info("Executing the getUserInfo queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT user_uid,fname,sname,dob,gender,email_id,is_supervisor,role_name,branch_name FROM user_details ud join role_master rm on ud.role_id=rm.role_id join branch_details bd on ud.branch_id=bd.branch_id where ud.msisdn='"
							+ mobilenum + "';");
			while (result.next()) {
				userdetails.put("user_uid", result.getString("user_uid"));
				userdetails.put("fname", result.getString("fname"));
				userdetails.put("sname", result.getString("sname"));
				userdetails.put("dob", result.getString("dob"));
				userdetails.put("gender", result.getString("gender"));
				userdetails.put("email_id", result.getString("email_id"));
				if (result.getInt("is_supervisor") == 1) {
					userdetails.put("is_supervisor", "yes");
				} else if (result.getInt("is_supervisor") == 0) {
					userdetails.put("is_supervisor", "no");
				}

				userdetails.put("role_name", result.getString("role_name"));
				userdetails.put("branch_name", result.getString("branch_name"));
			}

		} catch (SQLException e) {
			logger.error("Error while executing the getUserInfo queries", e);
		}
		return userdetails;
	}

	public static int getActiveUser() {
		int count = 0;
		try {
			logger.info("Executing the getUserInfo queries");
			String prefix = MIP_AdminConfig_Queries.getLoginPrefix();
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count  FROM user_details where is_active=1 and user_uid like '"
							+ prefix + "%';");
			result.next();
			count = result.getInt("count");
		} catch (Exception e) {
			logger.error("Error while executing the getActiveUser queries", e);
		}
		return count;
	}

}