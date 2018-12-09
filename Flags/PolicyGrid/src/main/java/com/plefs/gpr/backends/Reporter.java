package com.plefs.gpr.backends;

import java.io.IOException;
import java.net.InetAddress;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Reporter {
	static ExtentReports report = null;
	static ExtentTest extentReportLogger;
	public static String folderPath = null;
	//public static String failedfolderPath = null;
	public static String reportName = null;
	static String environment = null;

	/**********************************************************************************************
	 * Description:This method will capture the details of the environment in
	 * which the script is executing. Created on : 03/08/2018
	 *********************************************************************************************/
	public static void setReportDetails(String path, String userName) throws IOException {
		report = new ExtentReports(path, false);
		environment = getExecutionEnvironment();
		report.config().reportName("GPR-2 Automation:");
		report.config().reportHeadline("Policy Grid Compare Execution Report");
		report.addSystemInfo("Host Name", InetAddress.getLocalHost().getHostName()).addSystemInfo("Environment", environment).addSystemInfo("User Name", userName);
	}

	public static String getExecutionEnvironment() throws IOException {
		environment = Backend.getProperty("URL");
		if (environment.contains("L4")) {
			environment = "L4";
		} else if (environment.contains("L3")) {
			environment = "L3";
		} else if (environment.contains("L2")) {
			environment = "L2";
		} else if (environment.contains("L1")) {
			environment = "L1";
		} else {
			environment = Backend.getProperty("URL");
		}
		return environment;
	}

	/**********************************************************************************************
	 * Description:This method will close the report. Created on : 03/08/2018
	 *********************************************************************************************/
	public static void flushReport() {
		
		report.flush();
		report.close();

	}

	/**********************************************************************************************
	 * Description:This method will print the data in the Report Created on :
	 * 03/08/2018
	 *********************************************************************************************/

	public static void testCaseStart(String TestName, String Description) {
		try {
			extentReportLogger = report.startTest(TestName, Description);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**********************************************************************************************
	 * Description:This method will end the test case in the report Created on :
	 * 03/08/2018
	 *********************************************************************************************/

	public static void testCaseEnd() {

		try {
			report.endTest(extentReportLogger);
			extentReportLogger = null;
		} catch (Exception e) {

		}

	}

	/**********************************************************************************************
	 * Description:This method will provide the status in the report. Created on
	 * : 03/08/2018
	 *********************************************************************************************/

	public static void setTestDetails(String status, String stepName, String testDetails, String screenShotPath) {
		try {
			String testCaseDetail = testDetails + "<br>" + extentReportLogger.addScreenCapture(screenShotPath);
			if (status.equalsIgnoreCase("PASS")) {
				extentReportLogger.log(LogStatus.PASS, stepName, testCaseDetail);
			} else if (status.equalsIgnoreCase("FAIL")) {
				extentReportLogger.log(LogStatus.FAIL, stepName, testCaseDetail);
			} else if (status.equalsIgnoreCase("ERROR")) {
				extentReportLogger.log(LogStatus.ERROR, stepName, testCaseDetail);
			} else if (status.equalsIgnoreCase("INFO")) {
				extentReportLogger.log(LogStatus.INFO, stepName, testCaseDetail);
			} else if (status.equalsIgnoreCase("EXCEPTION")) {
				extentReportLogger.log(LogStatus.UNKNOWN, stepName, testCaseDetail);
			} else {
				extentReportLogger.log(LogStatus.INFO, stepName, testCaseDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void intilizeReporter() {
		try {
			String reportPath = Backend.getProperty("ReportPath");
			String currentDateTime = Backend.currentDateTime;
			folderPath = reportPath + "\\" + "execution-report" + "_" + currentDateTime;
			reportName = folderPath + "\\execution-report_" + currentDateTime + ".html";
			Reporter.setReportDetails(reportName, System.getProperty("user.name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void failintilizeReporter() {
		try {
			String reportPath = Backend.getProperty("ReportPath");
			String currentDateTime = Backend.currentDateTime;
			folderPath = reportPath + "\\" + "Failed_execution-report" + "_" + currentDateTime;
			reportName = folderPath + "\\Failed_execution-report_" + currentDateTime + ".html";
			Reporter.setReportDetails(reportName, System.getProperty("user.name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void terminateReporter() {
		Reporter.flushReport();
	}
}
