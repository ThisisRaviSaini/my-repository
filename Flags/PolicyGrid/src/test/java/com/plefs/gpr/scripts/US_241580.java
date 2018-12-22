package com.plefs.gpr.scripts;

//import org.apache.jasper.tagplugins.jstl.core.Catch;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.seleniumhq.jetty9.util.ArrayUtil;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Configuration;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.plefs.gpr.backend.Application;
import com.plefs.gpr.backend.Backend;
import com.plefs.gpr.backend.BrowserInitialization;
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;
import com.plefs.gpr.backend.uftexecution;

import jxl.Sheet;
import jxl.Workbook;

import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class US_241580 {
	Logger logger = Logger.getLogger(US_241580.class);

	// ********************************************************************************************//
	// Created by : Loveen Dwivedi
	// Created on : 15/04/2018
	// Last updated by : Loveen Dwivedi
	// Last updated : 15/05/2018
	// *********************************************************************************************//

	static String policyGridName;
	static String ExtractId;
	static String FileName;

	@Test(priority = 1)
	/**********************************************************************************************
	 * Description:User should be able to see the Export to Excel button on GPR
	 * Extract. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_01() throws Exception {
		String testCaseName = "TC_01";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
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
				Thread.sleep(200000);
				String Policygrid = "OFFERING_CONSTRAINTS";
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Thread.sleep(10000);
				BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
				Thread.sleep(100000);
				String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_records).getText();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
				Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract1).click();
				Thread.sleep(10000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract4128).click();
				Thread.sleep(10000);
				BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected).click();
				Thread.sleep(50000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ReportsCheckList).click();
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedButton).click();
				Thread.sleep(50000);
				try {
					if (BrowserInitialization.driver.findElement(XpathRepository.by_ExportToExcel).isDisplayed()) {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
						Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
						Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
						uftexecution.testcasestatus("PASS", testCaseName);
					} else {
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
						uftexecution.testcasestatus("FAIL", testCaseName);
						Assert.fail();
					}
				} catch (Exception e) {
					e.printStackTrace();
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			uftexecution.testcasestatus("FAIL", testCaseName);
			Assert.fail();
		} finally {
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	@Test(priority = 2)
	/**********************************************************************************************
	 * Description:User should be able to click on the Export to Excel button
	 * onGPR Extract. Created on : 03/05/2018
	 *********************************************************************************************/
	public void TC_02() throws Exception {
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GPRExtendreportstitle, 20);
				Thread.sleep(100000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ReportsCheckList).click();
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedButton).click();
				Thread.sleep(50000);
				WebElement element = BrowserInitialization.driver.findElement(XpathRepository.by_ExportToExcel);
				if (element.isDisplayed() || element.isEnabled()) {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
					Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
					Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("PASS", testCaseName);
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			uftexecution.testcasestatus("FAIL", testCaseName);
			Assert.fail();

		} finally {
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	@Test(priority = 3)
	/**********************************************************************************************
	 * Description:User should be able to export the report by clicking on
	 * Export to excel. Created on : 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	public void TC_03() throws Exception {
		String testCaseName = "TC_03";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String home = System.getProperty("user.home");
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
				Thread.sleep(100000);
				String Policygrid = "OFFERING_CONSTRAINTS";
				Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_dropdownPG));
				policygrid_dropdown.selectByVisibleText(Policygrid);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_extractbtn).click();
				Thread.sleep(100000);
				String LimitCount = BrowserInitialization.driver.findElement(XpathRepository.by_records).getText();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).clear();
				BrowserInitialization.driver.findElement(XpathRepository.by_ExtractLimittextbox).sendKeys(LimitCount);
				Application.findButtonAndClick(XpathRepository.by_ExtractRefreshbutton);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract1).click();
				Thread.sleep(10000);
				BrowserInitialization.driver.findElement(XpathRepository.by_Extract4128).click();
				Thread.sleep(10000);
				BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected).click();
				Thread.sleep(500000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ReportsCheckList).click();
				policyGridName = BrowserInitialization.driver.findElement(XpathRepository.by_policyGridName).getText();
				ExtractId = BrowserInitialization.driver.findElement(XpathRepository.by_ExtractId).getText();
				FileName = policyGridName + " Comparison_Report (ID " + ExtractId + ")";
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedButton).click();
				Thread.sleep(50000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ExportToExcel).click();
				Thread.sleep(10000);
				File f = new File(home + "\\Downloads\\" + FileName + ".xlsx");
				if (f.exists() || !f.isDirectory()) {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
					Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
					Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("PASS", testCaseName);
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			uftexecution.testcasestatus("FAIL", testCaseName);
			Assert.fail();
		} finally {
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	@Test(priority = 4)
	/**********************************************************************************************
	 * Description:User should be able to open the exported report. Created on :
	 * 03/05/2018
	 * 
	 * @throws Exception
	 *********************************************************************************************/
	public void TC_04() throws Exception {
		String testCaseName = "TC_04";
		logger.info("Executing the Test Case No. " + testCaseName);
		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String home = System.getProperty("user.home");
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(20000);
				File f = new File(home + "\\Downloads\\" + FileName + ".xlsx");
				if (f.exists() || !f.isDirectory()) {
					try {
						String[] cmdarray = new String[] { "cmd.exe", "/c", home + "\\Downloads\\" + FileName + ".xlsx" };
						Runtime.getRuntime().exec(cmdarray);
						if (FileName == null) {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							uftexecution.testcasestatus("FAIL", testCaseName);
							Assert.fail();
						} else {
							if (Runtime.getRuntime().exec(cmdarray) != null) {
								screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
								Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
								Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
								uftexecution.testcasestatus("PASS", testCaseName);
							} else {
								screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
								Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
								Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
								uftexecution.testcasestatus("FAIL", testCaseName);
								Assert.fail();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
						uftexecution.testcasestatus("FAIL", testCaseName);
						Assert.fail();
					}
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			uftexecution.testcasestatus("FAIL", testCaseName);
			Assert.fail();
		} finally {
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	@Test(priority = 5)
	/**********************************************************************************************
	 * Description:User should be able to view all the columns displayed on
	 * screen in the exported excel sheet. Created on : 04/05/2018
	 *********************************************************************************************/
	public void TC_05() throws Exception {
		String testCaseName = "TC_05";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String home = System.getProperty("user.home");
		String rowarr[][];
		String celldata = null;
		int m = 0;
		int n = 0;
		int i = 0;
		int j = 0;
		String[][] data = null;
		String value3 = null;
		String value1 = null;
		String stringData = null;
		String stringRowarr = null;
		Boolean Status = null;
		int count1;
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GPRExtendreportstitle, 20);
				Thread.sleep(100000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ReportsCheckList).click();
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedButton).click();
				Thread.sleep(50000);
				// ========================================Excel to
				// array=========================================
				try {
					InputStream is = new BufferedInputStream(new FileInputStream(home + "\\Downloads\\" + FileName + ".xlsx"));
					// InputStream is = new BufferedInputStream(new
					// FileInputStream("C:\\Users\\LD5041738\\Downloads\\OFFERING_CONSTRAINTS
					// Comparison_Report (ID 1980).xlsx"));
					XSSFWorkbook wb = new XSSFWorkbook(is);
					XSSFSheet sheet = wb.getSheet("OFFERING_CONSTRAINTS_Comparison");
					int rowNum = 1;
					int colNum = sheet.getRow(0).getLastCellNum();
					data = new String[rowNum][colNum];
					for (i = 0; i < rowNum; i++) {
						XSSFRow row = sheet.getRow(i);
						for (j = 0; j < colNum; j++) {
							XSSFCell cell = row.getCell(j);
							value1 = Application.cellToString(cell);
							String value2 = value1.replaceAll("New ", "");
							value3 = value2.replaceAll("Old ", "");
							data[i][j] = value3;
							stringData = Arrays.deepToString(data).toString();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// =====================================================Extracting
				// data from GUI=======================
				// String RowCount = "//tr[3]//td";
				// String ColCount = "//tr//td[1]";
				String RowCount = "//tr[1]//th";
				int count2 = 1;
				count1 = BrowserInitialization.driver.findElements(By.xpath(RowCount)).size();
				rowarr = new String[count2][count1];
				for (m = 1; m <= count2; m++) {
					for (n = 1; n <= count1; n++) {
						celldata = BrowserInitialization.driver.findElement(By.xpath("//tr[" + m + "]//th[" + n + "]")).getText();
						rowarr[m - 1][n - 1] = celldata;
						stringRowarr = Arrays.deepToString(rowarr).toString();
						if (stringData.contains(rowarr[m - 1][n - 1])) 
						{
							Status=true;
							/*screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);uftexecution.testcasestatus("PASS", testCaseName);*/
						} else 
						{
							Status=false;
							/*screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);uftexecution.testcasestatus("FAIL", testCaseName);
							Assert.fail();*/
						}
					}
					
					if(Status==true)
					{
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
						Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
						Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
						uftexecution.testcasestatus("PASS", testCaseName);
					}
					else
					{
						screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
						uftexecution.testcasestatus("FAIL", testCaseName);
						Assert.fail();
					}
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			uftexecution.testcasestatus("FAIL", testCaseName);
			Assert.fail();
		} finally {
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
		}
	}

	@Test(priority = 6)
	/**********************************************************************************************
	 * Description:User should be able to view all the data for fields in
	 * exported sheet that are displayed on the screen. Created on : 04/05/2018
	 *********************************************************************************************/
	public void TC_06() throws Exception {
		String testCaseName = "TC_06";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		String[][] data = null;
		String home = System.getProperty("user.home");
		String rowarr[][] = null;
		int rowNum = 0;
		int colNum = 0;
		int counterA = 1;
		int counterB = 1;
		String stringRowarr = null;
		String stringData = null;
		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				Thread.sleep(20000);
				BrowserInitialization.driver.findElement(XpathRepository.by_GPRExtendreports).click();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GPRExtendreportstitle, 20);
				Thread.sleep(100000);
				BrowserInitialization.driver.findElement(XpathRepository.by_ReportsCheckList).click();
				BrowserInitialization.driver.findElement(XpathRepository.by_ViewSelectedButton).click();
				Thread.sleep(50000);
				// ==============================Extracting data from
				// GUI===================================================
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
						counterA++;
					}
				}
				// =================================================================================================================
				try {
					InputStream is = new BufferedInputStream(new FileInputStream(home + "\\Downloads\\" + FileName + ".xlsx"));
					// InputStream is = new BufferedInputStream(new
					// FileInputStream(
					// "C:\\Users\\LD5041738\\Downloads\\OFFERING_CONSTRAINTS
					// Comparison_Report (ID 2139).xlsx"));
					XSSFWorkbook wb = new XSSFWorkbook(is);
					XSSFSheet sheet = wb.getSheet("OFFERING_CONSTRAINTS_Comparison");
					rowNum = sheet.getLastRowNum() + 1;
					colNum = sheet.getRow(0).getLastCellNum();
					int totalNum = rowNum * colNum;
					data = new String[rowNum][colNum];
					for (int i = 1; i < rowNum; i++) {
						XSSFRow row = sheet.getRow(i);
						for (int k = 0; k < colNum; k++) {
							XSSFCell cell = row.getCell(k);
							String value = Application.cellToString(cell);
							data[i - 1][k] = value;
							stringData = Arrays.deepToString(data).toString();
							if (stringRowarr.contains(data[i - 1][k])) {
								counterB++;
							}
						}
					}
					//System.out.println(counterB);
					//System.out.println(Arrays.deepToString(data));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// ==============================================to compare
				// array====================================================================
				if (counterA == counterB) {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
					Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
					Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("PASS", testCaseName);
				} else {
					screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
					uftexecution.testcasestatus("FAIL", testCaseName);
					Assert.fail();
				}
			} else {
				try {
					throw new SkipException("Skipped Test Case No. " + testCaseName);
				} catch (SkipException e) {
					Backend.displaySkipException(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
			Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
			Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
			uftexecution.testcasestatus("FAIL", testCaseName);
			Assert.fail();
		} finally {
			BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");
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
