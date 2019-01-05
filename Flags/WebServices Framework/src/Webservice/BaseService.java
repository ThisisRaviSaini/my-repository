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
import backend.Resources;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BaseService {
	
	static ExtentHtmlReporter htmlReporter;
	static ExtentReports extent;
	static ExtentTest logger;
	static String dbUrl;
	static String username;
	static String password;
	static Connection con;
	static Logger logfile = Logger.getLogger(BaseService.class);
	final String UserStoryName="BaseService";
	static String ReportName;
	String zipFileName;
	
	public static String startReport() throws IOException
	{
		 String Date = Backend.getCurrentDateTime();
		 htmlReporter = new ExtentHtmlReporter(Backend.getProperty("ExtentReportPath")+"/"+Date+"_WebServiceReportForBaseService.html");
		 extent = new ExtentReports ();
		 extent.attachReporter(htmlReporter);
    	 extent.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
		 extent.setSystemInfo("Environment", Backend.getProperty("Test_Level"));
		 extent.setSystemInfo("User Name", System.getProperty("user.name"));
	     htmlReporter.config().setDocumentTitle(Backend.getProperty("Doc_Name"));
	     htmlReporter.config().setReportName(Backend.getProperty("Report_Name"));
	     htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		 htmlReporter.config().setChartVisibilityOnOpen(true);
	     htmlReporter.config().setTheme(Theme.DARK);
	     htmlReporter.config().setEncoding("utf-8");
	     htmlReporter.config().setCSS("css-string");
		 htmlReporter.config().setJS("js-string");
		 ReportName = Backend.getProperty("ExtentReportPath")+"/"+Date+"_WebServiceReportForBaseService.html";
		 return ReportName;
	 }
	
	public static void endReport()
	{
		 extent.flush();
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
	
	public static void skipTest(String testCaseName)
	{
		logfile.info("Skipped Test Case No. " + testCaseName);
		logfile.info("------------------------------------------------------------------");
		throw new SkipException("Skipped Test Case No. " + testCaseName);
	}
	
	
	@BeforeTest
	public void beforeTest() throws Exception 
	{
		startReport();
		
	}

	@AfterTest
	public void afterTest() throws IOException 
	{
		endReport();
		Mailer.takeReportScreenshot(ReportName);
		zipFileName= Mailer.zipFile(ReportName);
		Mailer.sendEmail(zipFileName,UserStoryName);
	}
	
	@BeforeSuite
	public void beforeSuite() throws IOException
	{
		//RestAssured.baseURI =Backend.getProperty("Env");
		RestAssured.baseURI = "http://product-test.app.wtcdev2.paas.fedex.com";
		DOMConfigurator.configure("log4j.xml");
	}

	
	
	@Test(priority = 01)
	public void TC_01() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_01";
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
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .when()
		            .get(Resources.ResourcedataBS())
		            .then()
		            .extract().response();
			    String responsestr=res.asString();  
			    JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected resultCount:"+expectedResultCount+  "  Actual  WsCount:"+ WsCount +">", ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected resultCount:"+expectedResultCount+  "  Actual  WsCount:"+ WsCount +">", ExtentColor.BLUE));
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    	  catch (Exception e)
		       {
		    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
		    	   Backend.displayException(e);
		       }
	    }
	   else
	   {
	   	skipTest(testCaseName);
	   }
	}

	
	@Test(priority = 2)
	public void TC_02() throws Exception
	{
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
				 Response res = given()
				            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
				            .when()
				            .get(Resources.ResourcedataBS())
				            .then()
				            .extract().response();
				 String responsestr=res.asString();  
				 JsonPath js = new JsonPath(responsestr);
				 int offeringcount =  js.get("offerings.size()");
				 Boolean StatusFlg=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_OfferingID=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_OfferingID = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Data=js.getString("offerings["+i+"].offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("Base Service"))
					 {
						 WS_offeringType="BS"; // As response value for DB and WS are different
					 }
					 String WS_description=js.getString("offerings["+i+"].description");
					 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 String DB_operatingOrgCds=null;
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
					 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
					 stmt3.close();
					 rs3.close();
					if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
							WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
							WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
							WS_operatingOrgCds.contains(DB_operatingOrgCds))
					{
						String StatusMsg = passTestCaseDesc + WS_OfferingID;
						logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
						StatusFlg=true;
					}
					else
					{
						String StatusMsg = failTestCaseDesc + WS_OfferingID;
						logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
						StatusFlg=false;
					}
					
					logger.info("Database and WebServices Details: " +WS_OfferingID);
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
					logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
				 }
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 3)
	public void TC_03() throws Exception
	{
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
				 Response res = given()
				            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
				            .when()
				            .get(Resources.ResourcedataBS())
				            .then()
				            .extract().response();
				 String responsestr=res.asString();  
				 JsonPath js = new JsonPath(responsestr);
				 int offeringcount =  js.get("offerings.size()");
				 connectDatabase();
				 String Query="SELECT ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' AND LFCL_STATUS_CD='AC'";
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(Query);
				 ArrayList<String> dbOfferingID=new ArrayList<>();
				 while (rs.next()) 
				 {
					 dbOfferingID.add(rs.getString(1)); 
				 }
				 Boolean StatusFlg=true;
				 stmt.close();
				 rs.close();
				 for(int i=0;i<offeringcount;i++)
				 {
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_stdDeliveryDays=null;
					 String DB_stdDeliveryTime=null;
					 while (rs2.next()) 
					 {
						 DB_stdDeliveryDays =  rs2.getString(1);
						 DB_stdDeliveryTime = rs2.getString(2);
					 }
					 stmt2.close();
					 rs2.close();
					 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
					 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
					 if(DB_stdDeliveryDays!=null)
					 {
						if((DB_stdDeliveryTime!=null))
						{
							if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
							{
								String StatusMsg = passTestCaseDesc + WS_OfferingID;
								logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								StatusFlg=true;
							}
							else
							{
								String StatusMsg = failTestCaseDesc + WS_OfferingID;
								logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								StatusFlg=false;
							}
						}	
						else
						{
							if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
							{
								String StatusMsg = passTestCaseDesc + WS_OfferingID;
								logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								StatusFlg=true;
							}
							else
							{
								String StatusMsg = failTestCaseDesc + WS_OfferingID;
								logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								StatusFlg=false;
							}
						}
					 }	 
					 else
					 {
						 if((DB_stdDeliveryTime!=null))
						 {
							 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + WS_OfferingID;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + WS_OfferingID;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
						 }
						 else
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
								logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								StatusFlg=true;
						 }
					 }
					
					logger.info("Database and WebServices Details: " +WS_OfferingID);
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
					logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 4)
	public void TC_04() throws Exception
	{
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
				            .get(Resources.ResourcedataBS())
				            .then()
				            .extract().response();
				 String responsestr=res.asString();  
				 JsonPath js = new JsonPath(responsestr);
				 int offeringcount =  js.get("offerings.size()");
				 Boolean offeredMarketsStatus=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt = con.createStatement();
					 ResultSet rs2 = stmt.executeQuery(Query2);
					 String DB_domIntlCd=null;
					 while (rs2.next()) 
					 {
						  DB_domIntlCd = rs2.getString(1);
					 }
					 stmt.close();
					 rs2.close();
					 String expectedTagValue1=null;
					 String expectedTagValue2=null;
					 if(DB_domIntlCd.equals("I"))
					 {
						 expectedTagValue1  = "international";
					 }
					 else if(DB_domIntlCd.equals("D"))
					 {
						 expectedTagValue1 = "domestic";
					 }
					 else
					 {
						 expectedTagValue1  = "international";
						 expectedTagValue2 = "domestic";
					 }
					 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
					 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + WS_OfferingID;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  offeredMarketsStatus=true;
							  logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + WS_OfferingID;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  offeredMarketsStatus=false;
							  logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.equals(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 offeredMarketsStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + WS_OfferingID;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  offeredMarketsStatus=false;
							  logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
				 }
				 if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 5)
	public void TC_05() throws Exception
	{
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
				 Boolean busiResiIndStatus=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String DB_busiResiInd1=null;
					 String DB_busiResiInd2=null;
					 String DB_busiResiIndModified1=null;
					 String DB_busiResiIndModified2=null;
					 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
					 if(busiResiIndcount==2)
					 {
						 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
						 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
						 if(WS_busiResiInd1.equals("business"))
						 {
							 DB_busiResiInd1="BUSI";
						 }
						 else
						 {
							 DB_busiResiInd1="RESI";
						 }
						 if(WS_busiResiInd2.equals("residential"))
						 {
							 DB_busiResiInd2="RESI";
						 }
						 else
						 {
							 DB_busiResiInd2="BUSI";
						 }
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+DB_busiResiInd1+"'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt = con.createStatement();
						 ResultSet rs2 = stmt.executeQuery(Query2);
						 while (rs2.next()) 
						 {
							 DB_busiResiIndModified1 = "residential";
						 }
						 stmt.close();
						 rs2.close();
						 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+DB_busiResiInd2+"'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 while (rs3.next()) 
						 {
							 DB_busiResiIndModified2 = "business";
						 }
						 stmt3.close();
						 rs3.close();
						 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 busiResiIndStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + WS_OfferingID;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 busiResiIndStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
						 }
						 
					 }
					 
					 else if(busiResiIndcount==1)
					 {
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt = con.createStatement();
						 ResultSet rs2 = stmt.executeQuery(Query2);
						 while (rs2.next()) 
						 {
							 DB_busiResiInd1 = rs2.getString(1);
						 }
						 if(DB_busiResiInd1.equals("BUSI"))
						 {
							 DB_busiResiIndModified1="business";
						 }
						 else
						 {
							 DB_busiResiIndModified1="residential";
						 }
						 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
						 stmt.close();
						 rs2.close();
						 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 busiResiIndStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + WS_OfferingID;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 busiResiIndStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
						 } 
					 }
					 else
					 {
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt = con.createStatement();
						 ResultSet rs2 = stmt.executeQuery(Query2);
						 if(!rs2.next())
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 busiResiIndStatus=true;
							 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
						 }
						 stmt.close();
						 rs2.close();
					 }
					 
				 }
				 if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 6)
	public void TC_06() throws Exception
	{
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
				            .get(Resources.ResourcedataBS())
				            .then()
				            .extract().response();
				 String responsestr=res.asString();  
				 JsonPath js = new JsonPath(responsestr);
				 int offeringcount =  js.get("offerings.size()");
				 Boolean serviceCategoryStatus=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String DB_serviceCategory1=null;
					 String DB_serviceCategory2=null;
					 String DB_serviceCategoryModified1=null;
					 String DB_serviceCategoryModified2=null;
					 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
					 if(serviceCategorycount==2)
					 {
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt = con.createStatement();
						 ResultSet rs2 = stmt.executeQuery(Query2);
						 while (rs2.next()) 
						 {
							 DB_serviceCategory1 = rs2.getString(1);
						 }
						 if(DB_serviceCategory1.equals("PRCL"))
						 {
							 DB_serviceCategoryModified1="parcel";
						 }
						 stmt.close();
						 rs2.close();
						 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3); 
						 while (rs3.next()) 
						 {
							 DB_serviceCategory2 = rs3.getString(1);
						 }
						 if(DB_serviceCategory2.equals("FGHT"))
						 {
							 DB_serviceCategoryModified2="freight";
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
						 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
						 
						 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 serviceCategoryStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + WS_OfferingID;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 serviceCategoryStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
						 }
						 
					 }
					 
					 else if(serviceCategorycount==1)
					 {
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt = con.createStatement();
						 ResultSet rs2 = stmt.executeQuery(Query2);
						 while (rs2.next()) 
						 {
							 DB_serviceCategory1 = rs2.getString(1);
						 }
						 if(DB_serviceCategory1.equals("PRCL"))
						 {
							 DB_serviceCategoryModified1="parcel";
						 }
						 else
						 {
							 DB_serviceCategoryModified1="freight";
						 }
						 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
						 stmt.close();
						 rs2.close();
						 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 serviceCategoryStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + WS_OfferingID;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 serviceCategoryStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
						 } 
					 }
					 else
					 {
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt = con.createStatement();
						 ResultSet rs2 = stmt.executeQuery(Query2);
						 if(!rs2.next())
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 serviceCategoryStatus=true;
							 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
						 }
						 stmt.close();
						 rs2.close();
					 }
					 
				 }
				 if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 7)
	public void TC_07() throws Exception
	{
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
				 Response res = given()
				            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
				            .when()
				            .get(Resources.ResourcedataBS())
				            .then()
				            .extract().response();
				 String responsestr=res.asString();  
				 JsonPath js = new JsonPath(responsestr);
				 int offeringcount =  js.get("offerings.size()");
				 Boolean distributionSvcFlgStatus=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String DB_distributionSvcFlg=null;
					 String disflag=null;
					 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
					 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt = con.createStatement();
					 ResultSet rs2 = stmt.executeQuery(Query2);
					 if(!rs2.next())
					 {
						 DB_distributionSvcFlg="null";
						 disflag="N";
					 }
					 else
					 {
						 DB_distributionSvcFlg = rs2.getString(1);
						 disflag="Y";
					 }
					 stmt.close();
					 rs2.close();
					 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
					 if(WS_distributionSvcFlg.equals(disflag))
					 {
						 String StatusMsg = passTestCaseDesc + WS_OfferingID;
						 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
						 distributionSvcFlgStatus=true;
						 logger.info("Database and WebServices Details: " +WS_OfferingID);
						 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
					 }
					 else
					 {
						 String StatusMsg = failTestCaseDesc + WS_OfferingID;
						 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
						 distributionSvcFlgStatus=false;
						 logger.info("Database and WebServices Details: " +WS_OfferingID);
						 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
					 }
					 
				}
					 
				 if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }	 
					 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 8)
	public void TC_08() throws Exception
	{
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
				 Response res = given()
				            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
				            .when()
				            .get(Resources.ResourcedataBS())
				            .then()
				            .extract().response();
				 String responsestr=res.asString();  
				 JsonPath js = new JsonPath(responsestr);
				 int offeringcount =  js.get("offerings.size()");
				 Boolean CompareStatus=true;
				 Boolean StatusFlg=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					int WS_namesCount = js.get("offerings["+i+"].names.size()");
					String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
					for(int j=0;j<WS_namesCount;j++)
					{
						String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
						String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
						String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
						String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
						String WSnamescountryCd=null;
						WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
						if(WSnamestype.equals("translated"))
						{
							int count = WSnameslanguageId.length();
							WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
						}
						String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
						String DBnamestype=null;
						String DBnamessubType=null;
						String DBnamesencoding=null; 
						String DBlanguageId=null; 
						String DBcountryCd=null; 
						String DBnamesvalue=null;
						
						if(WSnameslanguageId!="")
						{
							String WSLanguageId=WSnameslanguageId.substring(0, 2);
							String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
								      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
								      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
								      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
								      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
						
							Statement stmt = con.createStatement();
							ResultSet rs = stmt.executeQuery(Query);
						while (rs.next()) 
						 {
							 DBnamestype = rs.getString(1);
							 DBnamessubType = rs.getString(2);
							 DBnamesencoding = rs.getString(3);
							 DBlanguageId = rs.getString(4);
							 DBcountryCd = rs.getString(5);
							 DBnamesvalue= rs.getString(6);
						 }
						stmt.close();
						rs.close();
						if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
						{
							if(DBlanguageId!=null && DBcountryCd!=null)
							{
								if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
										&& DBnamesvalue.equals(WSnamesvalue))
								{
									CompareStatus=true;
									System.out.println("Passed For: " +j);
								}
								else
								{
									CompareStatus=false;
									System.out.println("Passed For: " +j);
								}
							}
							else
							{
								if(DBnamesvalue.equals(WSnamesvalue))
								{
									CompareStatus=true;
									System.out.println("Passed For: " +j);
								}
								else
								{
									CompareStatus=false;
									System.out.println("Passed For: " +j);
								}
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
						}
						else
						{
							CompareStatus=false;
							System.out.println("Passed For: " +j);
						}
						}
					else if(WSnamescountryCd!="")
						{
							String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
								      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
								      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
								      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
							Statement stmt = con.createStatement();
							ResultSet rs = stmt.executeQuery(Query);
						while (rs.next()) 
						 {
							 DBnamestype = rs.getString(1);
							 DBnamessubType = rs.getString(2);
							 DBnamesencoding = rs.getString(3);
							 DBcountryCd = rs.getString(5);
							 DBnamesvalue= rs.getString(6);
						 }
						stmt.close();
						rs.close();
						if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
						{
							
							CompareStatus=true;
							//System.out.println("Passed For: " +j);
						}
						else
						{
							CompareStatus=false;
							//System.out.println("Passed For: " +j);
						}
						
						logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
						logger.info("DbValues and WSValues are null for languageId");
						
						}
						else 
						{
							String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
								      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
								      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
								      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt = con.createStatement();
							ResultSet rs = stmt.executeQuery(Query);
						while (rs.next()) 
						 {
							 DBnamestype = rs.getString(1);
							 DBnamessubType = rs.getString(2);
							 DBnamesencoding = rs.getString(3);
							 DBnamesvalue= rs.getString(6);
						 }
						stmt.close();
						rs.close();
						if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
						{
							
							CompareStatus=true;
							//System.out.println("Passed For: " +j);
						}
						else
						{
							CompareStatus=false;
							//System.out.println("Passed For: " +j);
						}
						
						logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
						logger.info("DbValues and WSValues are null for countryCd");
						logger.info("DbValues and WSValues are null for languageId");
						
						}
					}
					if(CompareStatus==true)
					{
						String StatusMsg = passTestCaseDesc + WS_OfferingID;
						logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
						StatusFlg=true;
					}	
					else
					{
						String StatusMsg = failTestCaseDesc + WS_OfferingID;
						logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
						StatusFlg=false;
					}
						
				  }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
					
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 9)
	public void TC_09() throws Exception
	{
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
				 Response res = given()
				            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
				            .when()
				            .get(Resources.ResourcedataBS())
				            .then()
				            .extract().response();
				 String responsestr=res.asString();  
				 JsonPath js = new JsonPath(responsestr);
				 int offeringcount =  js.get("offerings.size()");
				 Boolean distributionSvcFlgStatus=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String DB_moneyBackGuarantee=null;
					 String moneyBackGuaranteeflag=null;
					 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
					 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt = con.createStatement();
					 ResultSet rs2 = stmt.executeQuery(Query2);
					 if(!rs2.next())
					 {
						 moneyBackGuaranteeflag="null";
					 }
					 else
					 {
						 DB_moneyBackGuarantee = rs2.getString(1);
						 if(DB_moneyBackGuarantee.equals("MBGM"))
						 {
							 moneyBackGuaranteeflag="Y";
						 }
						 else
						 {
							 moneyBackGuaranteeflag="N";
						 }
					 }
					 stmt.close();
					 rs2.close();
					 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
					 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
					 {
						 String StatusMsg = passTestCaseDesc + WS_OfferingID;
						 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
						 distributionSvcFlgStatus=true;
						 logger.info("Database and WebServices Details: " +WS_OfferingID);
						 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
					 }
					 else if(WS_moneyBackGuarantee.equals(""))
					 {
						 String StatusMsg = passTestCaseDesc + WS_OfferingID;
						 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
						 distributionSvcFlgStatus=true;
						 logger.info("Database and WebServices Details: " +WS_OfferingID);
						 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
					 }
					 else
					 {
						 String StatusMsg = failTestCaseDesc + WS_OfferingID;
						 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
						 distributionSvcFlgStatus=false;
						 logger.info("Database and WebServices Details: " +WS_OfferingID);
						 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
					 }
					 
				}
					 
				 if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }	 
					 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 10)
	public void TC_10() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_10";
		
		
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
				 Boolean applicablePartyTypesStatus=true;
				 connectDatabase();
				 for(int i=0;i<offeringcount;i++)
				 {
					 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String DB_applicablePartyTypes=null;
					 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
					 if(applicablePartyTypescount>0)
					 {
						 for(int j=0;j<applicablePartyTypescount;j++)
						 {
							 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
							 String WS_Value=null;
							 
							 if(WS_applicablePartyTypes.equals("customer"))
							 {
								 WS_Value="CUSS";
							 }
							 else if(WS_applicablePartyTypes.equals("operations"))
							 {
								 WS_Value="OPRS";
							 }
							 else
							 {
								 WS_Value="BRKS";
							 }
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt = con.createStatement();
							 ResultSet rs2 = stmt.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_applicablePartyTypes = rs2.getString(1);
							 }
							 stmt.close();
							 rs2.close();
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 if(WS_Value.equals(DB_applicablePartyTypes))
							 {
								 applicablePartyTypesStatus=true;
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								  
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
							 }
							 
							 if(applicablePartyTypesStatus==true)
							 {
								 String StatusMsg = passTestCaseDesc + WS_OfferingID;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + WS_OfferingID;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
						 }
					 }
					 else
					 {
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt = con.createStatement();
						 ResultSet rs2 = stmt.executeQuery(Query2);
						 if(!rs2.next())
						 {
							 applicablePartyTypesStatus=true;
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
						 }
						 else
						 {
							 applicablePartyTypesStatus=false;
							 String StatusMsg = failTestCaseDesc + WS_OfferingID;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
						 }
						 stmt.close();
						 rs2.close();
					 }
				 }
				 if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
	
	@Test(priority = 11)
	public void TC_11() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_11";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 12)
	public void TC_12() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_12";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
		
	@Test(priority = 13)
	public void TC_13() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_13";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	

	@Test(priority = 14)
	public void TC_14() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_14";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 15)
	public void TC_15() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_15";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 16)
	public void TC_16() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_16";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{

				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 17)
	public void TC_17() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_17";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 18)
	public void TC_18() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_18";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
			    {
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 19)
	public void TC_19() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_19";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 20)
	public void TC_20() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_20";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 21)
	public void TC_21() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_21";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
		
	@Test(priority = 22)
	public void TC_22() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_22";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	

	@Test(priority = 23)
	public void TC_23() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_23";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 24)
	public void TC_24() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_24";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 25)
	public void TC_25() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_25";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 26)
	public void TC_26() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_26";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 27)
	public void TC_27() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_27";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 28)
	public void TC_28() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_28";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 29)
	public void TC_29() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_29";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 30)
	public void TC_30() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_30";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{ 
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
		
	@Test(priority = 31)
	public void TC_31() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_31";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	

	@Test(priority = 32)
	public void TC_32() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_32";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
				catch (Exception e)
				{
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 33)
	public void TC_33() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_33";
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 34)
	public void TC_34() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_34";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 35)
	public void TC_35() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_35";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 36)
	public void TC_36() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_36";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 37)
	public void TC_37() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_37";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 38)
	public void TC_38() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_38";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 39)
	public void TC_39() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_39";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
		
	@Test(priority = 40)
	public void TC_40() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_40";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{

				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	

	@Test(priority = 41)
	public void TC_41() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_41";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 42)
	public void TC_42() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_42";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 43)
	public void TC_43() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_43";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 44)
	public void TC_44() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_44";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 45)
	public void TC_45() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_45";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 46)
	public void TC_46() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_46";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 47)
	public void TC_47() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_47";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
			}	}

	
	@Test(priority = 48)
	public void TC_48() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_48";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
		
	@Test(priority = 49)
	public void TC_49() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_49";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 50)
	public void TC_50() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_50";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	}	}


	@Test(priority = 51)
	public void TC_51() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_51";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 52)
	public void TC_52() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_52";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 53)
	public void TC_53() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_53";
		
		 
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 54)
	public void TC_54() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_54";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 55)
	public void TC_55() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_55";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('epic', 'o2e')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for EPIC Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/epictooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 56)
	public void TC_56() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_56";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
					
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 57)
	public void TC_57() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_57";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

		
	@Test(priority = 58)
	public void TC_58() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_58";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 59)
	public void TC_59() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_59";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 60)
	public void TC_60() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_60";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 61)
	public void TC_61() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_61";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 62)
	public void TC_62() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_62";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try 
				{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 63)
	public void TC_63() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_63";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 64)
	public void TC_64() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_64";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 65)
	public void TC_65() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_65";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 66)
	public void TC_66() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_66";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
		
	@Test(priority = 67)
	public void TC_67() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_67";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 68)
	public void TC_68() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_68";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 69)
	public void TC_69() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_69";
		
	
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 70)
	public void TC_70() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_70";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 71)
	public void TC_71() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_71";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 72)
	public void TC_72() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_73";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 73)
	public void TC_73() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_73";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 74)
	public void TC_74() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_74";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 75)
	public void TC_75() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_75";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
		
	@Test(priority = 76)
	public void TC_76() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_76";
		
		boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 77)
	public void TC_77() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_77";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 78)
	public void TC_78() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_78";
		
	
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 79)
	public void TC_79() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_79";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 80)
	public void TC_80() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_80";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 81)
	public void TC_81() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_81";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 82)
	public void TC_82() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_82";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 83)
	public void TC_83() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_83";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 84)
	public void TC_84() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_84";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
		
	@Test(priority = 85)
	public void TC_85() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_85";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	

	@Test(priority = 86)
	public void TC_86() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_86";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 87)
	public void TC_87() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_87";
		
	
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 88)
	public void TC_88() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_88";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 89)
	public void TC_89() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_89";
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 90)
	public void TC_90() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_90";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 91)
	public void TC_91() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_91";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 92)
	public void TC_92() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_92";
		
		boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				
				try{
					logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_offeringType=null;
						 String DB_description=null;
						 String DB_domIntlCd=null;
						 String DB_effectiveDt=null;
						 String DB_expirationDt=null;
						 String DB_OfferingID=null;
						 while (rs2.next()) 
						 {
							  DB_offeringType =  rs2.getString(1);
							  DB_description = rs2.getString(2);
							  DB_domIntlCd = rs2.getString(3);
							  DB_effectiveDt = rs2.getString(4).substring(0, 10);
							  DB_expirationDt = rs2.getString(5).substring(0, 10);
							  DB_OfferingID = rs2.getString(7);
						 }
						 stmt2.close();
						 rs2.close();
						 String Data=js.getString("offerings["+i+"].offeringType");
						 String WS_offeringType=null;
						 if(Data.contentEquals("Base Service"))
						 {
							 WS_offeringType="BS"; // As response value for DB and WS are different
						 }
						 String WS_description=js.getString("offerings["+i+"].description");
						 String WS_domIntlCd=js.getString("offerings["+i+"].domIntlCd");
						 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt3 = con.createStatement();
						 ResultSet rs3 = stmt3.executeQuery(Query3);
						 String DB_operatingOrgCds=null;
						 while (rs3.next()) 
						 {
							 DB_operatingOrgCds =  rs3.getString(1);
						 }
						 stmt3.close();
						 rs3.close();
						 String WS_operatingOrgCds=js.getString("offerings["+i+"].operatingOrgCds");
						 String WS_effectiveDt=js.getString("offerings["+i+"].effectiveDt");
						 String WS_expirationDt=js.getString("offerings["+i+"].expirationDt");
						 
						if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
								WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
						
						logger.info("Values Compared for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues OfferingID:" +DB_OfferingID +" WSValue OfferingID: " +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offeringType:" +DB_offeringType +" WSValue offeringType: " +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues description:" +DB_description +" WSValue description: " +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues domIntlCd:" +DB_domIntlCd +" WSValue domIntlCd: " +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues effectiveDt:" +DB_effectiveDt +" WSValue effectiveDt: " +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues expirationDt:" +DB_expirationDt +" WSValue expirationDt: " +WS_expirationDt, ExtentColor.BLUE));
						 
					 }
	            	
	            }
				
				 if(StatusFlg==true)
				 {
					 logfile.info("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 logfile.info("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 93)
	public void TC_93() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_93";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT STD_DEL_DAY_NBR,STD_DEL_HOUR_TM FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_stdDeliveryDays=null;
						 String DB_stdDeliveryTime=null;
						 while (rs2.next()) 
						 {
							 DB_stdDeliveryDays =  rs2.getString(1);
							 DB_stdDeliveryTime = rs2.getString(2);
						 }
						 stmt2.close();
						 rs2.close();
						 String WS_stdDeliveryDays=js.getString("offerings["+i+"].stdDeliveryDays");
						 String WS_stdDeliveryTime=js.getString("offerings["+i+"].stdDeliveryTime");
						 if(DB_stdDeliveryDays!=null)
						 {
							if((DB_stdDeliveryTime!=null))
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays) && WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}	
							else
							{
								if(WS_stdDeliveryDays.equals(DB_stdDeliveryDays))
								{
									String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
								}
								else
								{
									String StatusMsg = failTestCaseDesc + Code;
									logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
									StatusFlg=false;
								}
							}
						 }	 
						 else
						 {
							 if((DB_stdDeliveryTime!=null))
							 {
								 if(WS_stdDeliveryTime.equals(DB_stdDeliveryTime))
									{
										String StatusMsg = passTestCaseDesc + Code;
										logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
										StatusFlg=true;
									}
									else
									{
										String StatusMsg = failTestCaseDesc + Code;
										logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
										StatusFlg=false;
									}
							 }
							 else
							 {
								    String StatusMsg = passTestCaseDesc + Code;
									logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
									StatusFlg=true;
							 }
						 }
						
						logger.info("Database and WebServices Details: " +WS_OfferingID);
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryTime:" +DB_stdDeliveryTime +" WSValue stdDeliveryTime: " +WS_stdDeliveryTime, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("DbValues stdDeliveryDays:" +DB_stdDeliveryDays +" WSValue stdDeliveryDays: " +WS_stdDeliveryDays, ExtentColor.BLUE));
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID); 
					 }
	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

		
	@Test(priority = 94)
	public void TC_94() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_94";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean offeredMarketsStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BS' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2);
						 String DB_domIntlCd=null;
						 while (rs2.next()) 
						 {
							  DB_domIntlCd = rs2.getString(1);
						 }
						 stmt2.close();
						 rs2.close();
						 String expectedTagValue1=null;
						 String expectedTagValue2=null;
						 if(DB_domIntlCd.equals("I"))
						 {
							 expectedTagValue1  = "international";
						 }
						 else if(DB_domIntlCd.equals("D"))
						 {
							 expectedTagValue1 = "domestic";
						 }
						 else
						 {
							 expectedTagValue1  = "international";
							 expectedTagValue2 = "domestic";
						 }
						 int WS_offeredMarketsCount =  js.get("offerings["+i+"].offeredMarkets.size()");
						 String WS_offeredMarkets1 = js.getString("offerings["+i+"].offeredMarkets[0]");
						 String WS_offeredMarkets2 = js.getString("offerings["+i+"].offeredMarkets[1]");
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_offeredMarketsCount==2)
						 {
							  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
							  {
								  String StatusMsg = passTestCaseDesc + Code;
								  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								  offeredMarketsStatus=true;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
								  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets2, ExtentColor.BLUE));
							  }
						 }
						 else
						 {
							 if(WS_offeredMarkets1.equals(expectedTagValue1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 offeredMarketsStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
							  else
							  {
								  String StatusMsg = failTestCaseDesc + Code;
								  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								  offeredMarketsStatus=false;
								  logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  }
						 }
					 }
	        	}
	            if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 95)
	public void TC_95() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_95";
		
		boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean busiResiIndStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int busiResiIndcount = js.get("offerings["+i+"].busiResiInd.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_busiResiInd1=null;
						 String DB_busiResiInd2=null;
						 String DB_busiResiIndModified1=null;
						 String DB_busiResiIndModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(busiResiIndcount==2)
						 {
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 String WS_busiResiInd2 = js.getString("offerings["+i+"].busiResiInd[1]");
							 String data1;
							 String data2;
							 if(WS_busiResiInd1.equals("business"))
							 {
								  data1 = "BUSI";
							 }
							 else
							 {
								 data1 = "RESI";
							 }
							 
							 if(WS_busiResiInd2.equals("business"))
							 {
								  data2 = "BUSI";
							 }
							 else
							 {
								 data2 = "RESI";
							 }
							 
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data1+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+data2+"'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_busiResiInd2 = rs3.getString(1);
							 }
							 if(DB_busiResiInd2.equals("RESI"))
							 {
								 DB_busiResiIndModified2="residential";
							 }
							 stmt3.close();
							 rs3.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1) && WS_busiResiInd2.equals(DB_busiResiIndModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd2 +" WSValue offered Markets: " +WS_busiResiInd2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(busiResiIndcount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD=('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_busiResiInd1 = rs2.getString(1);
							 }
							 if(DB_busiResiInd1.equals("BUSI"))
							 {
								 DB_busiResiIndModified1="business";
							 }
							 else
							 {
								 DB_busiResiIndModified1="residential";
							 }
							 String WS_busiResiInd1 = js.getString("offerings["+i+"].busiResiInd[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_busiResiInd1.equals(DB_busiResiIndModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 busiResiIndStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues offered Markets:" +DB_busiResiInd1 +" WSValue offered Markets: " +WS_busiResiInd1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('RESI','BUSI') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 busiResiIndStatus=true;
								 logger.info("Database and WebServices Values are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
					 
	        	}
	            if(busiResiIndStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 96)
	public void TC_96() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_96";
		
	
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean serviceCategoryStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int serviceCategorycount = js.get("offerings["+i+"].serviceCategory.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_serviceCategory1=null;
						 String DB_serviceCategory2=null;
						 String DB_serviceCategoryModified1=null;
						 String DB_serviceCategoryModified2=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(serviceCategorycount==2)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='PRCL'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 stmt2.close();
							 rs2.close();
							 String Query3="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='FGHT'and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt3 = con.createStatement();
							 ResultSet rs3 = stmt3.executeQuery(Query3);
							 while (rs3.next()) 
							 {
								 DB_serviceCategory2 = rs3.getString(1);
							 }
							 if(DB_serviceCategory2.equals("FGHT"))
							 {
								 DB_serviceCategoryModified2="freight";
							 }
							 stmt3.close();
							 rs3.close();
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 String WS_serviceCategory2 = js.getString("offerings["+i+"].serviceCategory[1]");
							 
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1) && WS_serviceCategory2.equals(DB_serviceCategoryModified2))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory2 +" WSValue serviceCategory: " +WS_serviceCategory2, ExtentColor.BLUE));
							 }
							 
						 }
						 
						 else if(serviceCategorycount==1)
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 while (rs2.next()) 
							 {
								 DB_serviceCategory1 = rs2.getString(1);
							 }
							 if(DB_serviceCategory1.equals("PRCL"))
							 {
								 DB_serviceCategoryModified1="parcel";
							 }
							 else
							 {
								 DB_serviceCategoryModified1="freight";
							 }
							 String WS_serviceCategory1 = js.getString("offerings["+i+"].serviceCategory[0]");
							 stmt2.close();
							 rs2.close();
							 if(WS_serviceCategory1.equals(DB_serviceCategoryModified1))
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 }
							 else
							 {
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 serviceCategoryStatus=false;
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues serviceCategory:" +DB_serviceCategory1 +" WSValue serviceCategory: " +WS_serviceCategory1, ExtentColor.BLUE));
							 } 
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('PRCL','FGHT') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2);
							 if(!rs2.next())
							 {
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 serviceCategoryStatus=true;
								 logger.info("Database and WebServices Values for serviceCategory are null for offeringid: " +WS_OfferingID);
							 }
							 stmt2.close();
							 rs2.close();
						 }
						 
					 }
			}
	            if(serviceCategoryStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 97)
	public void TC_97() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_97";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_distributionSvcFlg=null;
						 String disflag=null;
						 String WS_distributionSvcFlg = js.getString("offerings["+i+"].distributionSvcFlg");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='DIST'and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
						 ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 DB_distributionSvcFlg="null";
							 disflag="N";
						 }
						 else
						 {
							 DB_distributionSvcFlg = rs2.getString(1);
							 disflag="Y";
						 }
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 stmt2.close();
						 rs2.close();
						 if(WS_distributionSvcFlg.equals(disflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_distributionSvcFlg +" WSValue distributionSvcFlg: " +WS_distributionSvcFlg, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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



	@Test(priority = 98)
	public void TC_98() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_98";
		
		boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean CompareStatus=true;
	            Boolean StatusFlg=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						int WS_namesCount = js.get("offerings["+i+"].names.size()");
						String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings["+i+"].names["+j+"].type");
							String WSnamessubType = js.getString("offerings["+i+"].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings["+i+"].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings["+i+"].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings["+i+"].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings["+i+"].names["+j+"].value");
							String DBnamestype=null;
							String DBnamessubType=null;
							String DBnamesencoding=null; 
							String DBlanguageId=null; 
							String DBcountryCd=null; 
							String DBnamesvalue=null;
							
							if(WSnameslanguageId!="")
							{
								String WSLanguageId=WSnameslanguageId.substring(0, 2);
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"' AND"
									      + " LANGUAGE_CD='"+WSLanguageId+"' AND COUNTRY_CD='"+WSnamescountryCd+"'";
							
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBlanguageId = rs1.getString(4);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding))
							{
								if(DBlanguageId!=null && DBcountryCd!=null)
								{
									if(DBlanguageId.equals(WSLanguageId) && DBcountryCd.equals(WSnamescountryCd)
											&& DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										System.out.println("Passed For: " +j);
									}
								}
								
								logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
								logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesLanguageId:" +DBlanguageId +" WSValue namesLanguageId: " +WSLanguageId, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								System.out.println("Passed For: " +j);
							}
							}
						else if(WSnamescountryCd!="")
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'AND COUNTRY_CD='"+WSnamescountryCd+"'";
								Statement stmt1 = con.createStatement();
								ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBcountryCd = rs1.getString(5);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue) && DBcountryCd.equals(WSnamescountryCd))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namescountryCd:" +DBcountryCd +" WSValue namescountryCd: " +WSnamescountryCd, ExtentColor.BLUE));
							logger.info("DbValues and WSValues are null for languageId");
							
							}
							else 
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							while (rs1.next()) 
							 {
								 DBnamestype = rs1.getString(1);
								 DBnamessubType = rs1.getString(2);
								 DBnamesencoding = rs1.getString(3);
								 DBnamesvalue= rs1.getString(6);
							 }
							stmt1.close();
							rs1.close();
							if(DBnamestype.equals(WSnamestype) && DBnamessubType.equals(WSnamessubType) && DBnamesencoding.equals(WSnamesencoding) && DBnamesvalue.equals(WSnamesvalue))
							{
								
								CompareStatus=true;
								//System.out.println("Passed For: " +j);
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for Offering ID: " +WS_OfferingID);
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namestype:" +DBnamestype +" WSValue namestype: " +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namessubType:" +DBnamessubType +" WSValue namessubType: " +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesencoding:" +DBnamesencoding +" WSValue namesencoding: " +WSnamesencoding, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("DbValues namesvalue:" +DBnamesvalue +" WSValue namesvalue: " +WSnamesvalue, ExtentColor.BLUE)); 
							logger.info("DbValues and WSValues are null for countryCd");
							logger.info("DbValues and WSValues are null for languageId");
							
							}
						}
						
						if(CompareStatus==true)
						{
							String StatusMsg = passTestCaseDesc + Code;
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}	
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}

	        	}
	            if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		}
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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

	
	@Test(priority = 99)
	public void TC_99() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_99";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean distributionSvcFlgStatus=true;
	            for(int j=0;j<dbCodes.size();j++)
	            {
	            	
	            	String Code = dbCodes.get(j).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_moneyBackGuarantee=null;
						 String moneyBackGuaranteeflag=null;
						 String WS_moneyBackGuarantee = js.getString("offerings["+i+"].moneyBackGuarantee");
						 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN ('MBGM','MBGN')and OFFERING_ID='"+WS_OfferingID+"'";
						 Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery(Query2); 
						 if(!rs2.next())
						 {
							 moneyBackGuaranteeflag="null";
						 }
						 else
						 {
							 DB_moneyBackGuarantee = rs2.getString(1);
							 if(DB_moneyBackGuarantee.equals("MBGM"))
							 {
								 moneyBackGuaranteeflag="Y";
							 }
							 else
							 {
								 moneyBackGuaranteeflag="N";
							 }
						 }
						 stmt2.close();
						 rs2.close();
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(WS_moneyBackGuarantee.equals(moneyBackGuaranteeflag))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 else if(WS_moneyBackGuarantee.equals(""))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 distributionSvcFlgStatus=true;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.info("DbValues distributionSvcFlg and WSValue distributionSvcFlg: " +moneyBackGuaranteeflag);
						 }
						 else
						 {
							 String StatusMsg = failTestCaseDesc + Code;
							 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 distributionSvcFlgStatus=false;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues distributionSvcFlg:" +DB_moneyBackGuarantee +" WSValue distributionSvcFlg: " +WS_moneyBackGuarantee, ExtentColor.BLUE));
						 }
						 
					}

	        	}
	            if(distributionSvcFlgStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 100)
	public void TC_100() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_100";
		
		
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				String targetDate = DataObject.getVariable("targetDate", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
						+ " where o.OFFERING_TYPE_CD='BS' and o.LFCL_STATUS_CD='AC' "
			            + " and (trunc(o.EFFECTIVE_DT) <= trunc(sysdate) and trunc(o.EXPIRATION_DT) >= trunc(sysdate))"
						+ " and o.OFFERING_TYPE_CD = om.OFFERING_TYPE_CD  and om.LFCL_STATUS_CD = 'AC'"
			            + " and ((om.EXPIRATION_DT is not NULL AND (om.EXPIRATION_DT >= trunc(sysdate))) OR om.EXPIRATION_DT is NULL) and om.MAP_TYPE_CD  in ('dom', 'o2d')"
						+ " and o.ENTERPRISE_PRODUCT_ID = om.OFFERING_ID"
			            +" order by o.ENTERPRISE_PRODUCT_ID asc, om.INCL_ORIG_CNTRY_CD asc, om.INCL_DEST_CNTRY_CD";
		       Statement stmt = con.createStatement();
		       ResultSet rs = stmt.executeQuery(CdQuery);
               ArrayList<String> dbOfferingID=new ArrayList<>();
	           ArrayList<String> dbCodes=new ArrayList<>();
	           Boolean StatusFlg=true;
	            while (rs.next()) 
	            {
		               dbOfferingID.add(rs.getString(1)); 
		                dbCodes.add(rs.getString(2)); 
	            }
	            stmt.close();
	            rs.close();
	            Boolean applicablePartyTypesStatus=true;
	            for(int k=0;k<dbCodes.size();k++)
	            {
	            	
	            	String Code = dbCodes.get(k).toString();
	            	logfile.info("Values Compared for DOM Code: " +Code);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/baseservices/domtooffering")
					            .then()
					            .extract().response();	
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 int offeringcount =  js.get("offerings.size()");
					 for(int i=0;i<offeringcount;i++)
					 {
						 int applicablePartyTypescount = js.get("offerings["+i+"].applicablePartyTypes.size()");
						 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
						 String DB_applicablePartyTypes=null;
						 logfile.info("Values Compared for Offering ID: " +WS_OfferingID);
						 if(applicablePartyTypescount>0)
						 {
							 for(int j=0;j<applicablePartyTypescount;j++)
							 {
								 String WS_applicablePartyTypes = js.getString("offerings["+i+"].applicablePartyTypes["+j+"]"); 
								 String WS_Value=null;
								 
								 if(WS_applicablePartyTypes.equals("customer"))
								 {
									 WS_Value="CUSS";
								 }
								 else if(WS_applicablePartyTypes.equals("operations"))
								 {
									 WS_Value="OPRS";
								 }
								 else
								 {
									 WS_Value="BRKS";
								 }
								 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD='"+WS_Value+"'and OFFERING_ID='"+WS_OfferingID+"'";
								 Statement stmt2 = con.createStatement();
								 ResultSet rs2 = stmt2.executeQuery(Query2); 
								 while (rs2.next()) 
								 {
									 DB_applicablePartyTypes = rs2.getString(1);
								 }
								 stmt2.close();
								 rs2.close();
								 logger.info("Database and WebServices Details: " +WS_OfferingID);
								 if(WS_Value.equals(DB_applicablePartyTypes))
								 {
									 applicablePartyTypesStatus=true;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
									  
								 }
								 else
								 {
									 applicablePartyTypesStatus=false;
									 logger.log(Status.INFO, MarkupHelper.createLabel("DbValues applicablePartyTypes:" +DB_applicablePartyTypes +" WSValue applicablePartyTypes: " +WS_applicablePartyTypes, ExtentColor.BLUE));
								 }
								 
								 if(applicablePartyTypesStatus==true)
								 {
									 String StatusMsg = passTestCaseDesc + Code;
									 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 }
								 else
								 {
									 String StatusMsg = failTestCaseDesc + Code;
									 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								 }
							 }
						 }
						 else
						 {
							 String Query2="select CTGY_TYPE_CD from OFFERING_CATEGORY WHERE CTGY_TYPE_CD IN('CUSS','OPRS','BRKS') and OFFERING_ID='"+WS_OfferingID+"'";
							 Statement stmt2 = con.createStatement();
							 ResultSet rs2 = stmt2.executeQuery(Query2); 
							 if(!rs2.next())
							 {
								 applicablePartyTypesStatus=true;
								 String StatusMsg = passTestCaseDesc + Code;
								 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								 logger.info("DbValues applicablePartyTypes and WSValue applicablePartyTypes are null" );
							 }
							 else
							 {
								 applicablePartyTypesStatus=false;
								 String StatusMsg = failTestCaseDesc + Code;
								 logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							 }
							 stmt2.close();
							 rs2.close();
						 }
					 }

	        	}
	            if(applicablePartyTypesStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 System.out.println("Failed: " +testCaseName);
					 Assert.fail();
				 }
				 
			}
		
			catch (Exception e)
			{
				logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	
	@Test(priority = 101)
	public void TC_101() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_101";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }

	
	
	@Test(priority = 102)
	public void TC_102() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_102";
	   
	 
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }


	
	@Test(priority = 103)
	public void TC_103() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_103";
	   
	   
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }


	
	@Test(priority = 104)
	public void TC_104() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_104";
	   
	 
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }



	@Test(priority = 105)
	public void TC_105() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_105";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }


	
	@Test(priority = 106)
	public void TC_106() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_106";
	   
	   
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }}
	    	   else
	    	   {
	    	   	skipTest(testCaseName);
	    	   }
	    	   

	}

	
	@Test(priority = 107)
	public void TC_107() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_107";
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }


	
	@Test(priority = 108)
	public void TC_108() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_108";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	      Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("epicid", "XXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/epictooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      
	    }

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }


	
	@Test(priority = 109)
	public void TC_109() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_109";
	   
	 
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      
	    }

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }

	

	@Test(priority = 110)
	public void TC_110() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_110";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }}
	    	   else
	    	   {
	    	   	skipTest(testCaseName);
	    	   }
	    	   }

	

	
	@Test(priority = 111)
	public void TC_111() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_111";
	   
	   
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }



	@Test(priority = 112)
	public void TC_112() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_112";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }


	
	@Test(priority = 113)
	public void TC_113() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_113";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }


	@Test(priority = 114)
	public void TC_114() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_114";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }



	@Test(priority = 115)
	public void TC_115() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_115";
	 
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	       else
	       {
	       	skipTest(testCaseName);
	       }
	       }



	@Test(priority = 116)
	public void TC_116() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_116";
	   
	  
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	    	  logfile.info("Executing Test Case: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	   	      String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   	       Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .param("domid", "XXXXXXXX").and().param("targetdt", targetDate)
		            .when()
		            .get("v1/offerings/baseservices/domtooffering")
		            .then()
		            .extract().response();
	            
	            String responsestr=res.asString();  
	            JsonPath js = new JsonPath(responsestr);
	            String Wsstatus= js.get("responseStatus.status");
	            String Wsmessage= js.get("responseStatus.message");
	            String Wscode= js.get("responseStatus.code");
	            String WsCount=Integer.toString(js.get("resultCount"));
	            
	            if (Wsstatus.equals(expectedStatus) && Wsmessage.equals(expectedMessage) && Wscode.contains(expectedCode) && WsCount.contentEquals(expectedResultCount) )
	                {
	            	     logger.log(Status.PASS, MarkupHelper.createLabel(passTestCaseDesc, ExtentColor.GREEN));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.PASS, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected Status:" +expectedStatus + " Actual Wsstatus:"+ Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected message:"+expectedMessage+  " Actual Wsstatus: "+ Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Compared:"+" Expected code:"+expectedCode+  " Actual Wscode:"+ Wscode, ExtentColor.BLUE));
	            	     logger.info("Compared:"+" Expected resultCount:"+expectedResultCount+  " Actual  WsCount:<"+ WsCount +">");
	                     
	                }             

	           if(StatusFlg==true)
	           {
	        	   System.out.println("Passed Succesfully: " +testCaseName);

	           }
	           else
	           {
	        	   System.out.println("Failed: " +testCaseName);     
	        	   Assert.fail();
	                    
	           }
	      }
	    

	       catch (Exception e)
	       {
	    	   logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
	    	   Backend.displayException(e);
	          
	       }

	}
	 else
	       {
	       	skipTest(testCaseName);
	       }
}



}
