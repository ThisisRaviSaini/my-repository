package com.plefs.backend;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.jacob.com.LibraryLoader;
//
//import atu.alm.wrapper.ALMServiceWrapper;
//import atu.alm.wrapper.enums.StatusAs;
//import atu.alm.wrapper.exceptions.ALMServiceException;  

public class CommonMethods extends BaseClass {

	/**********************************************************************************************
	 * Method: Login() Description:This method will take the arguments from
	 * testNG.xml file and will perform login into spotfire application. Created on
	 * : 01/29/2018
	 * 
	 * @throws IOException
	 * 
	 *********************************************************************************************/

	public void Login() throws IOException

	{
		String username = Call_config("username");
		String password = Call_config("password");
		System.out.println("Parameter for User Name passed as : " + username);
		System.out.println("Parameter for Password passed as : " + password);
		obj_common_methods.waitForElement(driver, ObjectRepository.by_username, 50);
		driver.findElement(ObjectRepository.by_username).sendKeys(username);
		driver.findElement(ObjectRepository.by_password).sendKeys(password);
		driver.findElement(ObjectRepository.by_submit).click();
		// obj_common_methods.waitForElement(driver, ObjectRepository.by_Landingpage,
		// 30);
		System.out.println("Login Successfully");

	}

	/**********************************************************************************************
	 * Method: make_direcory() Description:This method will create folder where all
	 * screenshot will be saved. Created on : 01/29/2018
	 * 
	 * @throws IOException
	 * 
	 *********************************************************************************************/
	public static String make_direcory(String name) throws IOException

	{
		String sourceFolder = Call_config("Screenshot_Path");
		File f = new File(sourceFolder);
		if (!f.exists()) {
			f.mkdir();
			f = new File(sourceFolder + "/" + name);
			f.mkdir();
		} else {
			f = new File(sourceFolder + "/" + name);
			f.mkdir();
		}
		// System.out.println("Absolute Path: " +f.getAbsolutePath());
		return f.getAbsolutePath();

	}

