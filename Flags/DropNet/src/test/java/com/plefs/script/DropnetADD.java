package com.plefs.script;

import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.crypto.Data;

import org.apache.log4j.Logger;
import org.omg.CORBA.INITIALIZE;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.plefs.backend.BaseClass;
import com.plefs.backend.CommonMethods;
import com.plefs.backend.CurrentDateTime;
import com.plefs.backend.DataObject;
import com.plefs.backend.HTMLReportGenerator;
import com.plefs.backend.ObjectRepository;
import com.plefs.backend.UserStory;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;



public class DropnetADD  extends BaseClass
{

	String file_name;
	boolean isRunnableTest; //Global Variable
	List <WebElement> allRowsElements; //Global Variable to store the Elements
	ExtentTest logger;	  //Object for Logs like (pass,fail skip)
	Logger Log = Logger.getLogger("devpinoyLogger"); //Log4j logging
	ExtentReports report = new ExtentReports("D:/TestNG_Sample/rep.html"); //Object For generating report
	
	
	@BeforeSuite
	public void reportinit() throws IOException, InterruptedException{
		String ReportPath = CommonMethods.Call_config("ReportPath");
	    String currentdatetime = CurrentDateTime.currentdtime();		
		HTMLReportGenerator.TestSuiteStart(ReportPath+"\\"+"TestResult_Regression_"+currentdatetime+".html", "OffshoreFlags");						
		/*DropnetADD browser = new DropnetADD();
		browser.intilaize();*/
	}
	
	
	@AfterSuite
	public void reportclose() {
	
		try {
			HTMLReportGenerator.TestSuiteEnd();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Parameters({"US_name"})
	@BeforeTest
	public  void main(String US_name) throws IOException, InterruptedException
	{	
		UserStory.setUS_name(US_name);
		CommonMethods.Delete_DefaultSuite();
		intilaize();
		obj_common_methods.Login();
		System.out.println("started execution" + US_name);
	}
	
	@AfterTest
	public void exit() throws IOException
	{
		driver.close();
		System.out.println("Logged Off From Application");
		//report.endTest(logger);
		//report.flush();
	}
	
	
	   @Test(priority=1)
	public void TC_01() throws Exception
	
		{
		try{
		Actions act = new Actions(driver);
		System.out.println("Start of Test Case 01");
		boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_01");
		System.out.println("Run STatus is" + isRunnableTest);
		
	if(isRunnableTest)
		{
		 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_01");
		 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
		// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
		 driver.switchTo().frame("upper");
		 Thread.sleep(2000);
		 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
		 Thread.sleep(4000);
		 driver.findElement(ObjectRepository.by_ChannelCd).click();
		 Thread.sleep(2000);
		 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
		 Thread.sleep(4000);
		 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_01"));
		 Thread.sleep(2000);
		 driver.findElement(ObjectRepository.by_AddBttn).click();
		 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
		 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
		 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
		 Thread.sleep(1000);
		 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_01"));
		 Thread.sleep(1000);
		 String Street = DataObject.getVariable("Street", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
		 String Suite = DataObject.getVariable("Suite", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
		 Thread.sleep(1000);
		 String Room = DataObject.getVariable("Room_Floor", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
		 Thread.sleep(1000);
		 String city =  DataObject.getVariable("City_Name", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
		 Thread.sleep(1000);
		 String State = DataObject.getVariable("State", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
		 Thread.sleep(1000);
		 String Postal_code = DataObject.getVariable("Postal", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
		 Thread.sleep(1000);
		 String Country = DataObject.getVariable("Country_Code", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
		 Thread.sleep(1000);
		 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
		 Thread.sleep(1000);
		 String Company_name = DataObject.getVariable("Company_Name", "TC_01");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
		 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_01");
		 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
		 String BuildingStreet = DataObject.getVariable("Street_2", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
		 String BuildingSuite = DataObject.getVariable("Suite", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
		 String Building_postal = DataObject.getVariable("Postal_2", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
		 String City_name= DataObject.getVariable("CityNm", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
		 String Bldg_State = DataObject.getVariable("State_2", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
		 String email = DataObject.getVariable("Company_Email_Address", "TC_01");
		 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
		 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_01");
		 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
		 String Contct_number = DataObject.getVariable("Phone_number", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
		 String fax_number = DataObject.getVariable("FAX_Number", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
		 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_01");
		 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
		 String building_contact_num = DataObject.getVariable("Phone_number", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
		 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
		 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_01"));
		 driver.findElement(ObjectRepository.by_VerifyAddress).click();
		// String parentwindow = driver.getWindowHandle();
		 
		 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
		 
		 /*for(String Windowhandle  : allWindowhandles)
		 {
			if (!Windowhandle.equals(parentwindow)) 
			{*/
				driver.switchTo().window(allWindowhandles.get(1));
				driver.findElement(ObjectRepository.by_ValidateAddress).click();
				//driver.switchTo().alert().accept();
				driver.findElement(ObjectRepository.by_Accept).click();
		/*	}*/
			//driver.close();
			driver.switchTo().window(allWindowhandles.get(0));
			driver.switchTo().frame("upper");
			
		 
		/* JavascriptExecutor je = (JavascriptExecutor) driver;
		 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
		 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
		 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
		}
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	
	
		}

	    @Test(priority=2)
		public void TC_02() throws Exception
		
		{
		try{
		Actions act = new Actions(driver);
		System.out.println("Start of Test Case 02");
		boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_02");
		System.out.println("Run STatus is" + isRunnableTest);
		
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_02");
			 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_02"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_02"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_01");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_02"));
			 String Payee_co_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by__Payee_Company_nm).sendKeys(Payee_co_name);
			 Select min_code = new Select(driver.findElement(ObjectRepository.by__Payee_minorityCdPayee));
			 min_code.selectByVisibleText(DataObject.getVariable("Minority _Code", "TC_02"));
			 Select Vendor_setup =  new Select(driver.findElement(ObjectRepository.by__payee_vendorSetup));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			 String Street2 = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_StreetValue).sendKeys(Street2);
			 String Suite_payee = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite_payee);
			 String city2 =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city2);
			 String State2 = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State2);
			 String Postal_code2 = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code2);
			 String Country_payee = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country_payee);
			 String Vendr_no = DataObject.getVariable("Vendor_num", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_vendorNbr).sendKeys(Vendr_no);
			 String fee_amt = DataObject.getVariable("Fee_amount", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_FeeAmount).sendKeys(fee_amt);
			 String taxidpayee = DataObject.getVariable("Tax_id", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxIDAppValue).sendKeys(taxidpayee);
			 String taxidpayeepercent = DataObject.getVariable("Tax_id%", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxPerAppValue).sendKeys(taxidpayeepercent);
			 String Escalation = DataObject.getVariable("Escalation", "TC_02") ;
			 driver.findElement(ObjectRepository.by__payee_escalationPct).sendKeys(Escalation);
			 String Contct_number_payee = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_PhoneNbr).sendKeys(Contct_number_payee);
			 String fax_number_payee = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_payeeFaxNbr).sendKeys(fax_number_payee);
			 String Comments = DataObject.getVariable("Comments", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_CommentsAppValue).sendKeys(Comments);
			 String Contract_exp = DataObject.getVariable("Expirationdt", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_contractExpirationDt).sendKeys(Contract_exp);
			 
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Accept).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			}


	    @Test(priority=3)
		public void TC_03() throws Exception
		
		{
		try{
		Actions act = new Actions(driver);
		System.out.println("Start of Test Case 02");
		boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_02");
		System.out.println("Run STatus is" + isRunnableTest);
		
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_02");
			 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_02"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_02"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_01");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_02"));
			 String Payee_co_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by__Payee_Company_nm).sendKeys(Payee_co_name);
			 Select min_code = new Select(driver.findElement(ObjectRepository.by__Payee_minorityCdPayee));
			 min_code.selectByVisibleText(DataObject.getVariable("Minority _Code", "TC_02"));
			 Select Vendor_setup =  new Select(driver.findElement(ObjectRepository.by__payee_vendorSetup));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			 String Street2 = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_StreetValue).sendKeys(Street2);
			 String Suite_payee = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite_payee);
			 String city2 =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city2);
			 String State2 = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State2);
			 String Postal_code2 = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code2);
			 String Country_payee = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country_payee);
			 String Vendr_no = DataObject.getVariable("Vendor_num", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_vendorNbr).sendKeys(Vendr_no);
			 String fee_amt = DataObject.getVariable("Fee_amount", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_FeeAmount).sendKeys(fee_amt);
			 String taxidpayee = DataObject.getVariable("Tax_id", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxIDAppValue).sendKeys(taxidpayee);
			 String taxidpayeepercent = DataObject.getVariable("Tax_id%", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxPerAppValue).sendKeys(taxidpayeepercent);
			 String Escalation = DataObject.getVariable("Escalation", "TC_02") ;
			 driver.findElement(ObjectRepository.by__payee_escalationPct).sendKeys(Escalation);
			 String Contct_number_payee = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_PhoneNbr).sendKeys(Contct_number_payee);
			 String fax_number_payee = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_payeeFaxNbr).sendKeys(fax_number_payee);
			 String Comments = DataObject.getVariable("Comments", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_CommentsAppValue).sendKeys(Comments);
			 String Contract_exp = DataObject.getVariable("Expirationdt", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_contractExpirationDt).sendKeys(Contract_exp);
			 
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Accept).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			}

	    @Test(priority=4)
		public void TC_04() throws Exception
		
		{
		try{
		Actions act = new Actions(driver);
		System.out.println("Start of Test Case 02");
		boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_02");
		System.out.println("Run STatus is" + isRunnableTest);
		
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_02");
			 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_02"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_02"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_01");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_02"));
			 String Payee_co_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by__Payee_Company_nm).sendKeys(Payee_co_name);
			 Select min_code = new Select(driver.findElement(ObjectRepository.by__Payee_minorityCdPayee));
			 min_code.selectByVisibleText(DataObject.getVariable("Minority _Code", "TC_02"));
			 Select Vendor_setup =  new Select(driver.findElement(ObjectRepository.by__payee_vendorSetup));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			 String Street2 = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_StreetValue).sendKeys(Street2);
			 String Suite_payee = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite_payee);
			 String city2 =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city2);
			 String State2 = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State2);
			 String Postal_code2 = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code2);
			 String Country_payee = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country_payee);
			 String Vendr_no = DataObject.getVariable("Vendor_num", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_vendorNbr).sendKeys(Vendr_no);
			 String fee_amt = DataObject.getVariable("Fee_amount", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_FeeAmount).sendKeys(fee_amt);
			 String taxidpayee = DataObject.getVariable("Tax_id", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxIDAppValue).sendKeys(taxidpayee);
			 String taxidpayeepercent = DataObject.getVariable("Tax_id%", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxPerAppValue).sendKeys(taxidpayeepercent);
			 String Escalation = DataObject.getVariable("Escalation", "TC_02") ;
			 driver.findElement(ObjectRepository.by__payee_escalationPct).sendKeys(Escalation);
			 String Contct_number_payee = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_PhoneNbr).sendKeys(Contct_number_payee);
			 String fax_number_payee = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_payeeFaxNbr).sendKeys(fax_number_payee);
			 String Comments = DataObject.getVariable("Comments", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_CommentsAppValue).sendKeys(Comments);
			 String Contract_exp = DataObject.getVariable("Expirationdt", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_contractExpirationDt).sendKeys(Contract_exp);
			 
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Accept).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			}
	    @Test(priority=5)
		public void TC_05() throws Exception
		
		{
		try{
		Actions act = new Actions(driver);
		System.out.println("Start of Test Case 05");
		boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_05");
		System.out.println("Run STatus is" + isRunnableTest);
		
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_02");
			 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_02"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_02"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_01");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_02"));
			 String Payee_co_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by__Payee_Company_nm).sendKeys(Payee_co_name);
			 Select min_code = new Select(driver.findElement(ObjectRepository.by__Payee_minorityCdPayee));
			 min_code.selectByVisibleText(DataObject.getVariable("Minority _Code", "TC_02"));
			 Select Vendor_setup =  new Select(driver.findElement(ObjectRepository.by__payee_vendorSetup));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			 String Street2 = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_StreetValue).sendKeys(Street2);
			 String Suite_payee = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite_payee);
			 String city2 =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city2);
			 String State2 = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State2);
			 String Postal_code2 = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code2);
			 String Country_payee = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country_payee);
			 /*String Vendr_no = DataObject.getVariable("Vendor_num", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_vendorNbr).sendKeys(Vendr_no);
			 String fee_amt = DataObject.getVariable("Fee_amount", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_FeeAmount).sendKeys(fee_amt);
			 String taxidpayee = DataObject.getVariable("Tax_id", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxIDAppValue).sendKeys(taxidpayee);
			 String taxidpayeepercent = DataObject.getVariable("Tax_id%", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxPerAppValue).sendKeys(taxidpayeepercent);
			 String Escalation = DataObject.getVariable("Escalation", "TC_02") ;
			 driver.findElement(ObjectRepository.by__payee_escalationPct).sendKeys(Escalation);
			 String Contct_number_payee = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_PhoneNbr).sendKeys(Contct_number_payee);
			 String fax_number_payee = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_payeeFaxNbr).sendKeys(fax_number_payee);
			 String Comments = DataObject.getVariable("Comments", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_CommentsAppValue).sendKeys(Comments);
			 String Contract_exp = DataObject.getVariable("Expirationdt", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_contractExpirationDt).sendKeys(Contract_exp);
			 */
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Accept).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			}
	    @Test(priority=6)
	public void TC_06() throws Exception
		
		{
		try{
		Actions act = new Actions(driver);
		System.out.println("Start of Test Case 02");
		boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_02");
		System.out.println("Run STatus is" + isRunnableTest);
		
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_02");
			 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_02"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_02"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_01");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_02"));
			 String Payee_co_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
			 driver.findElement(ObjectRepository.by__Payee_Company_nm).sendKeys(Payee_co_name);
			 Select min_code = new Select(driver.findElement(ObjectRepository.by__Payee_minorityCdPayee));
			 min_code.selectByVisibleText(DataObject.getVariable("Minority _Code", "TC_02"));
			 Select Vendor_setup =  new Select(driver.findElement(ObjectRepository.by__payee_vendorSetup));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			 String Street2 = DataObject.getVariable("Street_2", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_StreetValue).sendKeys(Street2);
			 String Suite_payee = DataObject.getVariable("Suite", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite_payee);
			 String city2 =  DataObject.getVariable("City_Name", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city2);
			 String State2 = DataObject.getVariable("State", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State2);
			 String Postal_code2 = DataObject.getVariable("Postal", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code2);
			 String Country_payee = DataObject.getVariable("Country_Code", "TC_02");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country_payee);
			 String Vendr_no = DataObject.getVariable("Vendor_num", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_vendorNbr).sendKeys(Vendr_no);
			 String fee_amt = DataObject.getVariable("Fee_amount", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_FeeAmount).sendKeys(fee_amt);
			 String taxidpayee = DataObject.getVariable("Tax_id", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxIDAppValue).sendKeys(taxidpayee);
			 String taxidpayeepercent = DataObject.getVariable("Tax_id%", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_TaxPerAppValue).sendKeys(taxidpayeepercent);
			 String Escalation = DataObject.getVariable("Escalation", "TC_02") ;
			 driver.findElement(ObjectRepository.by__payee_escalationPct).sendKeys(Escalation);
			 String Contct_number_payee = DataObject.getVariable("Phone_number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_PhoneNbr).sendKeys(Contct_number_payee);
			 String fax_number_payee = DataObject.getVariable("FAX_Number", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_payeeFaxNbr).sendKeys(fax_number_payee);
			 String Comments = DataObject.getVariable("Comments", "TC_02");
			 driver.findElement(ObjectRepository.by_Payee_CommentsAppValue).sendKeys(Comments);
			 String Contract_exp = DataObject.getVariable("Expirationdt", "TC_02");
			 driver.findElement(ObjectRepository.by__payee_contractExpirationDt).sendKeys(Contract_exp);
			 
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Accept).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			}
	    @Test(priority=9)
	public void TC_09() throws Exception
	
	{
	try{
	Actions act = new Actions(driver);
	System.out.println("Start of Test Case 02");
	boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_02");
	System.out.println("Run STatus is" + isRunnableTest);
	
	if(isRunnableTest)
		{
		 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_02");
		 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
		// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
		 driver.switchTo().frame("upper");
		 Thread.sleep(2000);
		 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
		 Thread.sleep(4000);
		 driver.findElement(ObjectRepository.by_ChannelCd).click();
		 Thread.sleep(2000);
		 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
		 Thread.sleep(4000);
		 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_02"));
		 Thread.sleep(2000);
		 driver.findElement(ObjectRepository.by_AddBttn).click();
		 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
		 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
		 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
		 Thread.sleep(1000);
		 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_02"));
		 Thread.sleep(1000);
		 String Street = DataObject.getVariable("Street", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
		 String Suite = DataObject.getVariable("Suite", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
		 Thread.sleep(1000);
		 String Room = DataObject.getVariable("Room_Floor", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
		 Thread.sleep(1000);
		 String city =  DataObject.getVariable("City_Name", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
		 Thread.sleep(1000);
		 String State = DataObject.getVariable("State", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
		 Thread.sleep(1000);
		 String Postal_code = DataObject.getVariable("Postal", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
		 Thread.sleep(1000);
		 String Country = DataObject.getVariable("Country_Code", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
		 Thread.sleep(1000);
		 String Email_location = DataObject.getVariable("Email_address", "TC_09");
		 driver.findElement(ObjectRepository.by_ContactEmailId).sendKeys(Email_location);
		 String Contct_number = DataObject.getVariable("Phone_number", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
		 String placement_no = DataObject.getVariable("Placement_Number", "TC_09");
		 String fax_number = DataObject.getVariable("FAX_Number", "TC_09");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
		 String Manager_nm = DataObject.getVariable("Manager_name", "TC_09");
		 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(Manager_nm);
		 String Store_no  = DataObject.getVariable("Store_number", "TC_09");
		 driver.findElement(ObjectRepository.by_Store_number).sendKeys(Store_no);
		 String loc_name = DataObject.getVariable("Location_Name", "TC_09");
		 driver.findElement(ObjectRepository.by_loc_name).sendKeys(loc_name);
		 String Company_name = DataObject.getVariable("Company_Name", "TC_09");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name); 
		 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_09");
		 driver.findElement(ObjectRepository.by_bldg_nm).sendKeys(buiding_contactnm);
		 String loc_city = DataObject.getVariable("CityNm", "TC_09");
		 driver.findElement(ObjectRepository.by_loc_city).sendKeys(loc_city);
		 String bar_code = DataObject.getVariable("Bar_Code", "TC_09");
		 driver.findElement(ObjectRepository.by_bar_cd).sendKeys(bar_code);
		 String Loc_prop = DataObject.getVariable("Loc_propert", "TC_09");
		 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_prop);
		 String Crossstreet = DataObject.getVariable("Cross_street", "TC_09");
		 driver.findElement(ObjectRepository.by_cross_street).sendKeys(Crossstreet);
		 String Serial = DataObject.getVariable("Serial_number", "TC_09");
		 driver.findElement(ObjectRepository.by_cross_street).sendKeys(Serial);
		 String mon_open = DataObject.getVariable("monopen", "TC_09");
		 driver.findElement(ObjectRepository.by_mon_open).sendKeys(mon_open);
		 String tue_open = DataObject.getVariable("tueopen", "TC_09");
		 driver.findElement(ObjectRepository.by_tue_open).sendKeys(tue_open);
		 String wed_open = DataObject.getVariable("wedopen", "TC_09");
		 driver.findElement(ObjectRepository.by_wed_open).sendKeys(wed_open);
		 String thu_open = DataObject.getVariable("thuopen", "TC_09");
		 driver.findElement(ObjectRepository.by_thu_open).sendKeys(thu_open);
		 String fri_open = DataObject.getVariable("friopen", "TC_09");
		 driver.findElement(ObjectRepository.by_fri_open).sendKeys(fri_open);
		 String sat_open = DataObject.getVariable("satopen", "TC_09");
		 driver.findElement(ObjectRepository.by_sat_open).sendKeys(sat_open);
		 String sun_open = DataObject.getVariable("sunopen", "TC_09");
		 driver.findElement(ObjectRepository.by_sun_open).sendKeys(sun_open);
		 driver.findElement(ObjectRepository.by_cross_street).sendKeys(Serial);
		 String mon_close = DataObject.getVariable("monclose", "TC_09");
		 driver.findElement(ObjectRepository.by_mon_clos).sendKeys(mon_close);
		 String tue_close = DataObject.getVariable("tueclose", "TC_09");
		 driver.findElement(ObjectRepository.by_tue_clos).sendKeys(tue_close);
		 String wed_close = DataObject.getVariable("wedclose", "TC_09");
		 driver.findElement(ObjectRepository.by_wed_clos).sendKeys(wed_close);
		 String thu_close = DataObject.getVariable("thuclose", "TC_09");
		 driver.findElement(ObjectRepository.by_thu_clos).sendKeys(thu_close);
		 String fri_close = DataObject.getVariable("friclose", "TC_09");
		 driver.findElement(ObjectRepository.by_fri_clos).sendKeys(fri_close);
		 String sat_close = DataObject.getVariable("satclose", "TC_09");
		 driver.findElement(ObjectRepository.by_sat_clos).sendKeys(sat_close);
		 String sun_close = DataObject.getVariable("sunclose", "TC_09");
		 driver.findElement(ObjectRepository.by_sun_clos).sendKeys(sun_close);
		 Thread.sleep(2000);
		 driver.findElement(ObjectRepository.by_addrecords).click();
		 
		 
		
		 // String parentwindow = driver.getWindowHandle();
		 
		 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
		 
		 /*for(String Windowhandle  : allWindowhandles)
		 {
			if (!Windowhandle.equals(parentwindow)) 
			{*/
				driver.switchTo().window(allWindowhandles.get(1));
				driver.findElement(ObjectRepository.by_ValidateAddress).click();
				//driver.switchTo().alert().accept();
				driver.findElement(ObjectRepository.by_Accept).click();
		/*	}*/
			//driver.close();
			driver.switchTo().window(allWindowhandles.get(0));
			driver.switchTo().frame("upper");
			
		 
		/* JavascriptExecutor je = (JavascriptExecutor) driver;
		 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
		 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
		 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
		}
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	
	
		}
	    @Test(priority=22)
	public void TC_22() throws Exception
	
	{
	try{
	Actions act = new Actions(driver);
	System.out.println("Start of Test Case 02");
	boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_02");
	System.out.println("Run STatus is" + isRunnableTest);
	
	if(isRunnableTest)
		{
		 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_02");
		 HTMLReportGenerator.TestCaseStart("TC_01", "Dropnet_ADD");
		// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
		 driver.switchTo().frame("upper");
		 Thread.sleep(2000);
		 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
		 Thread.sleep(4000);
		 driver.findElement(ObjectRepository.by_ChannelCd).click();
		 Thread.sleep(2000);
		 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
		 Thread.sleep(4000);
		 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_02"));
		 Thread.sleep(2000);
		 driver.findElement(ObjectRepository.by_AddBttn).click();
		 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
		 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
		 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
		 Thread.sleep(1000);
		 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_02"));
		 Thread.sleep(1000);
		 String Street = DataObject.getVariable("Street", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
		 String Suite = DataObject.getVariable("Suite", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
		 Thread.sleep(1000);
		 String Room = DataObject.getVariable("Room_Floor", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
		 Thread.sleep(1000);
		 String city =  DataObject.getVariable("City_Name", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
		 Thread.sleep(1000);
		 String State = DataObject.getVariable("State", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
		 Thread.sleep(1000);
		 String Postal_code = DataObject.getVariable("Postal", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
		 Thread.sleep(1000);
		 String Country = DataObject.getVariable("Country_Code", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
		 Thread.sleep(1000);
		 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
		 Thread.sleep(1000);
		 String Company_name = DataObject.getVariable("Company_Name", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
		 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_02");
		 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
		 String BuildingStreet = DataObject.getVariable("Street_2", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
		 String BuildingSuite = DataObject.getVariable("Suite", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
		 String Building_postal = DataObject.getVariable("Postal_2", "TC_01");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
		 String City_name= DataObject.getVariable("CityNm", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
		 String Bldg_State = DataObject.getVariable("State_2", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
		 String email = DataObject.getVariable("Company_Email_Address", "TC_02");
		 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
		 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
		 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
		 String Contct_number = DataObject.getVariable("Phone_number", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
		 String fax_number = DataObject.getVariable("FAX_Number", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
		 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_02");
		 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
		 String building_contact_num = DataObject.getVariable("Phone_number", "TC_02");
		 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
		 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
		 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_02"));
		 String Payee_co_name = DataObject.getVariable("Company_Contact_Name", "TC_02");
		 driver.findElement(ObjectRepository.by__Payee_Company_nm).sendKeys(Payee_co_name);
		 Select min_code = new Select(driver.findElement(ObjectRepository.by__Payee_minorityCdPayee));
		 min_code.selectByVisibleText(DataObject.getVariable("Minority _Code", "TC_02"));
		 Select Vendor_setup =  new Select(driver.findElement(ObjectRepository.by__payee_vendorSetup));
		 driver.findElement(ObjectRepository.by_VerifyAddress).click();
		 String Street2 = DataObject.getVariable("Street_2", "TC_02");
		 driver.findElement(ObjectRepository.by_Payee_StreetValue).sendKeys(Street2);
		 String Suite_payee = DataObject.getVariable("Suite", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite_payee);
		 String city2 =  DataObject.getVariable("City_Name", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city2);
		 String State2 = DataObject.getVariable("State", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State2);
		 String Postal_code2 = DataObject.getVariable("Postal", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code2);
		 String Country_payee = DataObject.getVariable("Country_Code", "TC_02");
		 Thread.sleep(1000);
		 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country_payee);
		 String Vendr_no = DataObject.getVariable("Vendor_num", "TC_02");
		 driver.findElement(ObjectRepository.by__payee_vendorNbr).sendKeys(Vendr_no);
		 String fee_amt = DataObject.getVariable("Fee_amount", "TC_02");
		 driver.findElement(ObjectRepository.by_Payee_FeeAmount).sendKeys(fee_amt);
		 String taxidpayee = DataObject.getVariable("Tax_id", "TC_02");
		 driver.findElement(ObjectRepository.by_Payee_TaxIDAppValue).sendKeys(taxidpayee);
		 String taxidpayeepercent = DataObject.getVariable("Tax_id%", "TC_02");
		 driver.findElement(ObjectRepository.by_Payee_TaxPerAppValue).sendKeys(taxidpayeepercent);
		 String Escalation = DataObject.getVariable("Escalation", "TC_02") ;
		 driver.findElement(ObjectRepository.by__payee_escalationPct).sendKeys(Escalation);
		 String Contct_number_payee = DataObject.getVariable("Phone_number", "TC_02");
		 driver.findElement(ObjectRepository.by__payee_PhoneNbr).sendKeys(Contct_number_payee);
		 String fax_number_payee = DataObject.getVariable("FAX_Number", "TC_02");
		 driver.findElement(ObjectRepository.by__payee_payeeFaxNbr).sendKeys(fax_number_payee);
		 String Comments = DataObject.getVariable("Comments", "TC_02");
		 driver.findElement(ObjectRepository.by_Payee_CommentsAppValue).sendKeys(Comments);
		 String Contract_exp = DataObject.getVariable("Expirationdt", "TC_02");
		 driver.findElement(ObjectRepository.by__payee_contractExpirationDt).sendKeys(Contract_exp);
		 
		// String parentwindow = driver.getWindowHandle();
		 
		 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
		 
		 /*for(String Windowhandle  : allWindowhandles)
		 {
			if (!Windowhandle.equals(parentwindow)) 
			{*/
				driver.switchTo().window(allWindowhandles.get(1));
				driver.findElement(ObjectRepository.by_ValidateAddress).click();
				//driver.switchTo().alert().accept();
				driver.findElement(ObjectRepository.by_Accept).click();
		/*	}*/
			//driver.close();
			driver.switchTo().window(allWindowhandles.get(0));
			driver.switchTo().frame("upper");
			
		 
		/* JavascriptExecutor je = (JavascriptExecutor) driver;
		 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
		 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
		 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
		}
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
		}
	    
	    @Test(priority=25)
		public void TC_25() throws Exception
		
			{
			try{
			Actions act = new Actions(driver);
			System.out.println("Start of Test Case 25");
			boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_25");
			System.out.println("Run STatus is" + isRunnableTest);
			
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_25");
			 HTMLReportGenerator.TestCaseStart("TC_25", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_25"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_25"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_25");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_25");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_25");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_25");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_25");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_25");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_25"));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Accept).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			}


	    @Test(priority=26)
		public void TC_26() throws Exception
		
			{
			try{
			Actions act = new Actions(driver);
			System.out.println("Start of Test Case 26");
			boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_26");
			System.out.println("Run STatus is" + isRunnableTest);
			
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_26");
			 HTMLReportGenerator.TestCaseStart("TC_26", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_26"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_26"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_26");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_26");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_26");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_26");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_26");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_26");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_26"));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Accept).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}

			}
	    @Test(priority=28)
		public void TC_28() throws Exception
		
			{
			try{
			Actions act = new Actions(driver);
			System.out.println("Start of Test Case 28");
			boolean isRunnableTest = obj_common_methods.CheckRunStatus("TC_28");
			System.out.println("Run STatus is" + isRunnableTest);
			
		if(isRunnableTest)
			{
			 file_name=CommonMethods.make_direcory(CommonMethods.Call_config("Add") +  "_" + "TC_28");
			 HTMLReportGenerator.TestCaseStart("TC_28", "Dropnet_ADD");
			// obj_common_methods.waitForElement(driver, ObjectRepository.by_Dropnetpage, 30);
			 driver.switchTo().frame("upper");
			 Thread.sleep(2000);
			 act.moveToElement(driver.findElement(ObjectRepository.by_dropnetlink)).click().perform();
			 Thread.sleep(4000);
			 driver.findElement(ObjectRepository.by_ChannelCd).click();
			 Thread.sleep(2000);
			 Select Channel_cd =  new Select(driver.findElement(ObjectRepository.by_ChannelCd));
			 Thread.sleep(4000);
			 Channel_cd.selectByVisibleText(DataObject.getVariable("ChannelCd", "TC_28"));
			 Thread.sleep(2000);
			 driver.findElement(ObjectRepository.by_AddBttn).click();
			 obj_common_methods.waitForElement(driver, ObjectRepository.by_DropBox_AccessCdValue, 30);
			 driver.findElement(ObjectRepository.by_DropBox_AccessCdValue).click();
			 Select Access_typ_cd = new Select(driver.findElement(ObjectRepository.by_DropBox_AccessCdValue));
			 Thread.sleep(1000);
			 Access_typ_cd.selectByValue(DataObject.getVariable("Access Type Code", "TC_28"));
			 Thread.sleep(1000);
			 String Street = DataObject.getVariable("Street", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_DropBox_StreetValue).sendKeys(Street);
			 String Suite = DataObject.getVariable("Suite", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_SuiteValue).sendKeys(Suite);
			 Thread.sleep(1000);
			 String Room = DataObject.getVariable("Room_Floor", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_RoomValue).sendKeys(Room);
			 Thread.sleep(1000);
			 String city =  DataObject.getVariable("City_Name", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_CityValue).sendKeys(city);
			 Thread.sleep(1000);
			 String State = DataObject.getVariable("State", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_DropBox_StateValue).sendKeys(State);
			 Thread.sleep(1000);
			 String Postal_code = DataObject.getVariable("Postal", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_PostalValue).sendKeys(Postal_code);
			 Thread.sleep(1000);
			 String Country = DataObject.getVariable("Country_Code", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_CountryCdValue).sendKeys(Country);
			 Thread.sleep(1000);
			 String Loc_On_prop = DataObject.getVariable("Location_Name", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_Add_DropBox_LocOnPropertyValue).sendKeys(Loc_On_prop);
			 Thread.sleep(1000);
			 String Company_name = DataObject.getVariable("Company_Name", "TC_28");
			 Thread.sleep(1000);
			 driver.findElement(ObjectRepository.by_BuildingInfo_CompnyNmValue).sendKeys(Company_name);
			 String Agreement_date = DataObject.getVariable("Agreement_Date", "TC_28");
			 driver.findElement(ObjectRepository.by_BuildingInfo_AggrDateValue).sendKeys(Agreement_date);
			 String BuildingStreet = DataObject.getVariable("Street_2", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StreetValue).sendKeys(BuildingStreet);
			 String BuildingSuite = DataObject.getVariable("Suite", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_SuiteValue).sendKeys(BuildingSuite);
			 String Building_postal = DataObject.getVariable("Postal_2", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_PostalValue).sendKeys(Building_postal);
			 String City_name= DataObject.getVariable("CityNm", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CityValue).sendKeys(City_name);
			 String Bldg_State = DataObject.getVariable("State_2", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_StateValue).sendKeys(Bldg_State);
			 String email = DataObject.getVariable("Company_Email_Address", "TC_28");
			 driver.findElement(ObjectRepository.by_BuildingInfo_CmpnyEmailValue).sendKeys(email);
			 String co_contact_name = DataObject.getVariable("Company_Contact_Name", "TC_28");
			 driver.findElement(ObjectRepository.by_BuildingInfo_BuildngCntctNmValue).sendKeys(co_contact_name);
			 String Contct_number = DataObject.getVariable("Phone_number", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_BuildngCntctPhoneValue).sendKeys(Contct_number);
			 String fax_number = DataObject.getVariable("FAX_Number", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_FaxNoValue).sendKeys(fax_number);
			 String buiding_contactnm = DataObject.getVariable("Building_Contact_Name", "TC_28");
			 driver.findElement(ObjectRepository.by_ContactNm).sendKeys(buiding_contactnm);
			 String building_contact_num = DataObject.getVariable("Phone_number", "TC_28");
			 driver.findElement(ObjectRepository.by_Add_BuildingInfo_CmpnyCntctPhoneValue1).sendKeys(building_contact_num);
			 Select Fee_typ_cd = new Select(driver.findElement(ObjectRepository.by_Payee_FeeTypeCode));
			 Fee_typ_cd.selectByVisibleText(DataObject.getVariable("Fee_type_code", "TC_28"));
			 driver.findElement(ObjectRepository.by_VerifyAddress).click();
			// String parentwindow = driver.getWindowHandle();
			 
			 ArrayList<String> allWindowhandles = new ArrayList<String>( driver.getWindowHandles());
			 
			 /*for(String Windowhandle  : allWindowhandles)
			 {
				if (!Windowhandle.equals(parentwindow)) 
				{*/
					driver.switchTo().window(allWindowhandles.get(1));
					driver.findElement(ObjectRepository.by_ValidateAddress).click();
					//driver.switchTo().alert().accept();
					driver.findElement(ObjectRepository.by_Decline).click();
			/*	}*/
				//driver.close();
				driver.switchTo().window(allWindowhandles.get(0));
				driver.switchTo().frame("upper");
				
			 
			/* JavascriptExecutor je = (JavascriptExecutor) driver;
			 WebElement element = driver.findElement(ObjectRepository.by_AddBttn);
			 je.executeScript("arguments[0].scrollIntoView(true);",element);*/
			 act.moveToElement(driver.findElement(ObjectRepository.by_Add_Dropbox)).click().perform();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
			}



}
