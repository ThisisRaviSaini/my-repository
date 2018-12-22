package com.plefs.gpr.scripts;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.plefs.gpr.backend.Application;
import com.plefs.gpr.backend.Backend;
import com.plefs.gpr.backend.BrowserInitialization;
import com.plefs.gpr.backend.Copier;
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;
import com.plefs.gpr.backend.uftexecution;

import javafx.scene.control.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.openqa.selenium.support.Color;

public class US_286250 {
	Logger logger = Logger.getLogger(US_286250.class);
	static String policyGridName;
	static String ExtractId;
	static String FileName;

	@Test(priority = 1)
	/**********************************************************************************************
	 * Description:Verify that the User is able to view new data in orange color
	 * in excel report. Created on : 21/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	public void TC_01() throws Exception {

		String rowarr[][] = null;
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String hex = null;
		int rowNum = 0;
		int colNum = 0;
		String stringRowarr = null;
		String stringData = null;
		String[][] data = null;
		String home = System.getProperty("user.home");
		int CounterA = 0;
		int CounterB = 0;
		int CounterC = 0;
		int CounterD = 0;
		String excelcolor = null;
		String testCaseName = "TC_01";
		System.out.println("Executing Testcase:" + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GPRExtendreportstitle, 20);
				Thread.sleep(10000);
				String Policygrid = "OFFERING_CONSTRAINTS";
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
				Thread.sleep(10000);
				String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_records).getText();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
				Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract1).click();
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract4128).click();
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected).click();
				Thread.sleep(60000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ReportsCheckList).click();
				//policyGridName = BrowserInitialization.driver.findElement(XpathRepository.by_policyGridName).getText();
				policyGridName=Policygrid;
				ExtractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractId).getText();
				FileName = policyGridName + " Comparison_Report (ID " + ExtractId + ")";
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedButton).click();
				Thread.sleep(80000);

				// **********************Extracting data from GUI

				String RowCount = "//tr[3]//td";
				String ColCount = "//tr//td[1]";
				int count2 = BrowserInitialization.driver.findElements(By.xpath(ColCount)).size();
				int count1 = BrowserInitialization.driver.findElements(By.xpath(RowCount)).size();
				rowarr = new String[count2][count1];
				for (int m = 1; m <= count2; m++) {
					for (int n = 1; n <= count1; n++) {
						String celldata = BrowserInitialization.driver.findElement(By.xpath("//tr[2+" + m + "]//td[" + n + "]")).getText();
						rowarr[m - 1][n - 1] = celldata;
						stringRowarr = Arrays.deepToString(rowarr).toString();
						CounterA++;
					}
				}

				BrowserInitialization.driver.findElement(XpathRepository.by_ExportToExcel).click();
				Thread.sleep(10000);

				// *********************Extracting data from Excel

				try {
					InputStream is = new BufferedInputStream(new FileInputStream(home + "\\Downloads\\" + FileName + ".xlsx"));
					// InputStream is = new BufferedInputStream(new
					// FileInputStream(
					// "C:\\Users\\LD5041738\\Downloads\\OFFERING_CONSTRAINTS
					// Comparison_Report (ID 2197).xlsx"));
					XSSFWorkbook wb = new XSSFWorkbook(is);
					XSSFSheet sheet = wb.getSheet("OFFERING_CONSTRAINTS_Comparison");
					rowNum = sheet.getLastRowNum() + 1;
					colNum = sheet.getRow(0).getLastCellNum();
					data = new String[rowNum][colNum];
					for (int i = 1; i < rowNum; i++) {
						XSSFRow row = sheet.getRow(i);
						for (int k = 0; k < colNum; k++) {
							XSSFCell cell = row.getCell(k);
							String value = Application.cellToString(cell);
							data[i - 1][k] = value;
							stringData = Arrays.deepToString(data).toString();
							if (stringRowarr.contains(data[i - 1][k])) {
								CounterB++;
							}
						}
					}
					// System.out.println(CounterB);
					// System.out.println(Arrays.deepToString(data));
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (CounterA == CounterB) {
					String RowCounts = "//tr[3]//td";
					String ColCounts = "//tr//td[1]";
					int count22 = BrowserInitialization.driver.findElements(By.xpath(ColCounts)).size();
					int count11 = BrowserInitialization.driver.findElements(By.xpath(RowCounts)).size();
					rowarr = new String[count22][count11];
					for (int m = 1; m <= count22; m++) {
						for (int i = 1; i <= count11; i++) {
							String color = BrowserInitialization.driver.findElement(By.xpath("//tr[2+" + m + "]//td[" + i + "]")).getCssValue("color");
							hex = Color.fromString(color).asHex();
							if (hex.equals("#ffa500")) {
								CounterC++;
							}
						}
					}
					String excelColorExpected = "68bca528";
					try {
						InputStream is = new BufferedInputStream(new FileInputStream(home + "\\Downloads\\" + FileName + ".xlsx"));
						XSSFWorkbook wb = new XSSFWorkbook(is);
						XSSFSheet sheet = wb.getSheet("OFFERING_CONSTRAINTS_Comparison");
						rowNum = sheet.getLastRowNum() + 1;
						colNum = sheet.getRow(0).getLastCellNum();
						data = new String[rowNum][colNum];
						for (int p = 1; p < rowNum; p++) {
							XSSFRow row = sheet.getRow(p);
							for (int k = 0; k < colNum; k++) {
								XSSFCellStyle cs = sheet.getRow(p).getCell(k).getCellStyle();
								short fontIndex = cs.getFontIndex();
								XSSFFont font = wb.getFontAt(fontIndex);
								//System.out.println(font.getXSSFColor());
								String colorString = font.getXSSFColor().toString();
								// System.out.println(colorString);
								String arr[] = colorString.split("@");
								excelcolor = arr[1].toString();
								if (excelColorExpected.equals(excelcolor)) {
									CounterD++;
								} else {
								}
							}
						}
						if (CounterC == CounterD) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
				Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
				Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
				Assert.fail();
			}
		} catch (Exception e) {
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			Assert.fail();
			e.printStackTrace();
		} finally {
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			System.out.println("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			System.out.println("------------------------------------------------------------------");
		}
	}

	@Test(priority = 2)
	/**********************************************************************************************
	 * Description:Verify that the User is able to view different data
	 * highlighted in red color in excel report. Created on : 21/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	public void TC_02() throws Exception {

		String rowarr[][] = null;
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String hex = null;
		int rowNum = 0;
		int colNum = 0;
		String stringRowarr = null;
		String stringData = null;
		String[][] data = null;
		String home = System.getProperty("user.home");
		int CounterA = 0;
		int CounterB = 0;
		int CounterC = 0;
		int CounterD = 0;
		String excelcolor = null;
		String testCaseName = "TC_02";
		System.out.println("Executing Testcase:" + testCaseName);
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GPRExtendreportstitle, 20);
				Thread.sleep(10000);
				String Policygrid = "OFFERING_CONSTRAINTS";
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
				Thread.sleep(10000);
				String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_records).getText();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
				Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract1).click();
				Thread.sleep(25000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract4128).click();
				Thread.sleep(25000);
				BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected).click();
				Thread.sleep(70000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ReportsCheckList).click();
				//policyGridName = BrowserInitialization.driver.findElement(XpathRepository.by_policyGridName).getText();
				policyGridName=Policygrid;
				ExtractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractId).getText();
				FileName = policyGridName + " Comparison_Report (ID " + ExtractId + ")";
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedButton).click();
				Thread.sleep(80000);
				// ***********************Extracting data from GUI
				String RowCount = "//tr[3]//td";
				String ColCount = "//tr//td[1]";
				int count2 = BrowserInitialization.driver.findElements(By.xpath(ColCount)).size();
				int count1 = BrowserInitialization.driver.findElements(By.xpath(RowCount)).size();
				rowarr = new String[count2][count1];
				for (int m = 1; m <= count2; m++) {
					for (int n = 1; n <= count1; n++) {
						String celldata = BrowserInitialization.driver.findElement(By.xpath("//tr[2+" + m + "]//td[" + n + "]")).getText();
						rowarr[m - 1][n - 1] = celldata;
						stringRowarr = Arrays.deepToString(rowarr).toString();
						CounterA++;
					}
				}

				BrowserInitialization.driver.findElement(XpathRepository.by_ExportToExcel).click();
				Thread.sleep(10000);

				// *************************Extracting data from excel
				try {
					InputStream is = new BufferedInputStream(new FileInputStream(home + "\\Downloads\\" + FileName + ".xlsx"));
					// InputStream is = new BufferedInputStream(new
					// FileInputStream(
					// "C:\\Users\\LD5041738\\Downloads\\OFFERING_CONSTRAINTS
					// Comparison_Report (ID 2197).xlsx"));
					XSSFWorkbook wb = new XSSFWorkbook(is);
					XSSFSheet sheet = wb.getSheet("OFFERING_CONSTRAINTS_Comparison");
					rowNum = sheet.getLastRowNum() + 1;
					colNum = sheet.getRow(0).getLastCellNum();
					data = new String[rowNum][colNum];
					for (int i = 1; i < rowNum; i++) {
						XSSFRow row = sheet.getRow(i);
						for (int k = 0; k < colNum; k++) {
							XSSFCell cell = row.getCell(k);
							String value = Application.cellToString(cell);
							data[i - 1][k] = value;
							stringData = Arrays.deepToString(data).toString();
							if (stringRowarr.contains(data[i - 1][k])) {
								CounterB++;
							}
						}
					}
					// System.out.println(CounterB);
					// System.out.println(Arrays.deepToString(data));
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (CounterA == CounterB) {
					String RowCounts = "//tr[3]//td";
					String ColCounts = "//tr//td[1]";
					int count22 = BrowserInitialization.driver.findElements(By.xpath(ColCounts)).size();
					int count11 = BrowserInitialization.driver.findElements(By.xpath(RowCounts)).size();
					rowarr = new String[count22][count11];
					for (int m = 1; m <= count22; m++) {
						for (int i = 1; i <= count11; i++) {
							String color = BrowserInitialization.driver.findElement(By.xpath("//tr[2+" + m + "]//td[" + i + "]")).getCssValue("color");
							hex = Color.fromString(color).asHex();
							if (hex.equals("#ff0000")) {
								CounterC++;
							}
						}
					}
					String excelColorExpected = "6dff7a5d";
					try {
						InputStream is = new BufferedInputStream(new FileInputStream(home + "\\Downloads\\" + FileName + ".xlsx"));
						XSSFWorkbook wb = new XSSFWorkbook(is);
						XSSFSheet sheet = wb.getSheet("OFFERING_CONSTRAINTS_Comparison");
						rowNum = sheet.getLastRowNum() + 1;
						colNum = sheet.getRow(0).getLastCellNum();
						data = new String[rowNum][colNum];
						for (int p = 1; p < rowNum; p++) {
							XSSFRow row = sheet.getRow(p);
							for (int k = 0; k < colNum; k++) {
								XSSFCellStyle cs = sheet.getRow(p).getCell(k).getCellStyle();
								short fontIndex = cs.getFontIndex();
								XSSFFont font = wb.getFontAt(fontIndex);
								String colorString = font.getXSSFColor().toString();
								String arr[] = colorString.split("@");
								excelcolor = arr[1].toString();
								if (excelColorExpected.equals(excelcolor)) {
									CounterD++;
								} else {
								}
							}
						}
						if (CounterC == CounterD) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
				Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
				Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
				Assert.fail();
			}
		} catch (Exception e) {
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			Assert.fail();
			e.printStackTrace();
		} finally {
			Application.findButtonAndClick(XpathRepository.by_URSAUVDSKfedExOnsiteLink);
			System.out.println("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			System.out.println("------------------------------------------------------------------");
		}
	}

	@Parameters({ "US_Name" })
	@BeforeTest
	public void beforeTest(String US_Name) throws InterruptedException, IOException {
		TestConfiguration.beforeTest(US_Name);
	}

	@AfterTest
	public void afterTest() throws Exception {
		TestConfiguration.afterTest();
	}

	@BeforeSuite
	public void beforeSuite() throws IOException {
		TestConfiguration.beforeSuite();
	}

	@AfterSuite
	public void afterSuite() throws Exception {
		TestConfiguration.afterSuite();
	}


	
}
