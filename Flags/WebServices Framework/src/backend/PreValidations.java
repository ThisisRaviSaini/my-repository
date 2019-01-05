package backend;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import backend.Backend;
import backend.Resources;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PreValidations 
{
	final String UserStoryName="Conflicts";
	static String dbUrl;
	static String username;
	static String password;
	static Connection con;
	static Logger logfile = Logger.getLogger(PreValidations.class);
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

	@Test(enabled=false)
	public void TC_01() throws IOException    
	{
		ArrayList<String> StatusFlg= new ArrayList<String>();
		String FilePath = "D:\\PolicyGridWorkspace\\Demo\\TestDataSheet\\GPRProductMaster.xlsx";
		String SheetName = "Packaging";
		File inputWorkbook = new File(FilePath);
		FileInputStream fis;
		fis = new FileInputStream(inputWorkbook);
		XSSFWorkbook w = new XSSFWorkbook(fis);
		XSSFSheet sheet = w.getSheet(SheetName);
		int rowcount = sheet.getPhysicalNumberOfRows();
		ArrayList<String> POfferingID = new ArrayList<String>();
		for(int i=1;i<rowcount;i++)
		{
			XSSFRow row = sheet.getRow(i);
			String Value ;
			try
			{
				Value = row.getCell(0).getStringCellValue().trim();
			}
			catch(NullPointerException e)
			{
				Value = "null";
			}
			if(Value.startsWith("EP"))
			{
				POfferingID.add(Value);
			}
		}
		//System.out.println(POfferingID.size());
		ArrayList<String> PDescription = new ArrayList<String>();
		ArrayList<String> PDomAlternateIds = new ArrayList<String>();
		ArrayList<String> PEpicAlternateIds = new ArrayList<String>();
		ArrayList<String> POperatingOrgCds = new ArrayList<String>();
		ArrayList<String> PdomIntlCd = new ArrayList<String>();
		ArrayList<String> PapproxPkgVolCCm = new ArrayList<String>();
		ArrayList<String> PAbbreviation = new ArrayList<String>();
		ArrayList<String> PLongNameUTF = new ArrayList<String>();
		ArrayList<String> PLongNameASCII = new ArrayList<String>();
		ArrayList<String> PMediumNameUTF = new ArrayList<String>();
		ArrayList<String> PMediumNameASCII = new ArrayList<String>();
		ArrayList<String> PSmallNameUTF = new ArrayList<String>();
		ArrayList<String> PSmallNameASCII= new ArrayList<String>();
		ArrayList<String> PShortNameUTF = new ArrayList<String>();
		ArrayList<String> PShortNameASCII = new ArrayList<String>();
		for(int j=0;j<POfferingID.size();j++)
		{
			XSSFRow row = sheet.getRow(j+1);
			try
			{
				PDescription.add(row.getCell(1).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PDescription.add("null");
			}
			try
			{
				PDomAlternateIds.add(row.getCell(2).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PDomAlternateIds.add("null");
			}
			try
			{
				PEpicAlternateIds.add(row.getCell(4).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PEpicAlternateIds.add("null");
			}
			try
			{
				POperatingOrgCds.add(row.getCell(5).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				POperatingOrgCds.add("null");
			}
			try
			{
				PapproxPkgVolCCm.add(row.getCell(8).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PapproxPkgVolCCm.add("null");
			}
			catch(IllegalStateException e)
			{
				Double val = row.getCell(8).getNumericCellValue();
				String value = Double.toString(val).trim();
				PapproxPkgVolCCm.add(value);
			}
			try
			{
				PAbbreviation.add(row.getCell(9).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PAbbreviation.add("null");
			}
			catch(IllegalStateException e)
			{
				Double val = row.getCell(9).getNumericCellValue();
				String value = Double.toString(val).replace(".0", "").trim();
				PAbbreviation.add(value);
			}
			try
			{
				PLongNameUTF.add(row.getCell(10).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PLongNameUTF.add("null");
			}
			try
			{
				PLongNameASCII.add(row.getCell(11).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PLongNameASCII.add("null");
			}
			try
			{
				PMediumNameUTF.add(row.getCell(12).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PMediumNameUTF.add("null");
			}
			try
			{
				PMediumNameASCII.add(row.getCell(13).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PMediumNameASCII.add("null");
			}
			try
			{
				PSmallNameUTF.add(row.getCell(14).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PSmallNameUTF.add("null");
			}
			try
			{
				PSmallNameASCII.add(row.getCell(15).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PSmallNameASCII.add("null");
			}
			try
			{
				PShortNameUTF.add(row.getCell(16).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PShortNameUTF.add("null");
			}
			try
			{
				PShortNameASCII.add(row.getCell(17).getStringCellValue().trim());
			}
			catch(NullPointerException e)
			{
				PShortNameASCII.add("null");
			}
		}
	/*	System.out.println("POfferingID"+POfferingID);
		System.out.println("PDescription"+PDescription);
		System.out.println("PDomAlternateIds"+PDomAlternateIds);
		System.out.println("PEpicAlternateIds"+PEpicAlternateIds);
		System.out.println("POperatingOrgCds"+POperatingOrgCds);
		System.out.println("PapproxPkgVolCCm"+PapproxPkgVolCCm);
		System.out.println("PAbbreviation"+PAbbreviation);
		System.out.println("PLongNameUTF"+PLongNameUTF);
		System.out.println("PLongNameASCII"+PLongNameASCII);
		System.out.println("PMediumNameUTF"+PMediumNameUTF);
		System.out.println("PMediumNameASCII"+PMediumNameASCII);
		System.out.println("PSmallNameUTF"+PSmallNameUTF);
		System.out.println("PSmallNameASCII"+PSmallNameASCII);
		System.out.println("PShortNameUTF"+PShortNameUTF);
		System.out.println("PShortNameASCII"+PShortNameASCII);*/
		
		
		RestAssured.baseURI = "http://product-test.app.wtcdev2.paas.fedex.com";
		Response res = given()
	            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
	            .when()
	            .get(Resources.ResourcedataBPT())
	            .then()
	            .extract().response();
		 String responsestr=res.asString();  
		 JsonPath js = new JsonPath(responsestr);
		 int offeringcount =  js.get("offerings.size()");
		// System.out.println(offeringcount);
		 String WS_OfferingID = null;
		 String WS_description = null;
		 String WS_Epic = null;
		 String WS_Dom = null;
		 String WS_operatingOrgCds = null;
		 String WS_OpCoCd=null;
		 String WS_type= null;
		 String WS_subType= null;
		 String WS_encoding= null;
		 String WS_approxPkgVolCCm= null;
		 String WS_Abbreviation= null;
		 String WS_LongNameUTF = null;
		 String WS_LongNameASCII= null;
		 String WS_MediumNameUTF= null;
		 String WS_MediumNameASCII= null;
		 String WS_SmallNameUTF= null;
		 String WS_SmallNameASCII= null;
		 String WS_ShortNameUTF= null;
		 String WS_ShortNameASCII= null;
		 for(int k=0;k<offeringcount;k++)
		 {
			  WS_OfferingID=js.getString("offerings["+k+"].offeringId".trim());
			  WS_description=js.getString("offerings["+k+"].description".trim());
			 int alternateIdsCount =  js.get("offerings["+k+"].alternateIds.size()");
			 for(int l=0;l<alternateIdsCount;l++)
			 {
				 String CodeType =js.get("offerings["+k+"].alternateIds["+l+"].type".trim());
				 if(CodeType.equals("epic"))
				 {
					 WS_Epic=js.get("offerings["+k+"].alternateIds["+l+"].identifier".trim());
				 }
				 else if(CodeType.equals("dom"))
				 {
					 WS_Dom=js.get("offerings["+k+"].alternateIds["+l+"].identifier".trim());
				 }
			 }
			  WS_operatingOrgCds=js.getString("offerings["+k+"].operatingOrgCds".trim());
			 if(WS_operatingOrgCds.contains("FXE"))
			 {
				 WS_OpCoCd="Express";
			 }
			  WS_approxPkgVolCCm=js.getString("offerings["+k+"].approxPkgVolCCm".trim());
			 int namesCount =  js.get("offerings["+k+"].names.size()");
			
			 for(int r=0;r<namesCount;r++)
			 {
				 WS_type=js.get("offerings["+k+"].names["+r+"].type".trim());
				 WS_subType=js.get("offerings["+k+"].names["+r+"].subType".trim());
				 WS_encoding=js.get("offerings["+k+"].names["+r+"].encoding".trim());
				 if(WS_type.equals("default") && WS_subType.equals("abbrv") && WS_encoding.equals("ascii"))
				 {
					 WS_Abbreviation=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("long") && WS_encoding.equals("utf-8"))
				 {
					 WS_LongNameUTF=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("long") && WS_encoding.equals("ascii"))
				 {
					 WS_LongNameASCII=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("medium") && WS_encoding.equals("utf-8"))
				 {
					 WS_MediumNameUTF=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("medium") && WS_encoding.equals("ascii"))
				 {
					 WS_MediumNameASCII=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("small") && WS_encoding.equals("utf-8"))
				 {
					 WS_SmallNameUTF=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("small") && WS_encoding.equals("ascii"))
				 {
					 WS_SmallNameASCII=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("short") && WS_encoding.equals("utf-8"))
				 {
					 WS_ShortNameUTF=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
				 else if(WS_type.equals("default") && WS_subType.equals("short") && WS_encoding.equals("ascii"))
				 {
					 WS_ShortNameASCII=js.get("offerings["+k+"].names["+r+"].value".trim());
				 }
			 }
			 
			 if(POfferingID.contains(WS_OfferingID) && PDescription.contains(WS_description) && PDomAlternateIds.contains(WS_Dom)
						 && POperatingOrgCds.contains(WS_OpCoCd)
						&& 	PAbbreviation.contains(WS_Abbreviation) && PLongNameUTF.contains(WS_LongNameUTF) 	 
						&& 	PLongNameASCII.contains(WS_LongNameASCII) && PMediumNameUTF.contains(WS_MediumNameUTF)	 
						&& 	PMediumNameASCII.contains(WS_MediumNameASCII) && PSmallNameUTF.contains(WS_SmallNameUTF)	 
						&& 	PSmallNameASCII.contains(WS_SmallNameASCII) 
						&& PShortNameUTF.contains(WS_ShortNameUTF)
						&& PShortNameASCII.contains(WS_ShortNameASCII) && 	PEpicAlternateIds.contains(WS_Epic))
					 {
						 String excelApproxValue = PapproxPkgVolCCm.get(k).toString();
						 if(excelApproxValue!=null)
						 {
							 double roundoffvalue=Math.round(NumberUtils.toDouble(excelApproxValue));
							 String roundoffexcelApproxValue=Double.toString(roundoffvalue).substring(0,WS_approxPkgVolCCm.length());
							 if(roundoffexcelApproxValue.equals(WS_approxPkgVolCCm))
							 {
								 StatusFlg.add("Y");
								 System.out.println("Passed for " +WS_OfferingID);
							 }
							 else
							 {
								 StatusFlg.add("N");
								 System.out.println("Failed for " +WS_OfferingID);
							 }
						 }
						
						 
					 }
					 else
					 {
						 System.out.println("Failed for " +WS_OfferingID);
					 }
		 }
		
		if(StatusFlg.contains("N"))
		{
			System.out.println("Data Mismatch Exist in Product Master and BPT Web Service");
		}
		else
		{
			System.out.println("Data Validated Successfully in Product Master and BPT Web Service");
		}
		 
		 
		 
	}
	
	
	public static void validateDuplicateRecords(String getUrl) throws IOException, SQLException    
	{
		 connectDatabase();
		 RestAssured.baseURI = "http://product-test.app.wtcdev2.paas.fedex.com";
		 Response res = given()
		            .header("key","value").and().header("clientid","MY_ID").and().header("Accept","application/json")
		            .when()
		            .get(getUrl)
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
		        ArrayList<String> Db_Conflict2 = new ArrayList<String>();
		        while(rs.next())
		        {
		        	Db_Conflict1.add(rs.getString(2));
		        	Db_Conflict2.add(rs.getString(3));
		        }
		        stmt.close();
		        rs.close();
		        ArrayList<String> WS_Conflict = new ArrayList<String>();
		        for(int r=0;r<offeringsInConflictCount;r++)
		        {
		        	WS_Conflict.add(js.getString("offerings["+i+"].offeringsInConflict["+r+"].offeringId"));
		        }
			  
		        //System.out.println(WS_Conflict.size());
		        //System.out.println(Db_Conflict1.size());
		        if(WS_Conflict.size()==Db_Conflict1.size())
		        {
		        	logfile.info("No Duplicate Values present for Offering ID:" +WS_OfferingID);
		        }
		        else
		        {
		        	for(int x=0;x<WS_Conflict.size();x++)
		        	{
		        		for(int y=1;y<WS_Conflict.size();y++)
						{
							if(WS_Conflict.get(x).toString().equals(WS_Conflict.get(y).toString()))
							{
								logfile.info("Duplicate Conflict ID:" +WS_Conflict.get(x).toString() + "for Offering ID:" +WS_OfferingID );
							}
						}
		        	}

		        }
			  }
			  
			  else
			  {
				  logfile.info("No Conflict Values Exist for Offering ID:" +WS_OfferingID);
			  }
	
		  }
		  closeDatabase();
}}
