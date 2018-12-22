package com.plefs.gpr.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
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
import com.plefs.gpr.backend.DataObject;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;

public class US_259848 {
	Logger logger = Logger.getLogger(US_259848.class);
	// ********************************************************************************************//
	// Description: Verify that Compare Selected button is enabled when user
	// select 2 checkboxes of same policy grid
	// Created by : Vishal Shrivastava
	// Created on : 03/05/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
	public void TC_01() throws Exception {
		String testCaseName = "TC_01";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		int temp = 0;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
                    

				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					BrowserInitialization.driver.navigate().refresh();
					Application.waitForCursor();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 50);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						String Policygrid = DataObject.getVariable("DropDownMenu", "TC_01");

						Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
						policygrid_dropdown.selectByVisibleText(Policygrid);

						BrowserInitialization.driver.findElement(XpathRepository.by_CreateExtractButton).click();

						Thread.sleep(2000);

						if (BrowserInitialization.driver.findElement(XpathRepository.by_GrayoutScreen).isDisplayed())
							;
						{
							temp++;
						}

						if (temp == 1) {

							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}

					} else {
						Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Home Page not Opened", screenshotPath);
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
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
		} finally {
			Thread.sleep(20000);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}
	
	@Test
	public void TC_02() throws Exception {
		String testCaseName = "TC_02";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		int temp = 0;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 80);
				Thread.sleep(5000);


				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					BrowserInitialization.driver.navigate().refresh();
					Application.waitForCursor();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 50);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						String Policygrid = DataObject.getVariable("DropDownMenu", "TC_01");

						Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
						policygrid_dropdown.selectByVisibleText(Policygrid);
						Thread.sleep(5000);
						BrowserInitialization.driver.findElement(XpathRepository.by_CreateExtractButton).click();
						Application.waitForCursor();

						Thread.sleep(2000);

                        
						try{
							BrowserInitialization.driver.findElement(XpathRepository.by_Selectfirstcheckbox).click();
						}catch(Exception e){
							
							temp++;
						}
						
						if (temp == 1) {

							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}

					} else {
						Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Home Page not Opened", screenshotPath);
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
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
			
		} finally {
			Thread.sleep(20000);
			logger.info("Execution is completed for Test Case No." + testCaseName);
			Reporter.testCaseEnd();
			logger.info("------------------------------------------------------------------");

		}

	}
	
	@Test
	public void TC_03() throws Exception {
		String testCaseName = "TC_03";
		logger.info("Executing the Test Case No. " + testCaseName);

		String testCaseDesc = null;
		String passTestCaseDesc = null;
		String failTestCaseDesc = null;
		String screenShotName = null;
		String screenshotPath = null;
		int temp = 0;

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				
				BrowserInitialization.driver.findElement(XpathRepository.by_URSAUVDSKfedExOnsiteLink).click();
				Application.waitForCursor();
				Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_GprExtractReportLink, 80);
				Thread.sleep(5000);


				if (BrowserInitialization.driver.findElement(XpathRepository.by_homePage).isDisplayed()) {
					BrowserInitialization.driver.findElement(XpathRepository.by_GprExtractReportLink).click();
					BrowserInitialization.driver.navigate().refresh();
					Application.waitForCursor();
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_WaitTimeCreatedDataExtractTab, 50);
					if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
						String Policygrid = DataObject.getVariable("DropDownMenu", "TC_01");

						Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
						policygrid_dropdown.selectByVisibleText(Policygrid);
						Thread.sleep(5000);
						BrowserInitialization.driver.findElement(XpathRepository.by_CreateExtractButton).click();
						Application.waitForCursor();

						Thread.sleep(2000);

                        
						try{
							BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelectButton).click();
						}catch(Exception e){
							
							temp++;
						}
						
						if (temp == 1) {

							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "PASS");
							Backend.displayTestCaseStatus(passTestCaseDesc, "PASS");
							Reporter.setTestDetails("PASS", testCaseDesc, passTestCaseDesc, screenshotPath);
						} else {
							screenshotPath = Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
							Backend.displayTestCaseStatus(failTestCaseDesc, "FAIL");
							Reporter.setTestDetails("FAIL", testCaseDesc, failTestCaseDesc, screenshotPath);
							Assert.fail();
						}

					} else {
						Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
						Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
						Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
						Assert.fail();
					}
				} else {
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Home Page not Opened", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Home Page not Opened", screenshotPath);
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
			Backend.displayException(testCaseName, testCaseDesc, failTestCaseDesc, screenShotName, screenshotPath, e);
			
		} finally {

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