	/**********************************************************************************************
	 * Method: take_screenshot() Description:This method will take screenshot
	 * Created on : 01/29/2018
	 * 
	 *********************************************************************************************/
	public static void take_screenshot(String name, String file_name) throws Exception {
		try {
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, "png", new File(file_name + "/" + name));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**********************************************************************************************
	 * Method: take_screenshot_Time() Description:This method will take screenshot
	 * Created on : 01/29/2018
	 * 
	 * @return
	 *********************************************************************************************/
	public static String take_screenshot_Time(String name, String file_name) throws Exception {
		String s = null;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
			LocalDate localDate = LocalDate.now();
			String Str_Date = dtf.format(localDate).replace("/", "_");
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			File DestFile = new File(file_name + "/" + name + "_" + Str_Date + ".png");
			FileUtils.copyFile(SrcFile, DestFile);
			s = DestFile.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**********************************************************************************************
	 * Method: ReadExcel() Description:This method will read our test data sheet
	 * Created on : 01/29/2018
	 * 
	 *********************************************************************************************/
	public static void ReadExcel() throws Exception {

//	String FilePath=	Call_config("TestData_SheetPath");		 
//String FilePath = "D:\\Vaibhav\\Selenium\\Station Profile\\Vaibhav\\TestDataSheet\\Station_Profile_ClearButton.xlsx";
//String FilePath = "D:\\Vaibhav\\Selenium\\Station Profile\\Vaibhav\\TestDataSheet\\Station_Profile_US197015.xlsx";
//String FilePath = "D:\\Vaibhav\\Selenium\\Station Profile\\Vaibhav\\TestDataSheet\\Station_Profile_US_UAT.xlsx";
		String FilePath = "D:\\SAM\\Drop_net\\TestDataSheet\\TestdataSheetDropnet.xlsx";
		String UserStoryname = UserStory.getUS_name();
		HashMap<String, LinkedHashMap<String, String>> Excel = new LinkedHashMap<String, LinkedHashMap<String, String>>();

		DataObject o = new DataObject();
		File inputWorkbook = new File(FilePath);
		// File inputWorkbook = new File("C:/Users/SG5044534/Desktop/Book1.xlsx");
		FileInputStream fis = new FileInputStream(inputWorkbook);
		String path = FilePath;

		if (path.toString().endsWith(".xlsx")) {
			XSSFWorkbook w = new XSSFWorkbook(fis);
			XSSFSheet sheet = w.getSheet(UserStoryname);
			System.out.println(UserStoryname);
			int rowcount = sheet.getPhysicalNumberOfRows();
			// System.out.println("Row count : "+ rowcount);

			for (int i = 1; i < rowcount; i++) {

				LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
				int columncount = sheet.getRow(i).getLastCellNum();
				for (int j = 0; j < columncount; j++) {
					String Detail = sheet.getRow(0).getCell(j).toString();
					String Details = sheet.getRow(i).getCell(j).toString();
					if (Detail.equalsIgnoreCase("TC_ID")) {
						o.setKey(Details);

					}
					// System.out.println(Details);

					list.put(Detail, Details);

				}

				Excel.put(o.getKey(), list);

			}

			o.setExcelObject(Excel);

			fis.close();

		}

	}

	/**********************************************************************************************
	 * Method: waitForElement() Description:This method will wait until the passed
	 * argument is visible Created on : 01/29/2018
	 * 
	 *********************************************************************************************/
	public void waitForElement(WebDriver driver, By xpath, int timeTOWaitInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeTOWaitInSec);
		wait.until(ExpectedConditions.presenceOfElementLocated(xpath));

	}

	/**********************************************************************************************
	 * Method: CheckRunStatus() Description:This method will check whether we need
	 * to run test case or skip. Created on : 01/29/2018
	 * 
	 *********************************************************************************************/
	public boolean CheckRunStatus(String TC_ID) throws Exception {
		ReadExcel();
		String RunStatus = DataObject.getVariable("Run", TC_ID);
		if (RunStatus.equalsIgnoreCase("Yes")) {
			return true;
		} else {
			return false;
		}

	}

	/**********************************************************************************************
	 * Method: ALM_Pass() Description:This method will update ALM Created on :
	 * 01/29/2018
	 * 
	 *********************************************************************************************/
//		public void ALM_Pass(String TestCaseName) throws ALMServiceException
//		{
//			System.setProperty("jacob.dll.path","C:/Users/RD5038882/Desktop/TestNG_Framework/jacob-1.18-x86.dll");
//			LibraryLoader.loadJacobLibrary();
//			ALMServiceWrapper wrapper = new ALMServiceWrapper("https://conexus.prod.fedex.com:9445/qcbin");
//			wrapper.connect("5212257","Flags196","ESD","ESD_FLAGS");
//			System.out.println("QC Connection Established");
//			
//			wrapper.updateResult("Z-Trash\\TEMP","new",36533,TestCaseName, StatusAs.PASSED);
//			System.out.println("QC Status Updated");
//			wrapper.close();
//		}

	public static String Call_config(String Parameter) throws IOException {
		File file = new File("Config.properties");

		FileInputStream fileInput = null;
		fileInput = new FileInputStream(file);
		Properties prop = new Properties();
		prop.load(fileInput);

		String Value = prop.getProperty(Parameter);

		return Value;

	}

	public static void Delete_DefaultSuite() throws IOException {

		String Source_Folder = Call_config("DefaultSuite_Path");
		System.out.println(Source_Folder);

		File index = new File(Source_Folder);
		if (!index.exists()) {
			System.out.println("Folder Not exist");
		} else {
			index.delete();
			System.out.println("DefaultSuite deleted");
		}
	}

	public static ResultSet Database_Check(String SQL_Query) throws IOException, SQLException, ClassNotFoundException {
		String db_url = Call_config("db_url");
		String db_username = Call_config("db_username");
		String db_password = Call_config("db_password");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(db_url, db_username, db_password);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL_Query);
		return rs;

	}

}
