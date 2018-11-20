package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_CoverHistory_SwitchProductLevel_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging
				.logDetails("MIP_CoverHistory_SwitchProductLevel_Queries");
	}

	public static List<String> getCoverHistoryDetails(String msisdn) {
		List<String> coverDetails = new ArrayList<String>();

		logger.info("Executing getCoverHistoryDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT month,year,offer_cover_id as cover_level,offer_charges as Deducted_charges,offer_cover as Cover_Earned,rolled_out_amount "
							+ "FROM cover_history where msisdn=" + msisdn + ";");

			while (result.next()) {
				coverDetails.add(result.getString("month"));
				coverDetails.add(result.getString("year"));
				coverDetails.add(result.getString("cover_level"));
				coverDetails.add(result.getString("Deducted_charges"));
				coverDetails.add(result.getString("Cover_Earned"));
				coverDetails.add(result.getString("rolled_out_amount"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCoverHistoryDetails queries",
					e);
		}
		return coverDetails;
	}
}
