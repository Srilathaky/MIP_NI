package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class MIP_DeRegisterCustomer_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeRegisterCustomer_Queries");
	}
	static Map<String, String> custDetails = new HashMap<String, String>();
	static Map<String, String> deregcustDetails = new HashMap<String, String>();

	public static Map<String, String> getCustomerInfo(String msisdn) {
		logger.info("Executing getCustomerInfo for DeRegistration query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT cd.name,od.offer_name,ocd.offer_cover as offer_level,os.offer_cover,os.deducted_offer_amount,os.is_confirmed"
							+ " from offer_details od join offer_subscription os on os.offer_id=od.offer_id"
							+ " inner join  offer_cover_details ocd on os.offer_cover_id=ocd.offer_cover_id"
							+ " inner join customer_details cd on cd.cust_id=os.cust_id"
							+ " where os.cust_id=(select cd.cust_id from customer_details cd where cd.msisdn='"
							+ msisdn + "');");
			result.next();

			custDetails.put("cust_name", result.getString("name"));
			custDetails.put("offer_name", result.getString("offer_name"));
			custDetails.put("offer_level", result.getString("offer_level"));
			custDetails.put("offer_cover", result.getString("offer_cover"));
			custDetails.put("deducted_amount",
					result.getString("deducted_offer_amount"));
			if (result.getInt("is_confirmed") == 1) {
				custDetails.put("confirmation", "Confirmed");
			} else {
				custDetails.put("confirmation", "Not Confirmed");
			}
			if (custDetails.get("offer_cover") == null) {
				custDetails.put("offer_cover", "0.00");
			}
			if (custDetails.get("deducted_amount") == null) {
				custDetails.put("deducted_amount", "0");
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDetails queries", e);
		}
		return custDetails;
	}

	public static Map<String, String> getDeregisterCustInfo(String msisdn) {
		logger.info("Executing getDeregisterCustInfo query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT bc.churn_type as bc_churn_type,oc.churn_type as oc_churn_type,oc.refund_amount, date(bc.bc_date) as bc_date,date(oc.oc_date) as oc_date,date(oc.record_deletion_date) as record_deletion_date,"
							+ "(SELECT sq.sms_text from  sms_in_queue sq where  sq.sms_msisdn="
							+ msisdn
							+ " and sms_template_name='dereg_success_life') as sms_text"
							+ " FROM bima_cancellations bc "
							+ "join offer_cancellations oc on bc.cust_id=oc.cust_id "
							+ "where bc.cust_id=(select cust_id from bima_cancellations where msisdn="
							+ msisdn + ");");
			result.next();
			deregcustDetails.put("bc_churn_type",
					result.getString("bc_churn_type"));
			deregcustDetails.put("oc_churn_type",
					result.getString("oc_churn_type"));
			deregcustDetails.put("bc_date", result.getString("bc_date"));
			deregcustDetails.put("oc_date", result.getString("oc_date"));
			deregcustDetails.put("record_deletion_date",
					result.getString("record_deletion_date"));
			deregcustDetails.put("sms_text", result.getString("sms_text"));
			if (result.getString("refund_amount") == null) {
				deregcustDetails.put("refund_amount", "0");
			} else {
				deregcustDetails.put("refund_amount",
						result.getString("refund_amount"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeregisterCustInfo queries",
					e);
		}
		return deregcustDetails;
	}

	public static int getDeRegsterCustInCustDetails(String msisdn) {
		int count = -1;
		logger.info("Executing getDeRegsterCustInCustDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(cust_id) as count FROM customer_details where msisdn='"
							+ msisdn + "';");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeRegsterCustInCustDetails queries",
					e);
		}
		return count;
	}

	public static int getDeRegsterCustInOferSubscription(String cust_id) {
		int count = -1;
		logger.info("Executing getDeRegsterCustInOferSubscription query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(cust_id) as count FROM offer_subscription where cust_id="
							+ cust_id + ";");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getDeRegsterCustInCustDetails queries",
					e);
		}
		return count;
	}

	public static String getCustID(String msisdn) {
		String custi_id = "";
		logger.info("Executing getCustID query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT cust_id  FROM customer_details where msisdn='"
							+ msisdn + "';");
			result.next();
			custi_id = result.getString("cust_id");

		} catch (SQLException e) {
			logger.error("Error while executing the getCustID queries", e);
		}
		return custi_id;
	}

	public static void main(String[] args) {
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase();
		// Map<String, String> details = getDeregisterCustInfo("01546548");

		System.out.println(getCustID("28655822"));
	}
}
