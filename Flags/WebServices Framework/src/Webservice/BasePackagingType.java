package Webservice;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
import org.testng.annotations.Parameters;
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
import backend.UserStoryName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BasePackagingType 
{
	static ExtentHtmlReporter htmlReporter;
	static ExtentReports extent;
	static ExtentTest logger;
	static String dbUrl;
	static String username;
	static String password;
	static Connection con;
	static Logger logfile = Logger.getLogger(BasePackagingType.class);
	final String UserStoryName="BasePackagingType";
	static String ReportName;
	String zipFileName;
	
	public static String startReport() throws IOException
	{
		
		 String Date = Backend.getCurrentDateTime();
		 htmlReporter = new ExtentHtmlReporter(Backend.getProperty("ExtentReportPath")+"/"+Date+"_WebServiceReportForBasePackagingType.html");
		 extent = new ExtentReports ();
		 extent.attachReporter(htmlReporter);
    	 extent.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
		 extent.setSystemInfo("Environment", Backend.getProperty("Test_Level"));
		 extent.setSystemInfo("User Name", System.getProperty("user.name"));
	     htmlReporter.config().setDocumentTitle(Backend.getProperty("Doc_Name1"));
	     htmlReporter.config().setReportName(Backend.getProperty("Report_Name1"));
	     htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		 htmlReporter.config().setChartVisibilityOnOpen(true);
	     htmlReporter.config().setTheme(Theme.DARK);
	     htmlReporter.config().setEncoding("utf-8");
	     htmlReporter.config().setCSS("css-string");
		 htmlReporter.config().setJS("js-string");
		 ReportName = Backend.getProperty("ExtentReportPath")+"/"+Date+"_WebServiceReportForBasePackagingType.html";
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

	
	
	@Test(priority = 1)
	public void TC_01() throws Exception
	{
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
				 String Query="SELECT ENTERPRISE_PRODUCT_ID FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' AND LFCL_STATUS_CD='AC'";
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(Query);
				 ArrayList<String> dbOfferingID=new ArrayList<>();
				 while (rs.next()) 
				 {
					 dbOfferingID.add(rs.getString(1)); 
				 }
				 stmt.close();
				 rs.close();
				 Boolean StatusFlg=true;
				 for(int i=0;i<offeringcount;i++)
				 {
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Data=js.getString("offerings["+i+"].offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("Base Packaging Type"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
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
					 String WS_approxPkgVolCCm=js.getString("offerings["+i+"].approxPkgVolCCm");
					if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
							WS_description.equals(DB_description) && WS_domIntlCd.equals(DB_domIntlCd) &&	
							WS_effectiveDt.equals(DB_effectiveDt) && WS_expirationDt.equals(DB_expirationDt) &&
							WS_operatingOrgCds.contains(DB_operatingOrgCds))
					{
						if(DB_approxPkgVolCCm!=null)
						{
							if(WS_approxPkgVolCCm.equals(DB_approxPkgVolCCm))
							{
								String StatusMsg = passTestCaseDesc + WS_OfferingID;
								logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
								StatusFlg=true;
							}
							else
							{
								String StatusMsg = failTestCaseDesc + WS_OfferingID;
								logger.info("Database and WebServices Details: " +WS_OfferingID);
								logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
								StatusFlg=false;
							}
							
						}
						else
						{
							String StatusMsg = passTestCaseDesc + WS_OfferingID;
							logger.info("Database and WebServices Details: " +WS_OfferingID);
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=true;
						}
						
						
					}
					else
					{
						String StatusMsg = failTestCaseDesc + WS_OfferingID;
						logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
						StatusFlg=false;
					}
					
					logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }}
				 catch (Exception e)
				{
					 Backend.displayException(e);
					 logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				{logfile.info("Executing Test Case: " +testCaseName);
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
				 Boolean offeredMarketsStatus=true;
				 for(int i=0;i<offeringcount;i++)
				 {
					 String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.equals(expectedTagValue1) && WS_offeredMarkets2.equals(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + WS_OfferingID;
							  logger.info("Database and WebServices Details: " +WS_OfferingID);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  offeredMarketsStatus=true;
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + WS_OfferingID;
							  logger.info("Database and WebServices Details: " +WS_OfferingID);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							   logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  offeredMarketsStatus=false;
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.equals(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + WS_OfferingID;
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 offeredMarketsStatus=true;
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + WS_OfferingID;
							  logger.info("Database and WebServices Details: " +WS_OfferingID);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  offeredMarketsStatus=false;
						  }
					 }
					 
					
				 }
				 if(offeredMarketsStatus==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				{logfile.info("Executing Test Case: " +testCaseName);
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
				 Boolean CompareStatus=true;
				 Boolean StatusFlg=true;
				 for(int i=0;i<offeringcount;i++)
				 {
					int WS_alternateIdsCount = js.get("offerings["+i+"].alternateIds.size()");
					String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
					for(int j=0;j<WS_alternateIdsCount;j++)
					{
						String WS_alternateIdType=js.getString("offerings["+i+"].alternateIds["+j+"].type");
						String WS_alternateIdidentifier=js.getString("offerings["+i+"].alternateIds["+j+"].identifier");
						String WS_Type = null;
						if(WS_alternateIdType.equals("fusion"))
						{
							WS_Type="fsn";
						}
						else
						{
							WS_Type=WS_alternateIdType;
						}
						String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
						Statement stmt2 = con.createStatement();
						ResultSet rs = stmt2.executeQuery(Query);
						String DB_alternateIdType=null;
						String DB_alternateIdidentifier=null;
						while (rs.next()) 
						 {
							DB_alternateIdType = rs.getString(1);
							DB_alternateIdidentifier = rs.getString(2);
						 }
						stmt2.close();
						rs.close();
						if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
						{
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE));
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							 CompareStatus=true;
						}
						else
						{
							 logger.info("Database and WebServices Details: " +WS_OfferingID);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE));
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							 CompareStatus=false;
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
					 Assert.fail();
				 }
					
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				 Boolean CompareStatus=true;
				 Boolean StatusFlg=true;
				 for(int i=0;i<offeringcount;i++)
				 {
					int WS_namesCount = js.get("offerings["+i+"].names.size()");
					String WS_OfferingID=js.getString("offerings["+i+"].offeringId");
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
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
						}
						else
						{
							CompareStatus=false;
							System.out.println("Passed For: " +j);
						}
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
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
						
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
						logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
						StatusFlg=false;
					}
						
				  }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
					
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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


	@Test(priority = 05)
	public void TC_05() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_05";
	   
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try 
	    	   {logfile.info("Executing TestCase: " +testCaseName);
	    	  testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   	      String expectedCode =  DataObject.getVariable("code", testCaseName);
	   	      String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   	      String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	          Response res = given()
			            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
			            .when()
			            .get(Resources.ResourcedataBPT())
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
	
	@Test(priority = 6)
	public void TC_06() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_06";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
		
		
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
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
		
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
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for EPIC Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
				}
		finally 
		 {
		 	logfile.info("Execution is completed for Test Case No." + testCaseName);
		 	closeDatabase();
		 	logfile.info("------------------------------------------------------------------");
		 }}
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
					 Assert.fail();
				 }
		}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
			    try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
		}
			    catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
		boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					    /*logger.info("Database and WebServices Details for EPIC Code: " +Code);
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +">WSValue:<" +WS_OfferingID, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +">WSValue:<" +WS_offeringType, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +">WSValue:<" +WS_description, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +">WSValue:<" +WS_domIntlCd, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +">WSValue:<" +WS_effectiveDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +">WSValue:<" +WS_expirationDt, ExtentColor.BLUE));
						logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +">WSValue:<" +WS_approxPkgVolCCm, ExtentColor.BLUE));*/
					 
					 logger.info("Database and WebServices Details for EPIC Code: " +Code);
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
		
		catch (Exception e)
		{
			Backend.displayException(e);
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
	@Test(priority =11)
	public void TC_11() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_11";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
		boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for EPIC Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
					 Assert.fail();
				 }
		}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
		}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					 logger.info("Database and WebServices Details for EPIC Code: " +Code);
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
		
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
	
	@Test(priority =15)
	public void TC_15() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_15";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for EPIC Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
		
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
					 Assert.fail();
				 }
		}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
		}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					 logger.info("Database and WebServices Details for EPIC Code: " +Code);
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
		
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
	@Test(priority =19)
	public void TC_19() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_19";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for EPIC Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
		
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
					 Assert.fail();
				 }
		}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
		}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					 logger.info("Database and WebServices Details for DOM Code: " +Code);
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
	
	@Test(priority =23)
	public void TC_23() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_23";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for EPIC Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for EPIC Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
				catch (Exception e)
				{
					Backend.displayException(e);
					logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for EPIC Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as EPIC_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("epicid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/epictooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for EPIC Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_26";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 27)
	public void TC_27() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_27";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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

	@Test(priority = 28)
	public void TC_28() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_28";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 29)
	public void TC_29() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_29";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 30)
	public void TC_30() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_30";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	
	@Test(priority = 31)
	public void TC_31() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_31";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 32)
	public void TC_32() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_32";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 33)
	public void TC_33() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_33";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	          try{
	    	   testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("epicid", "XX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/epictooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 34)
	public void TC_34() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_34";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					 logger.info("Database and WebServices Details for DOM Code: " +Code);
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for DOM Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					 logger.info("Database and WebServices Details for DOM Code: " +Code);
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for DOM Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
					 Assert.fail();
				 }
			}
		catch (Exception e)
		{
			Backend.displayException(e);
			logger.log(Status.FAIL, MarkupHelper.createLabel("Test Case Failed Due To Exception", ExtentColor.RED));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					 logger.info("Database and WebServices Details for DOM Code: " +Code);
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
					 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for DOM Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					    logger.info("Database and WebServices Details for DOM Code: " +Code);
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
	
	@Test(priority = 48)
	public void TC_48() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_48";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for DOM Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
	
	@Test(priority = 51)
	public void TC_51() throws Exception
	{
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String testCaseName = "TC_51";
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String DB_OfferingID=dbOfferingID.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String WS_description=js.getString("offerings.description");
					 String WS_domIntlCd=js.getString("offerings.domIntlCd");
					 String WS_operatingOrgCds=js.getString("offerings.operatingOrgCds");
					 String WS_effectiveDt=js.getString("offerings.effectiveDt");
					 String WS_expirationDt=js.getString("offerings.expirationDt");
					 String WS_approxPkgVolCCm=js.getString("offerings.approxPkgVolCCm");
					 String Data=js.getString("offerings.offeringType");
					 String WS_offeringType=null;
					 if(Data.contentEquals("[Base Packaging Type]"))
					 {
						 WS_offeringType="BPT"; // As response value for DB and WS are different
					 }
					 
					 String Query2="SELECT OFFERING_TYPE_CD,OFFERING_DESC,DOM_INTL_CD,EFFECTIVE_DT,EXPIRATION_DT,LAST_UPDATE_TMSTP,PKG_TYP_APPROX_VOL_QTY FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
					 Statement stmt2 = con.createStatement();
					 ResultSet rs2 = stmt2.executeQuery(Query2);
					 String DB_offeringType=null;
					 String DB_description=null;
					 String DB_domIntlCd=null;
					 String DB_effectiveDt=null;
					 String DB_expirationDt=null;
					 String DB_approxPkgVolCCm=null;
					 String DB_operatingOrgCds=null;
					 while (rs2.next()) 
					 {
						  DB_offeringType =  rs2.getString(1);
						  DB_description = rs2.getString(2);
						  DB_domIntlCd = rs2.getString(3);
						  DB_effectiveDt = rs2.getString(4).substring(0, 10);
						  DB_expirationDt = rs2.getString(5).substring(0, 10);
						  DB_approxPkgVolCCm = rs2.getString(7);
					 }
					 stmt2.close();
					 rs2.close();
					 String Query3="SELECT ORGANIZATION_CD FROM OFFERING_ORG WHERE OFFERING_ID='"+WS_OfferingID+"'";
					 Statement stmt3 = con.createStatement();
					 ResultSet rs3 = stmt3.executeQuery(Query3);
					 while (rs3.next()) 
					 {
						 DB_operatingOrgCds =  rs3.getString(1);
					 }
					 stmt3.close();
					 rs3.close();
					 if(WS_OfferingID.equals(DB_OfferingID) && WS_offeringType.equals(DB_offeringType) &&
								WS_description.contains(DB_description) && WS_domIntlCd.contains(DB_domIntlCd) &&	
								WS_effectiveDt.contains(DB_effectiveDt) && WS_expirationDt.contains(DB_expirationDt) &&
								WS_operatingOrgCds.contains(DB_operatingOrgCds))
						{
							if(DB_approxPkgVolCCm!=null)
							{
								if(WS_approxPkgVolCCm.contains(DB_approxPkgVolCCm))
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
						else
						{
							String StatusMsg = failTestCaseDesc + Code;
							logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							StatusFlg=false;
						}
					 
					    logger.info("Database and WebServices Details for DOM Code: " +Code);
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_OfferingID +" Actual WSValue:" +WS_OfferingID, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_offeringType +" Actual WSValue:" +WS_offeringType, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_description +" Actual WSValue:" +WS_description, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd +" Actual WSValue:" +WS_domIntlCd, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_effectiveDt +" Actual WSValue:" +WS_effectiveDt, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_expirationDt +" Actual WSValue:" +WS_expirationDt, ExtentColor.BLUE));
						 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_approxPkgVolCCm +" Actual WSValue:" +WS_approxPkgVolCCm, ExtentColor.BLUE));
					 
				 }
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
		String targetDate = DataObject.getVariable("targetDate", testCaseName);
			boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
			if (isRunnableTest)
			{
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 String Query2="SELECT DOM_INTL_CD FROM OFFERING WHERE OFFERING_TYPE_CD ='BPT' and ENTERPRISE_PRODUCT_ID='"+WS_OfferingID+"'";
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
					 int WS_offeredMarketsCount =  js.get("offerings[0].offeredMarkets.size()");
					 String WS_offeredMarkets1 = js.getString("offerings[0].offeredMarkets[0]");
					 String WS_offeredMarkets2 = js.getString("offerings[0].offeredMarkets[1]");
					 if(WS_offeredMarketsCount==2)
					 {
						  if(WS_offeredMarkets1.contains(expectedTagValue1) && WS_offeredMarkets2.contains(expectedTagValue2))
						  {
							  String StatusMsg = passTestCaseDesc + Code;
							  logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							  StatusFlg=true;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue2 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 else
					 {
						 if(WS_offeredMarkets1.contains(expectedTagValue1))
						 {
							 String StatusMsg = passTestCaseDesc + Code;
							 logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							 StatusFlg=true;
							 logger.info("Database and WebServices Details for DOM Code: " +Code);
							 logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
						  else
						  {
							  String StatusMsg = failTestCaseDesc + Code;
							  logger.log(Status.FAIL, MarkupHelper.createLabel(StatusMsg, ExtentColor.RED));
							  StatusFlg=false;
							  logger.info("Database and WebServices Details for DOM Code: " +Code);
							  logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_domIntlCd+"[" +expectedTagValue1 +"]"+ "Actual WSValue:" +WS_offeredMarkets1, ExtentColor.BLUE));
						  }
					 }
					 
				 }
					
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_alternateIdsCount = js.get("offerings[0].alternateIds.size()");
					  for(int j=0;j<WS_alternateIdsCount;j++)
						{
							String WS_alternateIdType=js.getString("offerings[0].alternateIds["+j+"].type");
							String WS_alternateIdidentifier=js.getString("offerings[0].alternateIds["+j+"].identifier");
							String WS_Type = null;
							if(WS_alternateIdType.equals("fusion"))
							{
								WS_Type="fsn";
							}
							else
							{
								WS_Type=WS_alternateIdType;
							}
							String Query="SELECT MAP_TYPE_CD,ALTERNATE_DESC FROM OFFERING_MAPPING WHERE OFFERING_TYPE_CD ='BPT' and MAP_TYPE_CD='"+WS_Type+"' and OFFERING_ID='"+WS_OfferingID+"'";
							Statement stmt1 = con.createStatement();
							ResultSet rs1 = stmt1.executeQuery(Query);
							String DB_alternateIdType=null;
							String DB_alternateIdidentifier=null;
							while (rs1.next()) 
							 {
								DB_alternateIdType = rs1.getString(1);
								DB_alternateIdidentifier = rs1.getString(2);
							 }
							stmt1.close();
							rs1.close();
							if(DB_alternateIdType.equals(WS_Type) && DB_alternateIdidentifier.equals(WS_alternateIdidentifier) )
							{
								
								CompareStatus=true;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
							}
							else
							{
								CompareStatus=false;
								logger.info("Database and WebServices Details for DOM Code: " +Code);
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdType +" Actual WSValue:" +WS_Type, ExtentColor.BLUE)); 
								logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DB_alternateIdidentifier +" Actual WSValue:" +WS_alternateIdidentifier, ExtentColor.BLUE));
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
				try{
				logfile.info("Executing Test Case: " +testCaseName);
				testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
				passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
				failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
				logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
				connectDatabase();
				String CdQuery = "select o.ENTERPRISE_PRODUCT_ID as OFFERING_ID,om.ALTERNATE_DESC as DOM_CD from OFFERING o, OFFERING_MAPPING om"
									+ " where o.OFFERING_TYPE_CD='BPT' and o.LFCL_STATUS_CD='AC' "
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
				 Boolean StatusFlg=true;
				 Boolean CompareStatus=true;
				 for(int i=0;i<dbCodes.size();i++)
				 {
					 String Code = dbCodes.get(i).toString();
					 String targetDate = DataObject.getVariable("targetDate", testCaseName);
					 Response res = given()
					            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
					            .param("domid", Code).and().param("targetdt", targetDate)
					            .when()
					            .get("v1/offerings/basepackagetypes/domtooffering")
					            .then()
					            .extract().response();
					 
					 String responsestr=res.asString();  
					 JsonPath js = new JsonPath(responsestr);
					 String OfferingID = js.getString("offerings.offeringId");
					 String WS_OfferingID=OfferingID.substring(1, OfferingID.length()-1);
					 int WS_namesCount = js.get("offerings[0].names.size()");
					 for(int j=0;j<WS_namesCount;j++)
						{
							String WSnamestype = js.getString("offerings[0].names["+j+"].type");
							String WSnamessubType = js.getString("offerings[0].names["+j+"].subType");
							String WSnamesencoding = js.getString("offerings[0].names["+j+"].encoding");
							String WSnameslanguageId = js.getString("offerings[0].names["+j+"].languageId");
							String WSnamescountryCd=null;
							WSnamescountryCd=js.getString("offerings[0].names["+j+"].countryCd");
							if(WSnamestype.equals("translated"))
							{
								int count = WSnameslanguageId.length();
								WSnamescountryCd = WSnameslanguageId.substring(count-2, count);
							}
							String WSnamesvalue=js.getString("offerings[0].names["+j+"].value");
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
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								else
								{
									if(DBnamesvalue.equals(WSnamesvalue))
									{
										CompareStatus=true;
										//System.out.println("Passed For: " +j);
									}
									else
									{
										CompareStatus=false;
										//System.out.println("Passed For: " +j);
									}
								}
								
							}
							else
							{
								CompareStatus=false;
								//System.out.println("Passed For: " +j);
							}
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBlanguageId +" Actual WSValue:" +WSLanguageId, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBcountryCd +" Actual WSValue:" +WSnamescountryCd, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE)); 
							
							}
						else
							{
								String Query="SELECT NAME_TYPE_CD, NAME_SUBTYPE_CD, ENCODING_CD, LANGUAGE_CD, COUNTRY_CD, OFFERING_NAME_DESC"
									      + " FROM OFFERING_NAME WHERE LFCL_STATUS_CD = 'AC' AND" 
									      + " OFFERING_ID='"+WS_OfferingID+"' AND NAME_TYPE_CD='"+WSnamestype+"' AND"
									      + " NAME_SUBTYPE_CD='"+WSnamessubType+"' AND ENCODING_CD='"+WSnamesencoding+"'";
								Statement stmt2 = con.createStatement();
								ResultSet rs2 = stmt2.executeQuery(Query);
							while (rs2.next()) 
							 {
								 DBnamestype = rs2.getString(1);
								 DBnamessubType = rs2.getString(2);
								 DBnamesencoding = rs2.getString(3);
								 DBnamesvalue= rs2.getString(6);
							 }
							stmt2.close();
							rs2.close();
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
							
							logger.info("Database and WebServices Details for DOM Code: " +Code);
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamestype +" Actual WSValue:" +WSnamestype, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamessubType +" Actual WSValue:" +WSnamessubType, ExtentColor.BLUE));
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesencoding +" Actual WSValue:" +WSnamesencoding, ExtentColor.BLUE)); 
							logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +DBnamesvalue +" Actual WSValue:" +WSnamesvalue, ExtentColor.BLUE));
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
							logger.log(Status.PASS, MarkupHelper.createLabel(StatusMsg, ExtentColor.GREEN));
							StatusFlg=false;
						}
					 
				 }
				 
				 if(StatusFlg==true)
				 {
					 System.out.println("Passed Succesfully: " +testCaseName);
				 }
				 else
				 {
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
	
	
	
	
	
	
	
	
	
	@Test(priority = 54)
	public void TC_54() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_54";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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

	@Test(priority = 55)
	public void TC_55() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_55";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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

	@Test(priority = 56)
	public void TC_56() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_56";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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

	@Test(priority = 57)
	public void TC_57() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_57";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 58)
	public void TC_58() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_58";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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

	
	@Test(priority = 59)
	public void TC_59() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_59";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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

	@Test(priority = 60)
	public void TC_60() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_60";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
	
	@Test(priority = 61)
	public void TC_61() throws Exception

	{
	   Boolean StatusFlg=true;
	   String testCaseDesc = null;
	   String passTestCaseDesc = null;
	   String failTestCaseDesc = null;
	   String testCaseName = "TC_61";
	   String targetDate = DataObject.getVariable("targetDate", testCaseName);
	   String expectedStatus =  DataObject.getVariable("status", testCaseName);
	   String expectedCode =  DataObject.getVariable("code", testCaseName);
	   String expectedMessage =  DataObject.getVariable("message", testCaseName);
	   String expectedResultCount =  DataObject.getVariable("resultCount", testCaseName);
	       boolean isRunnableTest = Backend.checkRunStatus(testCaseName,UserStoryName);
	       if (isRunnableTest)
	       {
	    	   try{
	          testCaseDesc=DataObject.getVariable("TestCaseDescription", testCaseName);
	          passTestCaseDesc=DataObject.getVariable("TestPass", testCaseName);
	          failTestCaseDesc=DataObject.getVariable("TestFail", testCaseName);
	          logger = extent.createTest("Test:" +testCaseName +": " +testCaseDesc);
	            Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .param("domid", "XXXX").and().param("targetdt", targetDate)
	            .when()
	            .get("v1/offerings/basepackagetypes/domtooffering")
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
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                }
	           else
	                {
	                     StatusFlg=false;
	                     logger.log(Status.FAIL, MarkupHelper.createLabel(failTestCaseDesc, ExtentColor.RED));
	                     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedStatus +" Actual WSValue:" +Wsstatus, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedMessage +" Actual WSValue:" +Wsmessage, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedCode +" Actual WSValue:" +Wscode, ExtentColor.BLUE));
	            	     logger.log(Status.INFO, MarkupHelper.createLabel("Expected Value:" +expectedResultCount +" Actual WSValue:" +WsCount, ExtentColor.BLUE));
	                     
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
