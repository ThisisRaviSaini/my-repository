package com.plefs.gpr.backend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Backend {
	static Logger logger = Logger.getLogger(Backend.class);
	public static String TC_ID = null;
	public static int executionCycle = 0;

	public static String currentDateTime = null;
	public static String dashViewScreenshotPath = null;
	public static File dashViewScreenshot = null;

	/**********************************************************************************************
	 * Description:This method will be used to log on to the specified web
	 * application. Created on : 01/29/2018
	 * 
	 * @throws Exception
	 * 
	 *********************************************************************************************/

	public static void login() throws Exception {

		String username = getProperty("AdminUser");
		String password = getProperty("Password");
		Thread.sleep(10000);
		BrowserInitialization.driver.findElement(XpathRepository.by_User_name).sendKeys(username);
		BrowserInitialization.driver.findElement(XpathRepository.by_Password).sendKeys(password);
		BrowserInitialization.driver.findElement(XpathRepository.by_Submit_button).click();
		Thread.sleep(10000);
		Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_homePage, 60);
		logger.info("<<<---------User Authenticated Successfully--------->>>");
	}

	/**********************************************************************************************
	 * Description:This method will create folder. Created on : 01/29/2018
	 *********************************************************************************************/
	public static String makeDirectory(String name) throws IOException

	{
		String sourceFolder = Reporter.folderPath + "\\Screenshots";
		File f = new File(sourceFolder);
		if (!f.exists()) {
			f.mkdir();
			f = new File(sourceFolder + "/" + name);
			f.mkdir();
		} else {
			f = new File(sourceFolder + "/" + name);
			f.mkdir();
		}

		return f.getAbsolutePath();

	}

	/**********************************************************************************************
	 * Method: take_screenshot_Time() Description:This method will take screenshot
	 * of browser window only Created on : 01/29/2018
	 *********************************************************************************************/
	public static String takeScreenshot(String name, String file_name, String status) throws Exception {
		String path = null;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
			LocalDate sysDate = LocalDate.now();
			String formattedDate = dtf.format(sysDate).replace("/", "_");
			TakesScreenshot scrShot = ((TakesScreenshot) BrowserInitialization.driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			File DestFile = new File(file_name + "/" + name + "_" + formattedDate + "_" + status + ".png");
			path = file_name + "/" + name + "_" + formattedDate + "_" + status + ".png";
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	/**********************************************************************************************
	 * Description:This method will read our test data sheet Created on : 01/29/2018
	 *********************************************************************************************/
	public static void readExcel() throws Exception {
		String userStoryName = UserStoryName.getUSName();
		String FilePath = Backend.getProperty("TestDataSheetPath");
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
	 * Method: waitForElement() Description:This method will wait until the passed
	 * argument is visible Created on : 01/29/2018
	 *********************************************************************************************/
	public static void waitForElement(WebDriver driver, By xpath, int timeTOWaitInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeTOWaitInSec);
		wait.until(ExpectedConditions.presenceOfElementLocated(xpath));

	}

	public static void waitForElementToBeClickable(WebDriver driver, By xpath, int timeTOWaitInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeTOWaitInSec);
		wait.until(ExpectedConditions.elementToBeClickable(xpath));

	}

	/**********************************************************************************************
	 * Method: waitForElement() Description:This method will wait until the passed
	 * argument is enabled Created on : 01/29/2018
	 *********************************************************************************************/
	public static void waitForElementE(WebDriver driver, By xpath, int timeTOWaitInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeTOWaitInSec);
		wait.until(ExpectedConditions.elementToBeClickable(xpath));

	}

	/**********************************************************************************************
	 * Description:This method will check whether we need to run test case or skip.
	 * Created on : 01/29/2018
	 *********************************************************************************************/
	public static boolean isTestRunnable(String TC_ID) throws Exception {
		readExcel();
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
	 *********************************************************************************************/
	// public void ALM_Pass(String TestCaseName) throws ALMServiceException {
	// System.setProperty("jacob.dll.path",
	// "C:/Users/RD5038882/Desktop/TestNG_Framework/jacob-1.18-x86.dll");
	// LibraryLoader.loadJacobLibrary();
	// ALMServiceWrapper wrapper = new
	// ALMServiceWrapper("https://conexus.prod.fedex.com:9445/qcbin");
	// wrapper.connect("5212257", "Flags196", "ESD", "ESD_FLAGS");
	// logger.info("QC Connection Established");
	//
	// wrapper.updateResult("Z-Trash\\TEMP", "new", 36533, TestCaseName,
	// StatusAs.PASSED);
	// logger.info("QC Status Updated");
	// wrapper.close();
	// }

	/**********************************************************************************************
	 * Description:This method will read the Property file. Created on :01/29/2018
	 *********************************************************************************************/

	public static String getProperty(String Parameter) throws IOException {
		File file = new File("Config.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties prop = new Properties();
		prop.load(fileInput);
		String Value = prop.getProperty(Parameter);
		return Value;

	}

	public static void deleteDirectory(File file) {

		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDirectory(f);
			}
		}
		file.delete();
	}

	/**********************************************************************************************
	 * Description:This method will delete the Index file. Created on :01/29/2018
	 *********************************************************************************************/

	public static void projectCleanup() throws IOException {
		deleteDirectory(new File(getProperty("TestNgSuitePath")));
		deleteDirectory(new File(getProperty("ReportPath")));
	}

	/**********************************************************************************************
	 * Description:This method will display the test case status on the console
	 * Created on :03/20/2018
	 * 
	 * @throws IOException
	 *********************************************************************************************/

	public static void displayTestCaseStatus(String message, String status) throws IOException {
		// uftexecution.testcasestatus(status, message);
		logger.info(message + "  : " + status);
	}

	/**********************************************************************************************
	 * Description:This method will take the system date and format it. Created on
	 * :04/03/2018
	 *********************************************************************************************/
	public static String getCurrentDateTime() {
		currentDateTime = new SimpleDateFormat("dd-MMMM-yyyy_HH.mm.ss").format(Calendar.getInstance().getTime());
		logger.info(currentDateTime);
		return currentDateTime;
	}

	public static void displayUSName(String US_Name) {
		logger.info("<<<------------Executing the US : " + US_Name + "------------>>>");

	}

	public static void takeReportScreenshot() throws IOException {
		BrowserInitialization.intilaize();
		File f = new File(Reporter.reportName);
		BrowserInitialization.driver.navigate().to(f.toURI().toURL());
		WebElement dashBoardElement = BrowserInitialization.driver
				.findElement(By.xpath("//div[@class='dashboard-view']"));
		File screenshot = ((TakesScreenshot) BrowserInitialization.driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		BufferedImage elementScreenshot = fullImg.getSubimage(dashBoardElement.getLocation().getX(),
				dashBoardElement.getLocation().getY(), dashBoardElement.getSize().getWidth(),
				dashBoardElement.getSize().getHeight());
		ImageIO.write(elementScreenshot, "png", screenshot);
		String resourcesLocation = Backend.getProperty("ResourcesPath");
		dashViewScreenshot = new File(resourcesLocation + "\\report-dashview.png");
		FileUtils.copyFile(screenshot, dashViewScreenshot);
		logger.info("Screenshot Captured");
		BrowserInitialization.driver.quit();
	}

	public static void displayException(String testCaseName, String testCaseDesc, String failTestCaseDesc,
			String screenShotName, String screenshotPath, Exception e) throws Exception {
		e.printStackTrace();
		logger.error(e.getMessage());
		screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
		Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
		Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
		Assert.fail();
	}

	public static void displaySkipException(Exception e) {
		logger.warn(e.getMessage());
		Assert.fail();
	}

}
