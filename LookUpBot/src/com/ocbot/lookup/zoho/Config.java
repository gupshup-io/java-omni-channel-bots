package com.ocbot.lookup.zoho;

import com.adventnet.zoho.client.report.ReportClient;

public class Config
{
	static final String DATABASENAME = "SalesReport";
	static final String LOGINEMAILID = "madhu@gupshup.io";
	static final String AUTHTOKEN = "0b4f2146357ac096654f7752107e9d02";

	// Set USEPROXY to true if proxy is present.
	static final boolean USEPROXY = false;

	static final String PROXYSERVER = "your proxy server ip or host name";
	static final int PROXYPORT = 80;
	static final String PROXYUSERNAME = "your proxy user name";
	static final String PROXYPASSWORD = "your proxy password";

	public static ReportClient getReportClient()
	{
		ReportClient rc = new ReportClient(Config.AUTHTOKEN);
		if (USEPROXY)
		{
			rc.setProxy(PROXYSERVER, PROXYPORT, PROXYUSERNAME, PROXYPASSWORD, "BOTH");
		}
		System.out.println("ReportClient created successfully");
		return rc;
	}

}
