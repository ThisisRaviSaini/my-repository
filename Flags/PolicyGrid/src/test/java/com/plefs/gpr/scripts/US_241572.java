package com.plefs.gpr.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestContext;
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
import com.plefs.gpr.backend.Mailer;
import com.plefs.gpr.backend.Reporter;
import com.plefs.gpr.backend.TestConfiguration;
import com.plefs.gpr.backend.UserStoryName;
import com.plefs.gpr.backend.XpathRepository;
import com.plefs.gpr.backend.Zipper;
import com.plefs.gpr.backend.uftexecution;

public class US_241572 {
	Logger logger = Logger.getLogger(US_241572.class);
	// ********************************************************************************************//
	// Description: Verify a new row is added in the Extract table with Approval
	// Status "IP" the
	// when user click on the Extract Button
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
						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_PolicyGridMonthlyProcessHeader, 10);

						String Policygrid = DataObject.getVariable("PolicyGridDropdown", "TC_01");

						Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
						policygrid_dropdown.selectByVisibleText(Policygrid);

						Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

						// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
						// ObjectRepository.by_CreateExtractButton, 20);

						Thread.sleep(4000);
						BrowserInitialization.driver.findElement(XpathRepository.by_CreateExtractButton).click();
						Application.waitForCursor();
						String Approval_status_ip = BrowserInitialization.driver.findElement(XpathRepository.by_ApprovalStatus_TableHeader).getText();

						Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

						Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ApprovalStatus_TableHeader, 4);

						String Approval_status_c = BrowserInitialization.driver.findElement(XpathRepository.by_ApprovalStatus_TableHeader).getText();

						Thread.sleep(6000);
						if (Approval_status_ip != Approval_status_c) {

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

	// ********************************************************************************************//
	// Description: Verify a new row is added in the Extract table with initial
	// Approval Status "IP"
	// and Approval status changes to "C " when com.flags.gpr.backend processing
	// is complete.
	// Created by : Vishal Shrivastava
	// Created on : 03/05/2018
	// Last updated by : Vishal Shrivastava
	// Last updated : 03/05/2018
	// *********************************************************************************************//

	@Test
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
			BrowserInitialization.driver.navigate().refresh();
			Application.waitForCursor();
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);
				// BrowserInitialization.driver.findElement(ObjectRepository.by_GprExtractReportLink).click();
				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_PolicyGridMonthlyProcessHeader, 10);

					BrowserInitialization.driver.navigate().refresh();
					Application.waitForCursor();

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_PolicyGridDropDown, 10);

					String Policygrid = DataObject.getVariable("PolicyGridDropdown", "TC_02");

					Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
					policygrid_dropdown.selectByVisibleText(Policygrid);

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					Thread.sleep(15000);

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 40);

					BrowserInitialization.driver.findElement(XpathRepository.by_CreateExtractButton).click();
					Application.waitForCursor();

					Thread.sleep(70000);
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_ApprovalStatus_TableHeader, 4);

					String Approval_status = BrowserInitialization.driver.findElement(XpathRepository.by_ApprovalStatus_TableHeader).getText();

					;

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					String Expected_Approval_status = "C";

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_PolicyGridMonthlyProcessHeader, 30);

					if (Approval_status.equals(Expected_Approval_status)) {

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
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
			// BrowserInitialization.driver.findElement(ObjectRepository.by_URSAUVDSKfedExOnsiteLink).click();
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

		try {
			boolean testRunnable = Backend.isTestRunnable(testCaseName);
			if (testRunnable) {
				testCaseDesc = DataObject.getVariable("Test Case Description", testCaseName);
				passTestCaseDesc = DataObject.getVariable("Pass Description", testCaseName);
				failTestCaseDesc = DataObject.getVariable("Fail Description", testCaseName);
				screenShotName = Backend.makeDirectory(UserStoryName.getUSName() + "_" + testCaseName);
				Reporter.testCaseStart(UserStoryName.getUSName() + "_" + testCaseName, testCaseDesc);

				BrowserInitialization.driver.navigate().refresh();
				Application.waitForCursor();

				Thread.sleep(8000);

				// BackendCommonFunctionality.waitForElement(BrowserInitialization.driver,
				// ObjectRepository.by_homePage, 30);

				if (BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridMonthlyProcessHeader).getText().equals(DataObject.getVariable("ScreenName", testCaseName))) {
					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_PolicyGridMonthlyProcessHeader, 50);

					BrowserInitialization.driver.navigate().refresh();
					Application.waitForCursor();

					String Policygrid = DataObject.getVariable("PolicyGridDropdown", "TC_03");

					Select policygrid_dropdown = new Select(BrowserInitialization.driver.findElement(XpathRepository.by_PolicyGridDropDown));
					policygrid_dropdown.selectByVisibleText(Policygrid);

					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					Backend.waitForElement(BrowserInitialization.driver, XpathRepository.by_CreateExtractButton, 2);

					Thread.sleep(15000);

					BrowserInitialization.driver.findElement(XpathRepository.by_CompareSelected_button).click();
					Backend.takeScreenshot(testCaseName, screenShotName, "PASS");

					String Expected_error_Message = "Please select 2 Extracts to compare!";

					String Actual_error_Message = BrowserInitialization.driver.findElement(XpathRepository.by_WarningMessage1).getText();

					if (Actual_error_Message.contains(Expected_error_Message)) {

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
					Backend.takeScreenshot(testCaseName, screenShotName, "FAIL");
					Backend.displayTestCaseStatus("Policy Grid Monthly Processes Screen does not exists", "FAIL");
					Reporter.setTestDetails("FAIL", testCaseDesc, "Policy Grid Monthly Processes Screen does not exists", screenshotPath);
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
	public void beforeTest(String US_Name,final ITestContext testContext) throws InterruptedException, IOException {
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
