package backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.SkipException;





public class Backend
{
	static Logger logger = Logger.getLogger(Backend.class);
	public static String TC_ID = null;
	
	/**********************************************************************************************
	 * Description:This method will read the Property file. Created on
	 * :01/29/2018
	 *********************************************************************************************/

	public static String getProperty(String Parameter) throws IOException 
	{
		File file = new File("Config.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties prop = new Properties();
		prop.load(fileInput);
		String Value = prop.getProperty(Parameter);
		return Value;

	}
	
	
	public static ResultSet databaseconnection(String Query) throws IOException, ClassNotFoundException, SQLException
	{
		String dbUrl=Backend.getProperty("dbUrl");
		String username=Backend.getProperty("dbusername");
		String password=Backend.getProperty("dbpassword");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(dbUrl, username, password);
		Statement stmt = con.createStatement();
		ResultSet rs1 = stmt.executeQuery(Query);
		stmt.close();
		return rs1;
		
	}
	
	/**********************************************************************************************
	 * Description:This method will read our test data sheet Created on :
	 * 01/29/2018
	 *********************************************************************************************/
	public static void readTestDataSheet(String UserStoryName) throws Exception {
		String userStoryName = UserStoryName;
		String FilePath = Backend.getProperty("WebServiceTestData");
		HashMap<String, LinkedHashMap<String, String>> Excel = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		DataObject o = new DataObject();
		File inputWorkbook = new File(FilePath);
		FileInputStream fis = new FileInputStream(inputWorkbook);
		String path = FilePath;
		if (path.toString().endsWith(".xlsx")) {
			XSSFWorkbook w = new XSSFWorkbook(fis);
			XSSFSheet sheet = w.getSheet(userStoryName);
			int rowcount = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rowcount; i++) {
				LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
				int columncount = sheet.getRow(i).getLastCellNum();
				for (int j = 0; j < columncount; j++) {
					String columnName = sheet.getRow(0).getCell(j).toString();
					String columnValue = sheet.getRow(i).getCell(j).toString();
					if (columnName.equalsIgnoreCase("TC_ID")) {
						o.setKey(columnValue);
					}
					list.put(columnName, columnValue);
				}
				Excel.put(o.getKey(), list);
			}
			o.setExcelObject(Excel);
			fis.close();
		}

	}
	
	/**********************************************************************************************
	 * Description:This method will check whether we need to run test case or
	 * skip. Created on : 01/29/2018
	 *********************************************************************************************/
	public static boolean checkRunStatus(String TC_ID,String UserStoryName ) throws Exception {
		readTestDataSheet(UserStoryName);
		String RunStatus = DataObject.getVariable("Run", TC_ID);
		if (RunStatus.equalsIgnoreCase("Yes")) {
			return true;
		} else {
			return false;
		}

	}
	
	public static void displayException( Exception e) throws Exception {
		e.printStackTrace();
		logger.error(e.getMessage());
		Assert.fail();

	}
	
	/**********************************************************************************************
	 * Description:This method will take the system date and format it. Created
	 * on :04/03/2018
	 *********************************************************************************************/
	public static String getCurrentDateTime() {
		String currentDateTime = new SimpleDateFormat("dd-MMMM-yyyy_HH.mm.ss").format(Calendar.getInstance().getTime());
		return currentDateTime;
	}

	
}
