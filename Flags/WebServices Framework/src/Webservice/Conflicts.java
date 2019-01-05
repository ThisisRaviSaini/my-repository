package Webservice;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import backend.Backend;
import backend.DataObject;
import backend.Mailer;
import backend.PreValidations;
import backend.Resources;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Conflicts 
{
	static ExtentHtmlReporter htmlReporter;
	static ExtentReports extent;
	static ExtentTest logger;
	final String UserStoryName="Conflicts";
	static String dbUrl;
	static String username;
	static String password;
	static Connection con;
	static Logger logfile = Logger.getLogger(Conflicts.class);
	static String ReportName;
	String zipFileName;
	
	public static String startReport() throws IOException
	{
		 String Date = Backend.getCurrentDateTime();
		 htmlReporter = new ExtentHtmlReporter(Backend.getProperty("ExtentReportPath")+"/"+Date+"_WebServiceReportForAllConflicts.html");
		 extent = new ExtentReports ();
		 extent.attachReporter(htmlReporter);
    	 extent.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
		 extent.setSystemInfo("Environment", Backend.getProperty("Test_Level"));
		 extent.setSystemInfo("User Name", System.getProperty("user.name"));
	     htmlReporter.config().setDocumentTitle(Backend.getProperty("Doc_Name3"));
	     htmlReporter.config().setReportName(Backend.getProperty("Report_Name3"));
	     htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		 htmlReporter.config().setChartVisibilityOnOpen(true);
	     htmlReporter.config().setTheme(Theme.DARK);
	     htmlReporter.config().setEncoding("utf-8");
	     htmlReporter.config().setCSS("css-string");
		 htmlReporter.config().setJS("js-string");
		 ReportName = Backend.getProperty("ExtentReportPath")+"/"+Date+"_WebServiceReportForAllConflicts.html";
		 return ReportName;
	 }
	
	public static void endReport()
	{
		 extent.flush();
    }
	 
	public static void skipTest(String testCaseName)
	{
		logfile.info("Skipped Test Case No. " + testCaseName);
		logfile.info("------------------------------------------------------------------");
		throw new SkipException("Skipped Test Case No. " + testCaseName);
	}
	
	@BeforeTest
	public void beforeTest() throws Exception 
	{
		//connectDatabase();
		startReport();
		
		
	}

	@AfterTest
	public void afterTest() throws IOException 
	{
		//closeDatabase();
		endReport();
		Mailer.takeReportScreenshot(ReportName);
		zipFileName= Mailer.zipFile(ReportName);
		Mailer.sendEmail(zipFileName,UserStoryName);
	}
	
	@BeforeSuite
	public void beforeSuite() throws IOException 
	{
		RestAssured.baseURI = "http://product-test.app.wtcdev2.paas.fedex.com";
		//RestAssured.baseURI =Backend.getProperty("Env");
		DOMConfigurator.configure("log4j.xml");
	}

	public static void connectDatabase() 
	{
		try 
		{
			dbUrl=Backend.getProperty("dbUrl");
			username=Backend.getProperty("dbusername");
			password=Backend.getProperty("dbpassword");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con= DriverManager.getConnection(dbUrl, username, password);
			System.out.println("Database Connected Successfull");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}

	public static void closeDatabase()
	{
		try
		{
			con.close();
			System.out.println("Database Connection Closed");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	@Test(priority = 01)
	public void TC_01() throws Exception  
	{
	   ArrayList<String> StatusFlg= new ArrayList<String>();
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_01";
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	 
	    	  try 
	    	  {
	    	  ArrayList<String> TestCaseStatus= new ArrayList<String>();
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	    	  connectDatabase();
	          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBSO())
			            .then()
			            .extract().response();
	          String responsestr=res.asString();  
			  JsonPath js = new JsonPath(responsestr);
			  int offeringcount =  js.get("offerings.size()");
			  for(int i=0;i<offeringcount;i++)
			  {
				  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
				  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
				  if(offeringsInConflictCount>0)
				  {
					  logger.info("Validating Conflicts value for offering id " +WS_OfferingID);
					  for(int j=0;j<offeringsInConflictCount;j++)
					  {
					  String WS_ConflictType=js.getString("offerings["+i+"].offeringsInConflict["+j+"].offeringType");
					  String WS_ConflictOfferingId=js.getString("offerings["+i+"].offeringsInConflict["+j+"].offeringId");
					  if(WS_ConflictType.equals("Base Packaging Type"))
					  {
						  Statement stmt = con.createStatement();
						  String Query = "select distinct  omc.cnft_1_offering_id as BSO_PRODUCT_ID, omc.cnft_2_offering_id as  BPT_PRODUCT_ID from offering_mktg_conflict omc "
								 + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = omc.cnft_1_offering_id  AND (trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'  and o1.offering_type_cd = 'BSO'"
								 + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID= omc.cnft_2_offering_id AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC' and o2.offering_type_cd = 'BPT'"
								 + " where omc.cnft_type_cd='55' AND omc.lfcl_status_cd = 'AC'"
								 + " AND omc.cnft_1_offering_id = '"+WS_OfferingID+"' and  omc.cnft_2_offering_id ='"+WS_ConflictOfferingId+"'";
						  ResultSet rs1 = stmt.executeQuery(Query); 
						  if(!rs1.next())
							 {
								 
								 StatusFlg.add("N");
								logger.log(Status.INFO, MarkupHelper.createLabel("Database and WebServices Values mismatch for offeringid: " +WS_ConflictOfferingId, ExtentColor.BLUE));
							 }
						  else
						     {
							     StatusFlg.add("Y");
								 String Db_ConflictOfferingId=rs1.getString(2);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
						     }
						  rs1.close();
					  }
					  else if(WS_ConflictType.equals("Base Service Option"))
					  {
						  Statement stmt = con.createStatement();
						  String Query = "SELECT DISTINCT  O1.ENTERPRISE_PRODUCT_ID AS O1_ENTERPRISE_PRODUCT_ID, O2.ENTERPRISE_PRODUCT_ID AS O2_ENTERPRISE_PRODUCT_ID  FROM SVC_SVC_OPTION_CONFLICT SSOC"  
								  + " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD"  
								  + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = OM1.OFFERING_ID AND ( trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'"  
								  + " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_2_CD"  
								  + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID=OM2.OFFERING_ID AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC'"  
								  + " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '50' AND O2.OFFERING_TYPE_CD = 'BSO'"   
								  + " AND O1.OFFERING_TYPE_CD = 'BSO'  AND (trunc(sysdate) BETWEEN SSOC.EFFECTIVE_DT AND SSOC.EXPIRATION_DT) AND SSOC.COUNTRY_CD='ZZ'" 
								  + " AND O1.ENTERPRISE_PRODUCT_ID = '"+WS_OfferingID+"' and  O2.ENTERPRISE_PRODUCT_ID ='"+WS_ConflictOfferingId+"'";
						  ResultSet rs1 = stmt.executeQuery(Query); 
						  if(!rs1.next())
							 {
								 
							  	Statement stmt1 = con.createStatement();
							  	String Query1 = "SELECT DISTINCT  O1.ENTERPRISE_PRODUCT_ID AS O1_ENTERPRISE_PRODUCT_ID, O2.ENTERPRISE_PRODUCT_ID AS O2_ENTERPRISE_PRODUCT_ID  FROM SVC_SVC_OPTION_CONFLICT SSOC"  
									  + " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD"  
									  + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = OM1.OFFERING_ID AND ( trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'"  
									  + " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_2_CD"  
									  + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID=OM2.OFFERING_ID AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC'"  
									  + " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '50' AND O2.OFFERING_TYPE_CD = 'BSO'"   
									  + " AND O1.OFFERING_TYPE_CD = 'BSO'  AND (trunc(sysdate) BETWEEN SSOC.EFFECTIVE_DT AND SSOC.EXPIRATION_DT) AND SSOC.COUNTRY_CD='ZZ'" 
									  + " AND O1.ENTERPRISE_PRODUCT_ID = '"+WS_ConflictOfferingId+"' and  O2.ENTERPRISE_PRODUCT_ID ='"+WS_OfferingID+"'";
							  	ResultSet rs = stmt1.executeQuery(Query1);
							  	if(!rs.next())
							  	{
								  StatusFlg.add("N");
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database and WebServices Values mismatch for offeringid: " +WS_ConflictOfferingId, ExtentColor.BLUE));  
							  	}
							  	else
							  	{
							  		StatusFlg.add("Y");
									String Db_ConflictOfferingId=rs.getString(1);
									logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
							  	}
							  	rs.close();
							  	stmt1.close();
							 }
						  else
						     {
							     StatusFlg.add("Y");
								 String Db_ConflictOfferingId=rs1.getString(2);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
						     }
						  rs1.close();
					  }
					  else
					  {
						  Statement stmt = con.createStatement();
						  String Query = "SELECT DISTINCT O1.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_1, O2.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_2"  
								  + " FROM SVC_SVC_OPTION_CONFLICT SSOC" 
								  + " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_TYPE_CD" 
								  + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID = OM1.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O1.EFFECTIVE_DT) AND TRUNC(O1.EXPIRATION_DT) AND O1.LFCL_STATUS_CD='AC'" 
								  + " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
								  + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID = OM2.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O2.EFFECTIVE_DT) AND TRUNC(O2.EXPIRATION_DT) AND O2.LFCL_STATUS_CD='AC'" 
								  + " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '60' AND O1.OFFERING_TYPE_CD = 'BS' AND O2.OFFERING_TYPE_CD = 'BSO'" 
								  + " AND (TRUNC(SYSDATE) BETWEEN TRUNC(SSOC.EFFECTIVE_DT) AND TRUNC(SSOC.EXPIRATION_DT)) AND SSOC.COUNTRY_CD='ZZ'"
								  + " AND ((O1.DOM_INTL_CD = SSOC.DOM_INTL_CD) OR O1.DOM_INTL_CD = 'B')"
								  + " AND O1.ENTERPRISE_PRODUCT_ID = '"+WS_ConflictOfferingId+"' and  O2.ENTERPRISE_PRODUCT_ID ='"+WS_OfferingID+"'";
						  ResultSet rs1 = stmt.executeQuery(Query); 
						  if(!rs1.next())
							 {
								 
							     StatusFlg.add("N");
								logger.log(Status.INFO, MarkupHelper.createLabel("Database and WebServices Values mismatch for offeringid: " +WS_ConflictOfferingId, ExtentColor.BLUE));
							 }
						  else
						     {
							     StatusFlg.add("Y");
								 String Db_ConflictOfferingId=rs1.getString(1);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
						     }
						  rs1.close();
					  }
					  }
				  }
				  else
				  {
					  StatusFlg.add("Y");
					  logger.log(Status.INFO, MarkupHelper.createLabel("Conflict values doesnot exist for OfferingId: " +WS_OfferingID, ExtentColor.BLUE));
				  }
				  if(StatusFlg.contains("N"))
				  {
					  String StatusMsg = failTestCaseDesc + WS_OfferingID;
					  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
					  TestCaseStatus.add("N");
			      }
				  else
				  {
					  String StatusMsg = passTestCaseDesc + WS_OfferingID;
					  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
					  TestCaseStatus.add("Y");
				  }
				  StatusFlg.clear();
			  }
			  if(TestCaseStatus.contains("N"))
			  {
				  System.out.println("Failed: " +testCaseName);     
	        	  Assert.fail();
		      }
			  else
			  {
				  System.out.println("Passed Succesfully: " +testCaseName);
			  }
	       }
	    	  catch (Exception e)
	    	  {
	    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
	    	    Backend.displayException(e);
	    	  }
	    	  finally 
	 		 {
	 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
	 		 	closeDatabase();
	 		 	logfile.info("------------------------------------------------------------------");
	 		 }
	   }
	       
	       else
	       {
	    	  skipTest(testCaseName);
	       }
	  
	}
	
	@Test(priority = 02)
	public void TC_02() throws Exception
	{
	   ArrayList<String> StatusFlg= new ArrayList<String>();
	   ArrayList<String> TestCaseStatus= new ArrayList<String>();
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_02";
	   boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	   if (isRunnableTest)
	       {
		   try 
		   {
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	    	  connectDatabase();
	          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBS())
			            .then()
			            .extract().response();
	          String responsestr=res.asString();  
			  JsonPath js = new JsonPath(responsestr);
			  int offeringcount =  js.get("offerings.size()");
			  for(int i=0;i<offeringcount;i++)
			  {
				  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
				  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
				  if(offeringsInConflictCount>0)
				  {
					  logger.info("Validating Conflicts value for offering id " +WS_OfferingID);
					  for(int j=0;j<offeringsInConflictCount;j++)
					  {
					  String WS_ConflictType=js.getString("offerings["+i+"].offeringsInConflict["+j+"].offeringType");
					  String WS_ConflictOfferingId=js.getString("offerings["+i+"].offeringsInConflict["+j+"].offeringId");
					  if(WS_ConflictType.equals("Base Packaging Type"))
					  {
						  Statement stmt = con.createStatement();
						  String Query = "select distinct omc.cnft_type_cd, omc.cnft_1_offering_id as BS_PRODUCT_ID, omc.cnft_2_offering_id as  BPT_PRODUCT_ID from offering_mktg_conflict omc"
								  + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = omc.cnft_1_offering_id  AND (trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'  and o1.offering_type_cd = 'BS'"
								  + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID= omc.cnft_2_offering_id AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC' and o2.offering_type_cd = 'BPT'"
								  + " where omc.cnft_type_cd='65' and omc.lfcl_status_cd = 'AC' "
								  + " AND omc.cnft_1_offering_id = '"+WS_OfferingID+"' and  omc.cnft_2_offering_id ='"+WS_ConflictOfferingId+"'";
						  ResultSet rs1 = stmt.executeQuery(Query); 
						  if(!rs1.next())
							 {
								 
								 StatusFlg.add("N");
								logger.log(Status.INFO, MarkupHelper.createLabel("Database and WebServices Values mismatch for offeringid: " +WS_ConflictOfferingId, ExtentColor.BLUE));
							 }
						  else
						     {
								 StatusFlg.add("Y");
								 String Db_ConflictOfferingId=rs1.getString(2);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
						     }
						  rs1.close();
					  }
					  else
					  {
						  Statement stmt = con.createStatement();
						  String Query = "SELECT DISTINCT O1.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_1, O2.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_2"  
								  + " FROM SVC_SVC_OPTION_CONFLICT SSOC" 
								  + " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_TYPE_CD" 
								  + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID = OM1.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O1.EFFECTIVE_DT) AND TRUNC(O1.EXPIRATION_DT) AND O1.LFCL_STATUS_CD='AC'" 
								  + " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
								  + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID = OM2.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O2.EFFECTIVE_DT) AND TRUNC(O2.EXPIRATION_DT) AND O2.LFCL_STATUS_CD='AC'" 
								  + " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '60' AND O1.OFFERING_TYPE_CD = 'BS' AND O2.OFFERING_TYPE_CD = 'BSO'" 
								  + " AND (TRUNC(SYSDATE) BETWEEN TRUNC(SSOC.EFFECTIVE_DT) AND TRUNC(SSOC.EXPIRATION_DT)) AND SSOC.COUNTRY_CD='ZZ'"
								  + " AND ((O1.DOM_INTL_CD = SSOC.DOM_INTL_CD) OR O1.DOM_INTL_CD = 'B')"
								  + " AND O2.ENTERPRISE_PRODUCT_ID = '"+WS_ConflictOfferingId+"' and  O1.ENTERPRISE_PRODUCT_ID ='"+WS_OfferingID+"'";
						  ResultSet rs1 = stmt.executeQuery(Query); 
						  if(!rs1.next())
							 {
								 
								 StatusFlg.add("N");
								logger.log(Status.INFO, MarkupHelper.createLabel("Database and WebServices Values mismatch for offeringid: " +WS_ConflictOfferingId, ExtentColor.BLUE));
							 }
						  else
						     {
								 StatusFlg.add("Y");
								 String Db_ConflictOfferingId=rs1.getString(2);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
						     }
						  rs1.close();
					  }
					  }
				  }
				  else
				  {
					  StatusFlg.add("Y");
					  logger.log(Status.INFO, MarkupHelper.createLabel("Conflict values doesnot exist for OfferingId: " +WS_OfferingID, ExtentColor.BLUE));
				  }
				  if(StatusFlg.contains("N"))
				  {
					  String StatusMsg = failTestCaseDesc + WS_OfferingID;
					  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
					  TestCaseStatus.add("N");
			      }
				  else
				  {
					  String StatusMsg = passTestCaseDesc + WS_OfferingID;
					  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
					  TestCaseStatus.add("Y");
				  }
				  StatusFlg.clear();
			  }
			  if(TestCaseStatus.contains("N"))
			  {
				  System.out.println("Failed: " +testCaseName);     
	        	  Assert.fail();
		      }
			  else
			  {
				  System.out.println("Passed Succesfully: " +testCaseName);
			  }
	       }
		   catch (Exception e)
			  {
			    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
			    Backend.displayException(e);
			  }
		   finally 
			 {
			 	logfile.info("Execution is completed for Test Case No." + testCaseName);
			 	closeDatabase();
			 	logfile.info("------------------------------------------------------------------");
			 }
	   }
	   else
       {
    	  skipTest(testCaseName);
       }
	   
	  
}
	
	@Test(priority = 03)
	public void TC_03() throws Exception
	{
	   ArrayList<String> StatusFlg= new ArrayList<String>();
	   ArrayList<String> TestCaseStatus= new ArrayList<String>();
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_03";
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	  
	    	  try 
	    	  {
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
		       connectDatabase();
	          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBPT())
			            .then()
			            .extract().response();
	          String responsestr=res.asString();  
			  JsonPath js = new JsonPath(responsestr);
			  int offeringcount =  js.get("offerings.size()");
			  for(int i=0;i<offeringcount;i++)
			  {
				  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
				  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
				  if(offeringsInConflictCount>0)
				  {
					  logger.info("Validating Conflicts value for offering id " +WS_OfferingID);
					  for(int j=0;j<offeringsInConflictCount;j++)
					  {
					  String WS_ConflictType=js.getString("offerings["+i+"].offeringsInConflict["+j+"].offeringType");
					  String WS_ConflictOfferingId=js.getString("offerings["+i+"].offeringsInConflict["+j+"].offeringId");
					  if(WS_ConflictType.equals("Base Service Option"))
					  {
						  Statement stmt = con.createStatement();
						  String Query = "select distinct  omc.cnft_1_offering_id as BSO_PRODUCT_ID, omc.cnft_2_offering_id as  BPT_PRODUCT_ID from offering_mktg_conflict omc "
								 + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = omc.cnft_1_offering_id  AND (trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'  and o1.offering_type_cd = 'BSO'"
								 + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID= omc.cnft_2_offering_id AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC' and o2.offering_type_cd = 'BPT'"
								 + " where omc.cnft_type_cd='55' AND omc.lfcl_status_cd = 'AC'"
								 + " AND omc.cnft_2_offering_id = '"+WS_OfferingID+"' and  omc.cnft_1_offering_id ='"+WS_ConflictOfferingId+"'";
						  ResultSet rs1 = stmt.executeQuery(Query); 
						  if(!rs1.next())
							 {
								 
								 StatusFlg.add("N");
								logger.log(Status.INFO, MarkupHelper.createLabel("Database and WebServices Values mismatch for offeringid: " +WS_ConflictOfferingId, ExtentColor.BLUE));
							 }
						  else
						     {
								 StatusFlg.add("Y");
								 String Db_ConflictOfferingId=rs1.getString(1);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
						     }
						  rs1.close();
					  }
					  else
					  {
						  Statement stmt = con.createStatement();
						  String Query = "SELECT DISTINCT OMC.CNFT_TYPE_CD AS CONFLICT_TYPE_CD,"
								  + " OMC.CNFT_1_OFFERING_ID AS CONFLICT_ID_1, OMC.CNFT_2_OFFERING_ID AS CONFLICT_ID_2"
								  + " FROM OFFERING_MKTG_CONFLICT OMC"
								  + " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = OMC.CNFT_1_OFFERING_ID"
								  + " AND TRUNC(SYSDATE) BETWEEN TRUNC(O1.EFFECTIVE_DT) AND TRUNC(O1.EXPIRATION_DT)"
								  + " AND O1.LFCL_STATUS_CD='AC' AND O1.OFFERING_TYPE_CD = 'BS'"
								  + " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID = OMC.CNFT_2_OFFERING_ID"
								  + " AND TRUNC(SYSDATE) BETWEEN TRUNC(O2.EFFECTIVE_DT) AND TRUNC(O2.EXPIRATION_DT)"
								  + " AND O2.LFCL_STATUS_CD='AC' AND O2.OFFERING_TYPE_CD = 'BPT'"
								  + " WHERE OMC.CNFT_TYPE_CD  = '65' AND OMC.LFCL_STATUS_CD='AC'"
								  + " AND OMC.CNFT_2_OFFERING_ID = '"+WS_OfferingID+"' and  OMC.CNFT_1_OFFERING_ID ='"+WS_ConflictOfferingId+"'";
						  ResultSet rs1 = stmt.executeQuery(Query); 
						  if(!rs1.next())
							 {
								 
								 StatusFlg.add("N");
								logger.log(Status.INFO, MarkupHelper.createLabel("Database and WebServices Values mismatch for offeringid: " +WS_ConflictOfferingId, ExtentColor.BLUE));
							 }
						  else
						     {
								 StatusFlg.add("Y");
								 String Db_ConflictOfferingId=rs1.getString(1);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Database value of conflict:" + Db_ConflictOfferingId + "WebServices Values of Conflict: " +WS_ConflictOfferingId, ExtentColor.BLUE));
						     }
						  rs1.close();
					  }
					  }
				  }
				  else
				  {
					  StatusFlg.add("Y");
					  logger.log(Status.INFO, MarkupHelper.createLabel("Conflict values doesnot exist for OfferingId: " +WS_OfferingID, ExtentColor.BLUE));
				  }
				  if(StatusFlg.contains("N"))
				  {
					  String StatusMsg = failTestCaseDesc + WS_OfferingID;
					  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
					  TestCaseStatus.add("N");
			      }
				  else
				  {
					  String StatusMsg = passTestCaseDesc + WS_OfferingID;
					  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
					  TestCaseStatus.add("Y");
				  }
				  StatusFlg.clear();
			  }
			  if(TestCaseStatus.contains("N"))
			  {
				  System.out.println("Failed: " +testCaseName);     
	        	  Assert.fail();
		      }
			  else
			  {
				  System.out.println("Passed Succesfully: " +testCaseName);
			  }
	       }
	    	  catch (Exception e)
	    	  {
	    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
	    	    Backend.displayException(e);
	    	  }
	    	  finally 
	 		 {
	 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
	 		 	closeDatabase();
	 		 	logfile.info("------------------------------------------------------------------");
	 		 }
	   }
	       else
	       {
	    	   skipTest(testCaseName);
	       }
	  
}
	
	@Test(priority = 04)
	public void TC_04() throws Exception
	{
	   ArrayList<String> StatusFlg= new ArrayList<String>();
	   ArrayList<String> TestCaseStatus= new ArrayList<String>();
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_04";
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	  
	    	  try 
	    	  {
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBSO())
			            .then()
			            .extract().response();
	          String responsestr=res.asString();  
			  JsonPath js = new JsonPath(responsestr);
			  int offeringcount =  js.get("offerings.size()");
			  for(int i=0;i<offeringcount;i++)
			  {
				  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
				  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
				  if(offeringsInConflictCount>0)
				  {
					    ArrayList<String> WS_Conflict = new ArrayList<String>();
				        for(int r=0;r<offeringsInConflictCount;r++)
				        {
				        	WS_Conflict.add(js.getString("offerings["+i+"].offeringsInConflict["+r+"].offeringId"));
				        } 
				        Set<String> store = new HashSet<>();
				        for (String name : WS_Conflict) 
				        {
				            if (store.add(name) == false)
				            {
				            	StatusFlg.add("N");
				            	System.out.println("Duplicate Conflict ID:" +name + "for Offering ID:" +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Duplicate Conflict ID:" +name + "for Offering ID:" +WS_OfferingID , ExtentColor.ORANGE));
				            }
				        }
				       
				  }
				  else
				  {
					  StatusFlg.add("Y");
					  logger.log(Status.INFO, MarkupHelper.createLabel("No Conflict Values Exist for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
				  }
				  
				  if(StatusFlg.contains("N"))
				  {
					  String StatusMsg = failTestCaseDesc + WS_OfferingID;
					  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
					  TestCaseStatus.add("N");
			      }
				  else
				  {
					  String StatusMsg = passTestCaseDesc + WS_OfferingID;
					  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
					  TestCaseStatus.add("Y");
				  }
				  StatusFlg.clear();
			  }
			  
			  if(TestCaseStatus.contains("N"))
			  {
				  System.out.println("Failed: " +testCaseName);     
	        	  Assert.fail();
		      }
			  else
			  {
				  System.out.println("Passed Succesfully: " +testCaseName);
			  }
	       }
	    	  catch (Exception e)
	    	  {
	    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
	    	    Backend.displayException(e);
	    	  }
	    	  finally 
	 		 {
	 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
	 		 	logfile.info("------------------------------------------------------------------");
	 		 }
	   }
else
{
	 skipTest(testCaseName);
}
	  
}

		@Test(priority = 05)
	public void TC_05() throws Exception
	{
	   ArrayList<String> StatusFlg= new ArrayList<String>();
	   ArrayList<String> TestCaseStatus= new ArrayList<String>();
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_05";
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	  
	    	  try 
	    	  {
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBS())
			            .then()
			            .extract().response();
	          String responsestr=res.asString();  
			  JsonPath js = new JsonPath(responsestr);
			  int offeringcount =  js.get("offerings.size()");
			  for(int i=0;i<offeringcount;i++)
			  {
				  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
				  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
				  if(offeringsInConflictCount>0)
				  {
					    ArrayList<String> WS_Conflict = new ArrayList<String>();
				        for(int r=0;r<offeringsInConflictCount;r++)
				        {
				        	WS_Conflict.add(js.getString("offerings["+i+"].offeringsInConflict["+r+"].offeringId"));
				        } 
				        Set<String> store = new HashSet<>();
				        for (String name : WS_Conflict) 
				        {
				            if (store.add(name) == false)
				            {
				            	StatusFlg.add("N");
				            	System.out.println("Duplicate Conflict ID:" +name + "for Offering ID:" +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Duplicate Conflict ID:" +name + "for Offering ID:" +WS_OfferingID , ExtentColor.ORANGE));
				            }
				        }
				       
				  }
				  else
				  {
					  StatusFlg.add("Y");
					  logger.log(Status.INFO, MarkupHelper.createLabel("No Conflict Values Exist for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
				  }
				  
				  if(StatusFlg.contains("N"))
				  {
					  String StatusMsg = failTestCaseDesc + WS_OfferingID;
					  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
					  TestCaseStatus.add("N");
			      }
				  else
				  {
					  String StatusMsg = passTestCaseDesc + WS_OfferingID;
					  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
					  TestCaseStatus.add("Y");
				  }
				  StatusFlg.clear();
			  }
			  
			  if(TestCaseStatus.contains("N"))
			  {
				  System.out.println("Failed: " +testCaseName);     
	        	  Assert.fail();
		      }
			  else
			  {
				  System.out.println("Passed Succesfully: " +testCaseName);
			  }
	       }
	    	  catch (Exception e)
	    	  {
	    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
	    	    Backend.displayException(e);
	    	  }
	    	  finally 
	 		 {
	 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
	 		 	logfile.info("------------------------------------------------------------------");
	 		 }
	   }
else
{
	 skipTest(testCaseName);
}
	  
}	
	
	@Test(priority = 06)
	public void TC_06() throws Exception
	{
	   ArrayList<String> StatusFlg= new ArrayList<String>();
	   ArrayList<String> TestCaseStatus= new ArrayList<String>();
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_06";
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	  
	    	  try 
	    	  {
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBPT())
			            .then()
			            .extract().response();
	          String responsestr=res.asString();  
			  JsonPath js = new JsonPath(responsestr);
			  int offeringcount =  js.get("offerings.size()");
			  for(int i=0;i<offeringcount;i++)
			  {
				  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
				  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
				  if(offeringsInConflictCount>0)
				  {
					    ArrayList<String> WS_Conflict = new ArrayList<String>();
				        for(int r=0;r<offeringsInConflictCount;r++)
				        {
				        	WS_Conflict.add(js.getString("offerings["+i+"].offeringsInConflict["+r+"].offeringId"));
				        } 
				        Set<String> store = new HashSet<>();
				        for (String name : WS_Conflict) 
				        {
				            if (store.add(name) == false)
				            {
				            	StatusFlg.add("N");
				            	System.out.println("Duplicate Conflict ID:" +name + "for Offering ID:" +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Duplicate Conflict ID:" +name + "for Offering ID:" +WS_OfferingID , ExtentColor.ORANGE));
				            }
				        }
				       
				  }
				  else
				  {
					  StatusFlg.add("Y");
					  logger.log(Status.INFO, MarkupHelper.createLabel("No Conflict Values Exist for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
				  }
				  
				  if(StatusFlg.contains("N"))
				  {
					  String StatusMsg = failTestCaseDesc + WS_OfferingID;
					  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
					  TestCaseStatus.add("N");
			      }
				  else
				  {
					  String StatusMsg = passTestCaseDesc + WS_OfferingID;
					  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
					  TestCaseStatus.add("Y");
				  }
				  StatusFlg.clear();
			  }
			  
			  if(TestCaseStatus.contains("N"))
			  {
				  System.out.println("Failed: " +testCaseName);     
	        	  Assert.fail();
		      }
			  else
			  {
				  System.out.println("Passed Succesfully: " +testCaseName);
			  }
	       }
	    	  catch (Exception e)
	    	  {
	    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
	    	    Backend.displayException(e);
	    	  }
	    	  finally 
	 		 {
	 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
	 		 	logfile.info("------------------------------------------------------------------");
	 		 }
	   }
else
{
	 skipTest(testCaseName);
}
	  
}	
	
	@Test(priority = 07)
	public  void TC_07() throws Exception    
	{
		   ArrayList<String> StatusFlg= new ArrayList<String>();
		   ArrayList<String> TestCaseStatus= new ArrayList<String>();
		   String testCaseDesc = null;
		   String passTestCaseDesc = null;
		   String failTestCaseDesc = null;
		   String testCaseName = "TC_07";
		   boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
		   if (isRunnableTest)
		   {
		     try 
		     {
		    	  logfile.info("Executing Test Case: " +testCaseName);
		    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
		          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
		          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
		          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
		          connectDatabase();
		          Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .when()
		            .get(Resources.ResourcedataBSO())
		            .then()
		            .extract().response();
          String responsestr=res.asString();  
		  JsonPath js = new JsonPath(responsestr);
		  int offeringcount =  js.get("offerings.size()");
		  for(int i=0;i<offeringcount;i++)
		  {
			  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
			  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
			  if(offeringsInConflictCount>0)
			  {	
		        String Query="select * from(SELECT DISTINCT SSOC.SVC_CONFLICT_TYPE_CD, O1.ENTERPRISE_PRODUCT_ID AS O1_ENTERPRISE_PRODUCT_ID, O2.ENTERPRISE_PRODUCT_ID AS O2_ENTERPRISE_PRODUCT_ID  FROM SVC_SVC_OPTION_CONFLICT SSOC"  
				+ " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
				+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = OM1.OFFERING_ID AND ( trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'"  
				+ " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_2_CD"  
				+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID=OM2.OFFERING_ID AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC'"  
				+ " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '50' AND O2.OFFERING_TYPE_CD = 'BSO'"   
				+ " AND O1.OFFERING_TYPE_CD = 'BSO'  AND (trunc(sysdate) BETWEEN SSOC.EFFECTIVE_DT AND SSOC.EXPIRATION_DT) AND SSOC.COUNTRY_CD='ZZ'"  
				+ " UNION "
				+ " select distinct omc.cnft_type_cd, omc.cnft_1_offering_id as BSO_PRODUCT_ID, omc.cnft_2_offering_id as  BPT_PRODUCT_ID from offering_mktg_conflict omc" 
				+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = omc.cnft_1_offering_id  AND (trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'  and o1.offering_type_cd = 'BSO'"
				+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID= omc.cnft_2_offering_id AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC' and o2.offering_type_cd = 'BPT'"
				+ " where omc.cnft_type_cd='55' AND omc.lfcl_status_cd = 'AC'"
				+ " UNION"  
				+ " SELECT DISTINCT SSOC.SVC_CONFLICT_TYPE_CD AS CONFLICT_TYPE_CD," 
				+ " O1.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_1, O2.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_2"  
				+ " FROM SVC_SVC_OPTION_CONFLICT SSOC" 
				+ " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_TYPE_CD" 
				+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID = OM1.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O1.EFFECTIVE_DT) AND TRUNC(O1.EXPIRATION_DT) AND O1.LFCL_STATUS_CD='AC'" 
				+ " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
				+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID = OM2.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O2.EFFECTIVE_DT) AND TRUNC(O2.EXPIRATION_DT) AND O2.LFCL_STATUS_CD='AC'" 
				+ " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '60' AND O1.OFFERING_TYPE_CD = 'BS' AND O2.OFFERING_TYPE_CD = 'BSO' "
				+ " AND (TRUNC(SYSDATE) BETWEEN TRUNC(SSOC.EFFECTIVE_DT) AND TRUNC(SSOC.EXPIRATION_DT)) AND SSOC.COUNTRY_CD='ZZ'"
				+ " AND ((O1.DOM_INTL_CD = SSOC.DOM_INTL_CD) OR O1.DOM_INTL_CD = 'B')"
				+ " ORDER BY 1,2,3)"
				+ "where (o1_ENTERPRISE_PRODUCT_ID = '"+WS_OfferingID+"' or o2_ENTERPRISE_PRODUCT_ID = '"+WS_OfferingID+"')";
	         
		        Statement stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery(Query);
		        ArrayList<String> Db_Conflict1 = new ArrayList<String>();
		        while(rs.next())
		        {
		        	Db_Conflict1.add(rs.getString(2));
		        }
		        stmt.close();
		        rs.close();
		        ArrayList<String> WS_Conflict = new ArrayList<String>();
		        for(int r=0;r<offeringsInConflictCount;r++)
		        {
		        	WS_Conflict.add(js.getString("offerings["+i+"].offeringsInConflict["+r+"].offeringId"));
		        }
			  
		        if(WS_Conflict.size()==Db_Conflict1.size())
		        {
		        	StatusFlg.add("Y");
					logger.log(Status.INFO, MarkupHelper.createLabel("Db and WebService Count are matching for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
		        	//logfile.info("No Duplicate Values present for Offering ID:" +WS_OfferingID);
		        }
		        else
		        {
		        	StatusFlg.add("N");
					logger.log(Status.INFO, MarkupHelper.createLabel("Db and WebService Count are not matching for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
		        	//logfile.info("No Duplicate Values present for Offering ID:" +WS_OfferingID);
		        }
			  }
			  
			  else
			  {   
				  StatusFlg.add("Y");
				  logger.log(Status.INFO, MarkupHelper.createLabel("No Conflict Values Exist for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
				  //logfile.info("No Conflict Values Exist for Offering ID:" +WS_OfferingID);
			  }
	
			  if(StatusFlg.contains("N"))
			  {
				  String StatusMsg = failTestCaseDesc + WS_OfferingID;
				  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
				  TestCaseStatus.add("N");
		      }
			  else
			  {
				  String StatusMsg = passTestCaseDesc + WS_OfferingID;
				  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
				  TestCaseStatus.add("Y");
			  }
			  StatusFlg.clear();
		  }
		  
		  if(TestCaseStatus.contains("N"))
		  {
			  System.out.println("Failed: " +testCaseName);     
        	  Assert.fail();
	      }
		  else
		  {
			  System.out.println("Passed Succesfully: " +testCaseName);
		  }
       }
    	  catch (Exception e)
    	  {
    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
    	    Backend.displayException(e);
    	  }
    	  finally 
 		 {
 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
 		 	logfile.info("------------------------------------------------------------------");
 		 }
   }
else
{
 skipTest(testCaseName);
}
  
}	

		@Test(priority = 07)
	public  void TC_08() throws Exception    
	{
		   ArrayList<String> StatusFlg= new ArrayList<String>();
		   ArrayList<String> TestCaseStatus= new ArrayList<String>();
		   String testCaseDesc = null;
		   String passTestCaseDesc = null;
		   String failTestCaseDesc = null;
		   String testCaseName = "TC_08";
		   boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
		   if (isRunnableTest)
		   {
		     try 
		     {
		    	  logfile.info("Executing Test Case: " +testCaseName);
		    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
		          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
		          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
		          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
		          connectDatabase();
		          Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .when()
		            .get(Resources.ResourcedataBS())
		            .then()
		            .extract().response();
          String responsestr=res.asString();  
		  JsonPath js = new JsonPath(responsestr);
		  int offeringcount =  js.get("offerings.size()");
		  for(int i=0;i<offeringcount;i++)
		  {
			  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
			  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
			  if(offeringsInConflictCount>0)
			  {	
		        String Query="select * from(SELECT DISTINCT SSOC.SVC_CONFLICT_TYPE_CD, O1.ENTERPRISE_PRODUCT_ID AS O1_ENTERPRISE_PRODUCT_ID, O2.ENTERPRISE_PRODUCT_ID AS O2_ENTERPRISE_PRODUCT_ID  FROM SVC_SVC_OPTION_CONFLICT SSOC"  
				+ " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
				+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = OM1.OFFERING_ID AND ( trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'"  
				+ " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_2_CD"  
				+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID=OM2.OFFERING_ID AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC'"  
				+ " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '50' AND O2.OFFERING_TYPE_CD = 'BSO'"   
				+ " AND O1.OFFERING_TYPE_CD = 'BSO'  AND (trunc(sysdate) BETWEEN SSOC.EFFECTIVE_DT AND SSOC.EXPIRATION_DT) AND SSOC.COUNTRY_CD='ZZ'"  
				+ " UNION "
				+ " select distinct omc.cnft_type_cd, omc.cnft_1_offering_id as BSO_PRODUCT_ID, omc.cnft_2_offering_id as  BPT_PRODUCT_ID from offering_mktg_conflict omc" 
				+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = omc.cnft_1_offering_id  AND (trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'  and o1.offering_type_cd = 'BSO'"
				+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID= omc.cnft_2_offering_id AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC' and o2.offering_type_cd = 'BPT'"
				+ " where omc.cnft_type_cd='55' AND omc.lfcl_status_cd = 'AC'"
				+ " UNION"  
				+ " SELECT DISTINCT SSOC.SVC_CONFLICT_TYPE_CD AS CONFLICT_TYPE_CD," 
				+ " O1.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_1, O2.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_2"  
				+ " FROM SVC_SVC_OPTION_CONFLICT SSOC" 
				+ " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_TYPE_CD" 
				+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID = OM1.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O1.EFFECTIVE_DT) AND TRUNC(O1.EXPIRATION_DT) AND O1.LFCL_STATUS_CD='AC'" 
				+ " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
				+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID = OM2.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O2.EFFECTIVE_DT) AND TRUNC(O2.EXPIRATION_DT) AND O2.LFCL_STATUS_CD='AC'" 
				+ " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '60' AND O1.OFFERING_TYPE_CD = 'BS' AND O2.OFFERING_TYPE_CD = 'BSO' "
				+ " AND (TRUNC(SYSDATE) BETWEEN TRUNC(SSOC.EFFECTIVE_DT) AND TRUNC(SSOC.EXPIRATION_DT)) AND SSOC.COUNTRY_CD='ZZ'"
				+ " AND ((O1.DOM_INTL_CD = SSOC.DOM_INTL_CD) OR O1.DOM_INTL_CD = 'B')"
				+ " ORDER BY 1,2,3)"
				+ "where (o1_ENTERPRISE_PRODUCT_ID = '"+WS_OfferingID+"' or o2_ENTERPRISE_PRODUCT_ID = '"+WS_OfferingID+"')";
	         
		        Statement stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery(Query);
		        ArrayList<String> Db_Conflict1 = new ArrayList<String>();
		        while(rs.next())
		        {
		        	Db_Conflict1.add(rs.getString(2));
		        }
		        stmt.close();
		        rs.close();
		        ArrayList<String> WS_Conflict = new ArrayList<String>();
		        for(int r=0;r<offeringsInConflictCount;r++)
		        {
		        	WS_Conflict.add(js.getString("offerings["+i+"].offeringsInConflict["+r+"].offeringId"));
		        }
			  
		        if(WS_Conflict.size()==Db_Conflict1.size())
		        {
		        	StatusFlg.add("Y");
					logger.log(Status.INFO, MarkupHelper.createLabel("Db and WebService Count are matching for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
		        	//logfile.info("No Duplicate Values present for Offering ID:" +WS_OfferingID);
		        }
		        else
		        {
		        	StatusFlg.add("N");
					logger.log(Status.INFO, MarkupHelper.createLabel("Db and WebService Count are not matching for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
		        	//logfile.info("No Duplicate Values present for Offering ID:" +WS_OfferingID);
		        }
			  }
			  
			  else
			  {   
				  StatusFlg.add("Y");
				  logger.log(Status.INFO, MarkupHelper.createLabel("No Conflict Values Exist for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
				  //logfile.info("No Conflict Values Exist for Offering ID:" +WS_OfferingID);
			  }
	
			  if(StatusFlg.contains("N"))
			  {
				  String StatusMsg = failTestCaseDesc + WS_OfferingID;
				  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
				  TestCaseStatus.add("N");
		      }
			  else
			  {
				  String StatusMsg = passTestCaseDesc + WS_OfferingID;
				  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
				  TestCaseStatus.add("Y");
			  }
			  StatusFlg.clear();
		  }
		  
		  if(TestCaseStatus.contains("N"))
		  {
			  System.out.println("Failed: " +testCaseName);     
        	  Assert.fail();
	      }
		  else
		  {
			  System.out.println("Passed Succesfully: " +testCaseName);
		  }
       }
    	  catch (Exception e)
    	  {
    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
    	    Backend.displayException(e);
    	  }
    	  finally 
 		 {
 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
 		 	logfile.info("------------------------------------------------------------------");
 		 }
   }
else
{
 skipTest(testCaseName);
}
  
}	
	
	@Test(priority = 07)
	public  void TC_09() throws Exception    
		{
			   ArrayList<String> StatusFlg= new ArrayList<String>();
			   ArrayList<String> TestCaseStatus= new ArrayList<String>();
			   String testCaseDesc = null;
			   String passTestCaseDesc = null;
			   String failTestCaseDesc = null;
			   String testCaseName = "TC_09";
			   boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			   if (isRunnableTest)
			   {
			     try 
			     {
			    	  logfile.info("Executing Test Case: " +testCaseName);
			    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
			          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
			          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
			          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
			          connectDatabase();
			          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBPT())
			            .then()
			            .extract().response();
	          String responsestr=res.asString();  
			  JsonPath js = new JsonPath(responsestr);
			  int offeringcount =  js.get("offerings.size()");
			  for(int i=0;i<offeringcount;i++)
			  {
				  String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
				  int offeringsInConflictCount =  js.get("offerings["+i+"].offeringsInConflict.size()");
				  if(offeringsInConflictCount>0)
				  {	
			        String Query="select * from(SELECT DISTINCT SSOC.SVC_CONFLICT_TYPE_CD, O1.ENTERPRISE_PRODUCT_ID AS O1_ENTERPRISE_PRODUCT_ID, O2.ENTERPRISE_PRODUCT_ID AS O2_ENTERPRISE_PRODUCT_ID  FROM SVC_SVC_OPTION_CONFLICT SSOC"  
					+ " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
					+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = OM1.OFFERING_ID AND ( trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'"  
					+ " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_2_CD"  
					+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID=OM2.OFFERING_ID AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC'"  
					+ " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '50' AND O2.OFFERING_TYPE_CD = 'BSO'"   
					+ " AND O1.OFFERING_TYPE_CD = 'BSO'  AND (trunc(sysdate) BETWEEN SSOC.EFFECTIVE_DT AND SSOC.EXPIRATION_DT) AND SSOC.COUNTRY_CD='ZZ'"  
					+ " UNION "
					+ " select distinct omc.cnft_type_cd, omc.cnft_1_offering_id as BSO_PRODUCT_ID, omc.cnft_2_offering_id as  BPT_PRODUCT_ID from offering_mktg_conflict omc" 
					+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID  = omc.cnft_1_offering_id  AND (trunc(sysdate) BETWEEN O1.EFFECTIVE_DT AND O1.EXPIRATION_DT)  AND O1.LFCL_STATUS_CD='AC'  and o1.offering_type_cd = 'BSO'"
					+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID= omc.cnft_2_offering_id AND (trunc(sysdate) BETWEEN O2.EFFECTIVE_DT AND O2.EXPIRATION_DT)  AND O2.LFCL_STATUS_CD='AC' and o2.offering_type_cd = 'BPT'"
					+ " where omc.cnft_type_cd='55' AND omc.lfcl_status_cd = 'AC'"
					+ " UNION"  
					+ " SELECT DISTINCT SSOC.SVC_CONFLICT_TYPE_CD AS CONFLICT_TYPE_CD," 
					+ " O1.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_1, O2.ENTERPRISE_PRODUCT_ID AS CONFLICT_ID_2"  
					+ " FROM SVC_SVC_OPTION_CONFLICT SSOC" 
					+ " LEFT JOIN OFFERING_MAPPING OM1 ON OM1.MAP_TYPE_CD IN ('epic','o2e') AND OM1.ALTERNATE_DESC = SSOC.SVC_TYPE_CD" 
					+ " LEFT JOIN OFFERING O1 ON O1.ENTERPRISE_PRODUCT_ID = OM1.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O1.EFFECTIVE_DT) AND TRUNC(O1.EXPIRATION_DT) AND O1.LFCL_STATUS_CD='AC'" 
					+ " LEFT JOIN OFFERING_MAPPING OM2 ON OM2.MAP_TYPE_CD IN ('epic','o2e') AND OM2.ALTERNATE_DESC = SSOC.SVC_OPTION_1_CD" 
					+ " LEFT JOIN OFFERING O2 ON O2.ENTERPRISE_PRODUCT_ID = OM2.OFFERING_ID AND TRUNC(SYSDATE) BETWEEN TRUNC(O2.EFFECTIVE_DT) AND TRUNC(O2.EXPIRATION_DT) AND O2.LFCL_STATUS_CD='AC'" 
					+ " WHERE SSOC.SVC_CONFLICT_TYPE_CD = '60' AND O1.OFFERING_TYPE_CD = 'BS' AND O2.OFFERING_TYPE_CD = 'BSO' "
					+ " AND (TRUNC(SYSDATE) BETWEEN TRUNC(SSOC.EFFECTIVE_DT) AND TRUNC(SSOC.EXPIRATION_DT)) AND SSOC.COUNTRY_CD='ZZ'"
					+ " AND ((O1.DOM_INTL_CD = SSOC.DOM_INTL_CD) OR O1.DOM_INTL_CD = 'B')"
					+ " ORDER BY 1,2,3)"
					+ "where (o1_ENTERPRISE_PRODUCT_ID = '"+WS_OfferingID+"' or o2_ENTERPRISE_PRODUCT_ID = '"+WS_OfferingID+"')";
		         
			        Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery(Query);
			        ArrayList<String> Db_Conflict1 = new ArrayList<String>();
			        while(rs.next())
			        {
			        	Db_Conflict1.add(rs.getString(2));
			        }
			        stmt.close();
			        rs.close();
			        ArrayList<String> WS_Conflict = new ArrayList<String>();
			        for(int r=0;r<offeringsInConflictCount;r++)
			        {
			        	WS_Conflict.add(js.getString("offerings["+i+"].offeringsInConflict["+r+"].offeringId"));
			        }
				  
			        if(WS_Conflict.size()==Db_Conflict1.size())
			        {
			        	StatusFlg.add("Y");
						logger.log(Status.INFO, MarkupHelper.createLabel("Db and WebService Count are matching for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
			        	//logfile.info("No Duplicate Values present for Offering ID:" +WS_OfferingID);
			        }
			        else
			        {
			        	StatusFlg.add("N");
						logger.log(Status.INFO, MarkupHelper.createLabel("Db and WebService Count are not matching for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
			        	//logfile.info("No Duplicate Values present for Offering ID:" +WS_OfferingID);
			        }
				  }
				  
				  else
				  {   
					  StatusFlg.add("Y");
					  logger.log(Status.INFO, MarkupHelper.createLabel("No Conflict Values Exist for Offering ID:" +WS_OfferingID , ExtentColor.BLUE));
					  //logfile.info("No Conflict Values Exist for Offering ID:" +WS_OfferingID);
				  }
		
				  if(StatusFlg.contains("N"))
				  {
					  String StatusMsg = failTestCaseDesc + WS_OfferingID;
					  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
					  TestCaseStatus.add("N");
			      }
				  else
				  {
					  String StatusMsg = passTestCaseDesc + WS_OfferingID;
					  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
					  TestCaseStatus.add("Y");
				  }
				  StatusFlg.clear();
			  }
			  
			  if(TestCaseStatus.contains("N"))
			  {
				  System.out.println("Failed: " +testCaseName);     
	        	  Assert.fail();
		      }
			  else
			  {
				  System.out.println("Passed Succesfully: " +testCaseName);
			  }
	       }
	    	  catch (Exception e)
	    	  {
	    	    logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due to Exception", ExtentColor.RED));
	    	    Backend.displayException(e);
	    	  }
	    	  finally 
	 		 {
	 		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
	 		 	logfile.info("------------------------------------------------------------------");
	 		 }
	   }
	else
	{
	 skipTest(testCaseName);
	}
	  
	}	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
