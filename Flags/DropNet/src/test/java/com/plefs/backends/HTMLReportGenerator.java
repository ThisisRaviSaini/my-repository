package com.plefs.backends;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



public class HTMLReportGenerator extends BaseClass 
{
	static ExtentReports report=null;
	static ExtentTest logger;
	
	public static void TestSuiteStart(String ResultHTMLFilePath,String
	UserName) throws UnknownHostException
	{
	report=new ExtentReports(ResultHTMLFilePath,false);
//	report.config().reportHeadline("Regression_Test_Report");
	report.addSystemInfo("Host Name", InetAddress.getLocalHost().getHostName())
	        .addSystemInfo("Environment", "QA")
	        .addSystemInfo("User Name", UserName); }
	
	public static void TestSuiteEnd() throws Exception
	{ 
		report.flush();report.close(); 
	}
	
	public static void TestCaseStart(String TestName,String Description) 
	{ 
		logger=report.startTest(TestName, Description); 
	}
	
	public static void TestCaseEnd() 
	{ report.endTest(logger); 
	}
	
	public static void StepDetails(String Status,String StepName,String StepDetails,String objectImagePath) 
	{ String tbl=StepDetails+"<br>"+logger.addScreenCapture(objectImagePath);
	
	if(Status.equalsIgnoreCase("pass"))
	{ logger.log(LogStatus.PASS,StepName,tbl);}
	else if(Status.equalsIgnoreCase("fail"))
	{ logger.log(LogStatus.FAIL,StepName, tbl);} else if(Status.equalsIgnoreCase("error"))
	{ logger.log(LogStatus.ERROR,StepName, tbl);} else if(Status.equalsIgnoreCase("info"))
	{ logger.log(LogStatus.INFO,StepName, tbl);} else {logger.log(LogStatus.INFO,StepName, tbl);} }
	
	public static void main(String[] args) throws Exception 
	{ 
		TestSuiteStart("D:\\Santosh\\TestReport.html","ejagruti");
	   TestCaseStart("this is test name 1", "this is description");
	   StepDetails("pass", "this is step1", "this is step details1", "");
	   StepDetails("fail", "this is step2", "this is step details2", ""); 
	  TestCaseEnd(); 
	  TestCaseStart("this is test name 2", "this is description");
	  StepDetails("pass", "this is step1", "this is step details1","");
	  StepDetails("fail", "this is step2", "this is step details2",""); 
	  TestCaseEnd(); 
	  TestSuiteEnd();

	}


}
